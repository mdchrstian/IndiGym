package id.indigym.service;

import id.indigym.database.AkunDAO;
import id.indigym.model.Admin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class AkunService {

    private AkunDAO akunDAO = new AkunDAO();

    public Admin login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username dan password wajib diisi.");
        }

        Admin admin = cariAdmin(username.trim());

        if (admin == null || !admin.getPasswordHash().equals(hashPassword(password))) {
            throw new IllegalArgumentException("Username atau password salah.");
        }
        if (!admin.isAktif()) {
            throw new IllegalArgumentException("Akun ini sudah dinonaktifkan.");
        }

        Sesi.adminLogin = admin;
        return admin;
    }

    public void logout() {
        Sesi.adminLogin = null;
    }

    public List<Admin> daftarAdmin() {
        pastikanSuperAdmin();
        try {
            return akunDAO.daftarSemua();
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void tambahAdmin(String nama, String username, String password) {
        pastikanSuperAdmin();
        if (nama == null || nama.isBlank() || username == null || username.isBlank()) {
            throw new IllegalArgumentException("Nama dan username wajib diisi.");
        }
        pastikanPasswordValid(password);

        if (cariAdmin(username.trim()) != null) {
            throw new IllegalArgumentException("Username sudah dipakai.");
        }
        Admin adminBaru = new Admin(null, nama.trim(), username.trim(), hashPassword(password), true);
        try {
            akunDAO.simpan(adminBaru);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void resetPassword(Admin target, String passwordBaru) {
        pastikanSuperAdmin();
        pastikanTargetAdminBiasa(target);
        pastikanPasswordValid(passwordBaru);
        try {
            akunDAO.ubahPasswordHash(target.getId(), hashPassword(passwordBaru));
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void nonaktifkanAdmin(Admin target) {
        pastikanSuperAdmin();
        pastikanTargetAdminBiasa(target);
        try {
            akunDAO.ubahAktif(target.getId(), false);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void gantiPasswordSendiri(String passwordLama, String passwordBaru) {
        Admin saya = Sesi.adminLogin;
        if (saya == null) {
            throw new IllegalArgumentException("Silakan login dulu.");
        }
        if (passwordLama == null || !saya.getPasswordHash().equals(hashPassword(passwordLama))) {
            throw new IllegalArgumentException("Password lama salah.");
        }
        pastikanPasswordValid(passwordBaru);

        String hashBaru = hashPassword(passwordBaru);
        try {
            akunDAO.ubahPasswordHash(saya.getId(), hashBaru);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
        saya.setPasswordHash(hashBaru); 
    }

    private void pastikanSuperAdmin() {
        if (!Sesi.sudahLogin() || !Sesi.adminLogin.bolehKelolaAdmin()) {
            throw new IllegalArgumentException("Hanya SuperAdmin yang boleh melakukan ini.");
        }
    }

    private void pastikanTargetAdminBiasa(Admin target) {
        if (target == null) {
            throw new IllegalArgumentException("Pilih akun Admin di tabel dulu.");
        }
        if (target.bolehKelolaAdmin()) {
            throw new IllegalArgumentException("Akun SuperAdmin tidak boleh diubah di sini.");
        }
    }

    private void pastikanPasswordValid(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }
    }

    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa terhubung ke database: " + e.getMessage());
    }

    public String hashPassword(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hasil = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hasil) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 tidak tersedia.");
        }
    }

    private Admin cariAdmin(String username) {
        try {
            return akunDAO.cariByUsername(username);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }
}