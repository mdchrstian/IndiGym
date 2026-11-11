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

    public String getstatus () {
        if (diarsipkan) {
            return "diarsipkan";
        }
        return "aktif";
    }

    @Override 
    public String tampilkaninfo() {
        return kode + "-" + getnama();
    }

    public String getkode() {
        return kode;
    }

    public void setkode(String kode) {
        this.kode = kode;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak() {
        this.kontak = kontak;
    }

    public boolean isDiarsipkan() {
        return diarsipkan;
    }

    public void setDiarsipkan() {
        this.diarsipkan = diarsipkan;
    }
}
