package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.Pelanggan;
import id.indigym.service.PelangganService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PelangganController {

    @FXML
    private Label labelPesan;

    @FXML
    private TextField fieldCari;

    @FXML
    private TableView<Pelanggan> tabelPelanggan;

    private PelangganService pelangganService = new PelangganService();

    @FXML
    private void cari() {
        muatTabel();
    }

    private void muatTabel() {
        try {
            tabelPelanggan.setItems(FXCollections.observableArrayList(
                    pelangganService.cari(fieldCari.getText())));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }
}