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

    // Nama layar yang sedang tampil, dipakai navigasi samping untuk menandai menu aktif.
    private static String layarAktif = "";

    // Pelanggan yang dibawa dari layar Pelanggan ke layar Membership (tombol "Jual membership"),
    // dan kembali lagi setelah bayar. Layar tujuan membacanya sekali lalu mengosongkannya.
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

    // Mengganti isi jendela dengan layar FXML, contoh: gantiLayar("dashboard").
    public static void gantiLayar(String namaFxml) throws IOException {
<<<<<<< HEAD
=======
        layarAktif = namaFxml; // diisi sebelum FXML dimuat agar navigasi bisa membacanya
>>>>>>> origin/main
        Parent isi = FXMLLoader.load(MainApp.class.getResource("/fxml/" + namaFxml + ".fxml"));

        // Scene dibuat sekali saja; setelah itu cukup ganti isinya,
        // supaya ukuran jendela yang sudah diubah pengguna tidak kembali ke awal.
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

    // Pesan hijau di atas layar setelah aksi berhasil.
    public static void pesanBerhasil(Label label, String pesan) {
        label.getStyleClass().removeAll("pesan-berhasil", "pesan-gagal");
        label.getStyleClass().add("pesan-berhasil");
        label.setText(pesan);
<<<<<<< HEAD
        label.setVisible(true);
=======
        label.setVisible(true);  // label pesan disembunyikan selama belum ada aksi
>>>>>>> origin/main
        label.setManaged(true);
    }

    // Pesan merah di atas layar setelah aksi ditolak atau gagal.
    public static void pesanGagal(Label label, String pesan) {
        label.getStyleClass().removeAll("pesan-berhasil", "pesan-gagal");
        label.getStyleClass().add("pesan-gagal");
        label.setText(pesan);
        label.setVisible(true);
        label.setManaged(true);
    }

    // Menampilkan bukti apa pun yang bisa dicetak (pembayaran akses maupun struk penjualan).
    public static void tampilkanBukti(Cetak bukti) {
        TextArea isi = new TextArea(bukti.cetakBukti());
        isi.setEditable(false);
        isi.setPrefRowCount(18);   // cukup untuk struk beberapa produk tanpa digulir
        isi.setPrefColumnCount(44);

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Bukti");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(isi);
        pasangGaya(dialog);
        dialog.showAndWait();
    }

    // Menampilkan pertanyaan OK/Batal. Mengembalikan true jika petugas menekan OK.
    public static boolean konfirmasi(String pertanyaan) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Konfirmasi");
        dialog.setHeaderText(null);
        dialog.setContentText(pertanyaan);
        pasangGaya(dialog);
        dialog.showAndWait();
        return dialog.getResult() == ButtonType.OK;
    }

    // Dialog punya jendela sendiri, jadi CSS aplikasi harus dipasang juga ke dialog.
    private static void pasangGaya(Dialog<?> dialog) {
        dialog.getDialogPane().getStylesheets().add(MainApp.class.getResource("/css/application.css").toExternalForm());
        dialog.setGraphic(null); // ikon bawaan berbentuk bulat, tidak cocok dengan gaya tajam
        dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add("tombol-utama");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
