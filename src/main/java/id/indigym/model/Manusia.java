package id.indigym.model;

public abstract class Manusia {

    private Long id;
    private String nama;

    public Manusia(Long id, String nama) {
        this.id = id;
        this.nama = nama;
    }

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