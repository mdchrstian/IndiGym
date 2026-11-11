# IndiGym

Aplikasi desktop untuk operasional gym: kelola pelanggan, jual akses harian dan membership, catat check-in, serta jual produk dan pantau stok. Proyek akhir PBO, dibuat dengan Java, Maven, JavaFX (FXML + Scene Builder), dan MySQL.

## Mulai cepat

1. Ikuti [SETUP.md](SETUP.md) (install MySQL, import skema, salin konfigurasi).
2. `mvn clean verify` lalu `mvn javafx:run`.
3. Login: `superadmin` / `admin123`.

## Struktur

```
src/main/java/id/indigym/
├── MainApp.java        pindah layar, dialog, dan pesan hasil aksi
├── model/              13 class data + Rupiah (format uang)
├── database/           Database + 4 DAO (hanya SQL)
├── service/            Sesi + 4 service (aturan bisnis)
└── controller/         6 controller layar + NavigasiController (menu samping)
src/main/resources/fxml/    6 layar + navigasi.fxml, bisa dibuka di Scene Builder
src/main/resources/css/     application.css (satu-satunya file gaya)
src/main/resources/fonts/   JetBrains Mono untuk label kecil dan struk
database/                   01_skema.sql, 02_data_awal.sql
docs/                       diagram.md, demo.md, DESAIN.md
```

Alur satu arah: **FXML → Controller → Service → DAO → MySQL**. Penjelasan lengkap, class diagram, dan ERD ada di [docs/diagram.md](docs/diagram.md). Acuan tampilan (warna, huruf, kelas CSS) ada di [docs/DESAIN.md](docs/DESAIN.md).

## Aturan menulis kode di proyek ini

1. Nama class, method, dan variabel memakai bahasa Indonesia yang deskriptif.
2. Pakai `for` biasa. Tanpa stream, lambda, atau `Optional`. Data tidak ketemu: return `null`.
3. Satu method satu tugas, dan setiap aturan bisnis diberi komentar satu baris.
4. Service melempar `IllegalArgumentException("pesan")`; controller menangkapnya dan menampilkan pesan.
5. SQL selalu `PreparedStatement` dengan `?`.
6. Controller tidak berisi SQL dan aturan bisnis; DAO tidak berisi aturan bisnis.

## Pembagian modul

Pembagian per anggota (file, konsep PBO, dan aturan yang harus bisa dijelaskan) ada di [docs/pembagian-tugas.md](docs/pembagian-tugas.md).

| Modul | Model | Service | DAO | Layar |
| --- | --- | --- | --- | --- |
| Akun | Manusia, Admin, SuperAdmin | AkunService, Sesi | AkunDAO | login, dashboard, admin |
| Pelanggan | Pelanggan, CheckIn | PelangganService | PelangganDAO | pelanggan |
| Akses | PaketAkses (+2 turunan), PembayaranAkses, Cetak | AksesService | AksesDAO | akses |
| Penjualan | Produk, DetailTransaksi, Transaksi | PenjualanService | PenjualanDAO | penjualan |

## Pengujian

`mvn clean verify` menjalankan tes yang tidak butuh database (tanggal membership, keranjang, hash, kewenangan). Alur yang memakai database dicoba manual lewat skenario di [docs/demo.md](docs/demo.md).
