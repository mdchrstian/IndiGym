package id.indigym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaketHarian extends PaketAkses{
    //Constructor
    public PaketHarian(Long id, String nama,BigDecimal harga, boolean diarsipkan){
        super(id, nama, harga, diarsipkan);
    }

    //Methode
    @Override 
    public LocalDate hitungTanggalAkhir(LocalDate tanggalMulai){
        return tanggalMulai;
    }
    @Override 
    public String getJenis(){
        return "HARIAN";
    }
    @Override 
    public Integer getDurasiBulan(){
        return null;
    }
}