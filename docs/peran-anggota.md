# Peran dan checklist anggota

## Checklist Dimas selama tiga minggu

Pemilik model identitas/akses dan dua layanan: AkunService serta AksesService. Sesi login dan hash menjadi bagian AkunService. Cetak disepakati bersama Mahasurya.

### Minggu 1 Hari 1 sampai 7

- [ ] D1.1 Sepakati model identitas, ID, role, sesi, bentuk input/hasil, dan method DAO dengan Tian/Mahasurya.

- [ ] D1.2 Implementasikan login/logout, hash password, serta kewenangan Admin dan SuperAdmin.

- [ ] D1.3 Implementasikan PaketAkses dan turunan serta hitungPeriode dengan test tanggal.

- [ ] D1.4 Sediakan service pembayaran membership dasar dan snapshot ketentuan pembelian.

- [ ] D1.5 Berikan kontrak login/pembayaran dan contoh hasil kepada Wika; sambungkan DAO Tian.

- [ ] D1.6 Demo login dan pembayaran membership; tulis penjelasan PBO dan hasil uji awal.

### Minggu 2 Hari 8 sampai 14

- [ ] D2.1 Lengkapi kelola Admin, reset password, akun nonaktif, dan perlindungan SuperAdmin awal.

- [ ] D2.2 Lengkapi perpanjangan awal, terlambat, berulang, akhir bulan, dan visit ke membership besok.

- [ ] D2.3 Sediakan operasi pembayaran internal yang menerima Connection transaksi visit milik Mahasurya.

- [ ] D2.4 Implementasikan pembatalan membership terbaru/visit dengan alasan dan isi bukti Cetak.

- [ ] D2.5 Integrasikan UI Wika dan transaksi Tian; uji pembayaran bersamaan serta pengulangan request.

- [ ] D2.6 Perbarui diagram class/kontrak dan dokumentasi aturan akun/akses; tutup fitur inti Hari 14.

### Minggu 3 Hari 15 sampai 21

- [ ] D3.1 Jalankan seluruh kasus akun dan periode pada checklist penerimaan; perbaiki temuan.

- [ ] D3.2 Review UI/controller Wika untuk login, kewenangan, dan hasil pembayaran.

- [ ] D3.3 Periksa bukti lama setelah perubahan harga dan pembatalan; pastikan test tetap lulus.

- [ ] D3.4 Finalkan bagian laporan PBO, akun, dan membership serta diagram terkait.

- [ ] D3.5 Latihan menjelaskan bagian Wika dan satu alur lengkap sampai database.

- [ ] D3.6 Ikuti simulasi demo, perbaikan akhir, dan pemeriksaan berkas pengumpulan.

Koordinasi wajib: Tian untuk akun/akses dan transaksi; Mahasurya untuk identitas pelanggan serta visit; Wika untuk semua layar akun/pembayaran. Reviewer pekerjaan Dimas adalah Tian.

## Checklist Mahasurya selama tiga minggu

Pemilik Pelanggan, CheckIn, Produk, Transaksi, DetailTransaksi dan MutasiStok, serta dua layanan: PelangganService dan PenjualanService.

### Minggu 1 Hari 1 sampai 7

- [ ] M1.1 Sepakati turunan Pelanggan, Cetak, bentuk input/hasil, dan method DAO dengan Dimas/Tian.

- [ ] M1.2 Implementasikan profil, pencarian, pembaruan, arsip, dan pengaktifan kembali pelanggan.

- [ ] M1.3 Implementasikan pemeriksaan akses/check-in dasar dan hasil penolakan yang jelas.

- [ ] M1.4 Implementasikan Produk, DetailTransaksi, keranjang, dan perhitungan subtotal/total.

- [ ] M1.5 Berikan contoh data dan kontrak service kepada Wika; sambungkan DAO Tian.

- [ ] M1.6 Demo pendaftaran, check-in, dan penjualan sederhana; catat hasil uji serta penjelasan model.

### Minggu 2 Hari 8 sampai 14

- [ ] M2.1 Lengkapi log ditolak/diterima dan konfirmasi kedatangan berulang tanpa kunjungan ganda.

- [ ] M2.2 Integrasikan visit: pemeriksaan akses, pembayaran Dimas, hak akses, dan check-in atomik Tian.

- [ ] M2.3 Lengkapi penambahan stok, pembeli umum, penjualan banyak item, dan isi struk Cetak.

- [ ] M2.4 Implementasikan pembatalan penjualan penuh, pengembalian stok, dan penanda log pembayaran batal.

- [ ] M2.5 Uji gagal menyimpan visit/penjualan, stok terakhir, perubahan harga, dan pembatalan berulang.

- [ ] M2.6 Finalkan kontrak operasional, diagram model penjualan, dan dokumentasi aturan Hari 14.

### Minggu 3 Hari 15 sampai 21

- [ ] M3.1 Jalankan seluruh kasus pelanggan/check-in/penjualan; perbaiki temuan lintas bagian.

- [ ] M3.2 Review DAO Tian, relasi, rollback, dan penyimpanan transaksi gabungan.

- [ ] M3.3 Periksa arsip/aktif kembali dan kunjungan setelah pembayaran sumber dibatalkan.

- [ ] M3.4 Finalkan laporan operasional, perhitungan, mutasi stok, dan bukti pengujian.

- [ ] M3.5 Latihan menjelaskan bagian Tian dan hubungan class dengan tabel.

- [ ] M3.6 Ikuti simulasi demo, perbaikan akhir, dan pemeriksaan berkas pengumpulan.

Koordinasi wajib: Dimas untuk pembayaran/hak akses dan Cetak; Tian untuk SQL/transaction; Wika untuk UI pelanggan, check-in, produk, dan penjualan. Reviewer pekerjaan Mahasurya adalah Wika.

## Checklist Wika selama tiga minggu

Pemilik MainApp, enam controller (Login, Dashboard, Admin, Pelanggan, Akses, Penjualan), enam FXML dan CSS. Riwayat berada di halaman modul; bukti memakai dialog bersama MainApp.

### Minggu 1 Hari 1 sampai 7

- [ ] W1.1 Siapkan MainApp untuk bootstrap, navigasi/controller factory, enam FXML dan CSS bersama.

- [ ] W1.2 Sepakati input/hasil service dengan Dimas/Mahasurya; gunakan data contoh sambil menunggu integrasi.

- [ ] W1.3 Bangun login/dashboard dan form pelanggan; sambungkan satu alur lengkap dengan Tian.

- [ ] W1.4 Bangun tampilan pembayaran membership dan form produk/keranjang sederhana.

- [ ] W1.5 Sediakan proses background, indikator pemuatan, validasi input, serta pesan gagal dasar.

- [ ] W1.6 Demo navigasi dan alur awal; catat peta FXML/controller/service untuk seluruh kelompok.

### Minggu 2 Hari 8 sampai 14

- [ ] W2.1 Lengkapi halaman Admin, paket, pembayaran, check-in/visit, produk/stok, dan penjualan.

- [ ] W2.2 Hubungkan seluruh tombol ke service; tampilkan konfirmasi periode, harga, dan pembatalan.

- [ ] W2.3 Implementasikan konfirmasi check-in berulang, riwayat penolakan, dan status pembayaran sumber.

- [ ] W2.4 Sediakan tampilan bukti dan penyimpanan teks menggunakan Cetak tanpa menghitung ulang riwayat.

- [ ] W2.5 Uji input kosong, data tidak ditemukan, loading, jaringan gagal, dan klik konfirmasi berulang.

- [ ] W2.6 Samakan komponen/tampilan, dokumentasikan cara pakai, dan integrasikan semua alur Hari 14.

### Minggu 3 Hari 15 sampai 21

- [ ] W3.1 Buka semua halaman dan jalankan semua kontrol; perbaiki hubungan FXML/controller dan layout.

- [ ] W3.2 Review kontrak Mahasurya dan kesesuaian alur pelanggan/check-in/penjualan.

- [ ] W3.3 Verifikasi respons UI saat koneksi lambat dan penanganan hasil transaksi yang belum pasti.

- [ ] W3.4 Finalkan screenshot, bagian laporan UI, panduan menjalankan, dan panduan penggunaan.

- [ ] W3.5 Latihan menjelaskan service Mahasurya dan alur lengkap dari tombol sampai penyimpanan.

- [ ] W3.6 Pimpin koordinasi integrasi akhir, simulasi presentasi, serta pemeriksaan distribusi aplikasi.

Koordinasi wajib: Dimas/Mahasurya untuk semua kontrak dan hasil service; Tian untuk konfigurasi dan kegagalan koneksi. Reviewer pekerjaan Wika adalah Dimas.

## Checklist Tian selama tiga minggu

Pemilik Database, AkunDAO, PelangganDAO, AksesDAO dan PenjualanDAO, lima SQL setup/data, konfigurasi Aiven dan pengujian database. DAO tidak memakai interface terpisah.

### Minggu 1 Hari 1 sampai 7

- [ ] T1.1 Siapkan Aiven, TLS, konfigurasi contoh, dan database pengembangan/demo/uji yang dibutuhkan.

- [ ] T1.2 Sepakati ERD, tipe ID/nominal/waktu, role, dan kontrak DAO dengan Dimas/Mahasurya.

- [ ] T1.3 Sepakati method empat DAO, siapkan skema awal, akun SuperAdmin awal, dan contoh penyimpanan.

- [ ] T1.4 Implementasikan operasi awal pada AkunDAO, PelangganDAO dan AksesDAO.

- [ ] T1.5 Siapkan Database.jalankanTransaksi dan operasi check-in/penjualan pada DAO terkait.

- [ ] T1.6 Demo data bersama, jalankan migration dari awal, dan dokumentasikan konfigurasi tanpa rahasia.

### Minggu 2 Hari 8 sampai 14

- [ ] T2.1 Lengkapi DAO, constraint, indeks, audit petugas, snapshot, dan pemetaan subclass.

- [ ] T2.2 Hubungkan pembayaran Dimas dan check-in Mahasurya ke satu koneksi/transaksi untuk visit.

- [ ] T2.3 Pastikan penjualan, detail, mutasi, saldo stok, dan pembatalan tersimpan atomik.

- [ ] T2.4 Uji rollback, penguncian, stok terakhir, perpanjangan bersamaan, dan idempotensi request.

- [ ] T2.5 Siapkan seed demo terkendali, ekspor cadangan, dan penanganan putus koneksi.

- [ ] T2.6 Finalkan ERD/kamus data dan dokumentasi migration; semua service terhubung Hari 14.

### Minggu 3 Hari 15 sampai 21

- [ ] T3.1 Jalankan pengujian integrasi pada schema uji terpisah dan perbaiki masalah integritas.

- [ ] T3.2 Review service Dimas untuk kewenangan, tanggal, pembatalan, dan batas transaksi.

- [ ] T3.3 Uji clone baru, konfigurasi koneksi, pemulihan layanan, dan penyiapan ulang data demo.

- [ ] T3.4 Finalkan laporan database, ERD, migration, data awal, dan bukti pengujian.

- [ ] T3.5 Latihan menjelaskan service Dimas serta hubungan hasil perhitungan dengan penyimpanan.

- [ ] T3.6 Pastikan Aiven aktif dan koneksi perangkat/lokasi demo tersedia; ikuti simulasi serta pengumpulan.

Koordinasi wajib: Dimas/Mahasurya untuk semua model dan operasi atomik; Wika untuk konfigurasi aplikasi serta hasil gangguan jaringan. Reviewer pekerjaan Tian adalah Mahasurya.
