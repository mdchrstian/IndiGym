package id.indigym.service;

import id.indigym.database.PelangganDAO;
import id.indigym.model.CheckIn;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PelangganService {

    private PelangganDAO pelangganDAO = new PelangganDAO();
    private AksesService aksesService = new AksesService();

    public Pelanggan daftar(String nama, String kontak) {
        pastikanLogin();
        pastikanNamaValid(nama);
        Pelanggan pelanggan = new Pelanggan(null, null, nama.trim(), rapikanKontak(kontak), false);
        try {
            pelangganDAO.simpan(pelanggan);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
        return pelanggan;
    }

    public List<Pelanggan> cari(String kataKunci) {
        pastikanLogin();
        if (kataKunci == null) {
            kataKunci = "";
        }
        try {
            return pelangganDAO.cari(kataKunci.trim());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void ubah(Pelanggan pelanggan, String nama, String kontak) {
        pastikanLogin();
        pastikanDipilih(pelanggan);
        pastikanNamaValid(nama);
        try {
            pelangganDAO.ubah(pelanggan.getId(), nama.trim(), rapikanKontak(kontak));
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void arsipkan(Pelanggan pelanggan) {
        pastikanLogin();
        pastikanDipilih(pelanggan);
        if (pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Pelanggan ini sudah diarsipkan.");
        }
        ubahArsip(pelanggan, true);
    }

    public void aktifkan(Pelanggan pelanggan) {
        pastikanLogin();
        pastikanDipilih(pelanggan);
        if (!pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Pelanggan ini sudah aktif.");
        }
        ubahArsip(pelanggan, false);
    }


    public CheckIn checkIn(String kode, boolean kunjunganTambahanDisetujui) {
        pastikanLogin();
        Pelanggan pelanggan = cariPelangganByKode(kode);

        if (pelanggan.isDiarsipkan()) {
            return catatDitolak(pelanggan, "Profil pelanggan diarsipkan.");
        }
        PembayaranAkses akses = aksesService.cariAksesAktifHariIni(pelanggan);
        if (akses == null) {
            return catatDitolak(pelanggan, "Tidak punya akses hari ini.");
        }
        if (sudahCheckInHariIni(pelanggan) && !kunjunganTambahanDisetujui) {
            return null;
        }

        CheckIn checkIn = buatCheckIn(pelanggan, CheckIn.DIIZINKAN, akses.getId(), null);
        simpanCheckIn(checkIn);
        return checkIn;
    }


    public PembayaranAkses beliVisit(String kode) {
        pastikanLogin();
        Pelanggan pelanggan = cariPelangganByKode(kode);

        if (pelanggan.isDiarsipkan()) {
            throw new IllegalArgumentException("Profil pelanggan diarsipkan. Aktifkan dulu sebelum membeli visit.");
        }
        if (aksesService.cariAksesAktifHariIni(pelanggan) != null) {
            throw new IllegalArgumentException("Pelanggan sudah punya akses hari ini. Gunakan Check-in biasa.");
        }

        PembayaranAkses pembayaran = aksesService.siapkanPembayaranHarian(pelanggan);
        CheckIn checkIn = buatCheckIn(pelanggan, CheckIn.DIIZINKAN, null, null);
        try {
            pelangganDAO.simpanVisitDanCheckIn(pembayaran, checkIn);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
        return pembayaran;
    }

    public List<CheckIn> riwayatCheckIn(String kode) {
        pastikanLogin();
        Pelanggan pelanggan = cariPelangganByKode(kode);
        try {
            return pelangganDAO.daftarCheckIn(pelanggan);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }


    public int jumlahCheckInHariIni() {
        pastikanLogin();
        try {
            return pelangganDAO.hitungCheckInDiizinkan(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }


    public Pelanggan cariPelangganByKode(String kode) {
        if (kode == null || kode.isBlank()) {
            throw new IllegalArgumentException("Masukkan kode pelanggan dulu.");
        }
        try {
            Pelanggan pelanggan = pelangganDAO.cariByKode(kode.trim());
            if (pelanggan == null) {
                throw new IllegalArgumentException("Kode pelanggan tidak ditemukan.");
            }
            return pelanggan;
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private CheckIn catatDitolak(Pelanggan pelanggan, String alasan) {
        CheckIn checkIn = buatCheckIn(pelanggan, CheckIn.DITOLAK, null, alasan);
        simpanCheckIn(checkIn);
        return checkIn;
    }

    private CheckIn buatCheckIn(Pelanggan pelanggan, String hasil, Long pembayaranId, String alasan) {
        return new CheckIn(null, pelanggan, Sesi.adminLogin.getId(), Sesi.adminLogin.getNama(),
                pembayaranId, LocalDateTime.now(), hasil, alasan);
    }

    private void simpanCheckIn(CheckIn checkIn) {
        try {
            pelangganDAO.simpanCheckIn(checkIn);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private boolean sudahCheckInHariIni(Pelanggan pelanggan) {
        try {
            return pelangganDAO.sudahCheckInDiizinkan(pelanggan.getId(), LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private void ubahArsip(Pelanggan pelanggan, boolean diarsipkan) {
        try {
            pelangganDAO.ubahArsip(pelanggan.getId(), diarsipkan);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private void pastikanLogin() {
        if (!Sesi.sudahLogin()) {
            throw new IllegalArgumentException("Silakan login dulu.");
        }
    }

    private void pastikanDipilih(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pilih pelanggan di tabel dulu.");
        }
    }

    private void pastikanNamaValid(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama pelanggan wajib diisi.");
        }
    }

    private String rapikanKontak(String kontak) {
        if (kontak == null || kontak.isBlank()) {
            return null;
        }
        return kontak.trim();
    }

    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa terhubung ke database: " + e.getMessage());
    }
}