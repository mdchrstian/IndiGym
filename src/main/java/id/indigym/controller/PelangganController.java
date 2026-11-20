package id.indigym.controller;

import id.indigym.MainApp;
import id.indigym.model.CheckIn;
import id.indigym.model.PaketAkses;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;
import id.indigym.service.AksesService;
import id.indigym.service.PelangganService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PelangganController {

    @FXML
    private Label labelPesan;

    @FXML
    private TextField fieldCari;
    @FXML
    private TableView<Pelanggan> tabelPelanggan;
    @FXML
    private TextField fieldNamaBaru;
    @FXML
    private TextField fieldKontakBaru;

    @FXML
    private VBox kotakKosong;
    @FXML
    private VBox kotakDetail;
    @FXML
    private Label labelKodeDetail;
    @FXML
    private Label labelNamaDetail;
    @FXML
    private Label labelKontakDetail;
    @FXML
    private VBox kotakAkses;
    @FXML
    private Label labelStatusAkses;
    @FXML
    private Label labelDetailAkses;
    @FXML
    private Button tombolCheckIn;
    @FXML
    private Button tombolVisit;
    @FXML
    private Button tombolMembership;
    @FXML
    private TableView<CheckIn> tabelRiwayat;
    @FXML
    private TextField fieldNama;
    @FXML
    private TextField fieldKontak;
    @FXML
    private Button tombolArsip;

    private PelangganService pelangganService = new PelangganService();
    private AksesService aksesService = new AksesService();

    private Pelanggan terpilih = null;

    @FXML
    private void initialize() {
        muatTabel();
        tampilkanDetail(null);
    }


    @FXML
    private void cari() {
        muatTabel();
    }

    @FXML
    private void pilihBaris() {
        Pelanggan pilihan = tabelPelanggan.getSelectionModel().getSelectedItem();
        if (pilihan != null && pilihan != terpilih) {
            tampilkanDetail(pilihan);
        }
    }

    @FXML
    private void daftarBaru() {
        try {
            Pelanggan baru = pelangganService.daftar(fieldNamaBaru.getText(), fieldKontakBaru.getText());
            fieldNamaBaru.clear();
            fieldKontakBaru.clear();
            fieldCari.clear();
            muatTabel();
            pilihDiTabel(baru.getId());
            MainApp.pesanBerhasil(labelPesan, baru.getNama() + " terdaftar dengan kode " + baru.getKode()
                    + ". Jual visit atau membership agar bisa check-in.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }


    @FXML
    private void checkIn() {
        try {
            CheckIn hasil = pelangganService.checkIn(terpilih.getKode(), false);

            if (hasil == null) {
                if (!MainApp.konfirmasi(terpilih.getNama() + " sudah check-in hari ini.\nCatat kunjungan tambahan tanpa biaya?")) {
                    MainApp.pesanBerhasil(labelPesan, "Kunjungan tambahan dibatalkan. Tidak ada yang dicatat.");
                    return;
                }
                hasil = pelangganService.checkIn(terpilih.getKode(), true);
            }

            if (hasil.diizinkan()) {
                MainApp.pesanBerhasil(labelPesan, "Check-in diizinkan untuk " + terpilih.getNama() + ".");
            } else {
                MainApp.pesanGagal(labelPesan, "Check-in ditolak: " + hasil.getAlasan() + " Penolakan tetap tercatat di riwayat.");
            }
            tampilkanDetail(terpilih);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void jualVisit() {
        try {
            PaketAkses paketHarian = aksesService.cariPaketHarian();
            if (paketHarian == null) {
                MainApp.pesanGagal(labelPesan, "Paket harian belum tersedia. Tambahkan di layar Membership.");
                return;
            }
            if (!MainApp.konfirmasi("Terima pembayaran LUNAS " + paketHarian.getHargaTeks() + " untuk "
                    + paketHarian.getNama() + "?\nCheck-in pertama " + terpilih.getNama() + " langsung dicatat.")) {
                return;
            }
            PembayaranAkses pembayaran = pelangganService.beliVisit(terpilih.getKode());
            MainApp.pesanBerhasil(labelPesan, "Visit dibayar dan check-in " + terpilih.getNama() + " tercatat.");
            tampilkanDetail(terpilih);
            MainApp.tampilkanBukti(pembayaran);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void jualMembership() throws IOException {
        MainApp.pelangganDibawa = terpilih;
        MainApp.gantiLayar("akses");
    }

    @FXML
    private void simpanPerubahan() {
        try {
            pelangganService.ubah(terpilih, fieldNama.getText(), fieldKontak.getText());
            Long id = terpilih.getId();
            muatTabel();
            pilihDiTabel(id);
            MainApp.pesanBerhasil(labelPesan, "Data pelanggan disimpan.");
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    @FXML
    private void ubahStatusArsip() {
        try {
            Long id = terpilih.getId();
            if (terpilih.isDiarsipkan()) {
                pelangganService.aktifkan(terpilih);
                MainApp.pesanBerhasil(labelPesan, terpilih.getNama() + " aktif kembali.");
            } else {
                pelangganService.arsipkan(terpilih);
                MainApp.pesanBerhasil(labelPesan, terpilih.getNama() + " diarsipkan. Riwayatnya tetap tersimpan.");
            }
            muatTabel();
            pilihDiTabel(id);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }


    private void tampilkanDetail(Pelanggan pelanggan) {
        terpilih = pelanggan;
        boolean adaPilihan = pelanggan != null;
        kotakKosong.setVisible(!adaPilihan);
        kotakKosong.setManaged(!adaPilihan);
        kotakDetail.setVisible(adaPilihan);
        kotakDetail.setManaged(adaPilihan);
        if (!adaPilihan) {
            return;
        }

        labelKodeDetail.setText(pelanggan.getKode() + " / " + pelanggan.getStatus().toUpperCase());
        labelNamaDetail.setText(pelanggan.getNama());
        if (pelanggan.getKontak() == null) {
            labelKontakDetail.setText("Tanpa kontak");
        } else {
            labelKontakDetail.setText(pelanggan.getKontak());
        }
        fieldNama.setText(pelanggan.getNama());
        fieldKontak.setText(pelanggan.getKontak());
        if (pelanggan.isDiarsipkan()) {
            tombolArsip.setText("Aktifkan kembali");
        } else {
            tombolArsip.setText("Arsipkan");
        }

        try {
            List<CheckIn> riwayat = pelangganService.riwayatCheckIn(pelanggan.getKode());
            tabelRiwayat.setItems(FXCollections.observableArrayList(riwayat));
            tampilkanStatusAkses(pelanggan, riwayat);
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void tampilkanStatusAkses(Pelanggan pelanggan, List<CheckIn> riwayat) {
        kotakAkses.getStyleClass().removeAll("akses-ada", "akses-tidak");

        if (pelanggan.isDiarsipkan()) {
            labelStatusAkses.setText("PROFIL DIARSIPKAN");
            labelDetailAkses.setText("Check-in akan ditolak. Aktifkan kembali profil di bagian Ubah Data.");
            tombolCheckIn.setText("Catat check-in (akan ditolak)");
            tombolVisit.setDisable(true);
            tombolMembership.setDisable(true);
            jadikanTombolUtama(null);
            return;
        }

        PembayaranAkses akses = aksesService.cariAksesAktifHariIni(pelanggan);

        if (akses == null) {
            kotakAkses.getStyleClass().add("akses-tidak");
            labelStatusAkses.setText("BELUM PUNYA AKSES");
            labelDetailAkses.setText("Check-in akan ditolak. Jual visit harian atau membership dulu.");
            tombolCheckIn.setText("Catat check-in (akan ditolak)");
            tombolVisit.setDisable(false);
            tombolMembership.setDisable(false);
            jadikanTombolUtama(tombolVisit);
            return;
        }

        kotakAkses.getStyleClass().add("akses-ada");
        labelStatusAkses.setText("BOLEH MASUK");
        String keterangan = akses.getNamaPaket() + ", berlaku " + akses.getPeriode() + ".";
        CheckIn hariIni = cariCheckInHariIni(riwayat);
        if (hariIni != null) {
            keterangan = keterangan + " Sudah check-in hari ini pukul "
                    + hariIni.getWaktu().format(DateTimeFormatter.ofPattern("HH:mm")) + ".";
        }
        labelDetailAkses.setText(keterangan);
        tombolCheckIn.setText("Catat check-in");
        tombolVisit.setDisable(true);
        tombolMembership.setDisable(false);
        jadikanTombolUtama(tombolCheckIn);
    }

    private void jadikanTombolUtama(Button tombol) {
        tombolCheckIn.getStyleClass().remove("tombol-utama");
        tombolVisit.getStyleClass().remove("tombol-utama");
        tombolMembership.getStyleClass().remove("tombol-utama");
        if (tombol != null) {
            tombol.getStyleClass().add("tombol-utama");
        }
    }

    private CheckIn cariCheckInHariIni(List<CheckIn> riwayat) {
        for (CheckIn checkIn : riwayat) {
            if (checkIn.diizinkan() && checkIn.getWaktu().toLocalDate().equals(LocalDate.now())) {
                return checkIn;
            }
        }
        return null;
    }


    private void muatTabel() {
        try {
            tabelPelanggan.setItems(FXCollections.observableArrayList(pelangganService.cari(fieldCari.getText())));
        } catch (IllegalArgumentException e) {
            MainApp.pesanGagal(labelPesan, e.getMessage());
        }
    }

    private void pilihDiTabel(Long id) {
        for (Pelanggan pelanggan : tabelPelanggan.getItems()) {
            if (pelanggan.getId().equals(id)) {
                tabelPelanggan.getSelectionModel().select(pelanggan);
                tabelPelanggan.scrollTo(pelanggan);
                tampilkanDetail(pelanggan);
                return;
            }
        }
        tampilkanDetail(null);
    }
}