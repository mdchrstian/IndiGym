# Struktur folder IndiGym

File Java, FXML, CSS, SQL, properties dan pom.xml adalah file kosong untuk diisi anggota kelompok. Dokumen rancangan tetap berisi panduan. Folder sementara dan output lokal tidak ditampilkan.

Arti setiap folder dan contoh isinya dijelaskan di [panduan mengenal folder](mengenal-folder.md).

```text
IndiGym/
├── config/
│   └── application.example.properties
├── database/
│   ├── initial_data/
│   │   ├── data_awal.sql
│   │   └── data_demo.sql
│   └── schema_changes/
│       ├── V001__akun_dan_pelanggan.sql
│       ├── V002__paket_pembayaran_hak_checkin.sql
│       └── V003__produk_transaksi_mutasi.sql
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
│   ├── mengenal-folder.md
│   ├── panduan-menjalankan.md
│   ├── Panduan_Kelompok_IndiGym.docx
│   ├── peran-anggota.md
│   └── struktur-folder.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── id/
│   │   │       └── indigym/
│   │   │           ├── application/
│   │   │           │   ├── AppContext.java
│   │   │           │   ├── MainApp.java
│   │   │           │   └── NavigationManager.java
│   │   │           ├── background_tasks/
│   │   │           │   └── UiTaskRunner.java
│   │   │           ├── business_logic/
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
│   │   │           ├── data_access/
│   │   │           │   ├── interfaces/
│   │   │           │   │   ├── AkunRepository.java
│   │   │           │   │   ├── CheckInRepository.java
│   │   │           │   │   ├── HakAksesRepository.java
│   │   │           │   │   ├── MutasiStokRepository.java
│   │   │           │   │   ├── PaketAksesRepository.java
│   │   │           │   │   ├── PelangganRepository.java
│   │   │           │   │   ├── PembayaranAksesRepository.java
│   │   │           │   │   ├── ProdukRepository.java
│   │   │           │   │   └── TransaksiRepository.java
│   │   │           │   └── mysql_queries/
│   │   │           │       ├── JdbcAkunRepository.java
│   │   │           │       ├── JdbcCheckInRepository.java
│   │   │           │       ├── JdbcHakAksesRepository.java
│   │   │           │       ├── JdbcMutasiStokRepository.java
│   │   │           │       ├── JdbcPaketAksesRepository.java
│   │   │           │       ├── JdbcPelangganRepository.java
│   │   │           │       ├── JdbcPembayaranAksesRepository.java
│   │   │           │       ├── JdbcProdukRepository.java
│   │   │           │       └── JdbcTransaksiRepository.java
│   │   │           ├── data_transfer/
│   │   │           │   ├── akses/
│   │   │           │   │   └── .gitkeep
│   │   │           │   ├── akun/
│   │   │           │   │   └── .gitkeep
│   │   │           │   └── operasional/
│   │   │           │       └── .gitkeep
│   │   │           ├── database_connection/
│   │   │           │   ├── ConnectionFactory.java
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── TransactionManager.java
│   │   │           │   └── TxContext.java
│   │   │           ├── errors/
│   │   │           │   └── .gitkeep
│   │   │           ├── login_security/
│   │   │           │   ├── PasswordHasher.java
│   │   │           │   └── SessionContext.java
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
│   │   │           └── page_controllers/
│   │   │               ├── AdminController.java
│   │   │               ├── BuktiController.java
│   │   │               ├── CheckInController.java
│   │   │               ├── DashboardController.java
│   │   │               ├── LoginController.java
│   │   │               ├── PelangganController.java
│   │   │               ├── PembayaranController.java
│   │   │               ├── PenjualanController.java
│   │   │               ├── ProdukController.java
│   │   │               └── RiwayatController.java
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
│       │           ├── business_logic/
│       │           │   ├── akses/
│       │           │   │   └── .gitkeep
│       │           │   ├── akun/
│       │           │   │   └── .gitkeep
│       │           │   └── operasional/
│       │           │       └── .gitkeep
│       │           ├── data_access/
│       │           │   └── .gitkeep
│       │           └── integration_tests/
│       │               └── .gitkeep
│       └── resources/
│           └── application-test.example.properties
├── .gitignore
├── pom.xml
└── README.md
```
