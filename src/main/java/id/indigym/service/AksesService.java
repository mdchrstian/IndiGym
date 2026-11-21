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
// Visit harian disimpan oleh PelangganService (bersama check-in pertama), memakai siapkanPembayaranHarian di sini.
public class AksesService {

    private AksesDAO aksesDAO = new AksesDAO();

    // ---------- Paket ----------

    public List<PaketAkses> daftarPaket() {
        pastikanLogin();
        try {
            return aksesDAO.daftarPaket();
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

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

    // Paket yang diarsipkan tidak dijual lagi, tapi pembayaran lama tetap utuh.
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

    // ---------- Pembayaran membership ----------

    // Menghitung periode tanpa menyimpan apa pun, untuk ditampilkan sebelum petugas menekan Bayar.
    public PembayaranAkses pratinjauPeriode(Pelanggan pelanggan, PaketAkses paket) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        pastikanPaketDipilih(paket);

        LocalDate mulai = hitungTanggalMulai(pelanggan);
        LocalDate akhir = paket.hitungTanggalAkhir(mulai); // polimorfisme: cara hitung tergantung jenis paket

        return new PembayaranAkses(null, pelanggan, paket.getId(), Sesi.adminLogin.getId(),
                Sesi.adminLogin.getNama(), paket.getNama(), paket.getHarga(), null, mulai, akhir);
    }

    // Mencatat pembayaran lunas dan mengembalikan pembayaran yang sudah tersimpan (siap dicetak).
    public PembayaranAkses bayarMembership(Pelanggan pelanggan, PaketAkses paket) {
        PembayaranAkses pembayaran = pratinjauPeriode(pelanggan, paket);

        // profil yang diarsipkan tidak boleh membeli akses baru
        if (pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Profil pelanggan diarsipkan. Aktifkan dulu sebelum membeli akses.");
        }
        // paket harian hanya dijual lewat tombol "Jual visit harian" di layar Pelanggan
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

    public List<PembayaranAkses> riwayatPembayaran(Pelanggan pelanggan) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        try {
            return aksesDAO.daftarPembayaran(pelanggan);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Akses yang berlaku hari ini, atau null. Dipakai check-in, visit, dan panel Pelanggan.
    public PembayaranAkses cariAksesAktifHariIni(Pelanggan pelanggan) {
        pastikanLogin();
        try {
            return aksesDAO.cariAksesAktif(pelanggan, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Jumlah pelanggan yang boleh masuk hari ini (untuk Beranda).
    public int jumlahPelangganBeraksesHariIni() {
        pastikanLogin();
        try {
            return aksesDAO.hitungPelangganBerakses(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // ---------- Untuk Visit (dipakai PelangganService) ----------

    // Paket harian yang masih dijual, atau null jika tidak ada.
    public PaketAkses cariPaketHarian() {
        for (PaketAkses paket : daftarPaket()) {
            if (paket.getJenis().equals("HARIAN")) {
                return paket;
            }
        }
        return null;
    }

    // Menyusun pembayaran harian (berlaku hari ini saja) tanpa menyimpannya.
    // Penyimpanan dilakukan PelangganService bersama check-in dalam satu transaksi.
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

    // ---------- Aturan tanggal mulai ----------

    // Mengambil data dari database, lalu menyerahkan keputusan ke tentukanTanggalMulai.
    private LocalDate hitungTanggalMulai(Pelanggan pelanggan) {
        try {
            LocalDate akhirTerjauh = aksesDAO.cariTanggalAkhirTerjauh(pelanggan.getId());
            return tentukanTanggalMulai(akhirTerjauh, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    // Aturan tanggal mulai membership. Tidak menyentuh database, jadi bisa dites langsung (AksesServiceTest).
    // Belum punya akses, atau semua aksesnya sudah lewat -> mulai hari ini.
    // Masih punya akses (termasuk visit hari ini) -> mulai sehari setelah akses paling jauh berakhir.
    public static LocalDate tentukanTanggalMulai(LocalDate akhirTerjauh, LocalDate hariIni) {
        if (akhirTerjauh == null || akhirTerjauh.isBefore(hariIni)) {
            return hariIni;
        }
        return akhirTerjauh.plusDays(1);
    }

    // ---------- Pemeriksaan kecil ----------

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

    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa terhubung ke database: " + e.getMessage());
    }
}
