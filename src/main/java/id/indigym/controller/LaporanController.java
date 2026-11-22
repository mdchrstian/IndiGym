package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.Rupiah;
import id.indigym.service.LaporanService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LaporanController {

    @FXML
    private Label labelPesan;
    @FXML
    private Button tombolHariIni;
    @FXML
    private Button tombolBulanIni;
    @FXML
    private Button tombolTahunIni;
    @FXML
    private DatePicker tanggalDari;
    @FXML
    private DatePicker tanggalSampai;
    @FXML
    private Label labelPeriode;
    @FXML
    private Label labelMembership;
    @FXML
    private Label labelHarian;
    @FXML
    private Label labelProduk;
    @FXML
    private Label labelTotal;

    private LaporanService laporanService = new LaporanService();

    @FXML
    private void initialize() {
        pilihBulanIni();
    }

    @FXML
    private void pilihHariIni() {
        aturPeriode(LocalDate.now(), LocalDate.now(), tombolHariIni);
    }

    @FXML
    private void pilihBulanIni() {
        aturPeriode(LocalDate.now().withDayOfMonth(1), LocalDate.now(), tombolBulanIni);
    }

    @FXML
    private void pilihTahunIni() {
        aturPeriode(LocalDate.now().withDayOfYear(1), LocalDate.now(), tombolTahunIni);
    }

    @FXML
    private void tampilkan() {
        tandaiTombol(null);
        muatRingkasan();
    }

    private void aturPeriode(LocalDate dari, LocalDate sampai, Button tombol) {
        tanggalDari.setValue(dari);
        tanggalSampai.setValue(sampai);
        tandaiTombol(tombol);
        muatRingkasan();
    }

    private void muatRingkasan() {
        LocalDate dari = tanggalDari.getValue();
        LocalDate sampai = tanggalSampai.getValue();
        try {
            BigDecimal membership = laporanService.pendapatanMembership(dari, sampai);
            BigDecimal harian = laporanService.pendapatanHarian(dari, sampai);
            BigDecimal produk = laporanService.pendapatanProduk(dari, sampai);

            labelMembership.setText(Rupiah.format(membership));
            labelHarian.setText(Rupiah.format(harian));
            labelProduk.setText(Rupiah.format(produk));
            labelTotal.setText(Rupiah.format(LaporanService.hitungTotal(membership, harian, produk)));

            DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.of("id", "ID"));
            labelPeriode.setText(dari.format(format) + " sampai " + sampai.format(format));
            labelPesan.setVisible(false);
            labelPesan.setManaged(false);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void tandaiTombol(Button tombol) {
        tombolHariIni.getStyleClass().remove("tombol-utama");
        tombolBulanIni.getStyleClass().remove("tombol-utama");
        tombolTahunIni.getStyleClass().remove("tombol-utama");
        if (tombol != null) {
            tombol.getStyleClass().add("tombol-utama");
        }
    }
}
