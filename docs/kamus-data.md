# Kamus data rancangan IndiGym

Rincian berikut berasal dari kamus data panduan kelompok. Ini acuan skema yang perlu diimplementasikan Tian, bukan SQL yang sudah tersedia.

Seluruh ID internal memakai BIGINT dengan mekanisme ID otomatis database; FK memakai tipe yang sama dengan PK yang dirujuk. Nominal memakai DECIMAL(12,2), periode memakai DATE, dan kejadian memakai DATETIME(6) dengan konvensi UTC. Kolom wajib diisi kecuali ditandai NULL. Penambahan field teknis untuk idempotensi atau pemetaan lain harus disepakati bersama pemilik service.

| Tabel | Kolom utama dan ketentuan |
| --- | --- |
| `akun_admin` | id PK; nama VARCHAR(100); kontak VARCHAR(30) NULL; username VARCHAR(50) UNIQUE; password_hash VARCHAR(255); role VARCHAR(20) bernilai ADMIN/SUPER_ADMIN; aktif BOOLEAN; session_version INT. Nama dan username wajib. |
| `pelanggan` | id PK; kode_pelanggan VARCHAR(30) UNIQUE; nama VARCHAR(100); kontak VARCHAR(30) NULL; diarsipkan BOOLEAN; created_at DATETIME(6). Nama wajib; nama bukan unique key. |
| `paket_akses` | id PK; nama VARCHAR(100); jenis VARCHAR(20) HARIAN/MEMBERSHIP; harga DECIMAL(12,2); durasi_bulan INT NULL; diarsipkan BOOLEAN. Membership harus memiliki durasi positif; harian tidak memakai durasi_bulan. |
| `pembayaran_akses` | id PK; request_id CHAR(36) UNIQUE; pelanggan_id, paket_id, admin_id FK; tanggal_bayar DATE; waktu_utc DATETIME(6); jumlah_bayar DECIMAL(12,2); nama_paket_snapshot, jenis_snapshot, harga_snapshot, durasi_bulan_snapshot; status LUNAS/DIBATALKAN; dibatalkan_oleh FK NULL; dibatalkan_pada NULL; alasan NULL. |
| `hak_akses` | id PK; pembayaran_id FK UNIQUE NOT NULL; tanggal_mulai DATE; tanggal_terakhir_berlaku DATE. Akhir tidak boleh sebelum mulai. Identitas pelanggan diperoleh melalui pembayaran sumber; tidak ada status aktif manual. |
| `checkin` | id PK; request_id CHAR(36) UNIQUE; pelanggan_id dan admin_id FK; hak_akses_id FK NULL; waktu_utc DATETIME(6); hasil VARCHAR(20) DIIZINKAN/DITOLAK; alasan VARCHAR(50), keterangan opsional. Hasil DIIZINKAN membutuhkan hak_akses_id. |
| `produk` | id BIGINT PK; kode_produk VARCHAR(30) UNIQUE; nama VARCHAR(100); harga DECIMAL(12,2); stok INT; diarsipkan BOOLEAN. Harga dan stok tidak negatif. |
| `transaksi_produk` | id BIGINT PK; request_id CHAR(36) UNIQUE; pelanggan_id FK NULL; admin_id FK; waktu_utc DATETIME(6); total_harga DECIMAL(12,2); status SELESAI/DIBATALKAN; dibatalkan_oleh FK NULL; dibatalkan_pada NULL; alasan NULL. |
| `detail_transaksi` | id BIGINT PK; transaksi_id FK; produk_id FK; nama_produk_snapshot VARCHAR(100); jumlah INT positif; harga_satuan dan subtotal DECIMAL(12,2). UNIQUE(transaksi_id, produk_id). transaksi_id adalah foreign key, bukan primary key tunggal. |
| `mutasi_stok` | id BIGINT PK; request_id CHAR(36) UNIQUE; produk_id dan admin_id FK; transaksi_id FK NULL; jenis MASUK/PENJUALAN/BATAL_PENJUALAN; delta INT; waktu_utc DATETIME(6); keterangan VARCHAR(255). UNIQUE(transaksi_id, produk_id, jenis) mencegah mutasi transaksi ganda. |

Referensi historis tidak dihapus melalui ON DELETE CASCADE. Indeks mendukung pencarian pelanggan, status, periode dan rentang waktu. Model Manusia/Cetak tidak memerlukan tabel sendiri. Admin/SuperAdmin memakai role pada akun_admin; PaketHarian/PaketMembership memakai jenis pada paket_akses.

Hak akses sah tetap bergantung pada pembayaran sumber dan tanggal; tidak ada kolom aktif manual. Penjualan/detail/stok/mutasi disimpan satu transaksi, dan detail transaksi mempunyai ID sendiri serta batas unik transaksi-produk. Kolom dan constraint saja tidak menggantikan validasi lintas tabel oleh service dalam transaksi.

[Panduan file dan SQL](panduan-file.md) · [Kontrak integrasi](kontrak-integrasi.md)
