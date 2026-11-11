# Aturan Kolaborasi IndiGym

Kita menulis ulang seluruh kode sambil memahami project ini. Tujuannya **paham**, bukan cepat selesai. Aturan di bawah dibuat supaya tidak ada yang bingung dan tidak saling menimpa pekerjaan.

## 1. Satu orang, satu modul

Tiap anggota memegang satu modul utuh (model, DAO, service, controller, FXML, tes). Hanya pemilik modul yang mengedit file modulnya.

| Orang | Modul |
| --- | --- |
| 1 | Kasir dan produk |
| 2 | Pelanggan dan check-in |
| 3 | Membership dan paket |
| 4 | Akun dan kerangka aplikasi |

Perlu mengubah file milik orang lain? Minta orangnya yang mengubah, atau kabari di grup sebelum menyentuhnya.

## 2. Alur kerja Git

1. **Jangan commit langsung ke `main`.** Semua lewat branch dan Pull Request (PR).
2. Nama branch: `nama/modul-fitur`, contoh `tian/kasir-keranjang`.
3. Sebelum mulai kerja, ambil versi terbaru:
   ```bash
   git checkout main
   git pull
   git checkout -b nama/modul-fitur
   ```
4. Commit kecil dan sering, dengan pesan jelas dalam bahasa Indonesia: `Tambah validasi stok negatif di Produk`.
5. Push, buka PR ke `main`, minta minimal **satu teman** me-review.
6. Merge hanya setelah di-review dan `mvn clean verify` lolos.
7. Setelah merge, hapus branch-nya.

## 3. Urutan menulis kode

Tulis dari bawah ke atas supaya tiap lapisan bisa dites sebelum lapisan berikutnya:

`model → DAO → service → controller → FXML`

Alur aplikasi satu arah: **FXML → Controller → Service → DAO → MySQL**. Controller tidak boleh memanggil DAO langsung, dan DAO tidak boleh berisi aturan bisnis.

## 4. Aturan memahami kode

- **Tulis sendiri, jangan salin buta.** Kalau memakai AI atau menyalin dari sumber lain, kamu harus bisa menjelaskan tiap baris tanpa melihat.
- Tiap PR memuat deskripsi singkat: apa yang dibuat, konsep PBO yang dipakai, dan satu aturan bisnis yang bisa kamu jelaskan.
- Reviewer boleh bertanya "kenapa ditulis begini?". Pembuat PR harus bisa menjawab.
- Bagian yang belum dimengerti ditandai `// TODO(nama): belum paham ...` lalu dibahas bersama, bukan dibiarkan.

## 5. Gaya kode

- Nama class `PascalCase`, method dan variabel `camelCase`, konstanta `HURUF_BESAR`.
- Nama dalam bahasa Indonesia, konsisten dengan yang sudah ada (`Pelanggan`, `hitungTotal`).
- Satu class satu file, satu tanggung jawab.
- Komentar hanya untuk alasan ("kenapa"), bukan menjelaskan apa yang sudah jelas dari kodenya.
- Tidak ada kode yang di-comment dan ditinggal. Hapus saja, Git menyimpan riwayatnya.

## 6. Database

- Perubahan skema hanya lewat `database/01_skema.sql` dan data awal lewat `database/02_data_awal.sql`. Jangan ubah tabel manual di MySQL lalu lupa dicatat.
- Ubah skema = wajib kabari semua orang di grup, karena database tiap laptop harus dibuat ulang.
- Tes memakai database terpisah `indigym_uji`. Jangan menjalankan tes ke database yang berisi data cobamu.

## 7. Yang tidak boleh masuk Git

- `config/db.properties` (berisi kredensial lokal; contohnya ada di `config/db.example.properties`).
- Folder `target/`, `.idea/`, `.DS_Store`, file `.class`.
- Password, token, atau data pribadi apa pun.

Cek `git status` sebelum `git add`. Hindari `git add .` kalau belum yakin isinya.

## 8. Tes

- Setiap logika baru (percabangan, perhitungan, validasi) punya minimal satu tes.
- Jalankan `mvn clean verify` sebelum membuka PR. PR yang tesnya merah tidak di-merge.

## 9. Komunikasi

- Satu grup chat untuk koordinasi. Kabari kalau mulai mengerjakan sesuatu, kalau terblokir, atau kalau mengubah file bersama (`pom.xml`, `SETUP.md`, skema SQL).
- Bingung lebih dari 30 menit? Tanya. Jangan diam.
- Konflik merge diselesaikan oleh orang yang membuat PR, dengan menarik `main` terbaru ke branch-nya dulu.
