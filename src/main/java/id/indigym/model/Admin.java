package id.indigym.model;

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

    public boolean bolehKelolaAdmin() {
        return false;
    }

    public String getRole() {
        return "ADMIN";
    }

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