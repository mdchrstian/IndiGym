package id.indigym.model;

import java.math.BigDecimal;

public class Produk {

    private Long id;
    private String kode;
    private String nama;
    private BigDecimal harga;
    private int stok;
    private boolean diarsipkan;

    public Produk(Long id, String kode, String nama, BigDecimal harga, int stok, boolean diarsipkan) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        setStok(stok);
        this.diarsipkan = diarsipkan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    // Teks harga untuk tabel, contoh "Rp 5.000".
    public String getHargaTeks() {
        return Rupiah.format(harga);
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
        this.stok = stok;
    }

    public boolean isDiarsipkan() {
        return diarsipkan;
    }
}
