package id.indigym.service;

import id.indigym.database.PenjualanDAO;
import id.indigym.model.DetailTransaksi;
import id.indigym.model.Pelanggan;
import id.indigym.model.Produk;
import id.indigym.model.Transaksi;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PenjualanService {

    private PenjualanDAO penjualanDAO = new PenjualanDAO();
    private PelangganService pelangganService = new PelangganService();

    public List<Produk> cariProduk(String kataKunci) {
        pastikanLogin();
        if (kataKunci == null) {
            kataKunci = "";
        }
        try {
            return penjualanDAO.cariProduk(kataKunci.trim());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void tambahProduk(String kode, String nama, BigDecimal harga, int stokAwal) {
        pastikanLogin();
        if (kode == null || kode.isBlank() || nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Kode dan nama produk wajib diisi.");
        }
        pastikanHargaValid(harga);
        if (stokAwal < 0) {
            throw new IllegalArgumentException("Stok awal tidak boleh negatif.");
        }
        try {
            // kode produk harus unik
            if (penjualanDAO.cariProdukByKode(kode.trim()) != null) {
                throw new IllegalArgumentException("Kode produk sudah dipakai.");
            }
            penjualanDAO.simpanProduk(new Produk(null, kode.trim(), nama.trim(), harga, stokAwal, false));
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void ubahProduk(Produk produk, String nama, BigDecimal harga) {
        pastikanLogin();
        pastikanProdukDipilih(produk);
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama produk wajib diisi.");
        }
        pastikanHargaValid(harga);
        try {
            penjualanDAO.ubahProduk(produk.getId(), nama.trim(), harga);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void tambahStok(Produk produk, int jumlah) {
        pastikanLogin();
        pastikanProdukDipilih(produk);
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah stok yang ditambahkan harus lebih dari 0.");
        }
        try {
            penjualanDAO.tambahStok(produk.getId(), jumlah);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void arsipkanProduk(Produk produk) {
        pastikanLogin();
        pastikanProdukDipilih(produk);
        try {
            penjualanDAO.arsipkanProduk(produk.getId());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void tambahKeKeranjang(List<DetailTransaksi> keranjang, Produk produk, int jumlah) {
        pastikanProdukDipilih(produk);
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        }

        if (produk.isDiarsipkan()) {
            throw new IllegalArgumentException("Produk ini sudah diarsipkan.");
        }

        DetailTransaksi baris = cariBaris(keranjang, produk.getId());
        int jumlahBaru = jumlah;
        if (baris != null) {
            jumlahBaru = baris.getJumlah() + jumlah;
        }

        if (jumlahBaru > produk.getStok()) {
            throw new IllegalArgumentException("Stok " + produk.getNama() + " hanya " + produk.getStok() + ".");
        }

        if (baris == null) {
            keranjang.add(new DetailTransaksi(produk.getId(), produk.getNama(), produk.getHarga(), jumlah));
        } else {
            baris.setJumlah(jumlahBaru);
        }
    }

    public BigDecimal hitungTotal(List<DetailTransaksi> keranjang) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetailTransaksi baris : keranjang) {
            total = total.add(baris.hitungSubtotal());
        }
        return total;
    }

    public Transaksi checkout(List<DetailTransaksi> keranjang, String kodePelanggan) {
        pastikanLogin();
        if (keranjang == null || keranjang.isEmpty()) {
            throw new IllegalArgumentException("Keranjang masih kosong.");
        }

        Pelanggan pelanggan = null;
        if (kodePelanggan != null && !kodePelanggan.isBlank()) {
            pelanggan = pelangganService.cariPelangganByKode(kodePelanggan);
        }

        List<DetailTransaksi> detailBaru = periksaUlangKeranjang(keranjang);
        Transaksi transaksi = new Transaksi(null, pelanggan, Sesi.adminLogin.getId(), Sesi.adminLogin.getNama(),
                LocalDateTime.now(), Transaksi.LUNAS, null, detailBaru);
        try {
            penjualanDAO.simpanTransaksi(transaksi);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
        return transaksi;
    }

    public List<Transaksi> riwayat() {
        pastikanLogin();
        try {
            return penjualanDAO.daftarTransaksi();
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public void batalkan(Transaksi transaksi, String alasan) {
        pastikanLogin();
        if (transaksi == null) {
            throw new IllegalArgumentException("Pilih transaksi di tabel dulu.");
        }
        if (alasan == null || alasan.isBlank()) {
            throw new IllegalArgumentException("Alasan pembatalan wajib diisi.");
        }
        // transaksi yang sudah batal tidak boleh dibatalkan lagi (stok tidak boleh bertambah dua kali)
        if (transaksi.sudahDibatalkan()) {
            throw new IllegalArgumentException("Transaksi ini sudah dibatalkan.");
        }
        try {
            penjualanDAO.batalkanTransaksi(transaksi.getId(), alasan.trim());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public BigDecimal totalPenjualanHariIni() {
        pastikanLogin();
        try {
            return penjualanDAO.totalPenjualan(LocalDate.now());
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private List<DetailTransaksi> periksaUlangKeranjang(List<DetailTransaksi> keranjang) {
        List<DetailTransaksi> hasil = new ArrayList<>();
        for (DetailTransaksi baris : keranjang) {
            Produk terbaru = bacaProduk(baris.getProdukId());

            if (terbaru == null || terbaru.isDiarsipkan()) {
                throw new IllegalArgumentException("Produk " + baris.getNamaProduk() + " sudah tidak dijual.");
            }

            if (terbaru.getStok() < baris.getJumlah()) {
                throw new IllegalArgumentException("Stok " + terbaru.getNama() + " tidak cukup (tersisa "
                        + terbaru.getStok() + ").");
            }
            hasil.add(new DetailTransaksi(terbaru.getId(), terbaru.getNama(), terbaru.getHarga(), baris.getJumlah()));
        }
        return hasil;
    }

    private Produk bacaProduk(Long id) {
        try {
            return penjualanDAO.cariProdukById(id);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    private DetailTransaksi cariBaris(List<DetailTransaksi> keranjang, Long produkId) {
        for (DetailTransaksi baris : keranjang) {
            if (baris.getProdukId().equals(produkId)) {
                return baris;
            }
        }
        return null;
    }

    private void pastikanLogin() {
        if (!Sesi.sudahLogin()) {
            throw new IllegalArgumentException("Silakan login dulu.");
        }
    }

    private void pastikanProdukDipilih(Produk produk) {
        if (produk == null) {
            throw new IllegalArgumentException("Pilih produk di tabel dulu.");
        }
    }

    private void pastikanHargaValid(BigDecimal harga) {
        if (harga == null || harga.signum() < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
    }

    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa menyimpan ke database: " + e.getMessage());
    }
}
