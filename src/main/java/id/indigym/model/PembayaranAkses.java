package id.indigym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Satu pembayaran akses (membership / visit harian)
public class PembayaranAkses implements Cetak{
    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    //atribut
    private Long id;
    private Pelanggan pelanggan;
    private Long paketId;
    private Long adminId;
    private String namaAdmin;
    private String namaPaket;
    private BigDecimal harga;
    private LocalDateTime waktuBayar;
    private LocalDate tanggalMulai;
    private LocalDate tanggalAkhir;
    //Consturctor
    public PembayaranAkses(Long id, Pelanggan pelanggan, Long paketId, Long adminId, String namaAdmin,
        String namaPaket, BigDecimal harga, LocalDateTime waktuBayar, LocalDate tanggalMulai, LocalDate tanggalAkhir
    ){
        this.id = id;
        this.pelanggan = pelanggan;
        this.paketId = paketId;
        this.adminId = adminId;
        this.namaAdmin = namaAdmin;
        this.namaPaket = namaPaket;
        this.harga = harga;
        this.waktuBayar = waktuBayar;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
    }
    //Method
    //Cek tanggal termasuk dlm periode berlaku
    public boolean berlakuPada(LocalDate tanggal){
        boolean sudahMulai = !tanggal.isBefore(tanggalMulai);
        boolean belumBerakhir = !tanggal.isAfter(tanggalAkhir);
        return sudahMulai && belumBerakhir;
    }
    //Menghasilkan teks periode untuk pratinjau dan tabel
    public String getPeriode(){
        return tanggalMulai.format(FORMAT_TANGGAL)
        + " s/d "
        + tanggalAkhir.format(FORMAT_TANGGAL);
    }
    public String getHargaTeks(){
        return Rupiah.format(harga);
    }

    @Override 
    public String cetakBukti(){
        StringBuilder teks = new StringBuilder();
        teks.append("BUKTI PEMBAYARAN AKSES\n");
        teks.append("No. Bukti : ").append(id).append("\n");
        teks.append("Waktu     : ").append(waktuBayar.format(FORMAT_WAKTU)).append("\n");
        teks.append("Pelanggan : ").append(pelanggan.tampilkanInfo()).append("\n");
        teks.append("Paket     : ").append(namaPaket).append("\n");
        teks.append("Harga     : ").append(getHargaTeks()).append("\n");
        teks.append("Berlaku   : ").append(getPeriode()).append("\n");
        teks.append("Petugas   : ").append(namaAdmin).append("\n");
        teks.append("Status    : LUNAS");
        return teks.toString();
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Pelanggan getPelanggan(){
        return pelanggan;
    }
    public Long getPaketId(){
        return paketId;
    }
    public Long getAdminId(){
        return adminId;
    }
    public String getNamaAdmin(){
        return namaAdmin;
    }
    public String getNamaPaket(){
        return namaPaket;
    }
    public BigDecimal getHarga(){
        return harga;
    }
    public LocalDateTime getWaktuBayar(){
        return waktuBayar;
    }
    public void setWaktuBayar(LocalDateTime waktuBayar){
        this.waktuBayar = waktuBayar;
    }
    public LocalDate getTanggalMulai(){
        return tanggalMulai;
    }
    public LocalDate getTanggalAkhir(){
        return tanggalAkhir;
    }
}