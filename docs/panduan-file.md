# Panduan isi file versi sederhana

Panduan mengacu pada 32 file Java yang masih kosong. Nama operasi adalah acuan; signature final disepakati pemilik dan pemanggil. Semua file Java di bawah memakai awalan package id.indigym sesuai folder. Tidak ada lapisan interface DAO, folder transfer data, atau helper wajib tambahan.

| Bagian | Pemilik | Jumlah file Java |
| --- | --- | --- |
| MainApp dan controller | Wika | 7 |
| Model | Dimas/Mahasurya | 16 |
| Service | Dimas/Mahasurya | 4 |
| Database dan DAO | Tian | 5 |

## Indeks file Java

- [Manusia.java](#file-manusia) — `src/main/java/id/indigym/model/Manusia.java`
- [Admin.java](#file-admin) — `src/main/java/id/indigym/model/Admin.java`
- [SuperAdmin.java](#file-superadmin) — `src/main/java/id/indigym/model/SuperAdmin.java`
- [Pelanggan.java](#file-pelanggan) — `src/main/java/id/indigym/model/Pelanggan.java`
- [CheckIn.java](#file-checkin) — `src/main/java/id/indigym/model/CheckIn.java`
- [Produk.java](#file-produk) — `src/main/java/id/indigym/model/Produk.java`
- [DetailTransaksi.java](#file-detailtransaksi) — `src/main/java/id/indigym/model/DetailTransaksi.java`
- [Transaksi.java](#file-transaksi) — `src/main/java/id/indigym/model/Transaksi.java`
- [MutasiStok.java](#file-mutasistok) — `src/main/java/id/indigym/model/MutasiStok.java`
- [PaketAkses.java](#file-paketakses) — `src/main/java/id/indigym/model/PaketAkses.java`
- [PaketHarian.java](#file-paketharian) — `src/main/java/id/indigym/model/PaketHarian.java`
- [PaketMembership.java](#file-paketmembership) — `src/main/java/id/indigym/model/PaketMembership.java`
- [PeriodeAkses.java](#file-periodeakses) — `src/main/java/id/indigym/model/PeriodeAkses.java`
- [PembayaranAkses.java](#file-pembayaranakses) — `src/main/java/id/indigym/model/PembayaranAkses.java`
- [HakAksesGym.java](#file-hakaksesgym) — `src/main/java/id/indigym/model/HakAksesGym.java`
- [Cetak.java](#file-cetak) — `src/main/java/id/indigym/model/Cetak.java`
- [MainApp.java](#file-mainapp) — `src/main/java/id/indigym/MainApp.java`
- [AkunService.java](#file-akunservice) — `src/main/java/id/indigym/service/AkunService.java`
- [AksesService.java](#file-aksesservice) — `src/main/java/id/indigym/service/AksesService.java`
- [PelangganService.java](#file-pelangganservice) — `src/main/java/id/indigym/service/PelangganService.java`
- [PenjualanService.java](#file-penjualanservice) — `src/main/java/id/indigym/service/PenjualanService.java`
- [Database.java](#file-database) — `src/main/java/id/indigym/database/Database.java`
- [AkunDAO.java](#file-akundao) — `src/main/java/id/indigym/database/AkunDAO.java`
- [PelangganDAO.java](#file-pelanggandao) — `src/main/java/id/indigym/database/PelangganDAO.java`
- [AksesDAO.java](#file-aksesdao) — `src/main/java/id/indigym/database/AksesDAO.java`
- [PenjualanDAO.java](#file-penjualandao) — `src/main/java/id/indigym/database/PenjualanDAO.java`
- [LoginController.java](#file-logincontroller) — `src/main/java/id/indigym/controller/LoginController.java`
- [DashboardController.java](#file-dashboardcontroller) — `src/main/java/id/indigym/controller/DashboardController.java`
- [AdminController.java](#file-admincontroller) — `src/main/java/id/indigym/controller/AdminController.java`
- [PelangganController.java](#file-pelanggancontroller) — `src/main/java/id/indigym/controller/PelangganController.java`
- [AksesController.java](#file-aksescontroller) — `src/main/java/id/indigym/controller/AksesController.java`
- [PenjualanController.java](#file-penjualancontroller) — `src/main/java/id/indigym/controller/PenjualanController.java`

<a id="file-manusia"></a>

## Manusia.java

**Lokasi:** `src/main/java/id/indigym/model/Manusia.java`  
**Penanggung jawab:** Dimas

Class abstract untuk identitas dasar Admin dan Pelanggan.

**Data masuk dan hasil:** ID internal Long, nama wajib, dan kontak opsional; hasil berupa identitas dan ringkasan informasi.

**Yang perlu diisi:**

- Atribut private identitas, constructor, getter yang diperlukan, dan validasi nama.
- Penanganan kontak kosong secara konsisten; tidak menyamakan nama dengan identitas unik.
- Kontrak abstract `tampilkanInfo()` untuk menunjukkan polimorfisme.

**Operasi atau bagian utama:** Constructor dan `tampilkanInfo()`; bentuk hasil String mengikuti rancangan PBO.

**Batas tanggung jawab:** Tidak memiliki tabel sendiri dan tidak menyimpan status membership.

**Koordinasi:** Mahasurya memakai class ini sebagai induk Pelanggan; Tian memetakan identitas ke dua tabel.

**Kriteria selesai:** Admin dan Pelanggan dapat dipakai sebagai Manusia; nama kosong ditolak dan kontak kosong diizinkan.

<a id="file-admin"></a>

## Admin.java

**Lokasi:** `src/main/java/id/indigym/model/Admin.java`  
**Penanggung jawab:** Dimas

Akun petugas yang menjalankan layanan operasional.

**Data masuk dan hasil:** Identitas Manusia, username, passwordHash, status aktif, dan versi sesi; hasil berupa informasi peran dan kewenangan.

**Yang perlu diisi:**

- Pewarisan dari Manusia, atribut akun, serta perubahan status dan versi sesi melalui operasi terkontrol.
- Representasi kewenangan Admin biasa; username unik diperiksa di service dan constraint database.
- Ringkasan identitas tanpa menampilkan passwordHash.

**Operasi atau bagian utama:** `tampilkanInfo()` dan `bolehKelolaAdmin()`; operasi perubahan akun mengikuti AkunService.

**Batas tanggung jawab:** Password asli tidak menjadi atribut yang disimpan. Admin biasa tidak dapat mengelola akun Admin.

**Koordinasi:** Tian memetakan role ADMIN; Wika hanya menerima informasi akun yang aman ditampilkan.

**Kriteria selesai:** Admin biasa ditolak mengelola akun, termasuk saat memanggil service tanpa UI.

<a id="file-superadmin"></a>

## SuperAdmin.java

**Lokasi:** `src/main/java/id/indigym/model/SuperAdmin.java`  
**Penanggung jawab:** Dimas

Turunan Admin dengan kewenangan tambahan mengelola seluruh akun Admin.

**Data masuk dan hasil:** Data akun petugas dengan role SUPER_ADMIN; hasil berupa kewenangan operasional dan pengelolaan Admin.

**Yang perlu diisi:**

- Pewarisan dari Admin dan override kewenangan pengelolaan Admin.
- Identitas peran yang dapat dipetakan ulang oleh DAO saat login.

**Operasi atau bagian utama:** Override `bolehKelolaAdmin()` dan informasi peran; tidak perlu menyalin ulang operasi Admin.

**Batas tanggung jawab:** Bukan induk Admin dalam diagram pewarisan. Satu akun dibuat saat setup; tidak ada UI membuat SuperAdmin tambahan. Perlindungan penonaktifan juga wajib di service.

**Koordinasi:** Dimas dan Tian menyepakati pembuatan awal serta pemetaan role; Wika menampilkan menu akun.

**Kriteria selesai:** SuperAdmin memiliki fungsi Admin, dapat mengelola Admin, dan tidak dapat dinonaktifkan melalui aplikasi.

<a id="file-pelanggan"></a>

## Pelanggan.java

**Lokasi:** `src/main/java/id/indigym/model/Pelanggan.java`  
**Penanggung jawab:** Mahasurya

Satu profil untuk pelanggan membership, pengunjung harian, dan pembelian produk yang dikaitkan ke pelanggan.

**Data masuk dan hasil:** Identitas Manusia, kode pelanggan otomatis, penanda arsip, waktu pendaftaran; hasil berupa profil pelanggan.

**Yang perlu diisi:**

- Pewarisan dari Manusia dan atribut kodePelanggan serta diarsipkan.
- Operasi pengarsipan dan pengaktifan kembali tanpa menghapus ID atau riwayat.
- Ringkasan identitas untuk pencarian dan tampilan.

**Operasi atau bagian utama:** `tampilkanInfo()`, `arsipkan()`, dan `aktifkanKembali()`.

**Batas tanggung jawab:** Tidak memiliki username/password pelanggan. Tidak menyimpan boolean membershipAktif sebagai sumber kebenaran. Pengarsipan tidak menghentikan masa berlaku paket.

**Koordinasi:** Dimas untuk induk Manusia dan akses; Tian untuk kode unik dan penyimpanan; Wika untuk form pelanggan.

**Kriteria selesai:** Profil dapat dibuat tanpa pembayaran, dipakai lintas jenis akses, dan diaktifkan kembali dengan ID yang sama.

<a id="file-checkin"></a>

## CheckIn.java

**Lokasi:** `src/main/java/id/indigym/model/CheckIn.java`  
**Penanggung jawab:** Mahasurya

Catatan percobaan masuk seorang pelanggan yang dikenal.

**Data masuk dan hasil:** Pelanggan, petugas, hak akses opsional, waktu UTC, hasil, alasan, requestId; hasil log yang dapat dibaca kembali.

**Yang perlu diisi:**

- ID log, referensi pelanggan/petugas/hak, waktu, requestId, hasil DIIZINKAN atau DITOLAK.
- Kode alasan dan keterangan; hasil DIIZINKAN wajib merujuk hak yang dipakai.
- Riwayat kejadian yang tidak ditulis ulang setelah pembayaran sumber dibatalkan.

**Operasi atau bagian utama:** Constructor/factory hasil log; akses data riwayat untuk tampilan.

**Batas tanggung jawab:** ID pelanggan tidak dikenal tidak membuat log pelanggan. Batal konfirmasi check-in berulang tidak menambah log kunjungan.

**Koordinasi:** Tian untuk transaksi, constraint, dan pemetaan; Wika untuk alur operasional; Dimas untuk akses dan Cetak.

**Kriteria selesai:** Riwayat membedakan ditolak dan diizinkan; pembayaran yang kemudian batal diberi penanda dari data sumber tanpa mengubah log asli.

<a id="file-produk"></a>

## Produk.java

**Lokasi:** `src/main/java/id/indigym/model/Produk.java`  
**Penanggung jawab:** Mahasurya

Identitas barang, harga, dan saldo stok saat ini.

**Data masuk dan hasil:** Kode unik, nama, harga, stok, status arsip; hasil data katalog dan perubahan stok yang valid.

**Yang perlu diisi:**

- Atribut private dan validasi harga/stok tidak negatif.
- Operasi validasi jumlah, pengurangan/penambahan stok, dan pengarsipan.
- Perubahan stok harus selalu disertai MutasiStok melalui service dalam transaksi database.

**Operasi atau bagian utama:** `validasiJumlah()`, `kurangStok()`, dan operasi penambahan/pengarsipan yang disepakati.

**Batas tanggung jawab:** Tidak menyediakan perubahan stok bebas dari form edit produk. Validasi objek tidak menggantikan penguncian atau update bersyarat di database.

**Koordinasi:** Tian untuk transaksi, constraint, dan pemetaan; Wika untuk alur operasional; Dimas untuk akses dan Cetak.

**Kriteria selesai:** Jumlah tidak sah ditolak; produk arsip tetap ada pada riwayat dan tidak ditawarkan untuk penjualan baru.

<a id="file-detailtransaksi"></a>

## DetailTransaksi.java

**Lokasi:** `src/main/java/id/indigym/model/DetailTransaksi.java`  
**Penanggung jawab:** Mahasurya

Satu jenis produk dalam satu penjualan.

**Data masuk dan hasil:** Referensi produk, snapshot nama, jumlah positif, snapshot harga satuan; hasil subtotal.

**Yang perlu diisi:**

- ID detail dan relasi transaksi/produk.
- Snapshot nama dan harga satuan, jumlah, serta subtotal dengan BigDecimal.
- Produk yang sama digabung menjadi satu detail sebelum disimpan.

**Operasi atau bagian utama:** `hitungSubtotal()` berdasarkan jumlah dan snapshot harga satuan.

**Batas tanggung jawab:** Tidak membaca ulang harga katalog ketika menampilkan struk lama. transaksi_id bukan primary key tunggal detail.

**Koordinasi:** Tian untuk transaksi, constraint, dan pemetaan; Wika untuk alur operasional; Dimas untuk akses dan Cetak.

**Kriteria selesai:** Perubahan nama/harga katalog tidak mengubah subtotal transaksi lama.

<a id="file-transaksi"></a>

## Transaksi.java

**Lokasi:** `src/main/java/id/indigym/model/Transaksi.java`  
**Penanggung jawab:** Mahasurya

Penjualan yang memiliki minimal satu DetailTransaksi.

**Data masuk dan hasil:** Pembeli opsional, petugas, detail, requestId, waktu, total, status; hasil penjualan dan teks bukti.

**Yang perlu diisi:**

- Relasi komposisi dengan detail, nomor/ID, pembeli opsional, petugas, waktu UTC, dan requestId.
- Perhitungan total dari detail; status SELESAI/DIBATALKAN.
- Data alasan, petugas, dan waktu pembatalan; implementasi Cetak.

**Operasi atau bagian utama:** Perhitungan total, perubahan status yang tervalidasi, dan `cetakBukti()`.

**Batas tanggung jawab:** Tidak mensyaratkan membership pembeli, tidak mengurangi stok sendiri lewat SQL, dan tidak menghapus transaksi saat dibatalkan.

**Koordinasi:** Tian untuk transaksi, constraint, dan pemetaan; Wika untuk alur operasional; Dimas untuk akses dan Cetak.

**Kriteria selesai:** Transaksi beberapa produk menghasilkan total benar; bukti memakai snapshot dan menunjukkan status pembatalan.

<a id="file-mutasistok"></a>

## MutasiStok.java

**Lokasi:** `src/main/java/id/indigym/model/MutasiStok.java`  
**Penanggung jawab:** Mahasurya

Riwayat perubahan saldo stok sebuah produk.

**Data masuk dan hasil:** Produk, petugas, waktu, jenis, delta bertanda, keterangan, transaksi opsional, requestId; hasil catatan mutasi.

**Yang perlu diisi:**

- Jenis MASUK, PENJUALAN, BATAL_PENJUALAN dan delta yang sesuai.
- Referensi penjualan untuk penjualan/pembatalan; penambahan manual memakai keterangan.
- ID request unik per mutasi, bukan satu UUID mentah yang sama untuk semua produk dalam penjualan.

**Operasi atau bagian utama:** Pembentukan mutasi masuk/keluar/pembatalan dengan data audit lengkap.

**Batas tanggung jawab:** Bukan saldo stok terpisah yang boleh berubah tanpa produk. Delta masuk/pembatalan positif dan penjualan negatif.

**Koordinasi:** Tian untuk transaksi, constraint, dan pemetaan; Wika untuk alur operasional; Dimas untuk akses dan Cetak.

**Kriteria selesai:** Saldo dan mutasi disimpan bersama; retry atau pembatalan berulang tidak menggandakan mutasi.

<a id="file-paketakses"></a>

## PaketAkses.java

**Lokasi:** `src/main/java/id/indigym/model/PaketAkses.java`  
**Penanggung jawab:** Dimas

Abstraksi tarif yang menjadi induk PaketHarian dan PaketMembership.

**Data masuk dan hasil:** ID, nama, harga, penanda arsip, dan konteks tanggal pembelian; hasil berupa PeriodeAkses.

**Yang perlu diisi:**

- Class abstract dengan identitas paket, harga tidak negatif, dan operasi pengarsipan.
- Kontrak `hitungPeriode(konteks)` yang dapat dioverride jenis paket.
- Konteks tanggal berasal dari service yang telah membaca keadaan pelanggan.

**Operasi atau bagian utama:** `hitungPeriode(konteks)`; struktur konteks final disepakati, bukan dibuat terpisah oleh setiap anggota.

**Batas tanggung jawab:** Tidak mengambil data database atau menghitung periode berdasarkan tanggal laptop yang berbeda dari zona gym.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Perhitungan memanggil tipe PaketAkses dan menghasilkan perilaku sesuai subclass.

<a id="file-paketharian"></a>

## PaketHarian.java

**Lokasi:** `src/main/java/id/indigym/model/PaketHarian.java`  
**Penanggung jawab:** Dimas

Tarif masuk untuk hari pembayaran.

**Data masuk dan hasil:** Tanggal operasional gym dan harga; hasil periode dengan tanggal mulai sama dengan tanggal terakhir berlaku.

**Yang perlu diisi:**

- Pewarisan PaketAkses dan override perhitungan periode satu tanggal.
- Jenis HARIAN dan aturan tanpa durasi bulan.

**Operasi atau bagian utama:** Override `hitungPeriode(konteks)`.

**Batas tanggung jawab:** Bukan akses 24 jam dan bukan reservasi tanggal mendatang. Pengecekan akses yang sudah ada dilakukan service.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Pembayaran visit tanggal tertentu hanya menghasilkan akses tanggal tersebut.

<a id="file-paketmembership"></a>

## PaketMembership.java

**Lokasi:** `src/main/java/id/indigym/model/PaketMembership.java`  
**Penanggung jawab:** Dimas

Tarif dengan durasi bulan kalender.

**Data masuk dan hasil:** DurasiBulan positif dan tanggal mulai yang ditentukan service; hasil PeriodeAkses.

**Yang perlu diisi:**

- Pewarisan PaketAkses, durasi bulan positif, dan override perhitungan periode.
- Jika nomor hari tersedia pada bulan tujuan, akhir adalah sehari sebelum tanggal padanan; jika tidak tersedia, akhir adalah hari terakhir bulan tujuan.
- Kasus uji: 10 Oktober 2026 satu bulan berakhir 9 November; 31 atau 30 Januari 2027 berakhir 28 Februari; 29 Februari 2028 dua belas bulan berakhir 28 Februari 2029.

**Operasi atau bagian utama:** Override `hitungPeriode(konteks)` dan akses durasi bulan.

**Batas tanggung jawab:** Jangan menyamakan satu bulan dengan 30 hari atau memakai `plusMonths(...).minusDays(1)` tanpa menangani kasus hari yang tidak tersedia.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Seluruh contoh batas bulan dan tahun kabisat sesuai aturan PA06.

<a id="file-periodeakses"></a>

## PeriodeAkses.java

**Lokasi:** `src/main/java/id/indigym/model/PeriodeAkses.java`  
**Penanggung jawab:** Dimas

Objek nilai untuk awal dan akhir berlakunya akses.

**Data masuk dan hasil:** tanggalMulai dan tanggalTerakhirBerlaku; hasil pemeriksaan apakah sebuah tanggal tercakup.

**Yang perlu diisi:**

- Dua LocalDate wajib dengan akhir tidak lebih awal dari awal.
- Representasi yang sebaiknya tidak dapat diubah bebas setelah dibentuk.
- Operasi pemeriksaan tanggal dengan batas inklusif.

**Operasi atau bagian utama:** Constructor dan operasi pemeriksaan cakupan tanggal; nama method final disepakati.

**Batas tanggung jawab:** Tidak menghitung pembayaran, menggeser periode lama, atau menyimpan status aktif manual.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Tanggal awal dan akhir tercakup; tanggal sebelum dan sesudahnya tidak tercakup.

<a id="file-pembayaranakses"></a>

## PembayaranAkses.java

**Lokasi:** `src/main/java/id/indigym/model/PembayaranAkses.java`  
**Penanggung jawab:** Dimas

Catatan pembayaran paket dengan snapshot dan riwayat pembatalan.

**Data masuk dan hasil:** Pelanggan, paket, petugas, requestId, nominal, waktu, snapshot tarif/durasi; hasil pembayaran dan teks bukti.

**Yang perlu diisi:**

- ID/nomor pembayaran, referensi pelanggan/paket/petugas, tanggal bayar dan waktu UTC.
- Snapshot nama, jenis, harga, dan durasi paket; status LUNAS/DIBATALKAN.
- Data alasan, petugas, dan waktu pembatalan; implementasi interface Cetak.
- Representasi nominal yang telah divalidasi service sama dengan harga saat konfirmasi.

**Operasi atau bagian utama:** `cetakBukti()` dan perubahan status pembatalan melalui operasi domain yang tervalidasi.

**Batas tanggung jawab:** Tidak menghapus pembayaran lama atau mengubah snapshot ketika katalog berubah. Pengembalian uang tidak dikelola aplikasi.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Bukti lama tetap sama setelah harga berubah; pembatalan mempertahankan nomor, rincian, dan riwayat.

<a id="file-hakaksesgym"></a>

## HakAksesGym.java

**Lokasi:** `src/main/java/id/indigym/model/HakAksesGym.java`  
**Penanggung jawab:** Dimas

Hak masuk yang dihasilkan oleh satu pembayaran.

**Data masuk dan hasil:** Referensi pembayaran dan PeriodeAkses; hasil pemeriksaan tanggal serta status pembayaran sumber.

**Yang perlu diisi:**

- ID hak akses, referensi pembayaran, tanggal mulai dan terakhir berlaku.
- Operasi pemeriksaan cakupan periode dan pembacaan validitas pembayaran sumber.
- Relasi pelanggan didapat melalui pembayaran, bukan sumber data identitas terpisah.

**Operasi atau bagian utama:** `mencakup(tanggal)` atau operasi ekuivalen; keputusan masuk akhir dilakukan PelangganService.

**Batas tanggung jawab:** Tidak memiliki status aktif yang dapat diubah manual. Cakupan tanggal saja tidak cukup jika pembayaran sudah dibatalkan atau pelanggan diarsipkan.

**Koordinasi:** Tian untuk pemetaan tabel dan snapshot; Mahasurya untuk visit/check-in; Wika untuk pratinjau dan bukti.

**Kriteria selesai:** Hak dari pembayaran batal tidak bisa dipakai, sedangkan periode lain yang sah tetap dapat digunakan.

<a id="file-cetak"></a>

## Cetak.java

**Lokasi:** `src/main/java/id/indigym/model/Cetak.java`  
**Penanggung jawab:** Dimas dan Mahasurya

Kontrak bersama untuk menghasilkan teks bukti pembayaran akses dan penjualan.

**Data masuk dan hasil:** Data transaksi yang sudah tersimpan; hasil String berisi teks bukti.

**Yang perlu diisi:**

- Interface dengan kontrak `String cetakBukti()`.
- Kesepakatan isi minimal: nomor, tanggal/waktu, petugas, rincian, nominal, dan status pembatalan.

**Operasi atau bagian utama:** `String cetakBukti()`.

**Batas tanggung jawab:** Tidak membuka jendela, menulis SQL, atau memanggil printer. Penyimpanan teks ke file dilakukan bagian tampilan.

**Koordinasi:** Dimas mengimplementasikan PembayaranAkses, Mahasurya mengimplementasikan Transaksi, Wika menampilkan hasilnya.

**Kriteria selesai:** Bukti kedua jenis transaksi dapat ditampilkan melalui interface yang sama tanpa menghitung ulang harga katalog.

<a id="file-mainapp"></a>

## MainApp.java

**Lokasi:** `src/main/java/id/indigym/MainApp.java`  
**Penanggung jawab:** Wika

Titik masuk aplikasi JavaFX. Perakitan komponen, navigasi, dan dialog bukti cukup berada di class ini.

**Yang harus dibuat:**

- Mulai aplikasi, muat CSS dan login, serta susun satu set DAO/service yang dibutuhkan controller.
- Gunakan constructor/controller factory untuk memasukkan service; method navigasi memetakan enam FXML.
- Sediakan dialog bersama untuk String hasil Cetak dan simpan sebagai file teks.
- Kelola penutupan resource UI. Pemanggilan service yang lambat memakai Task JavaFX di controller; tidak membuat class helper khusus.

**Operasi awal:** Lifecycle JavaFX, bukaHalaman, tampilkanBukti; nama/signature final disepakati Wika.

**Batas pekerjaan:** Tidak ada SQL atau rumus bisnis di MainApp. Tidak perlu class perakitan/navigasi/dialog terpisah.

**Koordinasi:** Tian menyediakan Database dan DAO; Dimas/Mahasurya menyediakan empat service.

**Selesai bila:** Enam halaman dapat dibuka dengan dependency benar, logout kembali ke login, dan bukti dapat dibaca/disimpan.


<a id="file-akunservice"></a>

## AkunService.java

**Lokasi:** `src/main/java/id/indigym/service/AkunService.java`  
**Penanggung jawab:** Dimas

Satu layanan untuk login, sesi, password dan pengelolaan Admin.

**Yang harus dibuat:**

- Cari akun melalui AkunDAO, cek aktif, verifikasi hash dengan library password yang sesuai, dan simpan Admin yang sedang login beserta versi sesi saat login.
- Validasi ulang akun/versi sesi sebelum tindakan terlindungi; logout membersihkan sesi. Tidak perlu class sesi atau hash terpisah; hash/verify menjadi method private.
- SuperAdmin boleh membuat, mengubah, reset password dan menonaktifkan Admin; Admin biasa ditolak di service, bukan hanya disembunyikan menunya.
- Username unik, hash tidak masuk hasil tampilan, reset/nonaktif membatalkan versi sesi lama; satu SuperAdmin awal tidak dapat dinonaktifkan.
- Perubahan akun/password melalui transaksi yang sesuai; akun riwayat tidak dihapus.

**Operasi awal:** login, logout, validasiSesi, buatAdmin, ubahAdmin, resetPassword, ubahPassword, nonaktifkanAdmin.

**Batas pekerjaan:** Tidak membuat SuperAdmin tambahan dari UI, tidak memakai plaintext password tersimpan, dan tidak menganggap peran dari form sebagai kewenangan.

**Koordinasi:** Tian: AkunDAO dan setup akun awal. Wika: LoginController/AdminController. Service lain memanggil validasiSesi.

**Selesai bila:** Login benar/salah, akun nonaktif, reset dari perangkat lain, dan akses Admin biasa tertangani dengan benar.


<a id="file-aksesservice"></a>

## AksesService.java

**Lokasi:** `src/main/java/id/indigym/service/AksesService.java`  
**Penanggung jawab:** Dimas

Satu layanan untuk katalog paket, pratinjau, pembayaran, periode, pembatalan dan bukti akses.

**Yang harus dibuat:**

- Daftar/simpan/arsip paket dengan harga tidak negatif dan durasi membership positif; perubahan katalog tidak mengubah snapshot pembayaran.
- Pratinjau tanpa simpan, lalu baca ulang pelanggan/tarif/akhir periode sah saat konfirmasi dalam transaksi.
- Membership baru/expired mulai hari bayar; perpanjangan setelah akhir sah paling jauh termasuk masa depan; bila hanya ada visit sah hari ini, mulai besok.
- Gunakan PaketAkses/PaketMembership untuk bulan kalender; nominal lunas harus sama dengan harga yang dikonfirmasi.
- Simpan PembayaranAkses dan satu HakAksesGym dalam satu transaksi. Harga/periode yang berubah harus dikonfirmasi ulang.
- Sediakan bayarHarianDalamTransaksi(Connection, ...) untuk dipanggil PelangganService dalam transaksi visit; tidak membuka koneksi/commit sendiri.
- Void membership sah terbaru atau visit dengan audit; akses dihitung dari status sumber; periode tersimpan dan log check-in tidak digeser/ditimpa.
- Cari hasil lewat requestId yang sama saat retry/hasil tidak pasti dan tolak payload berbeda dengan ID sama.

**Operasi awal:** daftarPaket, simpanPaket, arsipkanPaket, pratinjauPeriode, bayarMembership, bayarHarianDalamTransaksi, batalkanMembershipTerbaru, batalkanVisit, riwayat, cariBukti.

**Batas pekerjaan:** Tidak melakukan refund uang, cicilan atau diskon khusus. Method internal bertanda DalamTransaksi hanya memakai Connection yang diterima.

**Koordinasi:** Tian: AksesDAO, lock pelanggan dan Database. Mahasurya: visit. Wika: AksesController dan bagian visit PelangganController.

**Selesai bila:** Contoh akhir bulan, perpanjangan mendatang, visit lalu membership besok, rollback dan retry semuanya sesuai aturan.


<a id="file-pelangganservice"></a>

## PelangganService.java

**Lokasi:** `src/main/java/id/indigym/service/PelangganService.java`  
**Penanggung jawab:** Mahasurya

Satu layanan untuk profil pelanggan, check-in biasa, riwayat kunjungan dan visit harian.

**Yang harus dibuat:**

- Daftar/cari/ubah/arsip/aktifkan profil; nama wajib, kontak opsional, kode unik otomatis; satu profil lintas membership/visit.
- ID tidak dikenal menghasilkan error tanpa log pelanggan. Untuk pelanggan dikenal catat DIIZINKAN atau DITOLAK dengan alasan.
- Periksa arsip, periode dan pembayaran sumber; jika beberapa hak sah ada, dahulukan membership.
- Kunjungan kedua hari itu memerlukan konfirmasi; lanjut tanpa biaya, batal tidak membuat log baru.
- bayarVisitDanCheckIn membuka satu Database.jalankanTransaksi. Periksa ulang akses hari ini; panggil AksesService.bayarHarianDalamTransaksi dengan Connection yang sama, simpan hak/pembayaran lalu CheckIn.
- Gagal satu langkah visit berarti rollback semuanya. Profil yang dibuat sebelum konfirmasi boleh tetap ada.
- Riwayat menampilkan hasil asli dan penanda sumber pembayaran kemudian batal; penolakan bukan kehadiran.
- requestId tetap dipakai saat konfirmasi diulang; minta validasi sesi AkunService untuk operasi terlindungi.

**Operasi awal:** daftar, cari, ubah, arsipkan, aktifkanKembali, periksaCheckIn, catatCheckIn, bayarVisitDanCheckIn, riwayatCheckIn.

**Batas pekerjaan:** Tidak membuat profil baru tiap kedatangan, menjual visit saat sudah ada akses sah, atau menghitung periode membership ulang sendiri.

**Koordinasi:** Dimas: Manusia, AksesService dan akses/pembayaran. Tian: PelangganDAO/Database; query hak lewat AksesDAO atau operasi AksesService yang disepakati. Wika: PelangganController.

**Selesai bila:** Profil/check-in/visit satu alur dapat didemokan; penolakan, kunjungan berulang dan rollback visit benar.


<a id="file-penjualanservice"></a>

## PenjualanService.java

**Lokasi:** `src/main/java/id/indigym/service/PenjualanService.java`  
**Penanggung jawab:** Mahasurya

Satu layanan untuk katalog produk, stok, keranjang, checkout, pembatalan dan bukti penjualan.

**Yang harus dibuat:**

- CRUD/arsip katalog dan restock qty bulat positif dengan keterangan/petugas; stok awal juga punya mutasi masuk.
- Keranjang minimal satu item, qty positif, produk sama digabung; total memakai snapshot BigDecimal dan stok belum berubah.
- Checkout baca ulang harga/arsip/stok dan lock produk berurutan; harga berubah memerlukan konfirmasi ulang.
- Pembeli boleh umum tanpa pelanggan_id, dan membership aktif bukan syarat.
- Simpan Transaksi, DetailTransaksi, saldo stok dan MutasiStok lewat PenjualanDAO dalam satu Database.jalankanTransaksi.
- Pembatalan penuh dengan alasan/petugas/waktu mengembalikan stok/mutasi sekali saja dalam satu transaksi; retry requestId tidak membuat duplikasi.
- Riwayat dan Cetak memakai nama/harga tersimpan, bukan katalog terbaru; minta validasi sesi AkunService.

**Operasi awal:** cariProduk, simpanProduk, arsipkanProduk, tambahStok, hitungKeranjang, selesaikan, batalkan, riwayatPenjualan, riwayatStok, cariBukti.

**Batas pekerjaan:** Tidak ada edit saldo stok bebas, supplier, harga modal/laba, akuntansi atau retur sebagian. Query tetap di DAO.

**Koordinasi:** Tian: PenjualanDAO dan transaksi/penguncian. Dimas: Cetak. Wika: PenjualanController.

**Selesai bila:** Restock, penjualan umum, stok terakhir, harga berubah, gagal simpan dan pembatalan ulang tidak merusak saldo/riwayat.


<a id="file-database"></a>

## Database.java

**Lokasi:** `src/main/java/id/indigym/database/Database.java`  
**Penanggung jawab:** Tian

Satu class untuk membaca konfigurasi, membuka koneksi JDBC dan mengelola commit/rollback.

**Yang harus dibuat:**

- Baca contoh config yang disalin ke file lokal atau environment; validasi tanpa menampilkan rahasia.
- Buka Connection MySQL dengan TLS yang benar, zona gym Asia/Makassar dan konvensi timestamp UTC.
- jalankanTransaksi membuka Connection, mematikan auto-commit, menjalankan pekerjaan, commit bila semua berhasil, rollback bila gagal, lalu menutup resource.
- Pekerjaan menerima Connection yang sama untuk seluruh DAO; operasi internal tidak membuat transaksi baru.
- DAO menutup Statement/ResultSet miliknya, bukan Connection pinjaman. Jangan berbagi satu Connection global antar thread.
- Hasil commit yang belum pasti harus dapat dibedakan agar requestId diperiksa; jangan otomatis mengulang dengan UUID baru.

**Operasi awal:** bukaKoneksi dan jalankanTransaksi; bentuk callback dan penanganan checked exception disepakati tanpa membuat lapisan framework baru.

**Batas pekerjaan:** Tidak berisi aturan boleh masuk/checkout. Tidak perlu class konfigurasi, factory atau konteks transaksi terpisah.

**Koordinasi:** Service terluar memulai unit kerja melalui Database; Tian menentukan API Connection dan pemilik commit. Semua anggota memahami contoh visit dan checkout.

**Selesai bila:** Rollback teruji, Connection selalu ditutup, dan satu visit/checkout menggunakan koneksi yang sama.


<a id="file-akundao"></a>

## AkunDAO.java

**Lokasi:** `src/main/java/id/indigym/database/AkunDAO.java`  
**Penanggung jawab:** Tian

Class JDBC langsung untuk akun_admin; tidak memakai interface terpisah.

**Yang harus dibuat:**

- Cari akun username/ID, daftar akun, cek username, simpan/ubah status/hash/versi sesi.
- Petakan role menjadi Admin atau SuperAdmin; akun aplikasi terpisah dari user database.
- Query berparameter, tipe Long/BIGINT, BigDecimal/DECIMAL dan NULL dipetakan konsisten.
- Method simpan dan baca dalam transaksi menerima Connection dari service; tidak membuka koneksi kedua atau commit sendiri.

**Operasi awal:** Method cari/simpan/ubah/riwayat sesuai kebutuhan tabel di atas; signature dicatat pada kontrak integrasi.

**Batas pekerjaan:** Tidak ada aturan kewenangan atau dialog UI di DAO. Satu DAO boleh menangani beberapa tabel yang termasuk satu alur.

**Koordinasi:** Dimas menyepakati kebutuhan data dengan Tian; controller selalu lewat service.

**Selesai bila:** Login, username duplikat, reset versi sesi dan nonaktif menyimpan hasil yang benar.


<a id="file-pelanggandao"></a>

## PelangganDAO.java

**Lokasi:** `src/main/java/id/indigym/database/PelangganDAO.java`  
**Penanggung jawab:** Tian

Class JDBC langsung untuk pelanggan dan checkin; tidak memakai interface terpisah.

**Yang harus dibuat:**

- Cari profil berdasarkan kode/ID/nama; simpan/ubah arsip dan hasil insert berkode unik.
- Kunci baris pelanggan untuk operasi akses bersama; simpan/check requestId CheckIn dan baca riwayat.
- Hari check-in dihitung melalui batas UTC untuk tanggal Asia/Makassar; join sumber hak/pembayaran bila perlu untuk penanda riwayat.
- Query berparameter, tipe Long/BIGINT, BigDecimal/DECIMAL dan NULL dipetakan konsisten.
- Method simpan dan baca dalam transaksi menerima Connection dari service; tidak membuka koneksi kedua atau commit sendiri.

**Operasi awal:** Method cari/simpan/ubah/riwayat sesuai kebutuhan tabel di atas; signature dicatat pada kontrak integrasi.

**Batas pekerjaan:** Tidak ada aturan kewenangan atau dialog UI di DAO. Satu DAO boleh menangani beberapa tabel yang termasuk satu alur.

**Koordinasi:** Mahasurya menyepakati kebutuhan data dengan Tian; controller selalu lewat service.

**Selesai bila:** Kode tidak bentrok; kunjungan hari gym benar; log asli tetap utuh saat sumber pembayaran batal.


<a id="file-aksesdao"></a>

## AksesDAO.java

**Lokasi:** `src/main/java/id/indigym/database/AksesDAO.java`  
**Penanggung jawab:** Tian

Class JDBC langsung untuk paket_akses, pembayaran_akses dan hak_akses; tidak memakai interface terpisah.

**Yang harus dibuat:**

- Simpan/cari/arsip paket; petakan HARIAN menjadi PaketHarian dan MEMBERSHIP menjadi PaketMembership.
- Simpan/baca snapshot pembayaran dan satu hak; cari requestId, riwayat dan pembayaran membership sah terbaru.
- Cari hak sah tanggal tertentu dan akhir membership sah paling jauh, termasuk periode belum dimulai.
- Void ber-audit dan status sumber menentukan akses; pelanggan berasal dari pembayaran, tanpa kolom aktif manual.
- Query berparameter, tipe Long/BIGINT, BigDecimal/DECIMAL dan NULL dipetakan konsisten.
- Method simpan dan baca dalam transaksi menerima Connection dari service; tidak membuka koneksi kedua atau commit sendiri.

**Operasi awal:** Method cari/simpan/ubah/riwayat sesuai kebutuhan tabel di atas; signature dicatat pada kontrak integrasi.

**Batas pekerjaan:** Tidak ada aturan kewenangan atau dialog UI di DAO. Satu DAO boleh menangani beberapa tabel yang termasuk satu alur.

**Koordinasi:** Dimas dan Mahasurya menyepakati kebutuhan data dengan Tian; controller selalu lewat service.

**Selesai bila:** Periode mendatang ikut perpanjangan, satu pembayaran satu hak, serta retry/void tidak menggandakan data.


<a id="file-penjualandao"></a>

## PenjualanDAO.java

**Lokasi:** `src/main/java/id/indigym/database/PenjualanDAO.java`  
**Penanggung jawab:** Tian

Class JDBC langsung untuk produk, transaksi_produk, detail_transaksi dan mutasi_stok; tidak memakai interface terpisah.

**Yang harus dibuat:**

- CRUD/arsip katalog, baca harga/stok, lock produk menurut ID dan update saldo bersyarat.
- Simpan header/detail penjualan dan seluruh mutasi, cari requestId, riwayat, serta status/audit pembatalan.
- Snapshot nama/harga dipertahankan; detail mempunyai ID sendiri dan unique transaksi-produk.
- Mutasi punya requestId unik per baris; jangan menyalin satu UUID ke semua baris; cegah mutasi transaksi/produk/jenis ganda.
- Query berparameter, tipe Long/BIGINT, BigDecimal/DECIMAL dan NULL dipetakan konsisten.
- Method simpan dan baca dalam transaksi menerima Connection dari service; tidak membuka koneksi kedua atau commit sendiri.

**Operasi awal:** Method cari/simpan/ubah/riwayat sesuai kebutuhan tabel di atas; signature dicatat pada kontrak integrasi.

**Batas pekerjaan:** Tidak ada aturan kewenangan atau dialog UI di DAO. Satu DAO boleh menangani beberapa tabel yang termasuk satu alur.

**Koordinasi:** Mahasurya menyepakati kebutuhan data dengan Tian; controller selalu lewat service.

**Selesai bila:** Gagal detail berarti semua rollback, saldo tidak negatif dan void berulang tidak menambah stok dua kali.


<a id="file-logincontroller"></a>

## LoginController.java

**Lokasi:** `src/main/java/id/indigym/controller/LoginController.java`  
**Penanggung jawab:** Wika

Form username/password, loading dan pesan login; sukses menuju dashboard.

**Yang harus dibuat:**

- Dependency AkunService; pasangan resource fxml/login.fxml.
- Baca input, jalankan login memakai Task JavaFX agar UI tidak membeku, jangan log password.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil AkunService dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


<a id="file-dashboardcontroller"></a>

## DashboardController.java

**Lokasi:** `src/main/java/id/indigym/controller/DashboardController.java`  
**Penanggung jawab:** Wika

Menu modul dan identitas petugas; navigasi serta logout.

**Yang harus dibuat:**

- Dependency AkunService dan MainApp; pasangan resource fxml/dashboard.fxml.
- Menu kelola Admin khusus SuperAdmin; main content dapat memuat halaman lewat MainApp.
- Tidak wajib membuat statistik baru; pengamanan service tetap berjalan walaupun menu disembunyikan.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil AkunService dan MainApp dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


<a id="file-admincontroller"></a>

## AdminController.java

**Lokasi:** `src/main/java/id/indigym/controller/AdminController.java`  
**Penanggung jawab:** Wika

Daftar/form akun Admin serta reset/nonaktif dan perubahan password sendiri sesuai kewenangan.

**Yang harus dibuat:**

- Dependency AkunService; pasangan resource fxml/admin.fxml.
- Konfirmasi tindakan penting; tangani username duplikat, sesi tidak sah dan perlindungan SuperAdmin awal.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil AkunService dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


<a id="file-pelanggancontroller"></a>

## PelangganController.java

**Lokasi:** `src/main/java/id/indigym/controller/PelangganController.java`  
**Penanggung jawab:** Wika

Satu halaman dengan bagian profil, check-in/visit dan riwayat kunjungan.

**Yang harus dibuat:**

- Dependency PelangganService dan AksesService untuk tarif visit; pasangan resource fxml/pelanggan.fxml.
- Cari/daftar/ubah/arsip/aktifkan profil; tampilkan pemeriksaan akses dan alasan ditolak.
- Dialog kunjungan kedua; batal tidak menambah log; visit satu konfirmasi pembayaran + check-in pertama.
- Tarif visit dari layanan akses; hasil visit tidak disusun dengan memanggil dua aksi UI terpisah.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil PelangganService dan AksesService untuk tarif visit dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


<a id="file-aksescontroller"></a>

## AksesController.java

**Lokasi:** `src/main/java/id/indigym/controller/AksesController.java`  
**Penanggung jawab:** Wika

Satu halaman untuk paket, membership/perpanjangan, riwayat pembayaran dan void akses.

**Yang harus dibuat:**

- Dependency AksesService; pasangan resource fxml/akses.fxml.
- Form katalog paket dan harga/durasi; pilih pelanggan dan tampilkan pratinjau sebelum bayar.
- Konfirmasi ulang harga/periode berubah, void dengan alasan, dan buka bukti lewat MainApp.tampilkanBukti.
- Pembayaran visit + check-in dilakukan di halaman pelanggan; tidak menyediakan jalur visit terpisah yang melewati orkestrasi PelangganService.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil AksesService dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


<a id="file-penjualancontroller"></a>

## PenjualanController.java

**Lokasi:** `src/main/java/id/indigym/controller/PenjualanController.java`  
**Penanggung jawab:** Wika

Satu halaman dengan bagian katalog/restock, keranjang, riwayat penjualan dan mutasi stok.

**Yang harus dibuat:**

- Dependency PenjualanService; pasangan resource fxml/penjualan.fxml.
- Edit katalog dan restock dipisah; saldo tidak dapat diubah bebas.
- Keranjang multi-item, pembeli opsional, konfirmasi harga/lunas, serta penanganan stok tidak cukup.
- Void penuh dengan alasan dan buka bukti dari MainApp; riwayat memakai snapshot.
- Validasi format, loading, kosong/gagal, konfirmasi dan pencegahan klik ganda; operasi lambat memakai Task JavaFX dan callback UI.

**Operasi awal:** Event handler halaman sesuai bagian yang dijelaskan; nama fx:id dan handler sama dengan FXML.

**Batas pekerjaan:** Tidak menulis SQL atau rumus periode/harga; tidak memanggil DAO langsung. Riwayat berada di halaman modul dan bukti berupa dialog bersama, tanpa controller tambahan.

**Koordinasi:** Wika menyepakati input/hasil PenjualanService dengan pemiliknya.

**Selesai bila:** Halaman dapat dimuat, seluruh kontrol memiliki alur dan kegagalan/hasil belum pasti dapat ditangani tanpa duplikasi transaksi.


## Enam FXML dan CSS

Wika mengisi resource berikut. fx:controller memakai package id.indigym.controller, fx:id/event harus sama dengan controller. Tampilan memuat status kosong/loading/gagal serta konfirmasi. Semua operasi lambat memakai Task JavaFX; query/perhitungan bisnis tidak ditulis di FXML atau controller.

| File | Controller | Isi |
| --- | --- | --- |
| src/main/resources/fxml/login.fxml | LoginController | Username/password dan pesan hasil. |
| src/main/resources/fxml/dashboard.fxml | DashboardController | Menu modul, identitas petugas dan logout. |
| src/main/resources/fxml/admin.fxml | AdminController | Daftar/form akun dan aksi reset/nonaktif. |
| src/main/resources/fxml/pelanggan.fxml | PelangganController | Profil, check-in/visit dan riwayat kunjungan dalam bagian/tab. |
| src/main/resources/fxml/akses.fxml | AksesController | Katalog paket, bayar/perpanjang membership, riwayat/void akses. |
| src/main/resources/fxml/penjualan.fxml | PenjualanController | Katalog/restock, keranjang, riwayat/void penjualan dan stok. |
| src/main/resources/css/application.css | Semua halaman | Font, warna, jarak, form, tabel, fokus/validasi dan status bersama. |

FXML hanya susunan visual. Bukti di layar dan simpan teks adalah dialog MainApp menggunakan Cetak. Tidak perlu FXML riwayat/bukti terpisah. Aset gambar ditambahkan hanya bila digunakan, tanpa menyiapkan folder kosong tambahan.

## Lima file SQL

Pemilik Tian. Rincian sepuluh tabel tetap sama dengan [kamus data](kamus-data.md). Seluruh operasi aplikasi memakai DAO, bukan menjalankan ulang SQL setup pada setiap klik.

| File | Yang harus diisi |
| --- | --- |
| database/01_akun_pelanggan.sql | DDL akun_admin dan pelanggan, kode/username unik, status/arsip dan versi sesi. |
| database/02_akses_checkin.sql | DDL paket_akses, pembayaran_akses, hak_akses dan checkin; snapshot, requestId, audit, periode dan key. |
| database/03_penjualan_stok.sql | DDL produk, transaksi_produk, detail_transaksi dan mutasi_stok; qty/stok, snapshot, key dan pencegahan mutasi ganda. |
| database/04_data_awal.sql | Satu SuperAdmin awal dengan hash, serta data minimal yang sudah disepakati; pengulangan tidak menggandakan. |
| database/05_data_demo.sql | Data contoh pada database demo terpisah, stok beserta mutasi dan skenario yang konsisten; tanpa data pribadi asli. |

01–03 dijalankan berurutan untuk setup. Dokumentasikan versi skema yang telah diterapkan; sesudah dipakai bersama, perubahan skema mendapat SQL berikutnya, bukan menulis ulang riwayat setup tanpa koordinasi. Tidak wajib memasang alat migration/framework tambahan.

## Konfigurasi dan build

**pom.xml — Tian dan Wika, ditinjau bersama.** Isi identitas Maven, versi Java yang disepakati, JavaFX controls/FXML, driver MySQL, library hash pilihan Dimas, library tes serta plugin build/run. Entry point id.indigym.MainApp. Jangan memakai path SDK pribadi atau menyimpan credential dalam pom. Saat ini kosong dan belum ada perintah build yang diverifikasi.

**config/application.example.properties — Tian.** Isi key yang dibaca Database: URL/database/user/password placeholder, TLS/truststore bila dibutuhkan, serta zona gym. Konfigurasi asli disimpan lokal dan diabaikan Git; key contoh harus sesuai pembaca config. Tidak mematikan verifikasi TLS demi tersambung.

**src/test/resources/application-test.example.properties — Tian.** Template tanpa rahasia untuk database uji yang terpisah dari pengembangan/demo. Jangan mengarahkan operasi reset tes ke database kerja anggota.

**.gitignore — bersama.** Abaikan target, IDE pribadi, .DS_Store, tmp/output, konfigurasi asli dan sertifikat/key pribadi. Contoh config, kode dan dokumen tetap dapat dibagikan.

**src/test/java/id/indigym/.gitkeep — bersama.** Penanda kosong, bukan tempat menulis tes. Saat diperlukan buat AkunServiceTest/AksesServiceTest (Dimas), PelangganServiceTest/PenjualanServiceTest (Mahasurya) dan DatabaseTest (Tian) pada folder ini; nama tersebut usulan. Wika memeriksa enam pasangan FXML/controller serta alur UI. Tidak perlu hierarki folder test per lapisan sejak awal.

## Dokumentasi

README menjelaskan proyek. docs/aturan-bisnis.md memuat keputusan dengan ID aturan; docs/kontrak-integrasi.md memuat method/input/hasil/transaksi; docs/peran-anggota.md memuat checklist mingguan; docs/struktur-folder.md adalah pohon aktual; docs/mengenal-folder.md menjelaskan empat bagian kode; docs/checklist-uji.md mencatat kriteria dan hasil nyata; docs/panduan-menjalankan.md berisi setup/build/run yang sudah diverifikasi ketika aplikasi tersedia; docs/demo.md memuat urutan demo; docs/kamus-data.md adalah tabel/tipe; docs/panduan-file.md adalah dokumen ini.

docs/diagram berisi enam diagram: architecture untuk alur lapisan; inheritance untuk pewarisan; access untuk akses/check-in; sales untuk penjualan; erd_access dan erd_sales untuk dua bagian database yang sama. File DOT adalah sumber yang diedit; SVG adalah gambar hasil render. Perubahan source/relasi harus menyelaraskan keduanya. Panduan_Kelompok_IndiGym.docx memuat keseluruhan rancangan dan checklist dalam Word, dengan nama class versi sederhana.

