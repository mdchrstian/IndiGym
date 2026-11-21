package id.indigym.database;

import id.indigym.model.DetailTransaksi;
import id.indigym.model.Pelanggan;
import id.indigym.model.Produk;
import id.indigym.model.Transaksi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PenjualanDAO {

    public List<Produk> cariProduk(String kataKunci) throws SQLException {
        String sql = "SELECT * FROM produk WHERE diarsipkan = FALSE AND (nama LIKE ? OR kode LIKE ?) ORDER BY id";
        List<Produk> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, "%" + kataKunci + "%");
            perintah.setString(2, "%" + kataKunci + "%");
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaProduk(hasil));
            }
        }
        return daftar;
    }

    public Produk cariProdukById(Long id) throws SQLException {
        String sql = "SELECT * FROM produk WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, id);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaProduk(hasil);
            }
            return null;
        }
    }

    public Produk cariProdukByKode(String kode) throws SQLException {
        String sql = "SELECT * FROM produk WHERE kode = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, kode);
            ResultSet hasil = perintah.executeQuery();
            if (hasil.next()) {
                return bacaProduk(hasil);
            }
            return null;
        }
    }

    public void simpanProduk(Produk produk) throws SQLException {
        String sql = "INSERT INTO produk (kode, nama, harga, stok) VALUES (?, ?, ?, ?)";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, produk.getKode());
            perintah.setString(2, produk.getNama());
            perintah.setBigDecimal(3, produk.getHarga());
            perintah.setInt(4, produk.getStok());
            perintah.executeUpdate();
        }
    }

    public void ubahProduk(Long id, String nama, BigDecimal harga) throws SQLException {
        String sql = "UPDATE produk SET nama = ?, harga = ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setString(1, nama);
            perintah.setBigDecimal(2, harga);
            perintah.setLong(3, id);
            perintah.executeUpdate();
        }
    }

    public void tambahStok(Long id, int jumlah) throws SQLException {
        String sql = "UPDATE produk SET stok = stok + ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setInt(1, jumlah);
            perintah.setLong(2, id);
            perintah.executeUpdate();
        }
    }

    public void arsipkanProduk(Long id) throws SQLException {
        String sql = "UPDATE produk SET diarsipkan = TRUE WHERE id = ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, id);
            perintah.executeUpdate();
        }
    }

    public void simpanTransaksi(Transaksi transaksi) throws SQLException {
        try (Connection koneksi = Database.getConnection()) {
            koneksi.setAutoCommit(false); // mulai transaksi database
            try {
                simpanKepalaTransaksi(koneksi, transaksi);
                for (DetailTransaksi detail : transaksi.getDaftarDetail()) {
                    kurangiStok(koneksi, detail);
                    simpanDetail(koneksi, transaksi.getId(), detail);
                }
                koneksi.commit();
            } catch (SQLException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    public void batalkanTransaksi(Long transaksiId, String alasan) throws SQLException {
        String sqlStatus = "UPDATE transaksi_produk SET status = 'BATAL', alasan_batal = ? WHERE id = ? AND status = 'LUNAS'";
        String sqlStok = "UPDATE produk SET stok = stok + ? WHERE id = ?";
        try (Connection koneksi = Database.getConnection()) {
            koneksi.setAutoCommit(false);
            try (PreparedStatement ubahStatus = koneksi.prepareStatement(sqlStatus);
                 PreparedStatement kembalikanStok = koneksi.prepareStatement(sqlStok)) {
                ubahStatus.setString(1, alasan);
                ubahStatus.setLong(2, transaksiId);

                // "AND status = 'LUNAS'" mencegah pembatalan ganda: stok tidak akan dikembalikan dua kali
                if (ubahStatus.executeUpdate() == 0) {
                    throw new SQLException("Transaksi sudah dibatalkan atau tidak ditemukan.");
                }
                for (DetailTransaksi detail : daftarDetail(koneksi, transaksiId)) {
                    kembalikanStok.setInt(1, detail.getJumlah());
                    kembalikanStok.setLong(2, detail.getProdukId());
                    kembalikanStok.executeUpdate();
                }
                koneksi.commit();
            } catch (SQLException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    public BigDecimal totalPenjualan(LocalDate dari, LocalDate sampai) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM transaksi_produk WHERE status = 'LUNAS' AND DATE(waktu) BETWEEN ? AND ?";
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setObject(1, dari);
            perintah.setObject(2, sampai);
            ResultSet hasil = perintah.executeQuery();
            hasil.next();
            return hasil.getBigDecimal(1);
        }
    }

    public List<Transaksi> daftarTransaksi() throws SQLException {
        String sql = "SELECT t.*, a.nama AS nama_admin, p.kode AS kode_pelanggan, p.nama AS nama_pelanggan "
                + "FROM transaksi_produk t JOIN akun_admin a ON a.id = t.admin_id "
                + "LEFT JOIN pelanggan p ON p.id = t.pelanggan_id ORDER BY t.id DESC";
        List<Transaksi> daftar = new ArrayList<>();
        try (Connection koneksi = Database.getConnection();
             PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(bacaTransaksi(koneksi, hasil));
            }
        }
        return daftar;
    }

    private void simpanKepalaTransaksi(Connection koneksi, Transaksi transaksi) throws SQLException {
        String sql = "INSERT INTO transaksi_produk (pelanggan_id, admin_id, waktu, total, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (transaksi.getPelanggan() == null) {
                perintah.setObject(1, null); // pembeli umum
            } else {
                perintah.setLong(1, transaksi.getPelanggan().getId());
            }
            perintah.setLong(2, transaksi.getAdminId());
            perintah.setObject(3, transaksi.getWaktu());
            perintah.setBigDecimal(4, transaksi.hitungTotal());
            perintah.setString(5, transaksi.getStatus());
            perintah.executeUpdate();

            ResultSet kunci = perintah.getGeneratedKeys();
            kunci.next();
            transaksi.setId(kunci.getLong(1));
        }
    }

    private void kurangiStok(Connection koneksi, DetailTransaksi detail) throws SQLException {
        String sql = "UPDATE produk SET stok = stok - ? WHERE id = ? AND stok >= ?";
        try (PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setInt(1, detail.getJumlah());
            perintah.setLong(2, detail.getProdukId());
            perintah.setInt(3, detail.getJumlah());
            if (perintah.executeUpdate() == 0) {
                throw new SQLException("Stok " + detail.getNamaProduk() + " tidak cukup.");
            }
        }
    }

    private void simpanDetail(Connection koneksi, Long transaksiId, DetailTransaksi detail) throws SQLException {
        String sql = "INSERT INTO detail_transaksi (transaksi_id, produk_id, nama_produk, harga_satuan, jumlah, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, transaksiId);
            perintah.setLong(2, detail.getProdukId());
            perintah.setString(3, detail.getNamaProduk());
            perintah.setBigDecimal(4, detail.getHargaSatuan());
            perintah.setInt(5, detail.getJumlah());
            perintah.setBigDecimal(6, detail.hitungSubtotal());
            perintah.executeUpdate();
        }
    }

    private List<DetailTransaksi> daftarDetail(Connection koneksi, Long transaksiId) throws SQLException {
        String sql = "SELECT * FROM detail_transaksi WHERE transaksi_id = ? ORDER BY id";
        List<DetailTransaksi> daftar = new ArrayList<>();
        try (PreparedStatement perintah = koneksi.prepareStatement(sql)) {
            perintah.setLong(1, transaksiId);
            ResultSet hasil = perintah.executeQuery();
            while (hasil.next()) {
                daftar.add(new DetailTransaksi(
                        hasil.getLong("produk_id"),
                        hasil.getString("nama_produk"),
                        hasil.getBigDecimal("harga_satuan"),
                        hasil.getInt("jumlah")));
            }
        }
        return daftar;
    }

    private Produk bacaProduk(ResultSet hasil) throws SQLException {
        return new Produk(
                hasil.getLong("id"),
                hasil.getString("kode"),
                hasil.getString("nama"),
                hasil.getBigDecimal("harga"),
                hasil.getInt("stok"),
                hasil.getBoolean("diarsipkan"));
    }

    private Transaksi bacaTransaksi(Connection koneksi, ResultSet hasil) throws SQLException {
        Long id = hasil.getLong("id");

        // pelanggan_id bernilai NULL untuk pembeli umum
        Pelanggan pelanggan = null;
        if (hasil.getObject("pelanggan_id") != null) {
            pelanggan = new Pelanggan(hasil.getLong("pelanggan_id"), hasil.getString("kode_pelanggan"),
                    hasil.getString("nama_pelanggan"), null, false);
        }
        return new Transaksi(id, pelanggan, hasil.getLong("admin_id"), hasil.getString("nama_admin"),
                hasil.getObject("waktu", LocalDateTime.class), hasil.getString("status"),
                hasil.getString("alasan_batal"), daftarDetail(koneksi, id));
    }
}
