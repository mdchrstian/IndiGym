# Diagram IndiGym

Enam diagram tersedia langsung dalam folder ini. Sumber DOT dapat diedit dan SVG adalah gambar untuk dibaca. Semua class domain tetap berlaku pada struktur sederhana; arsitektur memakai empat service dan empat DAO langsung.

- [Arsitektur](architecture.svg) — MainApp/controller → service → Database/DAO → MySQL.
- [Pewarisan PBO](inheritance.svg) — Manusia/Admin/SuperAdmin/Pelanggan dan PaketAkses.
- [Akses dan check-in](access.svg).
- [Penjualan dan stok](sales.svg).
- [ERD akun/akses](erd_access.svg).
- [ERD penjualan](erd_sales.svg).

Kedua ERD adalah satu database dengan akun_admin/pelanggan yang sama. Saat mengubah relasi, edit DOT dan render ulang SVG, lalu selaraskan class/skema dan panduan. Diagram merupakan rancangan, bukan bukti fitur sudah dibuat.
