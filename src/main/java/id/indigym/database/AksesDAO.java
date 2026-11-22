package id.indigym.database;

import id.indigym.model.PaketAkses;
import id.indigym.model.PaketHarian;
import id.indigym.model.PaketMembership;
import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AksesDAO {
    public List<PaketAkses> daftarPaket() throws SQLException {
        String sql = "SELECT * FROM paket_akses WHERE diarsipkan = FALSE ORDER BY id";
        List<PaketAkses> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaPaket(hasil));
            }
        }
        return daftar;
    }

    public void simpanPaket(PaketAkses paket) throws SQLException {
        String sql = "INSERT INTO paket_akses (jenis, nama, harga, durasi_bulan) VALUES (?, ?, ?, ?)";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, paket.getJenis());
            perintah.setString(2, paket.getNama());
            perintah.setBigDecimal(3, paket.getHarga());
            perintah.setObject(4, paket.getDurasiBulan());
            perintah.executeUpdate();
        }
    }

    public void arsipkanPaket(Long id) throws SQLException {
        String sql = "UPDATE paket_akses SET diarsipkan = TRUE WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, id);
            perintah.executeUpdate();
        }
    }

    public void simpanPembayaran(PembayaranAkses pembayaran) throws SQLException {
        try (Connection koneksi = Database.getConnection()) {
            simpanPembayaran(koneksi, pembayaran);
        }
    }

    public void simpanPembayaran(Connection koneksi, PembayaranAkses pembayaran) throws SQLException {
        String sql = "INSERT INTO pembayaran_akses (pelanggan_id, paket_id, admin_id, nama_paket, harga, "
                + "waktu_bayar, tanggal_mulai, tanggal_akhir) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            perintah.setLong(1, pembayaran.getPelanggan().getId());
            perintah.setLong(2, pembayaran.getPaketId());
            perintah.setLong(3, pembayaran.getAdminId());
            perintah.setString(4, pembayaran.getNamaPaket());
            perintah.setBigDecimal(5, pembayaran.getHarga());
            perintah.setObject(6, pembayaran.getWaktuBayar());
            perintah.setObject(7, pembayaran.getTanggalMulai());
            perintah.setObject(8, pembayaran.getTanggalAkhir());
            perintah.executeUpdate();

            ResultSet kunci = perintah.getGeneratedKeys();
            kunci.next();
            pembayaran.setId(kunci.getLong(1));
        }
    }

    public LocalDate cariTanggalAkhirTerjauh(Long pelangganId) throws SQLException {
        String sql = "SELECT MAX(tanggal_akhir) FROM pembayaran_akses WHERE pelanggan_id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelangganId);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getObject(1, LocalDate.class);
        }
    }

    public PembayaranAkses cariAksesAktif(Pelanggan pelanggan, LocalDate tanggal) throws SQLException {
        String sql = BAGIAN_SELECT + " WHERE p.pelanggan_id = ? AND p.tanggal_mulai <= ? AND p.tanggal_akhir >= ? "
                + "ORDER BY p.tanggal_akhir DESC LIMIT 1";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelanggan.getId());
            perintah.setObject(2, tanggal);
            perintah.setObject(3, tanggal);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaPembayaran(hasil, pelanggan);
            }
            return null;
        }
    }

    public PembayaranAkses cariMembershipBelumBerakhir(Pelanggan pelanggan, LocalDate tanggal) throws SQLException {
        String sql = BAGIAN_SELECT + " JOIN paket_akses k ON k.id = p.paket_id"
                + " WHERE p.pelanggan_id = ? AND k.jenis = 'MEMBERSHIP' AND p.tanggal_akhir >= ?"
                + " ORDER BY p.tanggal_akhir DESC LIMIT 1";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelanggan.getId());
            perintah.setObject(2, tanggal);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaPembayaran(hasil, pelanggan);
            }
            return null;
        }
    }

    public List<PembayaranAkses> daftarMembershipBelumBerakhir(LocalDate tanggal) throws SQLException {
        String sql = "SELECT p.*, a.nama AS nama_admin, pl.kode AS kode_pelanggan, pl.nama AS nama_pelanggan, "
                + "pl.kontak AS kontak_pelanggan FROM pembayaran_akses p "
                + "JOIN akun_admin a ON a.id = p.admin_id "
                + "JOIN paket_akses k ON k.id = p.paket_id "
                + "JOIN pelanggan pl ON pl.id = p.pelanggan_id "
                + "WHERE k.jenis = 'MEMBERSHIP' AND p.tanggal_akhir >= ? AND pl.diarsipkan = FALSE "
                + "ORDER BY p.tanggal_akhir, pl.nama";
        List<PembayaranAkses> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setObject(1, tanggal);
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                Pelanggan pelanggan = new Pelanggan(hasil.getLong("pelanggan_id"), hasil.getString("kode_pelanggan"),
                        hasil.getString("nama_pelanggan"), hasil.getString("kontak_pelanggan"), false);
                daftar.add(bacaPembayaran(hasil, pelanggan));
            }
        }
        return daftar;
    }

    public BigDecimal totalPembayaran(String jenis, LocalDate dari, LocalDate sampai) throws SQLException {
        String sql = "SELECT COALESCE(SUM(p.harga), 0) FROM pembayaran_akses p JOIN paket_akses k ON k.id = p.paket_id "
                + "WHERE k.jenis = ? AND DATE(p.waktu_bayar) BETWEEN ? AND ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, jenis);
            perintah.setObject(2, dari);
            perintah.setObject(3, sampai);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getBigDecimal(1);
        }
    }

    public int hitungPelangganBerakses(LocalDate tanggal) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT pelanggan_id) FROM pembayaran_akses WHERE tanggal_mulai <= ? AND tanggal_akhir >= ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setObject(1, tanggal);
            perintah.setObject(2, tanggal);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getInt(1);
        }
    }

    public List<PembayaranAkses> daftarPembayaran(Pelanggan pelanggan) throws SQLException {
        String sql = BAGIAN_SELECT + " WHERE p.pelanggan_id = ? ORDER BY p.id DESC";
        List<PembayaranAkses> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, pelanggan.getId());
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaPembayaran(hasil, pelanggan));
            }
        }
        return daftar;
    }

    private static final String BAGIAN_SELECT = "SELECT p.*, a.nama AS nama_admin FROM pembayaran_akses p "
            + "JOIN akun_admin a ON a.id = p.admin_id";

    private PaketAkses bacaPaket(ResultSet hasil) throws SQLException {
        Long id = hasil.getLong("id");
        String nama = hasil.getString("nama");
        BigDecimal harga = hasil.getBigDecimal("harga");
        boolean diarsipkan = hasil.getBoolean("diarsipkan");

        if (hasil.getString("jenis").equals("MEMBERSHIP")) {
            return new PaketMembership(id, nama, harga, hasil.getInt("durasi_bulan"), diarsipkan);
        }
        return new PaketHarian(id, nama, harga, diarsipkan);
    }

    private PembayaranAkses bacaPembayaran(ResultSet hasil, Pelanggan pelanggan) throws SQLException {
        return new PembayaranAkses(
                hasil.getLong("id"),
                pelanggan,
                hasil.getLong("paket_id"),
                hasil.getLong("admin_id"),
                hasil.getString("nama_admin"),
                hasil.getString("nama_paket"),
                hasil.getBigDecimal("harga"),
                hasil.getObject("waktu_bayar", java.time.LocalDateTime.class),
                hasil.getObject("tanggal_mulai", LocalDate.class),
                hasil.getObject("tanggal_akhir", LocalDate.class));
    }
}
