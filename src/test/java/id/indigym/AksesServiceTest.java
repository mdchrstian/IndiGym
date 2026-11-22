
package id.indigym;

import static org.junit.jupiter.api.Assertions.assertEquals;

import id.indigym.service.AksesService;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

// Menguji aturan tanggal mulai membership tanpa database.
class AksesServiceTest {

    private final LocalDate hariIni =
            LocalDate.of(2026, 11, 10);

    @Test
    void belumPernahBayarMulaiHariIni() {
        assertEquals(
                hariIni,
                AksesService.tentukanTanggalMulai(null, hariIni)
        );
    }

    @Test
    void aksesSudahLewatMulaiHariIni() {
        assertEquals(
                hariIni,
                AksesService.tentukanTanggalMulai(
                        LocalDate.of(2026, 9, 30),
                        hariIni
                )
        );
    }

    @Test
    void masihPunyaAksesMulaiSetelahAksesBerakhir() {
        // Membership lama berakhir 9 Desember,
        // membership baru mulai 10 Desember.
        assertEquals(
                LocalDate.of(2026, 12, 10),
                AksesService.tentukanTanggalMulai(
                        LocalDate.of(2026, 12, 9),
                        hariIni
                )
        );
    }

    @Test
    void visitHariIniMembershipMulaiBesok() {
        // Visit berlaku hari ini, membership mulai besok.
        assertEquals(
                LocalDate.of(2026, 11, 11),
                AksesService.tentukanTanggalMulai(
                        hariIni,
                        hariIni
                )
        );
    }
}
