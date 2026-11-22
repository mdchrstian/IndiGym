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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AksesController {

    @FXML
    private Label labelPesan;

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

    @FXML
    private TableView<PembayaranAkses> tabelMembershipAktif;
    @FXML
    private Label labelJumlahMember;

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

    private boolean datangDariPelanggan = false;

    @FXML
    private void initialize() {
        muatPelanggan();
        muatPaket();
        muatMembershipAktif();

        if (MainApp.pelangganDibawa != null) {
            fieldCari.setText(MainApp.pelangganDibawa.getKode());
            muatPelanggan();
            pilihPelangganDenganId(MainApp.pelangganDibawa.getId());
            MainApp.pelangganDibawa = null;
            datangDariPelanggan = true;
        }
        perbaruiRingkasan();
    }

    @FXML
    private void cariPelanggan() {
        muatPelanggan();
    }

    @FXML
    private void pilihPelanggan() {
        muatRiwayat();
        perbaruiRingkasan();
    }

    @FXML
    private void perbaruiRingkasan() {
        Pelanggan pelanggan = tabelPelanggan.getSelectionModel().getSelectedItem();
        PaketAkses paket = comboPaket.getValue();

        if (pelanggan == null) {
            labelPelangganRingkas.setText("Pelanggan: belum dipilih");
        } else {
            labelPelangganRingkas.setText("Pelanggan: " + pelanggan.tampilkanInfo());
        }
        if (paket == null) {
            labelPaketRingkas.setText("Paket: belum dipilih");
            labelHargaRingkas.setText("Rp 0");
        } else {
            labelPaketRingkas.setText("Paket: " + paket.getNama() + " (" + paket.getDurasiTeks() + ")");
            labelHargaRingkas.setText(paket.getHargaTeks());
        }

        tombolBayar.setDisable(pelanggan == null || paket == null);
        labelPeriodeRingkas.setText("");
        labelCatatanRingkas.setText("");
        if (pelanggan == null || paket == null) {
            return;
        }

        try {
            if (pelanggan.isDiarsipkan()) {
                tombolBayar.setDisable(true);
                labelCatatanRingkas.setText("Profil ini diarsipkan. Aktifkan lagi di layar Pelanggan.");
                return;
            }
            PembayaranAkses membershipLama = aksesService.cariMembershipBelumBerakhir(pelanggan);
            if (membershipLama != null) {
                tombolBayar.setDisable(true);
                labelCatatanRingkas.setText("Masih punya membership (" + membershipLama.getPeriode()
                        + "). Bisa beli lagi setelah itu berakhir.");
                return;
            }

            PembayaranAkses pratinjau = aksesService.pratinjauPeriode(pelanggan, paket);
            labelPeriodeRingkas.setText("Berlaku: " + pratinjau.getPeriode());

            if (pratinjau.getTanggalMulai().isAfter(LocalDate.now())) {
                labelCatatanRingkas.setText("Mulai besok, karena hari ini sudah memakai visit harian.");
            }
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void bayar() throws IOException {
        Pelanggan pelanggan = tabelPelanggan.getSelectionModel().getSelectedItem();
        PaketAkses paket = comboPaket.getValue();
        try {
            PembayaranAkses pratinjau = aksesService.pratinjauPeriode(pelanggan, paket);
            if (!MainApp.konfirmasi("Terima pembayaran " + paket.getHargaTeks() + " dari " + pelanggan.getNama()
                    + "?\n" + paket.getNama() + ", berlaku " + pratinjau.getPeriode())) {
                return;
            }
            PembayaranAkses tersimpan = aksesService.bayarMembership(pelanggan, paket);
            MainApp.pesanBerhasil(labelPesan, "Membership " + pelanggan.getNama() + " aktif, berlaku " + tersimpan.getPeriode() + ".");
            muatRiwayat();
            muatMembershipAktif();
            perbaruiRingkasan();
            MainApp.tampilkanBukti(tersimpan);
            if (datangDariPelanggan) {
                MainApp.pelangganDibawa = pelanggan;
                MainApp.gantiLayar("pelanggan");
            }
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void muatMembershipAktif() {
        try {
            List<PembayaranAkses> daftar = aksesService.daftarMembershipAktif();
            tabelMembershipAktif.setItems(FXCollections.observableArrayList(daftar));
            labelJumlahMember.setText(daftar.size() + " member. Yang paling cepat berakhir ada di atas.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void pilihPaket() {
        PaketAkses paket = tabelPaket.getSelectionModel().getSelectedItem();
        if (paket == null) {
            return;
        }
        labelPaketTerpilih.setText(paket.getNama());
        tombolArsipPaket.setDisable(!paket.getJenis().equals("MEMBERSHIP"));
        if (!paket.getJenis().equals("MEMBERSHIP")) {
            labelPaketTerpilih.setText(paket.getNama() + " (paket harian tidak bisa diarsipkan)");
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
            MainApp.pesanBerhasil(labelPesan, "Paket ditambahkan.");
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
            if (!MainApp.konfirmasi("Arsipkan " + paket.getNama() + "?\nPaket ini tidak bisa dijual lagi. Riwayat pembayarannya tetap ada.")) {
                return;
            }
            aksesService.arsipkanPaket(paket);
            muatPaket();
            MainApp.pesanBerhasil(labelPesan, paket.getNama() + " diarsipkan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

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
        labelPaketTerpilih.setText("Pilih paket di tabel untuk mengarsipkan.");
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
