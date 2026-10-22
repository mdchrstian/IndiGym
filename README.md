# IndiGym

IndiGym adalah rancangan aplikasi desktop untuk administrasi satu gym: pelanggan, membership dan kunjungan harian, check-in, serta penjualan produk. Proyek ini dikembangkan untuk tugas Pemrograman Berorientasi Objek (PBO).

Pelanggan dilayani melalui Admin. Satu profil dapat dipakai untuk membership maupun visit harian, sehingga riwayat tetap terhubung saat jenis akses berubah. SuperAdmin memiliki kewenangan Admin dan mengelola akun Admin.

**Status:** tahap perancangan. File implementasi masih kosong; aplikasi belum dapat dibangun atau dijalankan. Teknologi yang disepakati adalah Java, JavaFX dengan FXML + CSS, dan MySQL di Aiven.

## Fitur yang direncanakan

- Pengelolaan profil pelanggan, paket, pembayaran akses dan perpanjangan membership.
- Check-in melalui Admin, termasuk pemeriksaan akses, alasan penolakan dan konfirmasi kunjungan berulang.
- Pembelian visit harian beserta check-in pertama dalam satu konfirmasi.
- Katalog produk, penambahan stok, penjualan beberapa produk dan pembatalan penuh.
- Riwayat pada setiap modul serta bukti di layar dan file teks.
- Login dan pengelolaan akun petugas sesuai peran.

## Alur utama

**Membership:** Admin memilih profil dan paket, memeriksa pratinjau periode, lalu mencatat pembayaran lunas. Hak masuk berasal dari pembayaran sah dan tanggal berlaku. Perpanjangan dimulai setelah akhir periode sah paling jauh. Jika visit dibeli hari ini lalu membership dibeli hari yang sama, membership mulai besok.

**Visit:** Admin memilih profil dan tarif harian, lalu mengonfirmasi pembayaran serta check-in pertama sekaligus. Akses hanya untuk hari pembayaran, bukan 24 jam. Pelanggan yang sudah mempunyai akses sah tidak perlu membeli visit tambahan.

**Penjualan:** Admin menyusun keranjang dan mengonfirmasi pembayaran. Sistem menyimpan penjualan, detail, perubahan stok dan mutasi secara atomik. Pembeli umum boleh membeli tanpa membership. Pembatalan mempertahankan riwayat dan mengembalikan stok sekali saja.

## Struktur sederhana

Ada 32 file Java: 16 model, 4 service, 5 class database, 6 controller dan MainApp.

```text
src/main/java/id/indigym/
├── MainApp.java    # Startup, navigasi dan dialog bukti
├── model/          # Objek dan penerapan PBO
├── service/        # Empat layanan aturan bisnis
├── database/       # Database dan empat DAO langsung
└── controller/     # Enam pengendali halaman

src/main/resources/
├── fxml/           # Enam halaman; fitur terkait memakai bagian/tab
└── css/            # Gaya bersama
```

Controller memanggil service, service memakai DAO, dan DAO menjalankan JDBC. Satu Connection dipakai bersama untuk satu transaksi; Database mengelola commit/rollback. Riwayat berada di halaman modul terkait dan bukti memakai dialog, sehingga tidak memerlukan halaman/controller tersendiri.

## Penerapan PBO

Manusia dan PaketAkses adalah abstract class. Admin/Pelanggan mewarisi Manusia, SuperAdmin mewarisi Admin, serta PaketHarian/PaketMembership mewarisi PaketAkses. Polimorfisme dipakai pada informasi identitas dan perhitungan periode. Interface Cetak diimplementasikan PembayaranAkses dan Transaksi. Atribut dijaga melalui enkapsulasi dan Transaksi memiliki DetailTransaksi.

## Batas lingkup

Sistem memerlukan internet dan melayani satu gym. Banyak cabang, aplikasi mandiri pelanggan, kelas/personal trainer, pembekuan membership, pembayaran daring, akuntansi dan sinkronisasi offline berada di luar cakupan awal.

## Dokumentasi

- [Struktur folder dan arti bagiannya](docs/struktur-folder.md).
- [Apa yang harus diisi dalam tiap file](docs/panduan-file.md).
- [Aturan bisnis lengkap](docs/aturan-bisnis.md).
- [Kontrak antarbagian](docs/kontrak-integrasi.md).
- [Tugas anggota selama tiga minggu](docs/peran-anggota.md).
- [Diagram class dan ERD](docs/diagram/README.md).
- [Kamus data](docs/kamus-data.md).
- [Checklist pengujian](docs/checklist-uji.md).
