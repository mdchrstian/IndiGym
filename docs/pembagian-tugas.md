# Pembagian Tugas IndiGym

Setiap anggota memegang **satu modul utuh**, dari model (logika), DAO (SQL), service (aturan bisnis), sampai controller dan FXML (layar), beserta tesnya. Dengan begitu tiap orang bisa menjelaskan satu alur lengkap dari layar sampai database, dan tiap bagian berisi konsep PBO.

Isi nama di kolom pertama. Orang 1 mendapat bagian paling berat.

## Ringkasan

| Orang | Nama | Modul | Baris kode* | Tingkat kesulitan | Konsep PBO utama |
| --- | --- | --- | --- | --- | --- |
| 1 | ............ | Kasir, produk, dan laporan | 1.491 | Paling berat | Komposisi, interface, enkapsulasi |
| 2 | ............ | Pelanggan dan check-in | 998 | Berat | Pewarisan, override method abstrak, enkapsulasi |
| 3 | ............ | Membership dan paket | 1.180 | Sedang | Abstraksi, pewarisan, polimorfisme, interface |
| 4 | ............ | Akun dan kerangka aplikasi | 1.082 | Sedang | Abstraksi, pewarisan bertingkat, polimorfisme, enkapsulasi |

\* Baris yang tidak kosong pada Java, FXML, dan tes. Total 4.751 baris (sebelumnya 4.132), sudah termasuk aturan membership aktif, tab Membership aktif, layar Laporan, dan tampilan "Meja Depan".

**Kenapa Kasir paling berat:** kodenya paling banyak (ditambah layar Laporan), ada **dua transaksi database** (checkout dan pembatalan) dengan `commit`/`rollback`, penjagaan stok agar tidak minus walau dua laptop menjual bersamaan, logika keranjang, dan tiga tab layar.

---

## Orang 1: Kasir, produk, dan laporan (paling berat)

**Fitur:** katalog produk, tambah stok, arsip produk, keranjang, checkout untuk pelanggan atau pembeli umum, struk, riwayat penjualan, pembatalan penuh yang mengembalikan stok, dan laporan pendapatan per periode (membership, visit harian, produk).

| Lapisan | File |
| --- | --- |
| Model | `Produk`, `DetailTransaksi`, `Transaksi`, `Rupiah` |
| DAO | `PenjualanDAO` (Laporan juga membaca `AksesDAO.totalPembayaran` milik Orang 3) |
| Service | `PenjualanService`, `LaporanService` |
| Controller | `PenjualanController`, `LaporanController` |
| FXML | `penjualan.fxml`, `laporan.fxml` |
| Tes | `PenjualanTest`, `RupiahTest`, `LaporanServiceTest` |
| Tabel SQL | `produk`, `transaksi_produk`, `detail_transaksi` |

**Konsep PBO yang dikerjakan**
- **Komposisi:** `Transaksi` memiliki `List<DetailTransaksi>`. Detail tidak berarti apa-apa tanpa transaksinya. Lihat `Transaksi.hitungTotal()`.
- **Interface:** `Transaksi implements Cetak`, sehingga `cetakBukti()` menghasilkan struk. Dialog yang sama (`MainApp.tampilkanBukti`) juga dipakai Orang 3 untuk bukti pembayaran.
- **Enkapsulasi:** `Produk.setStok` menolak stok negatif, dan constructor ikut memakai setter itu supaya aturannya tidak bisa dilewati.

**Aturan yang harus bisa dijelaskan**
1. Produk yang sama di keranjang digabung menjadi satu baris (`PenjualanService.tambahKeKeranjang`).
2. Saat checkout, harga dan stok dibaca ulang dari database (`periksaUlangKeranjang`), dan struk memakai harga terbaru.
3. Checkout disimpan dalam satu transaksi. Jika stok tidak cukup, `UPDATE ... WHERE stok >= ?` mengubah 0 baris lalu semuanya di-`rollback` (`PenjualanDAO.simpanTransaksi`).
4. Pembatalan tidak bisa dilakukan dua kali karena dijaga `WHERE status = 'LUNAS'` (`PenjualanDAO.batalkanTransaksi`). Sebelum membatalkan, kasir diminta konfirmasi.
5. Laporan menghitung pendapatan dari tanggal bayar. Penjualan produk yang dibatalkan tidak dihitung, dan tanggal Dari tidak boleh sesudah tanggal sampai (`LaporanService.periksaRentang`). Total dijumlahkan di `LaporanService.hitungTotal`, bukan di controller.

**Demo:** bagian 4 dan 5 di [demo.md](demo.md).

---

## Orang 2: Pelanggan dan check-in

**Fitur:** daftar, cari, ubah, arsip dan aktifkan pelanggan, panel "akses hari ini", check-in (diizinkan, ditolak, kunjungan tambahan), jual visit harian beserta check-in pertama, dan riwayat kunjungan.

| Lapisan | File |
| --- | --- |
| Model | `Pelanggan`, `CheckIn` |
| DAO | `PelangganDAO` |
| Service | `PelangganService` |
| Controller | `PelangganController` |
| FXML | `pelanggan.fxml` |
| Tes | `PelangganServiceTest` |
| Tabel SQL | `pelanggan`, `checkin` |

**Konsep PBO yang dikerjakan**
- **Pewarisan:** `Pelanggan extends Manusia`. Id dan nama diwarisi dari class induk milik Orang 4.
- **Override method abstrak:** `Pelanggan.tampilkanInfo()` mengisi method abstrak dari `Manusia`. Hasilnya dipakai di struk dan bukti milik Orang 1 dan Orang 3.
- **Enkapsulasi:** `CheckIn` menyimpan hasil sebagai konstanta (`CheckIn.DIIZINKAN`, `CheckIn.DITOLAK`) dan menyediakan `diizinkan()` serta `getKeterangan()`, jadi class lain tidak perlu membandingkan teks sendiri.

**Aturan yang harus bisa dijelaskan**
1. Urutan pemeriksaan check-in: arsip → tidak punya akses → sudah check-in hari ini → diizinkan (`PelangganService.checkIn`).
2. Kode yang tidak dikenal tidak membuat log, sedangkan penolakan tetap dicatat.
3. Mengembalikan `null` artinya "perlu konfirmasi kunjungan tambahan". Controller lalu bertanya dan memanggil lagi dengan `true`.
4. Visit = pembayaran harian + check-in pertama dalam **satu transaksi** (`PelangganDAO.simpanVisitDanCheckIn`).
5. Kode `PLG` + id disimpan dalam satu transaksi (`PelangganDAO.simpan`).
6. Panel kanan memilih tombol utama sesuai keadaan pelanggan (`PelangganController.tampilkanStatusAkses`).

**Demo:** bagian 3 di [demo.md](demo.md), ditambah langkah 2.1, 2.2, dan 2.4.

---

## Orang 3: Membership dan paket

**Fitur:** daftar paket, tambah paket, arsip paket, pratinjau periode, pembayaran membership, bukti pembayaran, riwayat pembayaran, aturan tanggal mulai serta akhir, dan daftar member aktif beserta sisa harinya.

| Lapisan | File |
| --- | --- |
| Model | `PaketAkses`, `PaketHarian`, `PaketMembership`, `PembayaranAkses`, `Cetak` |
| DAO | `AksesDAO` |
| Service | `AksesService` |
| Controller | `AksesController` |
| FXML | `akses.fxml` |
| Tes | `PaketMembershipTest`, `AksesServiceTest`, `PembayaranAksesTest` |
| Tabel SQL | `paket_akses`, `pembayaran_akses` |

**Konsep PBO yang dikerjakan**
- **Abstraksi:** `PaketAkses` adalah class abstract dengan method abstrak `hitungTanggalAkhir`, `getJenis`, dan `getDurasiBulan`.
- **Pewarisan:** `PaketHarian` dan `PaketMembership` mewarisi `PaketAkses`.
- **Polimorfisme:** `paket.hitungTanggalAkhir(mulai)` di `AksesService.pratinjauPeriode` memberi hasil berbeda tergantung objeknya, tanpa `if` jenis paket.
- **Interface:** membuat kontrak `Cetak` dan mengimplementasikannya di `PembayaranAkses`.

**Aturan yang harus bisa dijelaskan**
1. Tanggal akhir membership: 10 Okt + 1 bulan → 9 Nov, dan 31 Jan + 1 bulan → 28/29 Feb (`PaketMembership.hitungTanggalAkhir`).
2. Membership baru tidak bisa dibeli selama membership sebelumnya belum berakhir (`AksesService.bayarMembership`, `AksesDAO.cariMembershipBelumBerakhir`).
3. Tanggal mulai: belum punya akses atau sudah lewat → hari ini; sudah visit hari ini → besok (`AksesService.tentukanTanggalMulai`).
4. Nama dan harga paket disalin ke pembayaran, jadi bukti lama tidak berubah saat paket diubah.
5. Paket harian tidak bisa diarsipkan dan tidak dijual di layar ini.
6. Tab Membership aktif hanya memuat membership yang belum berakhir milik profil yang tidak diarsipkan. Statusnya dihitung dari tanggal hari ini (`PembayaranAkses.statusPada`).

**Demo:** langkah 2.3, 2.5, 2.6, dan 3.3 di [demo.md](demo.md), ditunjukkan bersama tes tanggal.

---

## Orang 4: Akun dan kerangka aplikasi

**Fitur:** login dan logout, hash password, tambah, reset, dan nonaktifkan Admin, ganti password sendiri, navigasi samping, Beranda dengan ringkasan hari ini, perpindahan layar, dialog dan pesan, serta koneksi database.

| Lapisan | File |
| --- | --- |
| Model | `Manusia`, `Admin`, `SuperAdmin` |
| DAO | `AkunDAO`, `Database` |
| Service | `AkunService`, `Sesi` |
| Controller | `LoginController`, `AdminController`, `NavigasiController`, `DashboardController` |
| Aplikasi | `MainApp` (pindah layar, dialog, pesan), `application.css` (tema "Meja Depan") |
| Aset | Font Plus Jakarta Sans dan JetBrains Mono di `src/main/resources/fonts/`, gambar login `src/main/resources/gambar/login-gym.jpg` |
| FXML | `login.fxml`, `admin.fxml`, `navigasi.fxml`, `dashboard.fxml` |
| Tes | `AkunServiceTest` |
| Tabel SQL | `akun_admin`, serta `02_data_awal.sql` |

**Konsep PBO yang dikerjakan**
- **Abstraksi:** `Manusia` adalah class abstract dengan method abstrak `tampilkanInfo()`, dan menjadi induk bagi Admin dan Pelanggan.
- **Pewarisan bertingkat:** `SuperAdmin extends Admin extends Manusia`.
- **Polimorfisme:** `SuperAdmin` menimpa `bolehKelolaAdmin()` dan `getRole()`. `AkunService.pastikanSuperAdmin` dan `AdminController` cukup memanggil `bolehKelolaAdmin()` tanpa memeriksa jenis objek. `AkunDAO.bacaBaris` membuat objek yang tepat berdasarkan kolom `role`.
- **Enkapsulasi:** password tidak pernah disimpan apa adanya (`AkunService.hashPassword`, SHA-256), dan sesi login hanya diakses lewat `Sesi`.

**Aturan yang harus bisa dijelaskan**
1. Username atau password salah memberi pesan yang sama, supaya orang luar tidak bisa menebak username.
2. Akun nonaktif tidak bisa login, dan akun SuperAdmin tidak bisa diubah lewat layar Akun.
3. Kewenangan dicek di service, bukan hanya dengan menyembunyikan bagian layar.
4. `MainApp.gantiLayar` memakai satu Scene, dan navigasi menandai menu aktif lewat `getLayarAktif()`.

**Demo:** bagian 1 di [demo.md](demo.md) dan Beranda, ditambah bagian 6 (satu alur dari layar sampai database).

---

## Titik sambung antar anggota

File-file ini dipakai orang lain. Jika ingin mengubah nama method atau isinya, **bicarakan dulu** dengan pemakainya.

| Milik | Yang dipakai | Dipakai oleh |
| --- | --- | --- |
| Orang 4 | `Manusia`, `Sesi.adminLogin`, `MainApp.gantiLayar`, `konfirmasi`, `tampilkanBukti`, `pesanBerhasil`/`pesanGagal`, `navigasi.fxml`, `application.css` | Semua |
| Orang 3 | `Cetak` | Orang 1 (`Transaksi`) |
| Orang 3 | `AksesService.cariAksesAktifHariIni`, `cariMembershipBelumBerakhir`, `siapkanPembayaranHarian`, `cariPaketHarian`, `AksesDAO.simpanPembayaran(Connection, ...)` | Orang 2 (check-in, visit, dan tombol Jual membership) |
| Orang 2 | `PelangganService.cari`, `cariPelangganByKode` | Orang 3 (pilih pelanggan), Orang 1 (pembeli terdaftar) |
| Orang 2 dan 3 | `MainApp.pelangganDibawa`: Pelanggan → Membership saat menekan Jual membership, lalu kembali ke Pelanggan setelah bayar | Orang 2 dan Orang 3 |
| Orang 1 | `PenjualanDAO.totalPenjualan(dari, sampai)` | Orang 4 (Beranda, lewat `totalPenjualanHariIni`) |
| Orang 3 | `AksesDAO.totalPembayaran(jenis, dari, sampai)` | Orang 1 (Laporan) |
| Orang 4 | Tombol "Laporan" di `navigasi.fxml` dan `NavigasiController.bukaLaporan` | Orang 1 |
| Orang 1, 2, 3 | `jumlahCheckInHariIni`, `jumlahPelangganBeraksesHariIni`, `totalPenjualanHariIni` | Orang 4 (Beranda) |

## Aturan kerja bersama

1. Setiap orang hanya mengubah file miliknya. Perubahan di file orang lain dilakukan oleh pemiliknya, atau minimal setelah dia setuju.
2. Commit di Git dilakukan oleh pemilik modul, dengan pesan yang jelas (contoh: `Kasir: checkout dengan transaksi database`). Riwayat commit menunjukkan kontribusi tiap orang.
3. Sebelum push, jalankan `mvn clean verify`, dan semua tes harus lulus.
4. Ikuti [DESAIN.md](DESAIN.md) untuk tampilan dan aturan kode di [README](../README.md).
5. Setiap orang harus bisa menjelaskan modulnya tanpa membaca catatan: satu alur dari tombol di layar sampai baris di tabel database.
