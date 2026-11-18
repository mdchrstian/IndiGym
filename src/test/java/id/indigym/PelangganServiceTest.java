package id.indigym;

import static org.junit.jupiter.api.Assertions.assertThrows;

import id.indigym.model.Admin;
import id.indigym.service.PelangganService;
import id.indigym.service.Sesi;
import org.junit.jupiter.api.Test;

class PelangganServiceTest {

    private PelangganService pelangganService = new PelangganService();

    @Test
    void belumLoginDitolak() {
        Sesi.adminLogin = null;
        assertThrows(IllegalArgumentException.class, () -> pelangganService.daftar("Andi", null));
    }

    @Test
    void namaKosongDitolak() {
        Sesi.adminLogin = new Admin(1L, "A", "a", "x", true);
        assertThrows(IllegalArgumentException.class, () -> pelangganService.daftar("   ", null));
        Sesi.adminLogin = null;
    }

    @Test
    void ubahTanpaMemilihPelangganDitolak() {
        Sesi.adminLogin = new Admin(1L, "A", "a", "x", true);
        assertThrows(IllegalArgumentException.class, () -> pelangganService.ubah(null, "Andi", null));
        Sesi.adminLogin = null;
    }

    @Test
    void checkInTanpaKodeDitolak() {
        Sesi.adminLogin = new Admin(1L, "A", "a", "x", true);
        assertThrows(IllegalArgumentException.class, () -> pelangganService.checkIn("  ", false));
        Sesi.adminLogin = null;
    }

    @Test
    void beliVisitTanpaLoginDitolak() {
        Sesi.adminLogin = null;
        assertThrows(IllegalArgumentException.class, () -> pelangganService.beliVisit("PLG1"));
    }
}
