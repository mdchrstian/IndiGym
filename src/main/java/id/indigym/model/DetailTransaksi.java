package id.indigym.model;

import java.math.BigDecimal;

// Satu baris produk di dalam transaksi (atau di keranjang).
// Nama dan harga disalin dari produk agar struk lama tidak berubah saat katalog diubah.
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

    // Subtotal = jumlah unit x harga satuan.
    public BigDecimal hitungSubtotal() {
        return hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
    }

    // Teks untuk kolom tabel dan struk, contoh "Rp 5.000".
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
