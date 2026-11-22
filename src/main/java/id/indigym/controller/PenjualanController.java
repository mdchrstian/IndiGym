package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.DetailTransaksi;
import id.indigym.model.Produk;
import id.indigym.model.Rupiah;
import id.indigym.model.Transaksi;
import id.indigym.service.PenjualanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;
import java.util.List;

public class PenjualanController {

    @FXML
    private Label labelPesan;

    // tab Kasir
    @FXML
    private TextField fieldCariKasir;
    @FXML
    private TableView<Produk> tabelProdukKasir;
    @FXML
    private TextField fieldJumlah;
    @FXML
    private TableView<DetailTransaksi> tabelKeranjang;
    @FXML
    private Label labelTotal;
    @FXML
    private TextField fieldKodePelanggan;
    @FXML
    private Button tombolCheckout;

    // tab Produk dan stok
    @FXML
    private TableView<Produk> tabelProdukKelola;
    @FXML
    private Label labelProdukTerpilih;
    @FXML
    private TextField fieldNamaProduk;
    @FXML
    private TextField fieldHargaProduk;
    @FXML
    private Button tombolSimpanProduk;
    @FXML
    private TextField fieldTambahStok;
    @FXML
    private Button tombolTambahStok;
    @FXML
    private Button tombolArsipProduk;
    @FXML
    private TextField fieldKodeProduk;
    @FXML
    private TextField fieldNamaProdukBaru;
    @FXML
    private TextField fieldHargaProdukBaru;
    @FXML
    private TextField fieldStokAwal;

    // tab Riwayat
    @FXML
    private TableView<Transaksi> tabelRiwayat;
    @FXML
    private TextField fieldAlasanBatal;

    private PenjualanService penjualanService = new PenjualanService();

    private ObservableList<DetailTransaksi> keranjang = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tabelKeranjang.setItems(keranjang);
        muatProduk();
        muatRiwayat();
        perbaruiTotal();
    }

    // Tab Kasir

    @FXML
    private void cariProduk() {
        muatProduk();
    }

    @FXML
    private void klikProdukKasir(MouseEvent event) {
        if (event.getClickCount() == 2) {
            masukkanKeKeranjang(tabelProdukKasir.getSelectionModel().getSelectedItem(), 1);
        }
    }

    @FXML
    private void tambahKeKeranjang() {
        try {
            int jumlah = Integer.parseInt(fieldJumlah.getText().trim());
            masukkanKeKeranjang(tabelProdukKasir.getSelectionModel().getSelectedItem(), jumlah);
        } catch (NumberFormatException e) {
            MainApp.pesanGagal(labelPesan, "Jumlah harus angka bulat, contoh 2.");
        }
    }

    private void masukkanKeKeranjang(Produk produk, int jumlah) {
        try {
            penjualanService.tambahKeKeranjang(keranjang, produk, jumlah);
            tabelKeranjang.refresh();
            perbaruiTotal();
            fieldJumlah.setText("1");
            MainApp.pesanBerhasil(labelPesan, jumlah + " x " + produk.getNama() + " masuk keranjang.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void hapusDariKeranjang() {
        DetailTransaksi baris = tabelKeranjang.getSelectionModel().getSelectedItem();
        if (baris == null) {
            MainApp.pesanGagal(labelPesan, "Pilih baris keranjang yang mau dihapus.");
            return;
        }
        keranjang.remove(baris);
        perbaruiTotal();
    }

    @FXML
    private void kosongkanKeranjang() {
        keranjang.clear();
        perbaruiTotal();
    }

    @FXML
    private void checkout() {
        try {
            String total = Rupiah.format(penjualanService.hitungTotal(keranjang));
            if (!MainApp.konfirmasi("Terima pembayaran " + total + "?")) {
                return;
            }
            Transaksi transaksi = penjualanService.checkout(keranjang, fieldKodePelanggan.getText());
            keranjang.clear();
            fieldKodePelanggan.clear();
            perbaruiTotal();
            muatProduk();
            muatRiwayat();
            MainApp.pesanBerhasil(labelPesan, "Penjualan no. " + transaksi.getId() + " tersimpan, total " + transaksi.getTotalTeks() + ".");
            MainApp.tampilkanBukti(transaksi);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    // Tab Produk dan stok

    @FXML
    private void pilihProdukKelola() {
        Produk produk = tabelProdukKelola.getSelectionModel().getSelectedItem();
        if (produk == null) {
            return;
        }
        labelProdukTerpilih.setText("Dipilih: " + produk.getKode() + ", stok " + produk.getStok());
        fieldNamaProduk.setText(produk.getNama());
        fieldHargaProduk.setText(produk.getHarga().toPlainString());
        tombolSimpanProduk.setDisable(false);
        tombolTambahStok.setDisable(false);
        tombolArsipProduk.setDisable(false);
    }

    @FXML
    private void simpanPerubahanProduk() {
        try {
            BigDecimal harga = new BigDecimal(fieldHargaProduk.getText().trim());
            penjualanService.ubahProduk(tabelProdukKelola.getSelectionModel().getSelectedItem(), fieldNamaProduk.getText(), harga);
            muatProduk();
            MainApp.pesanBerhasil(labelPesan, "Produk disimpan.");
        } catch (NumberFormatException e) {
            MainApp.pesanGagal(labelPesan, "Harga harus berupa angka, contoh 5000.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void tambahStok() {
        try {
            int jumlah = Integer.parseInt(fieldTambahStok.getText().trim());
            Produk produk = tabelProdukKelola.getSelectionModel().getSelectedItem();
            penjualanService.tambahStok(produk, jumlah);
            fieldTambahStok.clear();
            muatProduk();
            MainApp.pesanBerhasil(labelPesan, "Stok " + produk.getNama() + " bertambah " + jumlah + ".");
        } catch (NumberFormatException e) {
            MainApp.pesanGagal(labelPesan, "Stok masuk harus angka bulat, contoh 10.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void arsipkanProduk() {
        try {
            Produk produk = tabelProdukKelola.getSelectionModel().getSelectedItem();
            if (produk == null) {
                MainApp.pesanGagal(labelPesan, "Pilih produk di tabel dulu.");
                return;
            }
            if (!MainApp.konfirmasi("Arsipkan " + produk.getNama() + "?\nProduk ini tidak bisa dijual lagi.")) {
                return;
            }
            penjualanService.arsipkanProduk(produk);
            muatProduk();
            MainApp.pesanBerhasil(labelPesan, produk.getNama() + " diarsipkan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void tambahProduk() {
        try {
            BigDecimal harga = new BigDecimal(fieldHargaProdukBaru.getText().trim());
            int stokAwal = Integer.parseInt(fieldStokAwal.getText().trim());
            penjualanService.tambahProduk(fieldKodeProduk.getText(), fieldNamaProdukBaru.getText(), harga, stokAwal);
            fieldKodeProduk.clear();
            fieldNamaProdukBaru.clear();
            fieldHargaProdukBaru.clear();
            fieldStokAwal.clear();
            muatProduk();
            MainApp.pesanBerhasil(labelPesan, "Produk ditambahkan.");
        } catch (NumberFormatException e) {
            MainApp.pesanGagal(labelPesan, "Harga dan stok awal harus berupa angka, contoh 5000 dan 20.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    //Tab Riwayat

    @FXML
    private void klikRiwayat(MouseEvent event) {
        if (event.getClickCount() == 2) {
            lihatStruk();
        }
    }

    @FXML
    private void lihatStruk() {
        Transaksi terpilih = tabelRiwayat.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            MainApp.pesanGagal(labelPesan, "Pilih transaksi di tabel dulu.");
            return;
        }
        MainApp.tampilkanBukti(terpilih);
    }

    @FXML
    private void batalkanTransaksi() {
        try {
            Transaksi terpilih = tabelRiwayat.getSelectionModel().getSelectedItem();
            if (terpilih != null && terpilih.getStatus().equals("LUNAS") && !MainApp.konfirmasi("Batalkan transaksi no. " + terpilih.getId() + "?\nStok produknya dikembalikan.")) {
                return;
            }
            penjualanService.batalkan(terpilih, fieldAlasanBatal.getText());
            fieldAlasanBatal.clear();
            muatProduk();
            muatRiwayat();
            MainApp.pesanBerhasil(labelPesan, "Transaksi no. " + terpilih.getId() + " dibatalkan dan stoknya dikembalikan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    //Memuat data

    private void muatProduk() {
        try {
            List<Produk> hasilCari = penjualanService.cariProduk(fieldCariKasir.getText());
            tabelProdukKasir.setItems(FXCollections.observableArrayList(hasilCari));
            tabelProdukKelola.setItems(FXCollections.observableArrayList(penjualanService.cariProduk("")));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
        kosongkanPilihanProduk();
    }

    private void kosongkanPilihanProduk() {
        labelProdukTerpilih.setText("Pilih produk di tabel untuk mengubahnya.");
        fieldNamaProduk.clear();
        fieldHargaProduk.clear();
        tombolSimpanProduk.setDisable(true);
        tombolTambahStok.setDisable(true);
        tombolArsipProduk.setDisable(true);
    }

    private void muatRiwayat() {
        try {
            tabelRiwayat.setItems(FXCollections.observableArrayList(penjualanService.riwayat()));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void perbaruiTotal() {
        labelTotal.setText(Rupiah.format(penjualanService.hitungTotal(keranjang)));
        tombolCheckout.setDisable(keranjang.isEmpty());
    }
}
