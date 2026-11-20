package id.indigym.database;

import id.indigym.model.CheckIn;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO {

    public void simpan(Pelanggan pelanggan) throws SQLException {
        String sqlSimpan = "INSERT INTO pelanggan (nama, kontak, diarsipkan) VALUES (?, ?, FALSE)";
        String sqlKode = "UPDATE pelanggan SET kode = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection()) {
            koneksi.setAutoCommit(false);
            try (PreparedStatement simpan = koneksi.prepareStatement(sqlSimpan, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ubahKode = koneksi.prepareStatement(sqlKode)) {
                simpan.setString(1, pelanggan.getNama());
                simpan.setString(2, pelanggan.getKontak());
                simpan.executeUpdate();

                ResultSet kunci = simpan.getGeneratedKeys();
                kunci.next();
                Long idBaru = kunci.getLong(1);
                String kodeBaru = "PLG" + idBaru;

                ubahKode.setString(1, kodeBaru);
                ubahKode.setLong(2, idBaru);
                ubahKode.executeUpdate();
                koneksi.commit();

                pelanggan.setId(idBaru);
                pelanggan.setKode(kodeBaru);
            } catch (SQLException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    public List<Pelanggan> cari(String kataKunci) throws SQLException {
        String sql = "SELECT * FROM pelanggan WHERE nama LIKE ? OR kode LIKE ? ORDER BY id";
        List<Pelanggan> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, "%" + kataKunci + "%");
            perintah.setString(2, "%" + kataKunci + "%");
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaBaris(hasil));
            }
        }
        return daftar;
    }

    public Pelanggan cariByKode(String kode) throws SQLException {
        String sql = "SELECT * FROM pelanggan WHERE kode = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, kode);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaBaris(hasil);
            }
            return null;
        }
    }

    public void ubah(Long id, String nama, String kontak) throws SQLException {
        String sql = "UPDATE pelanggan SET nama = ?, kontak = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, nama);
            perintah.setString(2, kontak);
            perintah.setLong(3, id);
            perintah.executeUpdate();
        }
    }

    public void ubahArsip(Long id, boolean diarsipkan) throws SQLException {
        String sql = "UPDATE pelanggan SET diarsipkan = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setBoolean(1, diarsipkan);
            perintah.setLong(2, id);
            perintah.executeUpdate();
        }
    }


    public void simpanCheckIn(CheckIn checkIn) throws SQLException {
        try (Connection koneksi = Database.getConnection()) {
            simpanCheckIn(koneksi, checkIn);
        }
    }

    public boolean sudahCheckInDiizinkan(Long pelangganId, LocalDate tanggal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM checkin WHERE pelanggan_id = ? AND hasil = 'DIIZINKAN' AND DATE(waktu) = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelangganId);
            perintah.setObject(2, tanggal);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getInt(1) > 0;
        }
    }

    public int hitungCheckInDiizinkan(LocalDate tanggal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM checkin WHERE hasil = 'DIIZINKAN' AND DATE(waktu) = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setObject(1, tanggal);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getInt(1);
        }
    }

    public List<CheckIn> daftarCheckIn(Pelanggan pelanggan) throws SQLException {
        String sql = "SELECT c.*, a.nama AS nama_admin FROM checkin c JOIN akun_admin a ON a.id = c.admin_id "
                + "WHERE c.pelanggan_id = ? ORDER BY c.id DESC";
        List<CheckIn> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelanggan.getId());
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaCheckIn(hasil, pelanggan));
            }
        }
        return daftar;
    }

    public void simpanVisitDanCheckIn(PembayaranAkses pembayaran, CheckIn checkIn) throws SQLException {
        try (Connection koneksi = Database.getConnection()) {
            koneksi.setAutoCommit(false);
            try {
                new AksesDAO().simpanPembayaran(koneksi, pembayaran);
                checkIn.setPembayaranId(pembayaran.getId());
                simpanCheckIn(koneksi, checkIn);
                koneksi.commit();
            } catch (SQLException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    private void simpanCheckIn(Connection koneksi, CheckIn checkIn) throws SQLException {
        String sql = "INSERT INTO checkin (pelanggan_id, admin_id, pembayaran_id, waktu, hasil, alasan) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            perintah.setLong(1, checkIn.getPelanggan().getId());
            perintah.setLong(2, checkIn.getAdminId());
            perintah.setObject(3, checkIn.getPembayaranId());
            perintah.setObject(4, checkIn.getWaktu());
            perintah.setString(5, checkIn.getHasil());
            perintah.setString(6, checkIn.getAlasan());
            perintah.executeUpdate();

            ResultSet kunci = perintah.getGeneratedKeys();
            kunci.next();
            checkIn.setId(kunci.getLong(1));
        }
    }

    private CheckIn bacaCheckIn(ResultSet hasil, Pelanggan pelanggan) throws SQLException {
        return new CheckIn(
                hasil.getLong("id"),
                pelanggan,
                hasil.getLong("admin_id"),
                hasil.getString("nama_admin"),
                hasil.getObject("pembayaran_id", Long.class),
                hasil.getObject("waktu", LocalDateTime.class),
                hasil.getString("hasil"),
                hasil.getString("alasan"));
    }

    private Pelanggan bacaBaris(ResultSet hasil) throws SQLException {
        return new Pelanggan(
                hasil.getLong("id"),
                hasil.getString("kode"),
                hasil.getString("nama"),
                hasil.getString("kontak"),
                hasil.getBoolean("diarsipkan"));
    }
}