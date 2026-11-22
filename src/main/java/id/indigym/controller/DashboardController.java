package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.service.Sesi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    @FXML
    private void initialize() {
        labelSelamat.setText("Halo, " + Sesi.adminLogin.getNama());

        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.of("id", "ID"));
        labelTanggal.setText(LocalDate.now().format(formatTanggal));

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
