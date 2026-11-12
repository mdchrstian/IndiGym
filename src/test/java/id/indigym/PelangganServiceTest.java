package id.indigym;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}