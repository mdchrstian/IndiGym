package id.indigym.model;

public class Pelanggan extends Manusia {
    private String kode;
    private String kontak;
    private String diarsipkan; 

    public Pelanggan (Long id, String kode, String nama, String kontak, boolean diarsipkan) {
        super(id, nama);
        this.kode = kode;
        this.kontak = kontak;
        this.diarsipkan = diarsipkan;
    }
}
