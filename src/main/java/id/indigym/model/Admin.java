package id.indigym.model;

// Petugas gym biasa. Admin mewarisi (extends) Manusia.
public class Admin extends Manusia {

    private String username;
    private String passwordHash;
    private boolean aktif;

    public Admin(Long id, String nama, String username, String passwordHash, boolean aktif) {
        super(id, nama);
        this.username = username;
        this.passwordHash = passwordHash;
        this.aktif = aktif;
    }

    // Admin biasa tidak boleh mengelola akun Admin lain.
    public boolean bolehKelolaAdmin() {
        return false;
    }

    // Nama peran yang disimpan di database.
    public String getRole() {
        return "ADMIN";
    }

    // Teks untuk kolom Status di tabel.
    public String getStatus() {
        if (aktif) {
            return "Aktif";
        }
        return "Nonaktif";
    }

    @Override
    public String tampilkanInfo() {
        return getNama() + " (" + username + ") - " + getRole();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }
}
