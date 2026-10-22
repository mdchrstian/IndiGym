# IndiGym

IndiGym adalah rancangan aplikasi desktop untuk membantu administrasi gym, mulai dari pencatatan pelanggan, pembayaran membership dan kunjungan harian, hingga check-in serta penjualan produk. Proyek ini dikembangkan sebagai tugas mata kuliah Pemrograman Berorientasi Objek (PBO).

Aplikasi dirancang agar petugas dapat memeriksa hak masuk pelanggan dan menyimpan riwayat layanan dalam satu sistem. Pelanggan membership dan pengunjung harian menggunakan profil yang sama, sehingga riwayat kunjungan tetap terhubung ketika pelanggan berpindah jenis akses.

**Status proyek:** tahap perancangan. Repository saat ini berisi dokumentasi serta struktur folder dan file kosong. Fitur berikut merupakan cakupan yang direncanakan dan belum diimplementasikan.

## Tujuan

- Memusatkan data pelanggan, masa berlaku akses, dan riwayat check-in.
- Mendukung pelanggan membership serta pengunjung yang membayar kunjungan harian.
- Mencatat pembayaran akses, penjualan produk, dan perubahan stok secara konsisten.
- Menerapkan konsep PBO melalui pemodelan identitas, paket akses, dan transaksi.

## Pengguna sistem

| Peran | Kewenangan |
| --- | --- |
| Admin | Mengelola pelanggan, paket akses, pembayaran, check-in, produk, stok, dan penjualan. |
| SuperAdmin | Memiliki seluruh kewenangan Admin serta mengelola akun Admin, termasuk reset password dan penonaktifan akun. |

Pelanggan dilayani melalui Admin dan tidak memiliki akun untuk mengoperasikan aplikasi. Sistem ditujukan untuk satu gym, dengan data terpusat yang dapat diakses petugas melalui beberapa komputer yang terhubung internet.

## Cakupan fitur

| Modul | Fungsi yang direncanakan |
| --- | --- |
| Pelanggan | Pendaftaran, pencarian, pembaruan, pengarsipan, dan riwayat pelanggan. |
| Paket dan pembayaran akses | Paket membership berdasarkan durasi bulan, paket harian, pembayaran lunas, dan perpanjangan periode akses. |
| Check-in | Pemeriksaan hak akses, pencatatan kunjungan dan penolakan beserta alasan, serta konfirmasi kunjungan berulang. |
| Visit harian | Pembayaran akses harian dan check-in pertama dalam satu proses konfirmasi. |
| Produk dan stok | Katalog, harga jual, pengarsipan produk, penambahan stok, dan riwayat mutasi stok. |
| Penjualan | Keranjang beberapa produk, pembayaran, pengurangan stok, dan pembatalan penuh dengan pengembalian stok. |
| Riwayat dan bukti | Riwayat transaksi, petugas pelaksana, alasan pembatalan, serta bukti di layar dan file teks. |
| Akun petugas | Login, kewenangan berdasarkan peran, dan pengelolaan akun Admin oleh SuperAdmin. |

Pembeli produk dapat berupa pelanggan terdaftar maupun pembeli umum. Membership aktif tidak menjadi syarat untuk membeli produk.

## Gambaran alur layanan

**Membership:** Admin mencari atau mendaftarkan pelanggan, memilih paket, lalu mencatat pembayaran lunas. Sistem menentukan periode akses yang digunakan saat pelanggan melakukan check-in.

**Kunjungan harian:** Admin memilih profil pelanggan dan tarif harian, kemudian mengonfirmasi pembayaran serta check-in pertama sekaligus. Akses berlaku untuk hari pembayaran tersebut.

**Penjualan produk:** Admin menyusun keranjang dan mengonfirmasi pembayaran. Sistem menyimpan transaksi serta mengurangi stok dalam satu transaksi database.

## Aturan bisnis utama

- Profil pelanggan yang terdaftar belum otomatis memiliki hak masuk gym. Hak akses berasal dari pembayaran yang sah dan periode yang sedang berlaku.
- Membership menggunakan bulan kalender. Perpanjangan dimulai sehari setelah akhir periode sah paling jauh, termasuk periode yang belum dimulai.
- Jika pelanggan membeli visit hari ini lalu membeli membership pada hari yang sama, membership dimulai besok. Biaya visit tetap terpisah.
- Visit harian berlaku pada tanggal pembayaran, bukan selama 24 jam. Pelanggan yang sudah memiliki akses sah hari itu tidak perlu membeli visit tambahan.
- Check-in berulang pada hari yang sama membutuhkan konfirmasi Admin dan tidak dikenai biaya tambahan.
- Stok berubah setelah penjualan dikonfirmasi dan tidak boleh menjadi negatif.
- Pembatalan mempertahankan riwayat transaksi. Pembatalan pembayaran menonaktifkan hak akses terkait; pembatalan penuh penjualan mengembalikan stok satu kali.

Rincian dan pengecualian tersedia pada [aturan bisnis](docs/aturan-bisnis.md).

## Teknologi dan arsitektur

| Komponen | Rancangan |
| --- | --- |
| Bahasa pemrograman | Java |
| Antarmuka desktop | JavaFX dengan FXML dan CSS |
| Database | MySQL di Aiven |
| Akses database | JDBC |
| Pengelolaan build | Maven; konfigurasi belum diisi |

Arsitektur memisahkan tampilan dan controller, service untuk aturan bisnis, model untuk objek domain, serta repository untuk penyimpanan data. Pemisahan ini menjaga logika pembayaran, akses, dan stok tetap terpisah dari kode antarmuka.

## Susunan kode aplikasi

Folder utama di `src/main/java/id/indigym/` mengikuti perannya dalam sistem.

| Folder | Isi |
| --- | --- |
| `application` | Titik awal aplikasi dan navigasi halaman. |
| `page_controllers` | Penanganan tombol, formulir, dan pembaruan halaman. |
| `model` | Objek pelanggan, paket, pembayaran, check-in, dan penjualan. |
| `business_logic` | Aturan dan proses bisnis, seperti perpanjangan membership dan checkout. |
| `data_access` | Kontrak penyimpanan data dan implementasi query MySQL. |
| `database_connection` | Konfigurasi koneksi dan pengelolaan transaksi database. |
| `data_transfer` | Data input dan hasil yang dikirim antar tampilan dan layanan. |
| `login_security` | Sesi login dan pengamanan password. |
| `errors` | Jenis kesalahan yang dapat ditangani aplikasi. |
| `background_tasks` | Pekerjaan latar belakang agar tampilan tetap merespons. |

Penjelasan setiap folder, contoh file, serta hubungan antar bagian tersedia di [panduan mengenal folder](docs/mengenal-folder.md).

## Penerapan PBO

- **Abstraksi:** `Manusia` memodelkan identitas dasar dan `PaketAkses` memodelkan karakteristik umum paket gym.
- **Pewarisan:** `Admin` dan `Pelanggan` merupakan turunan `Manusia`; `SuperAdmin` merupakan turunan `Admin`. `PaketHarian` dan `PaketMembership` merupakan turunan `PaketAkses`.
- **Polimorfisme:** paket harian dan membership dapat diproses melalui tipe `PaketAkses`. Interface `Cetak` menyediakan kontrak bukti untuk `PembayaranAkses` dan `Transaksi`.
- **Enkapsulasi:** atribut dan operasi objek dirancang untuk menjaga aturan domain, seperti validitas periode akses dan jumlah stok.
- **Komposisi:** `Transaksi` memiliki `DetailTransaksi` untuk merepresentasikan produk yang dibeli beserta jumlah dan harga saat penjualan.

Relasi tersebut merupakan rancangan class; file Java dalam repository masih kosong.

## Batas lingkup

Versi awal berfokus pada layanan administrasi satu gym. Pengelolaan banyak cabang, aplikasi mandiri pelanggan, kelas latihan, personal trainer, pembekuan membership, pembayaran daring, akuntansi, dan sinkronisasi offline berada di luar cakupan.

## Dokumentasi rancangan

- [Aturan bisnis lengkap](docs/aturan-bisnis.md)
- [Class diagram, ERD, dan arsitektur](docs/diagram/README.md)
- [Struktur folder dan nama file](docs/struktur-folder.md)
- [Skenario pengujian yang direncanakan](docs/checklist-uji.md)
- [Panduan lengkap proyek (Word)](docs/Panduan_Kelompok_IndiGym.docx)
