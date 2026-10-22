# Checklist penerimaan

Ini daftar target pengujian aplikasi lengkap. Saat ini hanya tes pemuatan kerangka FXML yang tersedia.

| Status | ID | Kasus dan hasil yang diharapkan | Pemilik |
| --- | --- | --- | --- |
| [ ] | U01 | SuperAdmin membuat Admin; akun baru bisa login. Admin biasa ditolak mengelola akun pada service. | Dimas dan Wika |
| [ ] | U02 | Akun nonaktif ditolak login/operasi berikutnya. Password tidak tersimpan sebagai teks biasa. | Dimas dan Tian |
| [ ] | U03 | Pelanggan terdaftar tanpa pembayaran ditolak masuk dan memiliki log penolakan; ID tidak dikenal tidak membuat log pelanggan. | Mahasurya |
| [ ] | U04 | Pembayaran lunas membuat satu hak dan bukti. Pembayaran kurang/nominal tak sesuai tidak mengaktifkan akses. | Dimas dan Tian |
| [ ] | U05 | Periode 10 Okt satu bulan berakhir 9 Nov; 31 Jan dan 30 Jan 2027 berakhir 28 Feb; kasus tahun kabisat sesuai PA06. | Dimas |
| [ ] | U06 | Perpanjangan sebelum waktunya menjaga sisa; perpanjangan berulang memakai akhir terbayar paling jauh; setelah kedaluwarsa mulai hari pembayaran. | Dimas |
| [ ] | U07 | Visit hari ini lalu membership hari ini menghasilkan membership mulai besok. Pembelian visit tambahan ketika sudah ada akses sah ditolak. | Dimas dan Mahasurya |
| [ ] | U08 | Visit menyimpan pembayaran, hak, dan check-in pertama sekali. Gagal menyimpan check-in menyebabkan seluruh proses rollback. | Mahasurya dan Tian |
| [ ] | U09 | Kunjungan kedua meminta konfirmasi; lanjut menambah kunjungan tanpa biaya, batal tidak menambah log kunjungan. | Mahasurya dan Wika |
| [ ] | U10 | Arsip menutup masuk tanpa membekukan waktu. Aktif kembali memanfaatkan sisa sah atau meminta pembelian baru jika sudah lewat. | Mahasurya |
| [ ] | U11 | Pembatalan membership hanya pembayaran sah terbaru; visit dapat dikoreksi. Log lama tetap ada dan ditandai bila sumber dibatalkan. | Dimas dan Mahasurya |
| [ ] | U12 | Harga/durasi paket diubah; pembayaran lama, periodenya, dan buktinya tetap memakai snapshot. | Dimas dan Tian |
| [ ] | U13 | Pembeli umum tanpa pelanggan dapat bertransaksi; keranjang banyak item menghitung jumlah/subtotal/total tepat. | Mahasurya |
| [ ] | U14 | Stok kurang atau produk arsip ditolak. Batal keranjang tidak mengubah stok. Perubahan harga meminta konfirmasi ulang. | Mahasurya dan Wika |
| [ ] | U15 | Gagal menyimpan detail/mutasi menyebabkan transaksi, stok, dan total rollback. Pembatalan penuh mengembalikan stok sekali saja. | Mahasurya dan Tian |
| [ ] | U16 | Dua aplikasi membeli stok terakhir; total penjualan berhasil tidak melebihi stok. Dua perpanjangan bersamaan tidak saling menimpa. | Tian |
| [ ] | U17 | Klik/permintaan pembayaran berulang memakai requestId yang sama; satu proses tersimpan. Respons hilang diperiksa sebelum pengulangan. | Tian dan Wika |
| [ ] | U18 | Harga/nama produk diubah; bukti lama tetap menampilkan snapshot. Pembatalan tidak menghapus bukti asli. | Mahasurya dan Tian |
| [ ] | U19 | Semua halaman FXML terbuka, kontrol bekerja, input kosong ditangani, dan UI merespons saat jaringan lambat/gagal. | Wika |
| [ ] | U20 | Dari clone baru, petunjuk konfigurasi cukup untuk menjalankan aplikasi. Data demo dapat disiapkan tanpa mengubah data uji anggota lain. | Semua anggota |

Untuk setiap hasil, catat tanggal, penguji, hasil aktual, dan referensi perbaikan.
