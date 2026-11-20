package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.Admin;
import id.indigym.service.AkunService;
import id.indigym.service.Sesi;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AdminController {

    @FXML
    private Label labelKeterangan;
    @FXML
    private Label labelPesan;

    @FXML
    private Label labelNamaSaya;
    @FXML
    private Label labelInfoSaya;
    @FXML
    private PasswordField fieldPasswordLama;
    @FXML
    private PasswordField fieldPasswordBaru;
    @FXML
    private PasswordField fieldPasswordUlang;

    @FXML
    private VBox kotakKelola;
    @FXML
    private TableView<Admin> tabelAdmin;
    @FXML
    private Label labelAdminTerpilih;
    @FXML
    private PasswordField fieldPasswordReset;
    @FXML
    private Button tombolReset;
    @FXML
    private Button tombolNonaktif;
    @FXML
    private TextField fieldNamaBaru;
    @FXML
    private TextField fieldUsernameBaru;
    @FXML
    private PasswordField fieldPasswordAdminBaru;

    private AkunService akunService = new AkunService();

    @FXML
    private void initialize() {
        Admin saya = Sesi.adminLogin;
        labelNamaSaya.setText(saya.getNama());
        labelInfoSaya.setText(saya.getUsername() + " / " + saya.getRole());

        boolean boleh = saya.bolehKelolaAdmin();
        kotakKelola.setVisible(boleh);
        kotakKelola.setManaged(boleh);
        if (boleh) {
            labelKeterangan.setText("Ganti password Anda, atau kelola akun Admin lain.");
            muatTabel();
        } else {
            labelKeterangan.setText("Ganti password Anda. Akun Admin lain dikelola oleh SuperAdmin.");
        }
    }

    @FXML
    private void gantiPasswordSendiri() {
        if (!fieldPasswordBaru.getText().equals(fieldPasswordUlang.getText())) {
            MainApp.pesanGagal(labelPesan, "Password baru dan ulangannya tidak sama.");
            return;
        }
        try {
            akunService.gantiPasswordSendiri(fieldPasswordLama.getText(), fieldPasswordBaru.getText());
            fieldPasswordLama.clear();
            fieldPasswordBaru.clear();
            fieldPasswordUlang.clear();
            MainApp.pesanBerhasil(labelPesan, "Password Anda sudah diganti. Pakai password baru saat login berikutnya.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void pilihAdmin() {
        Admin terpilih = tabelAdmin.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            return;
        }
        labelAdminTerpilih.setText("ADMIN TERPILIH: " + terpilih.getUsername().toUpperCase() + " / " + terpilih.getStatus().toUpperCase());
        boolean bisaDiubah = !terpilih.bolehKelolaAdmin(); 
        tombolReset.setDisable(!bisaDiubah);
        tombolNonaktif.setDisable(!bisaDiubah || !terpilih.isAktif());
        if (!bisaDiubah) {
            labelAdminTerpilih.setText("ADMIN TERPILIH: " + terpilih.getUsername().toUpperCase() + " / SUPERADMIN TIDAK BISA DIUBAH DI SINI");
        }
    }

    @FXML
    private void tambahAdmin() {
        try {
            akunService.tambahAdmin(fieldNamaBaru.getText(), fieldUsernameBaru.getText(), fieldPasswordAdminBaru.getText());
            String username = fieldUsernameBaru.getText().trim();
            fieldNamaBaru.clear();
            fieldUsernameBaru.clear();
            fieldPasswordAdminBaru.clear();
            muatTabel();
            MainApp.pesanBerhasil(labelPesan, "Admin " + username + " ditambahkan dan sudah bisa login.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void resetPassword() {
        try {
            Admin terpilih = tabelAdmin.getSelectionModel().getSelectedItem();
            akunService.resetPassword(terpilih, fieldPasswordReset.getText());
            fieldPasswordReset.clear();
            MainApp.pesanBerhasil(labelPesan, "Password " + terpilih.getUsername() + " direset. Beritahukan password barunya.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void nonaktifkanAdmin() {
        Admin terpilih = tabelAdmin.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            MainApp.pesanGagal(labelPesan, "Pilih akun Admin di tabel dulu.");
            return;
        }
        if (!MainApp.konfirmasi("Nonaktifkan akun " + terpilih.getUsername() + "?\nAkun ini tidak bisa login lagi.")) {
            return;
        }
        try {
            akunService.nonaktifkanAdmin(terpilih);
            muatTabel();
            MainApp.pesanBerhasil(labelPesan, "Akun " + terpilih.getUsername() + " dinonaktifkan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void muatTabel() {
        try {
            tabelAdmin.setItems(FXCollections.observableArrayList(akunService.daftarAdmin()));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
        labelAdminTerpilih.setText("ADMIN TERPILIH: BELUM ADA (KLIK BARIS DI TABEL)");
        tombolReset.setDisable(true);
        tombolNonaktif.setDisable(true);
    }
}