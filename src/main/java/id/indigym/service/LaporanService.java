package id.indigym.service;

import id.indigym.database.AksesDAO;
import id.indigym.database.PenjualanDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class LaporanService {

    private AksesDAO aksesDAO = new AksesDAO();
    private PenjualanDAO penjualanDAO = new PenjualanDAO();

    public BigDecimal pendapatanMembership(LocalDate dari, LocalDate sampai) {
        pastikanLogin();
        periksaRentang(dari, sampai);
        try {
            return aksesDAO.totalPembayaran("MEMBERSHIP", dari, sampai);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public BigDecimal pendapatanHarian(LocalDate dari, LocalDate sampai) {
        pastikanLogin();
        periksaRentang(dari, sampai);
        try {
            return aksesDAO.totalPembayaran("HARIAN", dari, sampai);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public BigDecimal pendapatanProduk(LocalDate dari, LocalDate sampai) {
        pastikanLogin();
        periksaRentang(dari, sampai);
        try {
            return penjualanDAO.totalPenjualan(dari, sampai);
        } catch (SQLException e) {
            throw errorDatabase(e);
        }
    }

    public static BigDecimal hitungTotal(BigDecimal membership, BigDecimal harian, BigDecimal produk) {
        return membership.add(harian).add(produk);
    }

    public static void periksaRentang(LocalDate dari, LocalDate sampai) {
        if (dari == null || sampai == null) {
            throw new IllegalArgumentException("Isi tanggal dari dan sampai.");
        }
        if (dari.isAfter(sampai)) {
            throw new IllegalArgumentException("Tanggal dari tidak boleh sesudah tanggal sampai.");
        }
    }

    private void pastikanLogin() {
        if (!Sesi.sudahLogin()) {
            throw new IllegalArgumentException("Silakan login dulu.");
        }
    }

    private IllegalArgumentException errorDatabase(SQLException e) {
        return new IllegalArgumentException("Tidak bisa terhubung ke database: " + e.getMessage());
    }
}
