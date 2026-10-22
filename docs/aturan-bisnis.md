# Aturan bisnis IndiGym

## Aturan identitas akun dan pelanggan

AK01  SuperAdmin dapat menjalankan seluruh fitur Admin serta membuat, memperbarui, mereset password, dan menonaktifkan akun Admin. Admin biasa tidak boleh menjalankan pengelolaan akun, termasuk lewat pemanggilan service.

AK02  Satu akun SuperAdmin dibuat pada penyiapan awal. Versi ini tidak menyediakan pembuatan SuperAdmin tambahan. Akun awal dilindungi dari penonaktifan melalui aplikasi dan dapat mengganti passwordnya sendiri.

AK03  Username harus unik. Password disimpan sebagai hash menggunakan library yang sesuai; akun aplikasi berbeda dari user koneksi database. Service memeriksa kewenangan petugas sebelum perubahan penting.

AK04  Akun Admin dinonaktifkan tanpa dihapus. Akun nonaktif tidak dapat login atau melanjutkan operasi yang dilindungi. Reset password atau penonaktifan membatalkan sesi lama melalui pemeriksaan versi sesi atau mekanisme setara.

AK05  Setiap pembayaran, penjualan, penambahan stok, pembatalan, dan check-in menyimpan petugas pelaksana. Pergantian atau penonaktifan petugas tidak menghilangkan referensi riwayatnya.

PL01  Pelanggan tidak mengakses aplikasi secara mandiri. Admin memasukkan data dan melayani check-in. Identitas pelanggan terpisah dari paket atau hak akses yang dibeli.

PL02  Profil memiliki ID internal, kode otomatis yang unik, nama wajib, dan kontak opsional. Kontak kosong disimpan sebagai NULL. Pencarian profil dilakukan sebelum pendaftaran untuk mengurangi data ganda; nama yang sama tetap mungkin dimiliki orang berbeda.

PL03  Pelanggan boleh didaftarkan sebelum membayar. Profil yang terdaftar belum otomatis memiliki hak masuk gym. Profil yang sama dipakai untuk visit, membership, dan pembelian produk yang ingin dihubungkan dengan pelanggan.

PL04  Pelanggan diarsipkan tanpa menghapus riwayat. Pengarsipan menutup check-in dan pelayanan pembelian akses sampai profil diaktifkan kembali. Pengarsipan tidak menghentikan hitungan waktu paket.

PL05  Setelah profil diaktifkan kembali, periode sah yang masih tersisa dapat dipakai. Jika seluruh periode sudah lewat atau dibatalkan, pelanggan perlu membeli akses baru.

### Batas lingkup

Sistem melayani satu gym dengan akun petugas yang dapat digunakan bergantian maupun dari beberapa laptop kelompok. Aplikasi pelanggan, banyak cabang, kelas, personal trainer, pembekuan membership, dan sinkronisasi offline berada di luar lingkup.

## Aturan paket dan perhitungan periode

PA01  PaketHarian memberikan akses untuk tanggal pembayaran saja. PaketMembership menyimpan durasiBulan positif; paket tahunan adalah durasi 12 bulan. Harga paket tidak boleh negatif. Paket yang dihentikan diarsipkan agar riwayat tetap terbaca.

PA02  Pembayaran lunas harus sama dengan harga paket yang dikonfirmasi. Aplikasi mencatat pembayaran yang telah diterima petugas; cicilan, utang, diskon khusus, dan integrasi pembayaran daring berada di luar lingkup.

PA03  Tanggal awal dan tanggal terakhir berlaku bersifat inklusif. Penentuan hari menggunakan satu zona waktu gym, Asia/Makassar. Waktu kejadian disimpan dalam UTC dan ditampilkan dalam zona waktu gym.

PA04  Membership baru atau yang seluruh masa aktifnya telah lewat dimulai pada hari pembayaran. Jika ada membership sah dengan akhir yang belum lewat, periode tambahan dimulai sehari setelah tanggal akhir sah paling jauh, termasuk periode yang belum dimulai.

PA05  Jika pelanggan hanya memiliki visit sah hari ini lalu membeli membership hari ini, membership dimulai besok. Visit hari ini tetap dipakai dan tidak mengurangi harga membership.

PA06  Hitung bulan tujuan dari tanggal mulai. Jika nomor hari tersedia pada bulan tujuan, tanggal terakhir berlaku adalah sehari sebelum tanggal padanan. Jika nomor hari tidak tersedia, tanggal terakhir berlaku adalah hari terakhir bulan tujuan.

PA07  Setiap pembayaran berhasil menghasilkan satu HakAksesGym. Akses sah jika pembayaran belum dibatalkan dan hari ini berada dalam periodenya. Pelanggan juga harus tidak diarsipkan agar diizinkan masuk. Status aktif tidak diubah manual atau dijadikan sumber kebenaran terpisah.

PA08  Snapshot nama, jenis, harga, dan durasi paket disimpan saat pembayaran. Perubahan paket berikutnya tidak mengubah periode atau bukti pembayaran lama. Periode yang tersimpan tidak digeser otomatis oleh koreksi transaksi lain.

## Aturan check-in visit dan koreksi akses

CI01  Admin mencari atau memasukkan kode pelanggan. ID yang tidak dikenal menghasilkan pesan kesalahan tanpa membuat log pelanggan. Untuk pelanggan dikenal, percobaan masuk dicatat sebagai DIIZINKAN atau DITOLAK beserta alasan.

CI02  Periksa pengarsipan pelanggan, periode akses, dan status pembayaran sumber. Jika terdapat beberapa hak sah, gunakan satu referensi secara konsisten, dengan membership didahulukan; tidak ada pembayaran tambahan untuk check-in.

CI03  Jika sudah ada check-in DIIZINKAN pada hari yang sama, tampilkan peringatan dan minta konfirmasi petugas. Konfirmasi melanjutkan kunjungan tambahan tanpa biaya. Pembatalan konfirmasi tidak menambah log kunjungan.

CI04  Log penolakan tidak dihitung sebagai kehadiran berhasil. Penolakan dapat disebabkan belum membayar, kedaluwarsa, diarsipkan, periode belum dimulai, atau pembayaran dibatalkan. Simpan kode alasan dan keterangan yang dapat dibaca.

VI01  Visit memakai profil pelanggan ringan dan tarif PaketHarian. Hari kunjungan selalu hari pembayaran, sampai operasional hari tersebut selesai. Visit bukan akses 24 jam dan tidak dapat dipesan untuk tanggal mendatang.

VI02  Alur visit adalah pilih/buat profil, pilih tarif, terima pembayaran lunas, lalu konfirmasi pembayaran dan check-in pertama sekaligus. Pembayaran, hak akses, dan check-in disimpan dalam satu transaksi database.

VI03  Sebelum menjual visit, periksa ulang akses sah hari ini di dalam transaksi. Jika sudah memiliki membership atau visit sah, arahkan ke check-in. Tidak boleh menjual visit tambahan untuk hak masuk yang sama.

BA01  Admin dapat membatalkan pembayaran membership sah yang terbaru milik pelanggan tersebut. Pembatalan visit juga tersedia. Alasan wajib, disertai petugas serta waktu pembatalan; transaksi asli tetap tersimpan.

BA02  Hak akses dari pembayaran yang dibatalkan tidak berlaku. Akses kembali mengacu pada pembayaran sah lain yang masih mencakup hari tersebut. Pembatalan yang sama tidak diterapkan dua kali. Pengembalian uang ditangani di luar aplikasi.

BA03  Check-in yang sudah terjadi tidak dihapus atau ditulis ulang. Tampilkan hasil pada saat kejadian dan penanda bahwa pembayaran sumber kemudian dibatalkan. Pembatalan visit tidak memajukan periode membership yang sudah ditetapkan.

### Hasil yang belum pasti saat koneksi terputus

Satu konfirmasi mempunyai requestId yang tetap sama saat dicoba ulang. Jika respons terputus setelah penyimpanan mungkin berhasil, periksa requestId sebelum mengirim operasi baru. requestId yang sama dengan isi berbeda harus ditolak. UI menampilkan status sedang diperiksa sampai hasil diketahui.

## Aturan produk stok dan penjualan

PR01  Produk memiliki kode unik, nama, harga jual tidak negatif, stok bilangan bulat tidak negatif, dan penanda arsip. Produk diarsipkan tanpa menghapus transaksi sebelumnya; produk arsip tidak ditawarkan dalam penjualan baru.

PR02  Penambahan stok membutuhkan jumlah bulat positif, keterangan, petugas, dan waktu. Pencatatan mutasi menyimpan perubahan stok. Stok awal dicatat sebagai mutasi masuk agar riwayat dapat ditelusuri.

PR03  Modul mencakup katalog, harga, stok, penjualan, riwayat, dan pembatalan penuh. Pemasok, harga modal, laba, akuntansi pengeluaran, dan retur sebagian berada di luar lingkup.

TR01  Pembeli boleh pelanggan terdaftar atau pembeli umum dengan pelanggan_id kosong. Membership aktif tidak menjadi syarat membeli produk. Setiap transaksi memiliki minimal satu detail dengan jumlah positif.

TR02  Keranjang boleh diubah atau dibatalkan sebelum konfirmasi tanpa mengurangi stok. Produk yang sama digabung menjadi satu baris dengan jumlah total. Subtotal adalah jumlah dikalikan harga satuan; total adalah jumlah seluruh subtotal.

TR03  Saat konfirmasi, baca ulang produk, status arsip, harga, dan stok. Jika harga berubah sejak keranjang dibuat, tampilkan nilai terbaru untuk dikonfirmasi ulang. Harga yang disetujui menjadi snapshot transaksi.

TR04  Petugas mengonfirmasi setelah pembayaran lunas diterima. Transaksi, detail, pengurangan stok, dan mutasi penjualan disimpan dalam satu transaksi database. Jika satu bagian gagal, seluruh perubahan dibatalkan.

TR05  Harga satuan dan nama produk saat penjualan disimpan pada detail. Perubahan katalog berikutnya tidak mengubah total atau struk lama. Nominal memakai BigDecimal pada Java dan DECIMAL pada MySQL.

TR06  Pembatalan seluruh penjualan membutuhkan alasan, petugas, dan waktu. Ubah status transaksi dan kembalikan stok beserta mutasinya dalam satu transaksi database. Pembatalan berulang tidak boleh menambah stok kembali.

TR07  Pemeriksaan stok akhir berlangsung di database dengan penguncian atau pembaruan bersyarat. Dua aplikasi yang membeli stok terakhir tidak boleh sama-sama berhasil jika jumlahnya melebihi stok tersedia.

BU01  Pembayaran akses dan penjualan menyediakan bukti di layar serta file teks. Bukti mencantumkan nomor, tanggal/waktu, petugas, rincian, nominal, dan status pembatalan bila ada. Versi awal tidak mengintegrasikan printer atau PDF.

Untuk tabel contoh tanggal dan diagram lengkap, baca [panduan kelompok](Panduan_Kelompok_IndiGym.docx).
