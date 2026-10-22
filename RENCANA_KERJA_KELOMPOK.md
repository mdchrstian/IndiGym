# Rencana kerja kelompok IndiGym

Dokumen ini menerjemahkan keputusan diskusi menjadi pembagian pekerjaan untuk tiga minggu. Dokumen ini merupakan rencana; implementasi dan pengujian aplikasi belum dilakukan.

## Dasar pembagian

- Empat anggota: Dimas, Christian (Tian), Mahasurya, dan Satwika (Wika). Nama panggilan Tian diasumsikan merujuk pada Christian dan Wika pada Satwika.
- Sesuai revisi terakhir, beban Dimas disetarakan dengan anggota lain. Target pembagian adalah total upaya yang relatif seimbang untuk keempat anggota.
- Wika menjadi penanggung jawab seluruh tampilan JavaFX/FXML/CSS dan controller. Tian menjadi penanggung jawab database, repository, dan infrastruktur penyimpanan.
- Dimas dan Mahasurya membagi model serta logika bisnis Java. Masing-masing menulis pengujian logikanya; Wika menguji tampilan dan Tian menguji database/integrasi penyimpanan.
- Pemilik fungsi bisnis menjelaskan input, keluaran, validasi, dan skenario kepada Wika dan Tian. Pengerjaan setiap alur menggabungkan kontribusi lintas lapisan sejak minggu pertama.
- Anggota lain tetap mereview dan menjelaskan modul tersebut. Keputusan rancangan bersama tidak diubah sepihak.
- Jadwal memakai Hari 1–21 karena tanggal dan jam pengumpulan persis belum diberikan.

## Lingkup yang dibangun

JavaFX + FXML + CSS dengan MySQL bersama di Aiven Free, untuk satu gym. Aplikasi memerlukan internet; sinkronisasi offline berada di luar lingkup.

SuperAdmin memiliki seluruh kemampuan Admin serta pengelolaan akun Admin. Satu akun SuperAdmin disediakan pada penyiapan awal. Pelanggan dilayani melalui petugas dan tidak memiliki antarmuka mandiri.

Identitas pelanggan tetap sama ketika berpindah dari visit harian ke membership. Pembayaran lunas menghasilkan hak akses. Visit berlaku pada hari pembayaran; membership memakai bulan kalender. Status masuk diperiksa dari periode yang sah, pembatalan, dan pengarsipan. Check-in berulang perlu konfirmasi. Pembayaran visit dan check-in pertama disimpan dalam satu transaksi database.

Jika pelanggan sudah membeli visit sah hari ini lalu membeli membership pada hari yang sama, membership dimulai besok agar hari visit tetap terpakai. Panduan rinci, diagram, aturan bisnis, dan checklist mingguan tersedia pada [panduan kelompok](docs/Panduan_Kelompok_IndiGym.docx).

Modul produk menyediakan katalog, pengarsipan, penambahan stok, penjualan banyak item, pembeli umum atau pelanggan, struk, dan pembatalan seluruh transaksi dengan pengembalian stok. Akuntansi, pemasok, retur sebagian, pemesanan visit mendatang, dan integrasi printer/PDF berada di luar lingkup awal.

## Pembagian utama

| Anggota | Bagian utama | Hasil yang menjadi tanggung jawab |
| --- | --- | --- |
| **Dimas** | Logika akun, paket, dan pembayaran akses | Model/service autentikasi dan kewenangan, paket harian/bulanan, perhitungan periode, pembayaran, perpanjangan, pembatalan, dan isi bukti pembayaran. |
| **Christian / Tian** | Database dan penyimpanan seluruh sistem | Aiven, ERD, migration, seluruh repository, transaksi database, integritas dan akses bersamaan, data awal/demo, cadangan, serta petunjuk konfigurasi database. |
| **Mahasurya** | Logika pelanggan, check-in, dan penjualan | Model/service pelanggan, arsip, validasi check-in, koordinasi visit, produk, keranjang, penjualan, pembatalan, dan isi struk. |
| **Satwika / Wika** | JavaFX/FXML/CSS seluruh sistem | Kerangka aplikasi desktop, navigasi, seluruh halaman dan controller, binding ke service, dialog/validasi input, tampilan/penyimpanan bukti, dan pengujian UI. |

### Dimas: logika akun, paket, dan pembayaran akses

Class utama: `Manusia`, `Admin`, `SuperAdmin`, `PaketAkses`, `PaketHarian`, `PaketMembership`, `PembayaranAkses`, `HakAksesGym`, serta service terkait. Kontrak `Cetak` disepakati bersama Mahasurya pada Hari 1.

Pekerjaan:

1. Identitas akun dan pewarisan `Manusia -> Admin -> SuperAdmin`, autentikasi, logout, serta konteks petugas aktif untuk transaksi modul lain.
2. Pemeriksaan kewenangan pengelolaan Admin pada service; hash password, reset/pergantian password, penonaktifan akun, dan perlindungan akun SuperAdmin awal. Penyimpanan datanya melalui repository Tian.
3. Pengelolaan paket dan perhitungan periode harian/bulanan/tahunan melalui polymorphism. Pengujian mencakup akhir bulan dan periode yang tanggalnya ditentukan dalam test.
4. Pembayaran lunas, ketentuan harga/durasi yang tersimpan, periode hak akses, dan pencatatan petugas.
5. Perpanjangan setelah akhir membership sah yang paling jauh, termasuk periode terbayar yang belum dimulai. Jika tidak ada masa tersisa, periode mulai pada hari pembayaran.
6. Pembatalan pembayaran membership terbaru milik pelanggan dan pembatalan visit dengan alasan wajib. Hak akses sumber dibatalkan, sedangkan riwayat tetap tersimpan.
7. Operasi pembayaran yang dapat dipakai service visit Mahasurya dalam transaksi database gabungan yang disediakan Tian.
8. Isi bukti pembayaran melalui `Cetak`, kontrak service untuk controller Wika, dan pengujian unit aturan akun serta membership.

Selesai apabila: kewenangan akun berlaku di service; login/logout dan akun nonaktif benar; pembayaran, perpanjangan awal/terlambat/berulang, akhir bulan, dan pembatalan menghasilkan periode yang benar. Seluruh operasi sudah dipanggil UI Wika dan disimpan melalui repository Tian.

### Tian: database dan penyimpanan seluruh sistem

Bagian utama: konfigurasi koneksi, seluruh implementasi repository, migration, dan pengelolaan transaksi database.

Pekerjaan:

1. Konfigurasi Aiven dengan TLS, konfigurasi lokal tanpa rahasia di Git, serta pemisahan database pengembangan, demo, dan uji bila diperlukan.
2. ERD, migration, primary/foreign key, batasan data, pembangkitan ID, dan pemetaan data ke model Dimas/Mahasurya. Pewarisan class Java tidak mewajibkan satu tabel per class.
3. Repository untuk akun, pelanggan, paket, pembayaran, hak akses, check-in, produk, transaksi, detail, dan pencatatan penambahan stok.
4. Satu mekanisme transaksi database untuk operasi yang melibatkan beberapa repository. Pembayaran visit dan check-in pertama tidak commit sendiri-sendiri.
5. Penanganan akses bersamaan: stok tidak negatif, pembatalan tidak diterapkan dua kali, perpanjangan tidak saling menimpa, dan pengulangan permintaan tidak mencatat pembayaran ganda.
6. Data awal SuperAdmin sesuai kontrak Dimas, paket, produk, serta data demo yang dapat disiapkan ulang secara terkendali.
7. Pengujian integrasi penyimpanan, rollback saat gagal, pemulihan koneksi, ekspor cadangan, dan petunjuk konfigurasi database.
8. Menyediakan interface repository sejak awal agar Dimas/Mahasurya dapat mengembangkan service dengan implementasi sementara sebelum SQL lengkap tersedia.

Selesai apabila: service Dimas/Mahasurya bisa membaca dan menyimpan seluruh data; transaksi gabungan berhasil atau dibatalkan seluruhnya; skenario akses bersamaan benar; migration dan data awal dapat dijalankan dari lingkungan baru.

### Mahasurya: logika pelanggan, check-in, dan penjualan

Class utama: `Pelanggan`, `CheckIn`, `Produk`, `Transaksi`, `DetailTransaksi`, serta service pelanggan, check-in, koordinasi visit, produk, dan penjualan. `Transaksi` mengimplementasikan `Cetak`.

Pekerjaan:

1. Profil pelanggan dengan ID/kode otomatis, nama, kontak opsional, pencarian, pembaruan, arsip, dan pengaktifan kembali.
2. Check-in dengan pemeriksaan pelanggan diarsipkan dan hak akses yang berlaku. Status aktif tidak diubah manual.
3. Log diterima/ditolak beserta alasan; ID tidak ditemukan hanya menghasilkan pesan kesalahan.
4. Peringatan check-in berulang dan konfirmasi Admin. Konfirmasi yang dibatalkan tidak menambah kunjungan.
5. Alur visit: pilih/buat pelanggan, pilih tarif harian, konfirmasi pembayaran lunas dan check-in pertama sekaligus.
6. Mencegah pembelian visit ketika pelanggan sudah memiliki akses sah untuk hari itu.
7. Menyediakan riwayat check-in dan kaitannya dengan pembayaran sumber yang kemudian dibatalkan, tanpa menghapus hasil historisnya.
8. Logika katalog, perubahan harga, pengarsipan, penambahan stok dengan keterangan, serta keranjang banyak item dengan jumlah positif.
9. Penjualan kepada pembeli umum atau pelanggan, pemeriksaan stok, harga satuan saat pembelian, perhitungan subtotal/total, dan penyelesaian setelah pembayaran lunas.
10. Pembatalan seluruh transaksi dengan alasan dan pengembalian stok tepat sekali melalui mekanisme penyimpanan Tian.
11. Isi struk melalui `Cetak`, kontrak service untuk controller Wika, dan pengujian unit aturan pelanggan, check-in, serta penjualan.

Selesai apabila: seluruh status pelanggan dan check-in ditangani benar; visit tersimpan atomik melalui operasi pembayaran Dimas dan transaksi database Tian; penjualan banyak item serta pembatalannya benar. UI Wika dapat menjalankan alur pelanggan, check-in, dan penjualan.

### Wika: JavaFX, FXML, CSS, dan seluruh controller

Bagian utama: bootstrap aplikasi desktop, navigasi, resources FXML/CSS, controller, dan komponen tampilan bersama.

Pekerjaan:

1. Menyiapkan bootstrap JavaFX dan resources FXML/CSS pada proyek Maven bersama. Versi JDK/JavaFX serta konfigurasi dependensi disepakati dengan Tian.
2. Kerangka navigasi, dashboard/menu, stylesheet bersama, dan komponen form/tabel/dialog yang dipakai ulang.
3. Halaman login dan pengelolaan Admin, terhubung ke service Dimas. Tampilan menu mengikuti kewenangan petugas.
4. Halaman pelanggan, paket/pembayaran membership, check-in/visit, produk/stok, serta keranjang penjualan.
5. Tampilan riwayat, konfirmasi pengarsipan/pembatalan/check-in ulang, bukti pembayaran, dan struk; penyimpanan keluaran `Cetak` sebagai teks.
6. Seluruh controller memanggil service Dimas/Mahasurya. Validasi input dasar berada di UI; keputusan bisnis tetap diperiksa di service; SQL berada di repository Tian.
7. Pemuatan/penyimpanan di background agar JavaFX tetap merespons, indikator proses, penanganan hasil/gagal, dan pencegahan pengiriman berulang dari UI.
8. Pengujian seluruh halaman, navigasi, hubungan FXML/controller/service, kondisi kosong/gagal, serta petunjuk menjalankan aplikasi.

Selesai apabila: seluruh alur bisa dijalankan melalui UI yang konsisten; semua kontrol terhubung ke service; tampilan tetap merespons saat akses jaringan; hasil berhasil/gagal ditampilkan sesuai hasil penyimpanan.

### Batas kerja dan keseimbangan

- Dimas memegang akun serta seluruh aturan membership/pembayaran; Mahasurya memegang pelanggan, kehadiran, dan penjualan. Keduanya mendapat porsi implementasi Java yang utama.
- Wika mengerjakan UI lintas modul; Tian mengerjakan penyimpanan lintas modul. Keduanya tidak ditambah tanggung jawab penuh atas logika bisnis.
- Dimas/Mahasurya menyediakan contoh data, kontrak pemanggilan, dan pengujian service untuk mempercepat pekerjaan Wika/Tian.
- Setiap orang menulis dokumentasi bagian sendiri dan mendapat satu tugas review lintas bagian. Tidak ada pengurangan porsi Dimas.
- Beban dievaluasi pada Hari 4 dan Hari 7 berdasarkan pekerjaan yang benar-benar tersisa. Bantuan lintas bagian dialihkan sebagai tugas spesifik; Wika tetap penanggung jawab JavaFX dan Tian tetap penanggung jawab database.

## Kesepakatan teknis pada Hari 1

Semua anggota mengikuti satu sesi bersama. Wika memandu bagian JavaFX, Tian bagian database, Dimas kontrak akun/pembayaran, dan Mahasurya kontrak pelanggan/check-in/penjualan. Empat anggota menyambungkan satu contoh controller -> service -> repository bersama-sama.

| Kontrak bersama | Keputusan yang harus dituliskan sebelum pemakaian antarmodul |
| --- | --- |
| ID dan relasi | Tipe ID, format kode yang ditampilkan, dan relasi pelanggan/akun. Hindari pembangkitan ID berdasarkan jumlah baris. |
| Waktu | Satu zona waktu gym, misalnya `Asia/Makassar`; tanggal operasional konsisten. |
| Uang | Gunakan tipe presisi desimal untuk nominal, bukan floating point. |
| Sesi petugas | Cara memperoleh ID dan kewenangan petugas aktif dari service. Akun aplikasi berbeda dari kredensial koneksi database. |
| Periode akses | Definisi tanggal mulai dan tanggal terakhir berlaku yang inklusif; contoh akhir bulan sesuai keputusan diskusi. |
| Transaksi database | Operasi visit gabungan memakai satu koneksi/transaksi; operasi internal tidak commit sendiri-sendiri. |
| Integrasi pembayaran–check-in | Input/output operasi pembayaran, pemeriksaan akses, pencatatan check-in, dan penanganan pembatalan. |
| Bukti | Bentuk keluaran kontrak `Cetak`, dipakai tampilan serta penyimpanan file teks. Model tidak membuka dialog UI sendiri. |
| Kesalahan | Perbedaan input tidak valid, operasi bisnis ditolak, dan koneksi gagal; UI tidak menyatakan berhasil jika transaksi belum dipastikan. |
| Struktur UI | Penamaan FXML/controller, navigasi, stylesheet bersama, dan satu contoh yang bisa dijalankan. |

Setelah kerangka minimal dan interface service/repository tersedia, setiap anggota bekerja pada bagiannya secara bersamaan. Wika memakai data contoh dan Dimas/Mahasurya memakai repository sementara jika implementasi SQL masih dikerjakan.

## Jadwal tiga minggu

### Minggu 1 — fondasi dan alur pertama

**Hari 1–2:** sesi rancangan bersama dan kontrak inti. Dimas menyiapkan model/service akun serta paket akses; Mahasurya model/service pelanggan, check-in, dan produk; Wika bootstrap JavaFX, navigasi, serta contoh FXML/controller; Tian Aiven, migration awal, dan interface repository. Susun satu contoh alur bersama.

**Hari 3–5:** integrasikan per alur, mulai login dan pendaftaran pelanggan, lalu pembayaran membership dan produk. Untuk setiap alur, Wika menyambungkan UI, Dimas/Mahasurya service, dan Tian repository. Hari 4 menjadi pemeriksaan beban pertama. Kontrak pembayaran dan check-in visit dikerjakan Dimas, Mahasurya, dan Tian sejak tahap ini.

**Hari 6–7:** integrasi pertama dan review silang. Seluruh anggota menjalankan versi yang sama dari branch utama. Demonstrasikan login -> daftar pelanggan -> bayar membership -> check-in. Lakukan penjualan produk sederhana. Tinjau ulang beban aktual; jika satu modul tertinggal, reviewer membantu bagian terukur.

### Minggu 2 — aturan lengkap dan integrasi

**Hari 8–10:** perpanjangan, akhir bulan, pengarsipan, penonaktifan Admin, check-in berulang, visit harian gabungan, penjualan banyak item, dan bukti transaksi.

**Hari 11–12:** pembatalan pembayaran/transaksi, pengembalian stok, riwayat, dan simulasi kegagalan. Uji dua aplikasi mengakses data bersama, termasuk perebutan stok terakhir dan pembayaran/perpanjangan yang bersamaan.

**Hari 13–14:** semua alur inti terintegrasi. Hentikan penambahan fitur baru. Dimas/Mahasurya memperbarui diagram class dan bagian logika, Tian ERD serta bagian database, Wika bagian UI dan petunjuk penggunaan. Semua menulis catatan uji bagiannya.

### Minggu 3 — pengujian, pemahaman bersama, dan pengumpulan

**Hari 15–17:** pengujian silang dari checklist, perbaikan masalah prioritas, penyamaan tampilan, dan verifikasi semua layar bisa dibuka. Reset data uji hanya pada lingkungan uji.

**Hari 18–19:** verifikasi instalasi/jalankan aplikasi dari clone baru, siapkan data demo dan ekspor cadangan, gabungkan laporan/diagram, serta latihan menjelaskan modul milik orang lain.

**Hari 20:** simulasi presentasi lengkap, termasuk tanya jawab PBO dan demonstrasi satu kasus gagal. Pastikan Aiven aktif serta koneksi dari lokasi/perangkat presentasi bisa digunakan.

**Hari 21:** waktu cadangan perbaikan dan finalisasi berkas pengumpulan. Tanggal dan jam penutupan pengumpulan harus dikonfirmasi agar jadwal relatif ini bisa dipetakan ke kalender.

## Review silang dan pemerataan pemahaman

| Pemilik | Reviewer utama | Fokus review |
| --- | --- | --- |
| Dimas | Tian | Kewenangan, periode akses, pembayaran/pembatalan, dan kesesuaian service dengan transaksi penyimpanan. |
| Tian | Mahasurya | Relasi, repository, rollback, dan penyimpanan atomik visit serta penjualan. |
| Mahasurya | Wika | Kontrak service pelanggan/check-in/penjualan, hasil validasi, dan kesesuaian alur UI. |
| Wika | Dimas | Navigasi, hubungan controller/service, kewenangan menu, dan tampilan hasil pembayaran. |

Reviewer menjalankan alur, memeriksa perubahan, lalu menjelaskan kembali cara kerjanya. Review berputar lintas bagian agar setiap orang memahami kontribusinya dalam alur lengkap. Wika mendemonstrasikan struktur UI dan Tian struktur database kepada seluruh anggota.

Rutinitas bersama:

1. Pembaruan singkat setiap hari melalui kanal kelompok: selesai apa, berikutnya apa, dan hambatan apa. Penyampaian pesan ini dilakukan anggota kelompok.
2. Dua pertemuan singkat per minggu untuk demonstrasi versi berjalan, pemeriksaan beban, dan penyelesaian hambatan.
3. Satu sesi awal praktik JavaFX/database bersama yang dipandu Wika dan Tian. Sesi bantuan berikutnya menggunakan contoh konkret dan catatan yang dapat dibaca semua anggota.
4. Rotasi koordinator integrasi: Hari 1–5 Tian, Hari 6–10 Dimas, Hari 11–15 Mahasurya, Hari 16–21 Wika. Koordinator memeriksa kesiapan dan mengoordinasikan; pemilik bagian tetap memperbaiki bagiannya. Penanggung jawab teknis JavaFX/database tetap Wika/Tian.
5. Setiap anggota wajib bisa menjalankan seluruh alur demo, menjelaskan hubungan model–service–repository–controller, dan memberi contoh konsep PBO yang benar-benar digunakan.
6. Pada latihan akhir, setiap orang mempresentasikan bagian miliknya dan bagian yang ia review, lalu menelusuri satu alur utuh dari UI sampai database. Dimas menjelaskan akun/pembayaran, Mahasurya pelanggan/check-in/penjualan, Wika UI/controller, dan Tian database/repository.

## Aturan kolaborasi kode dan data

- Gunakan branch kecil per fitur, misalnya `codex/login-admin` atau `codex/checkin-visit`; gabungkan setelah review dan pemeriksaan yang relevan. Hindari menunggu satu modul selesai seluruhnya untuk integrasi pertama.
- Wika menjadi pemilik perubahan FXML/CSS/controller; Tian pemilik migration/repository. Dimas/Mahasurya menulis model/service dan meminta perubahan antarlapisan melalui kontrak yang disepakati. Bantuan kode dari anggota lain tetap direview pemilik bagian.
- Perubahan struktur database dicatat sebagai migration berurutan di Git. File yang sudah diterapkan bersama tidak diubah; perbaikan memakai migration baru.
- Sediakan `indigym_dev` untuk integrasi dan `indigym_demo` untuk presentasi. Pengujian yang mengosongkan atau merusak data memakai schema uji terpisah. Semua tetap berbagi batas penyimpanan layanan gratis.
- Data awal dibuat dengan skrip yang dapat dijalankan ulang secara terkendali. Jangan mengubah data demo untuk eksperimen harian.
- Konfigurasi koneksi dan password tidak masuk Git. Sediakan berkas contoh konfigurasi tanpa rahasia dan README langkah menjalankan aplikasi.
- Setiap pemilik memperbarui bagian laporan serta diagram sesuai tanggung jawabnya ketika perilaku implementasi berubah.

## Pemeriksaan penerimaan bersama

Sebelum pengumpulan, jalankan dan catat hasil berikut:

1. SuperAdmin membuat Admin; Admin login; Admin biasa ditolak mengelola akun; penonaktifan akun berlaku.
2. Pelanggan baru tanpa pembayaran ditolak check-in.
3. Pembayaran membership mengaktifkan akses dan menghasilkan bukti dengan petugas yang benar.
4. Perpanjangan awal, terlambat, berulang sebelum waktunya, serta akhir bulan menghasilkan periode sesuai aturan.
5. Visit menyimpan pembayaran, hak akses, dan check-in pertama seluruhnya; kegagalan di tengah membatalkan seluruh perubahan.
6. Akses aktif mencegah penjualan visit tambahan; check-in ulang memerlukan konfirmasi; batal konfirmasi tidak menambah kunjungan.
7. Pembatalan pembayaran mempertahankan riwayat dan menonaktifkan akses terkait; log lama tetap menunjukkan kejadian historisnya.
8. Pengarsipan menutup akses tanpa menghentikan hitungan waktu paket. Pengaktifan kembali menggunakan sisa periode yang masih sah.
9. Penjualan beberapa produk mengurangi stok dengan benar; stok kurang atau kegagalan penyimpanan tidak meninggalkan perubahan parsial.
10. Dua aplikasi tidak dapat menjual stok terakhir yang sama sampai menjadi negatif. Klik pembayaran berulang tidak menghasilkan pencatatan ganda.
11. Pembatalan penjualan mengembalikan stok tepat sekali; perubahan harga tidak mengubah bukti transaksi lama.
12. Gangguan internet menghasilkan pesan yang sesuai; tampilan tetap merespons dan hasil transaksi yang belum pasti diperiksa sebelum pengulangan.
13. Semua anggota dapat menjalankan aplikasi dari petunjuk proyek dan menjelaskan konsep PBO melalui alur yang benar-benar digunakan.

Pengujian unit difokuskan pada aturan tanggal, perhitungan, dan validasi. Pengujian database difokuskan pada penyimpanan atomik, pembatalan, serta akses bersamaan. Demonstrasi UI tetap diperlukan untuk memeriksa hubungan FXML, controller, dan service.

## Hasil akhir yang diserahkan bersama

- Source code, konfigurasi contoh tanpa rahasia, dan petunjuk menjalankan aplikasi.
- Migration database, data awal, data demo, serta cadangan yang diperlukan.
- Diagram class dan ERD yang mengikuti implementasi akhir.
- Laporan dengan bagian yang ditulis dan ditinjau setiap pemilik pekerjaan.
- Catatan skenario uji beserta hasil aktualnya.
- Alur demo dan pembagian presentasi; semua anggota memahami alur keseluruhan.

Penyesuaian pembagian dilakukan berdasarkan kemajuan aktual pada Hari 4 dan Hari 7. Targetnya adalah beban keempat anggota yang setara; Dimas tetap mendapat porsi penuh, Wika tetap memimpin JavaFX, dan Tian tetap memimpin database.
