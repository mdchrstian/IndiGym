# Skenario demo

Siapkan database baru dari [SETUP.md](../SETUP.md) supaya datanya bersih (hanya `superadmin`, 3 paket, 3 produk). Jalankan dengan `mvn javafx:run`.

## 1. Akun dan kewenangan
1. Login `superadmin` / `admin123`. Beranda menampilkan ringkasan hari ini (semua masih 0).
2. Menu **Akun** → bagian "Tambah admin baru" → buat `budi` dengan password `rahasia1`.
3. **Keluar** (kiri bawah), lalu login sebagai `budi`. Di menu Akun hanya ada "Akun saya". Bagian kelola Admin tidak tampil.
4. *Yang dijelaskan:* `SuperAdmin extends Admin` dan menimpa `bolehKelolaAdmin()` (polimorfisme). Pengecekan juga ada di `AkunService.pastikanSuperAdmin`, bukan hanya menyembunyikan bagian layar.

## 2. Pelanggan, penolakan, membership, check-in
1. Menu **Pelanggan** → isi "Pelanggan baru" dengan nama "Andi" → **Daftarkan**. Andi langsung terpilih, dengan kode `PLG1`.
2. Panel kanan menunjukkan **Belum punya akses** (kuning). Tekan **Check-in** → pesan merah, dan penolakan tercatat di riwayat.
3. Tekan **Jual membership** → layar Membership terbuka dengan Andi sudah terpilih → pilih "Member 1 Bulan" → kotak "Periksa lalu bayar" menampilkan harga dan periode → **Terima pembayaran** → bukti tampil, lalu aplikasi kembali ke layar Pelanggan dengan Andi terpilih.
4. Panel hijau **Boleh masuk** → **Check-in**. Tekan lagi → muncul konfirmasi kunjungan tambahan. Pilih Cancel → tidak ada log baru.
5. Buka tab **Membership aktif** di layar Membership: Andi ada di daftar dengan status "Aktif, sisa ... hari".
6. Tombol **Jual membership** sekarang mati, karena membership Andi masih berjalan. Membership baru baru bisa dibeli setelah yang lama berakhir (`AksesService.bayarMembership` juga menolaknya).
7. *Yang dijelaskan:* `PaketMembership.hitungTanggalAkhir` (10 Okt → 9 Nov; 31 Jan → 28/29 Feb) dan tesnya di `PaketMembershipTest`.

## 3. Visit harian (satu konfirmasi, tiga catatan)
1. Daftarkan "Budi". Tombol utamanya otomatis **Jual visit harian** → bukti tampil, panel berubah hijau, dan riwayat terisi.
2. Tombol "Jual visit harian" sekarang mati karena Budi sudah punya akses hari ini.
3. **Jual membership** untuk Budi → kotak ringkasan menulis periode **mulai besok**, karena hari ini sudah memakai visit harian.
4. *Yang dijelaskan:* `PelangganDAO.simpanVisitDanCheckIn`: pembayaran dan check-in disimpan dalam satu transaksi, dan gagal salah satu berarti batal semua (`rollback`).

## 4. Penjualan dan stok
1. Menu **Kasir dan produk** → klik dua kali "Air Mineral" dua kali, lalu klik dua kali "Protein Bar". Keranjang di kanan menampilkan total `Rp 35.000`.
2. Kosongkan kode pembeli (pembeli umum) → **Bayar** → struk tampil, stok Air Mineral 50 → 48.
3. Tab **Riwayat penjualan** → pilih transaksi → isi alasan → **Batalkan transaksi** → konfirmasi → stok kembali. Batalkan lagi → ditolak.
4. Coba masukkan jumlah melebihi stok → pesan merah "Stok ... hanya ...".
5. Kembali ke **Beranda**: angka check-in, pelanggan yang boleh masuk, dan penjualan hari ini sudah terisi.
6. *Yang dijelaskan:* `Transaksi implements Cetak`, sama seperti `PembayaranAkses`, jadi `MainApp.tampilkanBukti(Cetak)` dipakai untuk kedua jenis bukti.

## 5. Laporan pendapatan
1. Menu **Laporan** → periode awal **Bulan ini**. Kartu menampilkan pendapatan membership, visit harian, dan produk, beserta totalnya.
2. Tekan **Hari ini** atau **Tahun ini**, atau pilih tanggal sendiri di kolom Dari dan sampai lalu tekan **Tampilkan**.
3. Isi tanggal Dari sesudah tanggal sampai → pesan merah.
4. Transaksi produk yang dibatalkan di langkah 4.3 tidak ikut dihitung.
5. *Yang dijelaskan:* pendapatan dihitung dari tanggal bayar (`AksesDAO.totalPembayaran`, `PenjualanDAO.totalPenjualan`). Aturan rentang tanggal ada di `LaporanService.periksaRentang` dan dites di `LaporanServiceTest`.

## 6. Satu alur dari layar sampai database
Contoh check-in: `pelanggan.fxml` (tombol "Check-in") → `PelangganController.checkIn` → `PelangganService.checkIn` → `AksesService.cariAksesAktifHariIni` → `AksesDAO.cariAksesAktif` → tabel `checkin`. Tunjukkan juga `pelanggan.fxml` di Scene Builder dan `navigasi.fxml` yang disisipkan lewat `fx:include`.
