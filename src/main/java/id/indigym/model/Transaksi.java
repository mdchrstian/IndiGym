package id.indigym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaksi implements Cetak {

    public static final String LUNAS = "LUNAS";
    public static final String BATAL = "BATAL";

    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private Long id;
    private Pelanggan pelanggan;
    private Long adminId;
    private String namaAdmin;
    private LocalDateTime waktu;
    private String status;
    private String alasanBatal;
    private List<DetailTransaksi> daftarDetail;

    public Transaksi(Long id, Pelanggan pelanggan, Long adminId, String namaAdmin, LocalDateTime waktu,
                     String status, String alasanBatal, List<DetailTransaksi> daftarDetail) {
        this.id = id;
        this.pelanggan = pelanggan;
        this.adminId = adminId;
        this.namaAdmin = namaAdmin;
        this.waktu = waktu;
        this.status = status;
        this.alasanBatal = alasanBatal;
        this.daftarDetail = daftarDetail;
    }

    public BigDecimal hitungTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetailTransaksi detail : daftarDetail) {
            total = total.add(detail.hitungSubtotal());
        }
        return total;
    }

    public String getTotalTeks() {
        return Rupiah.format(hitungTotal());
    }

    public String getNamaPelanggan() {
        if (pelanggan == null) {
            return "Pembeli umum";
        }
        return pelanggan.tampilkanInfo();
    }

    public String getWaktuTeks() {
        return waktu.format(FORMAT_WAKTU);
    }

    public boolean sudahDibatalkan() {
        return status.equals(BATAL);
    }

    @Override
    public String cetakBukti() {
        StringBuilder teks = new StringBuilder();
        teks.append("=== STRUK PENJUALAN ===\n");
        teks.append("No. Transaksi : ").append(id).append("\n");
        teks.append("Waktu         : ").append(getWaktuTeks()).append("\n");
        teks.append("Pelanggan     : ").append(getNamaPelanggan()).append("\n");
        teks.append("Petugas       : ").append(namaAdmin).append("\n");
        teks.append("-------------------------------\n");
        for (DetailTransaksi detail : daftarDetail) {
            teks.append(detail.getNamaProduk()).append("\n");
            teks.append("   ").append(detail.getJumlah()).append(" x ")
                    .append(detail.getHargaSatuanTeks()).append(" = ")
                    .append(detail.getSubtotalTeks()).append("\n");
        }
        teks.append("-------------------------------\n");
        teks.append("Total         : ").append(getTotalTeks()).append("\n");
        teks.append("Status        : ").append(status);
        if (sudahDibatalkan()) {
            teks.append(" (").append(alasanBatal).append(")");
        }
        return teks.toString();
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

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public String getStatus() {
        return status;
    }

    public String getAlasanBatal() {
        return alasanBatal;
    }

    public List<DetailTransaksi> getDaftarDetail() {
        return daftarDetail;
    }
}
