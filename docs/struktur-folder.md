# Struktur folder IndiGym

File Java, FXML, CSS, SQL, properties dan pom.xml adalah file kosong untuk diisi anggota kelompok. Dokumen rancangan tetap berisi panduan. Folder sementara dan output lokal tidak ditampilkan.

```text
IndiGym/
├── config/
│   └── application.example.properties
├── database/
│   ├── migrations/
│   │   ├── V001__akun_dan_pelanggan.sql
│   │   ├── V002__paket_pembayaran_hak_checkin.sql
│   │   └── V003__produk_transaksi_mutasi.sql
│   └── seed/
│       ├── data_awal.sql
│       └── data_demo.sql
├── docs/
│   ├── diagram/
│   │   ├── arsitektur/
│   │   │   ├── architecture.dot
│   │   │   └── architecture.svg
│   │   ├── class-akses/
│   │   │   ├── access.dot
│   │   │   └── access.svg
│   │   ├── class-identitas/
│   │   │   ├── inheritance.dot
│   │   │   └── inheritance.svg
│   │   ├── class-penjualan/
│   │   │   ├── sales.dot
│   │   │   └── sales.svg
│   │   ├── erd-akses/
│   │   │   ├── erd_access.dot
│   │   │   └── erd_access.svg
│   │   ├── erd-penjualan/
│   │   │   ├── erd_sales.dot
│   │   │   └── erd_sales.svg
│   │   └── README.md
│   ├── aturan-bisnis.md
│   ├── checklist-uji.md
│   ├── demo.md
│   ├── kontrak-integrasi.md
│   ├── panduan-menjalankan.md
│   ├── Panduan_Kelompok_IndiGym.docx
│   ├── peran-anggota.md
│   └── struktur-folder.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── id/
│   │   │       └── indigym/
│   │   │           ├── app/
│   │   │           │   ├── AppContext.java
│   │   │           │   ├── MainApp.java
│   │   │           │   └── NavigationManager.java
│   │   │           ├── controller/
│   │   │           │   ├── AdminController.java
│   │   │           │   ├── BuktiController.java
│   │   │           │   ├── CheckInController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── PelangganController.java
│   │   │           │   ├── PembayaranController.java
│   │   │           │   ├── PenjualanController.java
│   │   │           │   ├── ProdukController.java
│   │   │           │   └── RiwayatController.java
│   │   │           ├── dto/
│   │   │           │   ├── akses/
│   │   │           │   │   └── .gitkeep
│   │   │           │   ├── akun/
│   │   │           │   │   └── .gitkeep
│   │   │           │   └── operasional/
│   │   │           │       └── .gitkeep
│   │   │           ├── exception/
│   │   │           │   └── .gitkeep
│   │   │           ├── infrastructure/
│   │   │           │   ├── ConnectionFactory.java
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── TransactionManager.java
│   │   │           │   └── TxContext.java
│   │   │           ├── model/
│   │   │           │   ├── akses/
│   │   │           │   │   ├── HakAksesGym.java
│   │   │           │   │   ├── PaketAkses.java
│   │   │           │   │   ├── PaketHarian.java
│   │   │           │   │   ├── PaketMembership.java
│   │   │           │   │   ├── PembayaranAkses.java
│   │   │           │   │   └── PeriodeAkses.java
│   │   │           │   ├── identitas/
│   │   │           │   │   ├── Admin.java
│   │   │           │   │   ├── Manusia.java
│   │   │           │   │   ├── Pelanggan.java
│   │   │           │   │   └── SuperAdmin.java
│   │   │           │   ├── kontrak/
│   │   │           │   │   └── Cetak.java
│   │   │           │   └── operasional/
│   │   │           │       ├── CheckIn.java
│   │   │           │       ├── DetailTransaksi.java
│   │   │           │       ├── MutasiStok.java
│   │   │           │       ├── Produk.java
│   │   │           │       └── Transaksi.java
│   │   │           ├── repository/
│   │   │           │   ├── jdbc/
│   │   │           │   │   ├── JdbcAkunRepository.java
│   │   │           │   │   ├── JdbcCheckInRepository.java
│   │   │           │   │   ├── JdbcHakAksesRepository.java
│   │   │           │   │   ├── JdbcMutasiStokRepository.java
│   │   │           │   │   ├── JdbcPaketAksesRepository.java
│   │   │           │   │   ├── JdbcPelangganRepository.java
│   │   │           │   │   ├── JdbcPembayaranAksesRepository.java
│   │   │           │   │   ├── JdbcProdukRepository.java
│   │   │           │   │   └── JdbcTransaksiRepository.java
│   │   │           │   └── kontrak/
│   │   │           │       ├── AkunRepository.java
│   │   │           │       ├── CheckInRepository.java
│   │   │           │       ├── HakAksesRepository.java
│   │   │           │       ├── MutasiStokRepository.java
│   │   │           │       ├── PaketAksesRepository.java
│   │   │           │       ├── PelangganRepository.java
│   │   │           │       ├── PembayaranAksesRepository.java
│   │   │           │       ├── ProdukRepository.java
│   │   │           │       └── TransaksiRepository.java
│   │   │           ├── security/
│   │   │           │   ├── PasswordHasher.java
│   │   │           │   └── SessionContext.java
│   │   │           ├── service/
│   │   │           │   ├── akses/
│   │   │           │   │   ├── PaketAksesService.java
│   │   │           │   │   └── PembayaranAksesService.java
│   │   │           │   ├── akun/
│   │   │           │   │   ├── AdminManagementService.java
│   │   │           │   │   └── AuthService.java
│   │   │           │   └── operasional/
│   │   │           │       ├── CheckInService.java
│   │   │           │       ├── PelangganService.java
│   │   │           │       ├── PenjualanService.java
│   │   │           │       ├── ProdukService.java
│   │   │           │       └── VisitHarianService.java
│   │   │           └── ui/
│   │   │               └── UiTaskRunner.java
│   │   └── resources/
│   │       └── id/
│   │           └── indigym/
│   │               ├── css/
│   │               │   └── application.css
│   │               ├── fxml/
│   │               │   ├── admin.fxml
│   │               │   ├── bukti.fxml
│   │               │   ├── checkin.fxml
│   │               │   ├── dashboard.fxml
│   │               │   ├── login.fxml
│   │               │   ├── pelanggan.fxml
│   │               │   ├── pembayaran.fxml
│   │               │   ├── penjualan.fxml
│   │               │   ├── produk.fxml
│   │               │   └── riwayat.fxml
│   │               └── images/
│   │                   └── .gitkeep
│   └── test/
│       ├── java/
│       │   └── id/
│       │       └── indigym/
│       │           ├── integration/
│       │           │   └── .gitkeep
│       │           ├── repository/
│       │           │   └── .gitkeep
│       │           └── service/
│       │               ├── akses/
│       │               │   └── .gitkeep
│       │               ├── akun/
│       │               │   └── .gitkeep
│       │               └── operasional/
│       │                   └── .gitkeep
│       └── resources/
│           └── application-test.example.properties
├── .gitignore
├── PBO - Tugas Kelompok I – Kelas C – Kelompok 1.pdf
├── pom.xml
├── README.md
└── RENCANA_KERJA_KELOMPOK.md
```
