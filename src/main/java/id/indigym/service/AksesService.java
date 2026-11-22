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

public class AksesService {

    private AksesDAO aksesDAO = new AksesDAO();

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

    public void arsipkanPaket(PaketAkses paket) {
        pastikanLogin();
        if (paket == null) {
            throw new IllegalArgumentException("Pilih paket dulu.");
        }
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            throw new IllegalArgumentException("Paket harian tidak boleh diarsipkan.");
        }
        try {
            aksesDAO.arsipkanPaket(paket.getId());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public PembayaranAkses pratinjauPeriode(Pelanggan pelanggan, PaketAkses paket) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        pastikanPaketDipilih(paket);

        LocalDate mulai = hitungTanggalMulai(pelanggan);
        LocalDate akhir = paket.hitungTanggalAkhir(mulai);

        return new PembayaranAkses(null, pelanggan, paket.getId(), Sesi.adminLogin.getId(),
                Sesi.adminLogin.getNama(), paket.getNama(), paket.getHarga(), null, mulai, akhir);
    }

    public PembayaranAkses bayarMembership(Pelanggan pelanggan, PaketAkses paket) {
        PembayaranAkses pembayaran = pratinjauPeriode(pelanggan, paket);
        if (pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Profil pelanggan diarsipkan. Aktifkan dulu sebelum membeli akses.");
        }
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            throw new IllegalArgumentException("Paket harian dijual lewat tombol Jual visit harian di layar Pelanggan.");
        }
        PembayaranAkses membershipLama = cariMembershipBelumBerakhir(pelanggan);
        if (membershipLama != null) {
            throw new IllegalArgumentException(pelanggan.getNama() + " masih punya membership ("
                    + membershipLama.getPeriode() + "). Membership baru bisa dibeli setelah itu berakhir.");
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

    public PembayaranAkses cariAksesAktifHariIni(Pelanggan pelanggan) {
        pastikanLogin();
        try {
            return aksesDAO.cariAksesAktif(pelanggan, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public PembayaranAkses cariMembershipBelumBerakhir(Pelanggan pelanggan) {
        pastikanLogin();
        pastikanPelangganDipilih(pelanggan);
        try {
            return aksesDAO.cariMembershipBelumBerakhir(pelanggan, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public List<PembayaranAkses> daftarMembershipAktif() {
        pastikanLogin();
        try {
            return aksesDAO.daftarMembershipBelumBerakhir(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public int jumlahPelangganBeraksesHariIni() {
        pastikanLogin();
        try {
            return aksesDAO.hitungPelangganBerakses(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public PaketAkses cariPaketHarian() {
        for (PaketAkses paket : daftarPaket()) {
            if (paket.getJenis().equals("HARIAN")) {
                return paket;
            }
        }
        return null;
    }

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

    private LocalDate hitungTanggalMulai(Pelanggan pelanggan) {
        try {
            LocalDate akhirTerjauh = aksesDAO.cariTanggalAkhirTerjauh(pelanggan.getId());
            return tentukanTanggalMulai(akhirTerjauh, LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public static LocalDate tentukanTanggalMulai(LocalDate akhirTerjauh, LocalDate hariIni) {
        if (akhirTerjauh == null || akhirTerjauh.isBefore(hariIni)) {
            return hariIni;
        }
        return akhirTerjauh.plusDays(1);
    }

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
