package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.service.AkunService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField fieldUsername;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Label labelError;

    private AkunService akunService = new AkunService();

    private void masuk() {
        tampilkanError(null);
        try {
            akunService.login(fieldUsername.getText(), fieldPassword.getText());
            MainApp.gantiLayar("dashboard");
        } catch (IllegalArgumentException e) {
            tampilkanError(e.getMessage());
        } catch (IOException e) {
            tampilkanError("Gagal membuka dashboard: " + e.getMessage());
        }
    }

    private void tampilkanError(String pesan) {
        labelError.setText(pesan);
        labelError.setVisible(pesan != null);
        labelError.setManaged(pesan != null);
    }
}
