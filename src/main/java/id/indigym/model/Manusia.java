package id.indigym.model;

// Class dasar untuk semua orang di aplikasi (Admin dan Pelanggan).
// "abstract" artinya class ini tidak boleh dibuat langsung dengan "new Manusia()".
public abstract class Manusia {

    private Long id;
    private String nama;

    public Manusia(Long id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Setiap turunan wajib menulis sendiri cara menampilkan infonya.
    public abstract String tampilkanInfo();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
