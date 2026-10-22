# Kontrak integrasi

Status: kontrak rancangan. Interface repository dan service pada kode masih berupa kerangka.

| Pemilik | Bagian |
| --- | --- |
| Dimas | AuthService, AdminManagementService, PaketAksesService, PembayaranAksesService |
| Mahasurya | PelangganService, CheckInService, VisitHarianService, ProdukService, PenjualanService |
| Tian | Semua interface/implementasi repository, DatabaseConfig, ConnectionFactory, TransactionManager, TxContext |
| Wika | MainApp, AppContext, NavigationManager, controller, FXML, CSS, UiTaskRunner |

UI memanggil service; service menggunakan kontrak repository; implementasi JDBC menangani SQL. Service dan model tidak mengimpor kontrol JavaFX.

Sebelum mengubah signature, catat nama operasi, pemilik, pemanggil, input, output, kemungkinan kesalahan, kebutuhan transaksi, dan skenario pengujian. Dimas dan Mahasurya menyepakati kontrak Cetak bersama Wika.

## Operasi visit yang harus dikoordinasikan

VisitHarianService Mahasurya mengoordinasikan pemeriksaan pelanggan, pembayaran Dimas, hak akses, dan check-in pertama dalam satu TransactionManager.execute milik Tian. Semua operasi internal menggunakan TxContext yang sama dan tidak commit sendiri. Wika menampilkan satu hasil proses.

## Nilai bersama

- ID internal: Long pada Java dan BIGINT pada MySQL.
- Nominal: BigDecimal dan DECIMAL, tanpa float/double untuk uang.
- Tanggal periode: LocalDate, awal dan akhir inklusif.
- Waktu kejadian: UTC; tanggal operasional memakai Asia/Makassar.
- Satu konfirmasi memiliki requestId tetap untuk pengulangan; isi berbeda dengan ID yang sama ditolak.
- Kegagalan validasi, penolakan bisnis, dan gangguan penyimpanan harus dapat dibedakan UI.

Rincian operasi service dan kamus data ada pada [panduan kelompok](Panduan_Kelompok_IndiGym.docx), halaman 11 sampai 16.
