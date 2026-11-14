package id.indigym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import id.indigym.model.Admin;
import id.indigym.model.SuperAdmin;
import id.indigym.service.AkunService;
import id.indigym.service.Sesi;
import org.junit.jupiter.api.Test;

class AkunServiceTest {

    private AkunService akunService = new AkunService();

    @Test
    void hashPasswordSamaDenganDataAwal() {
        assertEquals("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
                akunService.hashPassword("admin123"));
    }

    @Test
    void hanyaSuperAdminBolehKelolaAdmin() {
        assertFalse(new Admin(1L, "A", "a", "x", true).bolehKelolaAdmin());
        assertTrue(new SuperAdmin(2L, "S", "s", "x", true).bolehKelolaAdmin());
    }

    @Test
    void loginKosongDitolakTanpaMenyentuhDatabase() {
        assertThrows(IllegalArgumentException.class, () -> akunService.login("", "abc"));
        assertThrows(IllegalArgumentException.class, () -> akunService.login("abc", ""));
    }

    @Test
    void adminBiasaDitolakMenambahAdmin() {
        Sesi.adminLogin = new Admin(1L, "A", "a", "x", true);
        assertThrows(IllegalArgumentException.class, () -> akunService.tambahAdmin("Budi", "budi", "rahasia1"));
        Sesi.adminLogin = null;
    }

    @Test
    void passwordPendekDitolak() {
        Sesi.adminLogin = new SuperAdmin(2L, "S", "s", "x", true);
        assertThrows(IllegalArgumentException.class, () -> akunService.tambahAdmin("Budi", "budi", "123"));
        Sesi.adminLogin = null;
    }
}