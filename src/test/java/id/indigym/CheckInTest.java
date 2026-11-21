package id.indigym;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import id.indigym.model.CheckIn;

class CheckInTest {

    private LocalDateTime waktu = LocalDateTime.of(2026, 11, 21, 18, 25);

    @Test
    void diizinkanMenyebutNamaPaket() {
        CheckIn checkIn = new CheckIn(1L, null, 1L, "Admin", 5L, waktu, CheckIn.DIIZINKAN, null);
        checkIn.setNamaPaket("Member 1 Bulan");
        assertEquals("Diizinkan, Member 1 Bulan", checkIn.getKeterangan());
    }

    @Test
    void ditolakMenyebutAlasan() {
        CheckIn checkIn = new CheckIn(2L, null, 1L, "Admin", null, waktu, CheckIn.DITOLAK, "Tidak punya akses hari ini.");
        assertEquals("Ditolak: Tidak punya akses hari ini.", checkIn.getKeterangan());
    }
}