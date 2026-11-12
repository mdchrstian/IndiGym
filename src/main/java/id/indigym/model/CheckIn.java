package id.indigym.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckIn {

    public static final String DIIZINKAN = "DIIZINKAN";
    public static final String DITOLAK = "DITOLAK";

    private static final DateTimeFormatter FORMAT_WAKTU =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private Long id;
    private Pelanggan pelanggan;
    private Long adminId;
    private String namaAdmin;
    private Long pembayaranId;
    private LocalDateTime waktu;
    private String hasil;
    private String alasan;

    public CheckIn(Long id, Pelanggan pelanggan, Long adminId, String namaAdmin,
                   Long pembayaranId, LocalDateTime waktu, String hasil, String alasan) {
        this.id = id;
        this.pelanggan = pelanggan;
        this.adminId = adminId;
        this.namaAdmin = namaAdmin;
        this.pembayaranId = pembayaranId;
        this.waktu = waktu;
        this.hasil = hasil;
        this.alasan = alasan;
    }

    public boolean diizinkan() {
        return DIIZINKAN.equals(hasil);
    }

    public String getKeterangan() {
        if (diizinkan()) {
            return hasil + " / akses no. " + pembayaranId;
        }
        return hasil + " / " + alasan;
    }

    public String getWaktuTeks() {
        return waktu.format(FORMAT_WAKTU);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public Long getPembayaranId() {
        return pembayaranId;
    }

    public void setPembayaranId(Long pembayaranId) {
        this.pembayaranId = pembayaranId;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public String getHasil() {
        return hasil;
    }

    public String getAlasan() {
        return alasan;
    }
}