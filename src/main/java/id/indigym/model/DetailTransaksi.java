package id.indigym.model;

import java.math.BigDecimal;

public class DetailTransaksi {

    private Long produkId;
    private String namaProduk;
    private BigDecimal hargaSatuan;
    private int jumlah;

    public DetailTransaksi(Long produkId, String namaProduk, BigDecimal hargaSatuan, int jumlah) {
        this.produkId = produkId;
        this.namaProduk = namaProduk;
        this.hargaSatuan = hargaSatuan;
        this.jumlah = jumlah;
    }

    public BigDecimal hitungSubtotal() {
        return hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
    }

    public String getHargaSatuanTeks() {
        return Rupiah.format(hargaSatuan);
    }

    public String getSubtotalTeks() {
        return Rupiah.format(hitungSubtotal());
    }

    public Long getProdukId() {
        return produkId;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public BigDecimal getHargaSatuan() {
        return hargaSatuan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
