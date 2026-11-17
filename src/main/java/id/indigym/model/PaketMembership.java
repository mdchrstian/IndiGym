package id.indigym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaketMembership extends PaketAkses{
    private int durasiBulan;

    //Constructor
    public PaketMembership(Long id, String nama, BigDecimal harga, int durasiBulan, boolean diarsipkan){
        super(id, nama, harga, diarsipkan);
            //Validasi Durasi Paket Membership
        if (durasiBulan<= 0){
            throw new IllegalArgumentException("Durasi membership harus lebih dari 0 bulan.");
        }
        this.durasiBulan = durasiBulan;
    }
    //Method
    @Override 
    public String getJenis(){
        return "MEMBERSHIP";
    }
    @Override 
    public Integer getDurasiBulan(){
        return durasiBulan;
    }
    //Menghitung Tanggal Akhir Membership
    @Override 
    public LocalDate hitungTanggalAkhir(LocalDate tanggalMulai){
        LocalDate tanggalTujuan =  tanggalMulai.plusMonths(durasiBulan);
        if(tanggalTujuan.getDayOfMonth() == tanggalMulai.getDayOfMonth()){
            return tanggalTujuan.minusDays(1);
        }
        return tanggalTujuan;
    }
}
