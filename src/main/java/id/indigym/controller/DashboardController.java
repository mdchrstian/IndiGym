package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.Rupiah;
import id.indigym.service.AksesService;
import id.indigym.service.PelangganService;
import id.indigym.service.PenjualanService;
import id.indigym.service.Sesi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Beranda: sapaan, ringkasan angka hari ini, dan pintasan ke tugas yang paling sering.
public class DashboardController {

    @FXML
    private Label labelSelamat;
    @FXML
    private Label labelTanggal;
    @FXML
    private Label labelPesan;
    @FXML
    private Label labelJumlahCheckIn;
    @FXML
    private Label labelJumlahBerakses;
    @FXML
    private Label labelTotalPenjualan;

    private PelangganService pelangganService = new PelangganService();
    private AksesService aksesService = new AksesService();
    private PenjualanService penjualanService = new PenjualanService();

    @FXML
    private void initialize() {
        labelSelamat.setText("Halo, " + Sesi.adminLogin.getNama());

        // contoh hasil: "Selasa, 10 November 2026"
        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.of("id", "ID"));
        labelTanggal.setText(LocalDate.now().format(formatTanggal));

        muatRingkasan();
    }

    private void muatRingkasan() {
        try {
            labelJumlahCheckIn.setText(String.valueOf(pelangganService.jumlahCheckInHariIni()));
            labelJumlahBerakses.setText(String.valueOf(aksesService.jumlahPelangganBeraksesHariIni()));
            labelTotalPenjualan.setText(Rupiah.format(penjualanService.totalPenjualanHariIni()));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void bukaPelanggan() throws IOException {
        MainApp.gantiLayar("pelanggan");
    }

    @FXML
    private void bukaAkses() throws IOException {
        MainApp.gantiLayar("akses");
    }

    @FXML
    private void bukaPenjualan() throws IOException {
        MainApp.gantiLayar("penjualan");
    }
}