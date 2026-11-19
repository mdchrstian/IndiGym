package id.indigym.database;

import id.indigym.model.Admin;
import id.indigym.model.SuperAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AkunDAO {

    public Admin cariByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM akun_admin WHERE username = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, username);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaBaris(hasil);
            }
            return null;
        }
    }

    public List<Admin> daftarSemua() throws SQLException {
        String sql = "SELECT * FROM akun_admin ORDER BY id";
        List<Admin> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaBaris(hasil));
            }
        }
        return daftar;
    }

    public void simpan(Admin admin) throws SQLException {
        String sql = "INSERT INTO akun_admin (nama, username, password_hash, role, aktif) VALUES (?, ?, ?, ?, ?)";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, admin.getNama());
            perintah.setString(2, admin.getUsername());
            perintah.setString(3, admin.getPasswordHash());
            perintah.setString(4, admin.getRole());
            perintah.setBoolean(5, admin.isAktif());
            perintah.executeUpdate();
        }
    }

    public void ubahPasswordHash(Long id, String passwordHashBaru) throws SQLException {
        String sql = "UPDATE akun_admin SET password_hash = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, passwordHashBaru);
            perintah.setLong(2, id);
            perintah.executeUpdate();
        }
    }

    public void ubahAktif(Long id, boolean aktif) throws SQLException {
        String sql = "UPDATE akun_admin SET aktif = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setBoolean(1, aktif);
            perintah.setLong(2, id);
            perintah.executeUpdate();
        }
    }

    private Admin bacaBaris(ResultSet hasil) throws SQLException {
        Long id = hasil.getLong("id");
        String nama = hasil.getString("nama");
        String username = hasil.getString("username");
        String passwordHash = hasil.getString("password_hash");
        boolean aktif = hasil.getBoolean("aktif");
        String role = hasil.getString("role");

        if (role.equals("SUPERADMIN")) {
            return new SuperAdmin(id, nama, username, passwordHash, aktif);
        }
        return new Admin(id, nama, username, passwordHash, aktif);
    }
}