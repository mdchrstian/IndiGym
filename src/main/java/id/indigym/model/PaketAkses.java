package id.indigym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class PaketAkses{
    //atribut
    private Long id;
    private String nama;
    private BigDecimal harga;
    private boolean diarsipkan;

    //constructor
    public PaketAkses(Long id, String nama, BigDecimal harga, boolean diarsipkan){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.diarsipkan = diarsipkan;
    }

    //method abstrak
    public abstract LocalDate hitungTanggalAkhir(LocalDate tanggalMulai);
    public abstract String getJenis();
    public abstract Integer getDurasiBulan();

    //method tambahan
    @Override 
    public String toString(){
        return nama + " . " + getHargaTeks();
    }
        //getter
        public Long getId(){
            return id;
        }
        public void setId(Long id){
            this.id = id;
        }
        public String getNama(){
            return nama;
        }
        public BigDecimal getHarga(){
            return harga;
        }
        public boolean isDiarsipkan(){
            return diarsipkan;
        }
    public String getHargaTeks(){
        return Rupiah.format(harga);    
    }
    public String getDurasiTeks(){
        if(getDurasiBulan() == null){
            return "Hari itu saja";
        }
        return getDurasiBulan() + " bulan";
    }
}