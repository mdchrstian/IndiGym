# Mengenal folder IndiGym

Nama folder menunjukkan perannya dalam aplikasi. Dokumen ini menjelaskan isi yang direncanakan; file implementasi saat ini masih kosong. Nama class tetap sama dengan rancangan PBO.

## Bagian utama repository

| Folder atau file | Tujuan | Contoh isi yang direncanakan |
| --- | --- | --- |
| `src/main/java/` | Kode Java aplikasi. | Class pelanggan, layanan pembayaran, controller halaman. |
| `src/main/resources/` | Berkas pendukung yang dimuat aplikasi. | Halaman FXML, CSS, gambar. |
| `src/test/java/` | Kode pengujian. | Uji periode membership, stok, dan alur visit. |
| `src/test/resources/` | Berkas pendukung pengujian. | Contoh konfigurasi database khusus pengujian. |
| `config/` | Konfigurasi aplikasi di luar kode. | Contoh pengaturan koneksi Aiven tanpa password asli. |
| `database/schema_changes/` | File SQL untuk membentuk dan mengubah struktur database secara berurutan. | V001 membuat tabel akun dan pelanggan; V002 tabel akses; V003 tabel penjualan. |
| `database/initial_data/` | File SQL untuk mengisi data awal dan data demo. | Data akun awal, paket, atau produk saat implementasi. |
| `docs/` | Penjelasan dan rancangan proyek. | Aturan bisnis, peran anggota, diagram, dan skenario uji. |
| `pom.xml` | Konfigurasi Maven untuk dependensi dan build. | JavaFX, driver MySQL, dan konfigurasi tes; saat ini kosong. |

`src` berarti source atau sumber proyek. `main` adalah bagian aplikasi dan `test` bagian pengujian. Susunan ini mengikuti struktur Maven. Di bawah `java`, folder `id/indigym` membentuk awalan package `id.indigym`, bukan dua modul fitur yang berbeda.

## Bagian kode Java aplikasi

Semua folder berikut berada di `src/main/java/id/indigym/`.

| Folder | Tanggung jawab | Contoh file |
| --- | --- | --- |
| `application/` | Titik awal aplikasi, penyusunan komponen, dan perpindahan halaman. | `MainApp.java`, `AppContext.java`, `NavigationManager.java` |
| `page_controllers/` | Menerima tindakan dari halaman, memanggil aturan bisnis, lalu memperbarui tampilan. | `LoginController.java`, `CheckInController.java` |
| `business_logic/` | Menjalankan aturan dan proses bisnis. | `PembayaranAksesService.java`, `VisitHarianService.java`, `PenjualanService.java` |
| `model/` | Mendefinisikan objek yang mewakili konsep dalam gym. | `Pelanggan.java`, `PaketMembership.java`, `Transaksi.java` |
| `data_access/interfaces/` | Mendefinisikan operasi penyimpanan dan pencarian yang dibutuhkan aplikasi. | `PelangganRepository.java`, `ProdukRepository.java` |
| `data_access/mysql_queries/` | Implementasi operasi tersebut dengan query SQL melalui JDBC ke MySQL. | `JdbcPelangganRepository.java`, `JdbcProdukRepository.java` |
| `database_connection/` | Mengatur koneksi dan transaksi database yang dipakai beberapa query. | `DatabaseConfig.java`, `ConnectionFactory.java`, `TransactionManager.java`, `TxContext.java` |
| `data_transfer/` | Menampung objek input dan hasil antar tampilan dan layanan. | Nanti berisi data formulir pembayaran atau ringkasan pelanggan; nama class belum ditetapkan. |
| `login_security/` | Menangani informasi sesi login dan pengamanan password. | `SessionContext.java`, `PasswordHasher.java` |
| `errors/` | Menampung jenis kesalahan yang dapat ditangani pemanggil. | Nanti berisi kesalahan validasi, penolakan bisnis, dan kegagalan penyimpanan; nama class belum ditetapkan. |
| `background_tasks/` | Mendukung pekerjaan di latar belakang agar halaman tetap merespons saat menunggu database. | `UiTaskRunner.java` |

Nama file `Service` tetap digunakan untuk class pelaksana proses bisnis, `Repository` untuk akses data, dan `Controller` untuk pengendali halaman. Nama folder sekarang memberi konteks tanpa perlu memahami istilah tersebut lebih dulu.

## Subfolder berdasarkan isi

| Lokasi | Isi yang direncanakan |
| --- | --- |
| `model/identitas/` | Identitas manusia, Admin, SuperAdmin, dan pelanggan. |
| `model/akses/` | Paket harian, membership, pembayaran, hak masuk, dan periode akses. |
| `model/operasional/` | Check-in, produk, penjualan, detail transaksi, dan perubahan stok. |
| `model/kontrak/` | Kontrak perilaku model yang dipakai bersama, yaitu interface `Cetak`. |
| `business_logic/akun/` | Proses login dan pengelolaan akun Admin. |
| `business_logic/akses/` | Pengelolaan paket serta pembayaran dan periode akses. |
| `business_logic/operasional/` | Proses pelanggan, check-in, visit harian, produk, dan penjualan. |
| `data_transfer/akun/` | Data input atau hasil terkait akun. |
| `data_transfer/akses/` | Data input atau hasil terkait paket dan pembayaran akses. |
| `data_transfer/operasional/` | Data input atau hasil terkait layanan harian dan penjualan. |

`model/kontrak` berisi kontrak perilaku seperti mencetak bukti. `data_access/interfaces` berisi kontrak akses data seperti mencari pelanggan. Keduanya menggunakan interface, dengan tujuan berbeda.

## Halaman dan aset tampilan

Folder berikut berada di `src/main/resources/id/indigym/`.

| Folder | Isi yang direncanakan |
| --- | --- |
| `fxml/` | Susunan elemen halaman: login, pelanggan, pembayaran, check-in, produk, dan penjualan. |
| `css/` | Warna, font, jarak, dan gaya komponen halaman. |
| `images/` | Gambar atau ikon yang digunakan aplikasi. |

FXML menjelaskan susunan halaman; `page_controllers` menangani kejadian pada halaman; `business_logic` menentukan proses bisnis yang dijalankan.

## Contoh hubungan antar bagian

Saat Admin menekan tombol check-in, `CheckInController` menerima tindakan itu dan memanggil `CheckInService`. Service memeriksa aturan akses memakai kontrak repository. Implementasi di `mysql_queries` membaca atau menyimpan data dengan koneksi dari `database_connection`. Hasil proses kemudian ditampilkan kembali pada halaman FXML.

Folder `database/schema_changes` berisi definisi tabel saat penyiapan database. Folder Java `database_connection` berisi pengelolaan koneksi saat aplikasi berjalan. Folder Java `data_access/mysql_queries` berisi query operasi aplikasi, misalnya mencari pelanggan atau menyimpan pembayaran.

## Folder pengujian

Di bawah `src/test/java/id/indigym/`, `business_logic` menampung pengujian aturan bisnis, `data_access` menampung pengujian penyimpanan data, dan `integration_tests` menampung pengujian alur yang melibatkan beberapa bagian. File `.gitkeep` hanya mempertahankan direktori kosong agar tersimpan di Git.

## Perubahan nama folder

| Nama sebelumnya | Nama sekarang |
| --- | --- |
| `app` | `application` |
| `controller` | `page_controllers` |
| `dto` | `data_transfer` |
| `exception` | `errors` |
| `infrastructure` | `database_connection` |
| `repository` | `data_access` |
| `repository/kontrak` | `data_access/interfaces` |
| `repository/jdbc` | `data_access/mysql_queries` |
| `security` | `login_security` |
| `service` | `business_logic` |
| `ui` | `background_tasks` |
| `database/migrations` | `database/schema_changes` |
| `database/seed` | `database/initial_data` |
| `src/test/java/id/indigym/integration` | `src/test/java/id/indigym/integration_tests` |

Lihat [struktur lengkap beserta nama file](struktur-folder.md) untuk lokasi setiap class. Diagram class dan ERD tetap berlaku karena tanggung jawab class dan relasi data tidak berubah.
