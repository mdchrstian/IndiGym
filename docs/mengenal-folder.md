# Mengenal struktur sederhana

Di bawah src/main/java/id/indigym hanya ada MainApp dan empat folder: model menyimpan objek PBO, service menjalankan aturan bisnis, database menyimpan koneksi dan empat DAO JDBC, controller menghubungkan enam halaman dengan service. Folder tidak dibagi lagi menurut lapisan tambahan.

MainApp menangani startup, navigasi dan dialog bukti. Empat service: AkunService, AksesService, PelangganService, PenjualanService. DAO (Data Access Object) di sini berarti class yang memuat query untuk membaca atau menyimpan data. Empat DAO: AkunDAO, AksesDAO, PelangganDAO, PenjualanDAO. Database adalah class kelima di folder database untuk config/koneksi/transaksi.

src/main/resources/fxml berisi enam halaman; css berisi satu stylesheet. src/test/java/id/indigym disediakan untuk pengujian tanpa subfolder tambahan. database di root berisi lima SQL setup/data; berbeda dari folder Java database yang menjalankan JDBC saat aplikasi hidup. config berisi contoh konfigurasi dan docs berisi dokumentasi.

Contoh check-in: PelangganController → PelangganService → PelangganDAO/AksesDAO → MySQL. Contoh visit: PelangganService memulai satu transaksi, memanggil AksesService untuk pembayaran harian pada Connection yang sama, lalu menyimpan CheckIn. Tidak ada commit per langkah.

Sesi dan hash adalah bagian AkunService. Riwayat berada di halaman modul masing-masing. Bukti memakai dialog MainApp dengan Cetak. Hasil kecil yang benar-benar dibutuhkan disepakati di service pemilik; tidak wajib membuat class data transfer/exception terpisah sejak awal.

[Struktur lengkap](struktur-folder.md) · [Isi setiap file](panduan-file.md) · [Kontrak integrasi](kontrak-integrasi.md)
