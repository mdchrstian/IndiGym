package id.indigym.database;

import id.indigym.model.Pelanggan;
import id.indigym.model.PembayaranAkses;

import java.sql.Statement;
import java.time.LocalDate;

import id.indigym.model.PaketAkses;
import id.indigym.model.PaketHarian;
import id.indigym.model.PaketMembership;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AksesDAO{
    //Segmen 1. Mengambil daftar paket MySQL
    public List<PaketAkses> daftarPaket() throws SQLException{
        String sql = "SELECT id, jenis, nama, harga, durasi_bulan, diarsipkan "
        + "FROM paket_akses "
        + "WHERE diarsipkan = FALSE "
        + "ORDER BY id";
        List <PaketAkses> daftar = new ArrayList<>();
        try(
            Connection koneksi = Database.getConnection();
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            ResultSet hasil = perintah.executeQuery()
    ) {
        while (hasil.next()){
            daftar.add(bacaPaket(hasil));
        }
     }
     return daftar;
    }
    //Mengubah satu baris hasil querry menjadi objek paket
    private  PaketAkses bacaPaket(ResultSet hasil) throws SQLException{
        Long id = hasil.getLong("id");
        String jenis = hasil.getString("jenis");
        String nama = hasil.getString("nama");
        BigDecimal harga = hasil.getBigDecimal("harga");
        boolean diarsipkan = hasil.getBoolean("diarsipkan");
        if("MEMBERSHIP".equals(jenis)){
            int durasiBulan = hasil.getInt("durasi_bulan");
            if(hasil.wasNull()){
                throw new SQLException("Paket membership tidak memiliki durasi bulan");
            }
            return new PaketMembership(id, nama, harga, durasiBulan, diarsipkan);
        }
        if("HARIAN".equals(jenis)){
            return new PaketHarian(id, nama, harga, diarsipkan);
        }
        throw new SQLException("Jenis paket tidak dikenali " + jenis);
    }
    //Segmen 2 Memasukkan paket baru ke tabel paket_akses
    //Menyimpan paket baru ke database
    public void simpanPaket(PaketAkses paket) throws SQLException{
        String sql = "INSERT INTO paket_akses "
        + "(jenis, nama, harga, durasi_bulan) "
        + "VALUES (?, ?, ?, ?)";
        try(
            Connection koneksi = Database.getConnection();
            PreparedStatement perintah = koneksi.prepareStatement(sql)
            ){
                perintah.setString(1, paket.getJenis());
                perintah.setString(2, paket.getNama());
                perintah.setBigDecimal(3, paket.getHarga());
                Integer durasi = paket.getDurasiBulan();
                if (durasi == null){
                    perintah.setNull(4, java.sql.Types.INTEGER);
                }
                else{
                    perintah.setInt(4, durasi);
                }
                perintah.executeUpdate();
            }
    }
    //Segmen 3 Mengubah status paket menjadi diarsipkan tanpa menghapus riwayatnya
    //Mengarsipkan paket berdasarkan ID
    public void arsipkanPaket(Long id) throws SQLException{
        String sql = "UPDATE paket_akses "
        + "SET diarsipkan = TRUE "
        + "WHERE id = ?";
        try(
            Connection koneksi = Database.getConnection();
            PreparedStatement perintah = koneksi.prepareStatement(sql)
        ){
            perintah.setLong(1, id);
            perintah.executeUpdate();
        }
    }
    
    //Segmen 4a Menyimpan Pembayaran menggunakan koneksi sendiri
    public void simpanPembayaran(PembayaranAkses pembayaran)throws SQLException{
        try(Connection koneksi = Database.getConnection()){
            simpanPembayaran(koneksi ,pembayaran);
        }
    }
    //Segmen 4b Menyimpan pembayaran menggunakan koneksi dari luar
    public void simpanPembayaran(Connection koneksi, PembayaranAkses pembayaran) throws SQLException{
        String sql = "INSERT INTO pembayaran_akses "
        + "(pelanggan_id, paket_id, admin_id, nama_paket, harga, "
        + "waktu_bayar, tanggal_mulai, tanggal_akhir) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement perintah = koneksi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            perintah.setLong(1, pembayaran.getPelanggan().getId());
            perintah.setLong(2, pembayaran.getPaketId());
            perintah.setLong(3, pembayaran.getAdminId());
            perintah.setString(4, pembayaran.getNamaPaket());
            perintah.setBigDecimal(5, pembayaran.getHarga());
            perintah.setObject(6, pembayaran.getWaktuBayar());
            perintah.setObject(7, pembayaran.getTanggalMulai());
            perintah.setObject(8, pembayaran.getTanggalAkhir());
            perintah.executeUpdate();

            try(ResultSet kunci = perintah.getGeneratedKeys()){
                if(!kunci.next()){
                    throw new SQLException("ID Pembayaran gagal diperoleh.");
                }
                pembayaran.setId(kunci.getLong(1));
            }
        }
    }
    //Segmen 5 Mencari Tanggal Terjauh Milik Pelanggan
    public LocalDate cariTanggalAkhirTerjauh(Long pelangganId) throws SQLException{
        String sql = "SELECT MAX(tanggal_akhir) "
        + "FROM pembayaran_akses "
        + "WHERE pelanggan_id = ?";
        try(
            Connection koneksi = Database.getConnection();
            PreparedStatement perintah = koneksi.prepareStatement(sql)
        ){
            perintah.setLong(1, pelangganId);
            try (ResultSet hasil = perintah.executeQuery()){
                if(hasil.next()){
                    return hasil.getObject(1, LocalDate.class);
                }
                return null;
            }
        }
    }
}