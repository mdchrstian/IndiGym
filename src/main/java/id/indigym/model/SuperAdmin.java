package id.indigym.model;

// SuperAdmin adalah Admin yang punya kewenangan lebih.
// Method yang ditulis ulang (override) memakai @Override: inilah contoh polimorfisme.
public class SuperAdmin extends Admin {

    public SuperAdmin(Long id, String nama, String username, String passwordHash, boolean aktif) {
        super(id, nama, username, passwordHash, aktif);
    }

    @Override
    public boolean bolehKelolaAdmin() {
        return true;
    }

    @Override
    public String getRole() {
        return "SUPERADMIN";
    }
}
