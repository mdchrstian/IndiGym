package id.indigym.service;

import id.indigym.database.AksesDAO;
import id.indigym.model.PaketAkses;
import id.indigym.model.PaketMembership;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Aturan bisnis untuk paket dan pembayaran akses.
public class AksesService {

    private AksesDAO aksesDAO = new AksesDAO();

    // Pengelolaan Paket

    public List<PaketAkses> daftarPaket() {
        pastikanLogin();
        try {
            return aksesDAO.daftarPaket();
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }
    // Menambahkan paket membership setelah validasi.
    public void tambahPaketMembership(String nama, BigDecimal harga, int durasiBulan) {
        pastikanLogin();
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama paket wajib diisi.");
        }
        if (harga == null || harga.signum() < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
        if (durasiBulan <= 0) {
            throw new IllegalArgumentException("Durasi harus lebih dari 0 bulan.");
        }
        try {
            aksesDAO.simpanPaket(new PaketMembership(null, nama.trim(), harga, durasiBulan, false));
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Paket harian tidak boleh diarsipkan.
    public void arsipkanPaket(PaketAkses paket) {
        pastikanLogin();
        if (paket == null) {
            throw new IllegalArgumentException("Pilih paket dulu.");
        }
        // paket harian dipakai untuk Visit, jadi tidak boleh dihentikan
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            throw new IllegalArgumentException("Paket harian tidak boleh diarsipkan.");
        }
        try {
            aksesDAO.arsipkanPaket(paket.getId());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Pembayaran membership
    // Menghitung periode tanpa menyimpan apa pun, untuk ditampilkan sebelum petugas menekan Bayar.
    public PembayaranAkses pratinjauPeriode(Pelanggan pelanggan, PaketAkses paket) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        pastikanPaketDipilih(paket);

        LocalDate mulai = hitungTanggalMulai(pelanggan);
        //Polimorfisme: perhitungan dilakukan oleh class paket.
        LocalDate akhir = paket.hitungTanggalAkhir(mulai); 
        return new PembayaranAkses(null, pelanggan, paket.getId(), Sesi.adminLogin.getId(),
                Sesi.adminLogin.getNama(), paket.getNama(), paket.getHarga(), null, mulai, akhir);
    }

    // Mencatat pembayaran membership yang sudah diterima.
    public PembayaranAkses bayarMembership(Pelanggan pelanggan, PaketAkses paket) {
        PembayaranAkses pembayaran = pratinjauPeriode(pelanggan, paket);

        if (pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Profil pelanggan diarsipkan. Aktifkan dulu sebelum membeli akses.");
        }
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            throw new IllegalArgumentException("Paket harian dijual lewat tombol Jual visit harian di layar Pelanggan.");
        }

        pembayaran.setWaktuBayar(LocalDateTime.now());
        try {
            aksesDAO.simpanPembayaran(pembayaran);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
        return pembayaran;
    }
    // Mengambil riwayat pembayaran pelanggan.
    public List<PembayaranAkses> riwayatPembayaran(Pelanggan pelanggan) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        try {
            return aksesDAO.daftarPembayaran(pelanggan);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Mencari akses pelanggan yang berlaku hari ini.
    public PembayaranAkses cariAksesAktifHariIni(Pelanggan pelanggan) {
        pastikanLogin();
        try {
            return aksesDAO.cariAksesAktif(pelanggan, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Menghitung jumlah pelanggan yang memiliki akses hari ini.
    public int jumlahPelangganBeraksesHariIni() {
        pastikanLogin();
        try {
            return aksesDAO.hitungPelangganBerakses(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    //Untuk Visit (dipakai PelangganService)
    //Mengambil paket harian yang masih tersedia.
    public PaketAkses cariPaketHarian() {
        for (PaketAkses paket : daftarPaket()) {
            if (paket.getJenis().equals("HARIAN")) {
                return paket;
            }
        }
        return null;
    }

    // Menyiapkan pembayaran harian tanpa menyimpannya.
    // Penyimpanan dilakukan bersama proses check-in oleh PelangganService.
    public PembayaranAkses siapkanPembayaranHarian(Pelanggan pelanggan) {
        pastikanLogin();
        PaketAkses paket = cariPaketHarian();
        if (paket == null) {
            throw new IllegalArgumentException("Paket harian belum tersedia.");
        }
        LocalDate hariIni = LocalDate.now();
        return new PembayaranAkses(null, pelanggan, paket.getId(), Sesi.adminLogin.getId(),
                Sesi.adminLogin.getNama(), paket.getNama(), paket.getHarga(), LocalDateTime.now(),
                hariIni, paket.hitungTanggalAkhir(hariIni));
    }

    //Aturan tanggal mulai

    // Mengambil data dari database, lalu menyerahkan keputusan ke tentukanTanggalMulai.
    private LocalDate hitungTanggalMulai(Pelanggan pelanggan) {
        try {
            LocalDate akhirTerjauh = aksesDAO.cariTanggalAkhirTerjauh(pelanggan.getId());
            return tentukanTanggalMulai(akhirTerjauh, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Menentukan tanggal mulai membership.
    // Method static agar bisa diuji tanpa koneksi database.
    public static LocalDate tentukanTanggalMulai(LocalDate akhirTerjauh, LocalDate hariIni) {
        // Belum memiliki akses atau seluruh akses sudah lewat.
        if (akhirTerjauh == null || akhirTerjauh.isBefore(hariIni)) {
            return hariIni;
        }
        // Masih memiliki akses, mulai sehari setelah tanggal akhir.
        return akhirTerjauh.plusDays(1);
    }

    // Validasi
    private void pastikanLogin() {
        if (!Sesi.sudahLogin()) {
            throw new IllegalArgumentException("Silakan login dulu.");
        }
    }

    private void pastikanPelangganDipilih(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pilih pelanggan dulu.");
        }
    }

    private void pastikanPaketDipilih(PaketAkses paket) {
        if (paket == null) {
            throw new IllegalArgumentException("Pilih paket dulu.");
        }
    }
    // Mengubah SQLException menjadi pesan kesalahan untuk lapisan aplikasi.
    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa terhubung ke database: " + e.getMessage());
    }
}
