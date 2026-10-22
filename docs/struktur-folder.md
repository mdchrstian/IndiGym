# Struktur folder versi sederhana

32 file Java dan enam FXML; semua file implementasi masih kosong. Nama folder Maven src/main/java, src/main/resources dan src/test tetap dipakai.

[Arti tiap bagian](mengenal-folder.md) · [Isi setiap file](panduan-file.md)

```text
IndiGym/
├── config/
│   └── application.example.properties
├── database/
│   ├── 01_akun_pelanggan.sql
│   ├── 02_akses_checkin.sql
│   ├── 03_penjualan_stok.sql
│   ├── 04_data_awal.sql
│   └── 05_data_demo.sql
├── docs/
│   ├── diagram/
│   │   ├── access.dot
│   │   ├── access.svg
│   │   ├── architecture.dot
│   │   ├── architecture.svg
│   │   ├── erd_access.dot
│   │   ├── erd_access.svg
│   │   ├── erd_sales.dot
│   │   ├── erd_sales.svg
│   │   ├── inheritance.dot
│   │   ├── inheritance.svg
│   │   ├── README.md
│   │   ├── sales.dot
│   │   └── sales.svg
│   ├── aturan-bisnis.md
│   ├── checklist-uji.md
│   ├── demo.md
│   ├── kamus-data.md
│   ├── kontrak-integrasi.md
│   ├── mengenal-folder.md
│   ├── panduan-file.md
│   ├── panduan-menjalankan.md
│   ├── Panduan_Kelompok_IndiGym.docx
│   ├── peran-anggota.md
│   └── struktur-folder.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── id/
│   │   │       └── indigym/
│   │   │           ├── controller/
│   │   │           │   ├── AdminController.java
│   │   │           │   ├── AksesController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── PelangganController.java
│   │   │           │   └── PenjualanController.java
│   │   │           ├── database/
│   │   │           │   ├── AksesDAO.java
│   │   │           │   ├── AkunDAO.java
│   │   │           │   ├── Database.java
│   │   │           │   ├── PelangganDAO.java
│   │   │           │   └── PenjualanDAO.java
│   │   │           ├── model/
│   │   │           │   ├── Admin.java
│   │   │           │   ├── Cetak.java
│   │   │           │   ├── CheckIn.java
│   │   │           │   ├── DetailTransaksi.java
│   │   │           │   ├── HakAksesGym.java
│   │   │           │   ├── Manusia.java
│   │   │           │   ├── MutasiStok.java
│   │   │           │   ├── PaketAkses.java
│   │   │           │   ├── PaketHarian.java
│   │   │           │   ├── PaketMembership.java
│   │   │           │   ├── Pelanggan.java
│   │   │           │   ├── PembayaranAkses.java
│   │   │           │   ├── PeriodeAkses.java
│   │   │           │   ├── Produk.java
│   │   │           │   ├── SuperAdmin.java
│   │   │           │   └── Transaksi.java
│   │   │           ├── service/
│   │   │           │   ├── AksesService.java
│   │   │           │   ├── AkunService.java
│   │   │           │   ├── PelangganService.java
│   │   │           │   └── PenjualanService.java
│   │   │           └── MainApp.java
│   │   └── resources/
│   │       ├── css/
│   │       │   └── application.css
│   │       └── fxml/
│   │           ├── admin.fxml
│   │           ├── akses.fxml
│   │           ├── dashboard.fxml
│   │           ├── login.fxml
│   │           ├── pelanggan.fxml
│   │           └── penjualan.fxml
│   └── test/
│       ├── java/
│       │   └── id/
│       │       └── indigym/
│       │           └── .gitkeep
│       └── resources/
│           └── application-test.example.properties
├── .gitignore
├── pom.xml
└── README.md
```
