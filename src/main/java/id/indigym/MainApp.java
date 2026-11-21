package id.indigym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import id.indigym.model.Cetak;
import id.indigym.model.Pelanggan;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage jendelaUtama;

    private static String layarAktif = "";

    public static Pelanggan pelangganDibawa = null;

    @Override
    public void start(Stage stage) throws IOException {
        jendelaUtama = stage;
        jendelaUtama.setTitle("IndiGym");
        jendelaUtama.setMinWidth(1100);
        jendelaUtama.setMinHeight(680);
        gantiLayar("login");
        jendelaUtama.show();
    }

    public static void gantiLayar(String namaFxml) throws IOException {
        layarAktif = namaFxml;
        Parent isi = FXMLLoader.load(MainApp.class.getResource("/fxml/" + namaFxml + ".fxml"));

        if (jendelaUtama.getScene() == null) {
            Scene scene = new Scene(isi, 1180, 720);
            scene.getStylesheets().add(MainApp.class.getResource("/css/application.css").toExternalForm());
            jendelaUtama.setScene(scene);
        } else {
            jendelaUtama.getScene().setRoot(isi);
        }
    }

    public static String getLayarAktif() {
        return layarAktif;
    }

    public static void pesanBerhasil(Label label, String pesan) {
        label.getStyleClass().removeAll("pesan-berhasil", "pesan-gagal");
        label.getStyleClass().add("pesan-berhasil");
        label.setText("[OK] " + pesan);
        label.setVisible(true);
        label.setManaged(true);
    }

    public static void pesanGagal(Label label, String pesan) {
        label.getStyleClass().removeAll("pesan-berhasil", "pesan-gagal");
        label.getStyleClass().add("pesan-gagal");
        label.setText("[GAGAL] " + pesan);
        label.setVisible(true);
        label.setManaged(true);
    }

    public static void tampilkanBukti(Cetak bukti) {
        TextArea isi = new TextArea(bukti.cetakBukti());
        isi.setEditable(false);
        isi.setPrefRowCount(18);
        isi.setPrefColumnCount(44);

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Bukti");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(isi);
        pasangGaya(dialog);
        dialog.showAndWait();
    }

    public static boolean konfirmasi(String pertanyaan) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Konfirmasi");
        dialog.setHeaderText(null);
        dialog.setContentText(pertanyaan);
        pasangGaya(dialog);
        dialog.showAndWait();
        return dialog.getResult() == ButtonType.OK;
    }

    private static void pasangGaya(Dialog<?> dialog) {
        dialog.getDialogPane().getStylesheets().add(MainApp.class.getResource("/css/application.css").toExternalForm());
        dialog.setGraphic(null);
        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("tombol-utama");
    }

    public static void main(String[] args) {
        launch(args);
    }
}