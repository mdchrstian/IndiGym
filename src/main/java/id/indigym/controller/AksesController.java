package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.PaketAkses;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;
import id.indigym.service.AksesService;
import id.indigym.service.PelangganService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Layar Membership: pilih pelanggan (kiri), pilih paket dan bayar (kanan).
public class AksesController {

    @FXML
    private Label labelPesan;

    // tab Jual membership
    @FXML
    private TextField fieldCari;
    @FXML
    private TableView<Pelanggan> tabelPelanggan;
    @FXML
    private ComboBox<PaketAkses> comboPaket;
    @FXML
    private Label labelPelangganRingkas;
    @FXML
    private Label labelPaketRingkas;
    @FXML
    private Label labelHargaRingkas;
    @FXML
    private Label labelPeriodeRingkas;
    @FXML
    private Label labelCatatanRingkas;
    @FXML
    private Button tombolBayar;
    @FXML
    private TableView<PembayaranAkses> tabelRiwayat;

    // tab Daftar paket
    @FXML
    private TableView<PaketAkses> tabelPaket;
    @FXML
    private Label labelPaketTerpilih;
    @FXML
    private Button tombolArsipPaket;
    @FXML
    private TextField fieldNamaPaket;
    @FXML
    private TextField fieldHargaPaket;
    @FXML
    private TextField fieldDurasiPaket;

    private AksesService aksesService = new AksesService();
    private PelangganService pelangganService = new PelangganService();

    @FXML
    private void initialize() {
        muatPelanggan();
        muatPaket();

        // datang dari tombol "Jual membership" di layar Pelanggan: langsung pilih pelanggannya
        if (MainApp.pelangganDibawa != null) {
            fieldCari.setText(MainApp.pelangganDibawa.getKode());
            muatPelanggan();
            pilihPelangganDenganId(MainApp.pelangganDibawa.getId());
            MainApp.pelangganDibawa = null;
        }
        perbaruiRingkasan();
    }

    // Tab Jual membership

    @FXML
    private void cariPelanggan() {
        muatPelanggan();
    }

    @FXML
    private void pilihPelanggan() {
        muatRiwayat();
        perbaruiRingkasan();
    }

    // Mengisi kotak "PERIKSA SEBELUM BAYAR". Tombol bayar hanya menyala jika pelanggan dan paket sudah dipilih.
    @FXML
    private void perbaruiRingkasan() {
        Pelanggan pelanggan = tabelPelanggan.getSelectionModel().getSelectedItem();
        PaketAkses paket = comboPaket.getValue();

        if (pelanggan == null) {
            labelPelangganRingkas.setText("Pelanggan : belum dipilih");
        } else {
            labelPelangganRingkas.setText("Pelanggan : " + pelanggan.tampilkanInfo());
        }
        if (paket == null) {
            labelPaketRingkas.setText("Paket     : belum dipilih");
            labelHargaRingkas.setText("Rp 0");
        } else {
            labelPaketRingkas.setText("Paket     : " + paket.getNama() + " (" + paket.getDurasiTeks() + ")");
            labelHargaRingkas.setText(paket.getHargaTeks());
        }

        tombolBayar.setDisable(pelanggan == null || paket == null);
        labelPeriodeRingkas.setText("");
        labelCatatanRingkas.setText("Pilih pelanggan dan paket untuk melihat periode berlakunya.");
        if (pelanggan == null || paket == null) {
            return;
        }

        try {
            PembayaranAkses pratinjau = aksesService.pratinjauPeriode(pelanggan, paket);
            labelPeriodeRingkas.setText("Berlaku   : " + pratinjau.getPeriode());
            if (pratinjau.getTanggalMulai().isAfter(LocalDate.now())) {
                labelCatatanRingkas.setText("Dimulai setelah akses yang sekarang berakhir, jadi tidak ada hari yang terbuang.");
            } else {
                labelCatatanRingkas.setText("Dimulai hari ini.");
            }
            if (pelanggan.isDiarsipkan()) {
                tombolBayar.setDisable(true);
                labelCatatanRingkas.setText("Profil diarsipkan. Aktifkan kembali di layar Pelanggan sebelum membeli.");
            }
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void bayar() {
        Pelanggan pelanggan = tabelPelanggan.getSelectionModel().getSelectedItem();
        PaketAkses paket = comboPaket.getValue();
        try {
            PembayaranAkses pratinjau = aksesService.pratinjauPeriode(pelanggan, paket);
            if (!MainApp.konfirmasi("Terima pembayaran LUNAS " + paket.getHargaTeks() + " dari " + pelanggan.getNama()
                    + "?\n" + paket.getNama() + ", berlaku " + pratinjau.getPeriode())) {
                return;
            }
            PembayaranAkses tersimpan = aksesService.bayarMembership(pelanggan, paket);
            MainApp.pesanBerhasil(labelPesan, "Pembayaran " + pelanggan.getNama() + " tersimpan. Berlaku " + tersimpan.getPeriode() + ".");
            muatRiwayat();
            perbaruiRingkasan();
            MainApp.tampilkanBukti(tersimpan);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    // ---------- Tab Daftar paket ----------

    @FXML
    private void pilihPaket() {
        PaketAkses paket = tabelPaket.getSelectionModel().getSelectedItem();
        if (paket == null) {
            return;
        }
        labelPaketTerpilih.setText("Terpilih: " + paket.getNama());
        // paket harian dipakai untuk visit, jadi tombol arsip dimatikan
        tombolArsipPaket.setDisable(!paket.getJenis().equals("MEMBERSHIP"));
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            labelPaketTerpilih.setText("Terpilih: " + paket.getNama() + " (paket harian tidak bisa diarsipkan)");
        }
    }

    @FXML
    private void tambahPaket() {
        try {
            BigDecimal harga = new BigDecimal(fieldHargaPaket.getText().trim());
            int durasi = Integer.parseInt(fieldDurasiPaket.getText().trim());
            aksesService.tambahPaketMembership(fieldNamaPaket.getText(), harga, durasi);
            fieldNamaPaket.clear();
            fieldHargaPaket.clear();
            fieldDurasiPaket.clear();
            muatPaket();
            MainApp.pesanBerhasil(labelPesan, "Paket baru ditambahkan dan sudah bisa dijual.");
        } catch (NumberFormatException e) {
            MainApp.pesanGagal(labelPesan, "Harga dan durasi harus berupa angka, contoh 700000 dan 3.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void arsipkanPaket() {
        try {
            PaketAkses paket = tabelPaket.getSelectionModel().getSelectedItem();
            if (paket == null) {
                MainApp.pesanGagal(labelPesan, "Pilih paket di tabel dulu.");
                return;
            }
            if (!MainApp.konfirmasi("Arsipkan " + paket.getNama() + "?\nPaket tidak bisa dijual lagi, tetapi pembayaran lama tetap utuh.")) {
                return;
            }
            aksesService.arsipkanPaket(paket);
            muatPaket();
            MainApp.pesanBerhasil(labelPesan, paket.getNama() + " diarsipkan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    // ---------- Memuat data ----------

    private void muatPelanggan() {
        try {
            tabelPelanggan.setItems(FXCollections.observableArrayList(pelangganService.cari(fieldCari.getText())));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void pilihPelangganDenganId(Long id) {
        for (Pelanggan pelanggan : tabelPelanggan.getItems()) {
            if (pelanggan.getId().equals(id)) {
                tabelPelanggan.getSelectionModel().select(pelanggan);
                muatRiwayat();
                return;
            }
        }
    }

    private void muatPaket() {
        try {
            List<PaketAkses> semuaPaket = aksesService.daftarPaket();
            tabelPaket.setItems(FXCollections.observableArrayList(semuaPaket));

            // pilihan di ComboBox hanya membership; paket harian dijual lewat layar Pelanggan
            List<PaketAkses> paketMembership = new ArrayList<>();
            for (PaketAkses paket : semuaPaket) {
                if (paket.getJenis().equals("MEMBERSHIP")) {
                    paketMembership.add(paket);
                }
            }
            comboPaket.setItems(FXCollections.observableArrayList(paketMembership));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
        // tabel dimuat ulang = pilihan hilang, jadi tombol arsip dimatikan lagi
        labelPaketTerpilih.setText("Klik paket di tabel untuk mengarsipkannya.");
        tombolArsipPaket.setDisable(true);
    }

    private void muatRiwayat() {
        Pelanggan pelanggan = tabelPelanggan.getSelectionModel().getSelectedItem();
        if (pelanggan == null) {
            return;
        }
        try {
            tabelRiwayat.setItems(FXCollections.observableArrayList(aksesService.riwayatPembayaran(pelanggan)));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }
}
