package id.indigym;

import id.indigym.model.PaketMembership;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaketMembershipTest{
    //Uji coba 1 Perhitungan Tanggal Normal
    @Test
    void testMembershipSatuBulan(){
        PaketMembership paket = new PaketMembership(1L, "Member 1 Bulan", new BigDecimal("250000"), 1, false);
        LocalDate mulai = LocalDate.of(2026, 10, 10);
        LocalDate hasil = paket.hitungTanggalAkhir(mulai);
        assertEquals(LocalDate.of(2026, 11, 9), hasil);
    }
    //Uji Coba 2 Penyesuaian Februari 27
    @Test 
    void testMembershipAkhirJanuariTahunBiasa(){
        PaketMembership paket = new PaketMembership(2L, "Member 1 Bulan", new BigDecimal("250000"), 1, false);
        LocalDate mulai = LocalDate.of(2027, 1, 31);
        LocalDate hasil = paket.hitungTanggalAkhir(mulai);
        assertEquals(LocalDate.of(2027, 2, 28), hasil);
    }
    //Uji Coba 3 Penyesuaian Tahun Kabisat
    @Test 
    void testMembershipTahunKabisat(){
        PaketMembership paket = new PaketMembership(3L, "Member 1 Bulan", new BigDecimal("250000"), 1, false);
        LocalDate mulai = LocalDate.of(2028,1,31);
        LocalDate hasil = paket.hitungTanggalAkhir(mulai);
        assertEquals(LocalDate.of(2028, 2, 29), hasil);
    }
    //Uji Coba 4 Membership 12 Bulan
    @Test 
    void testMembershipDuaBelasBulan(){
        PaketMembership paket = new PaketMembership(4L, "Member 12 Bulan", new BigDecimal("2500000"), 12, false);
        LocalDate mulai =LocalDate.of(2028, 2, 29);
        LocalDate hasil =paket.hitungTanggalAkhir(mulai);
        assertEquals(LocalDate.of(2029, 2, 28), hasil);
    }
    //Uji Coba 5 (Durasi Nol)
    @Test 
    void testDurasiNolDitolak(){
        assertThrows(
            IllegalArgumentException.class,
            ()-> new PaketMembership(5L, "Member Tidak Valid", new BigDecimal("250000"), 0, false)
        );
    }
    @Test
    void testJenisMembership() {
        PaketMembership paket = new PaketMembership(6L, "Member 1 Bulan", new BigDecimal("250000"), 1, false);
        assertEquals("MEMBERSHIP", paket.getJenis());
    }
    @Test
    void testGetDurasiBulan() {
        PaketMembership paket = new PaketMembership(7L,"Member 12 Bulan",new BigDecimal("2500000"),12,false);
        assertEquals(12, paket.getDurasiBulan());
    }
    @Test
    void testDurasiNegatifDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new PaketMembership( 8L, "Member Tidak Valid", new BigDecimal("250000"), -1, false));
    }
}