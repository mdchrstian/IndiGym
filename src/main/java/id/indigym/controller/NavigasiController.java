package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.Admin;
import id.indigym.service.AkunService;
import id.indigym.service.Sesi;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

// Navigasi samping yang sama di semua layar (disisipkan lewat fx:include).
public class NavigasiController {

    @FXML
    private Button tombolBeranda;
    @FXML
    private Button tombolPelanggan;
    @FXML
    private Button tombolAkses;
    @FXML
    private Button tombolPenjualan;
    @FXML
    private Button tombolLaporan;
    @FXML
    private Button tombolAkun;
    @FXML
    private Label labelPetugas;
    @FXML
    private Label labelPeran;

    private AkunService akunService = new AkunService();

    @FXML
    private void initialize() {
        Admin admin = Sesi.adminLogin;
        labelPetugas.setText(admin.getNama());
        labelPeran.setText(admin.getUsername() + " · " + admin.getRole());
        tandaiMenuAktif();
    }

    // Menu layar yang sedang dibuka diberi kelas "aktif" (garis aksen di kiri).
    private void tandaiMenuAktif() {
        String layar = MainApp.getLayarAktif();
        if (layar.equals("dashboard")) {
            tombolBeranda.getStyleClass().add("aktif");
        } else if (layar.equals("pelanggan")) {
            tombolPelanggan.getStyleClass().add("aktif");
        } else if (layar.equals("akses")) {
            tombolAkses.getStyleClass().add("aktif");
        } else if (layar.equals("penjualan")) {
            tombolPenjualan.getStyleClass().add("aktif");
        } else if (layar.equals("laporan")) {
            tombolLaporan.getStyleClass().add("aktif");
        } else if (layar.equals("admin")) {
            tombolAkun.getStyleClass().add("aktif");
        }
    }

    @FXML
    private void bukaBeranda() throws IOException {
        MainApp.gantiLayar("dashboard");
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

    @FXML
    private void bukaLaporan() throws IOException {
        MainApp.gantiLayar("laporan");
    }

    @FXML
    private void bukaAkun() throws IOException {
        MainApp.gantiLayar("admin");
    }

    @FXML
    private void keluar() throws IOException {
        akunService.logout();
        MainApp.gantiLayar("login");
    }
}
