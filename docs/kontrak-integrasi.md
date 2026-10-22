# Kontrak integrasi versi sederhana

32 file Java: MainApp, 16 model, 4 service, 5 class database dan 6 controller. Seluruh file implementasi masih kosong; operasi berikut adalah acuan awal, bukan API yang sudah berjalan.

| Pemilik | Class | Tanggung jawab |
| --- | --- | --- |
| Dimas | AkunService | Login, sesi, hash, pengelolaan Admin dan kewenangan. |
| Dimas | AksesService | Paket, pratinjau/periode, pembayaran, void akses, riwayat/bukti. |
| Mahasurya | PelangganService | Profil, check-in/visit dan riwayat kunjungan. |
| Mahasurya | PenjualanService | Produk, restock, checkout, void penjualan, riwayat/bukti. |
| Tian | Database | Config, koneksi, jalankanTransaksi dan commit/rollback. |
| Tian | AkunDAO | akun_admin. |
| Tian | PelangganDAO | pelanggan dan checkin. |
| Tian | AksesDAO | paket_akses, pembayaran_akses dan hak_akses. |
| Tian | PenjualanDAO | produk, transaksi_produk, detail_transaksi dan mutasi_stok. |
| Wika | MainApp dan 6 controller | Startup, navigasi, halaman, background Task dan dialog bukti. |

Controller memanggil service; service memvalidasi sesi dan aturan lalu memakai DAO; DAO menjalankan JDBC. Model tidak mengimpor JavaFX. DAO adalah class langsung, tanpa interface/implementasi ganda.

## Transaksi tanpa class tambahan

Service terluar memanggil Database.jalankanTransaksi dan menerima satu java.sql.Connection. Seluruh DAO dalam unit kerja memakai Connection tersebut dan tidak commit sendiri. Database menangani commit, rollback dan penutupan koneksi. Method internal bertanda DalamTransaksi menerima Connection dan tidak membuka transaksi baru.

PelangganService.bayarVisitDanCheckIn memeriksa pelanggan/akses, memanggil AksesService.bayarHarianDalamTransaksi(Connection, ...), kemudian menyimpan CheckIn pada koneksi yang sama. Jika satu langkah gagal, pembayaran, hak dan check-in ikut rollback. Pendaftaran profil sebelum konfirmasi boleh tetap ada.

AksesService.bayarMembership menyimpan pembayaran + hak dalam satu transaksi. PenjualanService.selesaikan menyimpan header + detail + saldo + mutasi dalam satu transaksi; pembatalan penjualan dan restock juga atomik. Kunci pelanggan untuk perpanjangan/visit bersamaan dan produk menurut urutan ID agar saldo tidak saling menimpa.

## Input dan hasil

Gunakan model, nilai sederhana, List atau Optional sesuai kebutuhan. Bila pratinjau atau status perlu konfirmasi membutuhkan gabungan nilai, buat hasil kecil di service pemilik setelah disepakati; tidak menyiapkan folder/class transfer atau exception massal sejak awal. Validasi yang ditolak, konfirmasi ulang, kegagalan penyimpanan dan hasil commit belum pasti harus tetap dapat dibedakan, bukan diringkas menjadi boolean sukses/gagal.

Catat nama method, pemanggil, parameter/tipe, field wajib/opsional, hasil, kemungkinan gagal dan batas transaksi sebelum mengerjakan kode yang bergantung padanya. Contoh nama operasi dan isi tiap file ada di [panduan file](panduan-file.md).

ID Long/BIGINT; uang BigDecimal/DECIMAL; tanggal periode LocalDate; waktu kejadian UTC dan tanggal operasional Asia/Makassar. Cetak tetap interface dengan String cetakBukti(). Admin login dan versi sesi disimpan pada AkunService; validasiSesi membaca ulang status/versi lewat AkunDAO sebelum tindakan terlindungi.

Satu konfirmasi memakai requestId tetap saat dicoba ulang; ID sama dengan payload berbeda ditolak. Respons hilang setelah konfirmasi belum berarti rollback: cari hasil dengan requestId yang sama sebelum membuat operasi baru. Header dan mutasi multi-produk mempunyai identitas baris yang sesuai constraint. Detail penyimpanan/perbandingan payload diputuskan Tian bersama pemilik service.

## Hubungan halaman dan fitur

| FXML | Controller | Isi |
| --- | --- | --- |
| login.fxml | LoginController | Login. |
| dashboard.fxml | DashboardController | Menu dan logout. |
| admin.fxml | AdminController | Akun petugas dan password. |
| pelanggan.fxml | PelangganController | Profil, check-in/visit dan riwayat kunjungan. |
| akses.fxml | AksesController | Katalog paket, membership, riwayat/void pembayaran akses. |
| penjualan.fxml | PenjualanController | Katalog/restock, keranjang dan riwayat/void penjualan/mutasi. |

Riwayat ada pada modul terkait. Bukti ditampilkan/disimpan lewat dialog MainApp yang menerima Cetak. Pekerjaan jaringan memakai Task JavaFX di controller, bukan class utilitas khusus.
