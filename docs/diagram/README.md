# Diagram sistem

Setiap subfolder menyediakan SVG untuk dibaca dan sumber DOT untuk diedit. Diagram adalah rancangan sistem yang disepakati; kode starter belum mengimplementasikan seluruh diagram.

Jika Graphviz tersedia, render ulang sumber dengan:

```bash
dot -Tsvg access.dot -o access.svg
```

Perubahan model/relasi harus memperbarui sumber, SVG, kontrak integrasi, dan migration terkait. ERD akses dan ERD penjualan merupakan dua bagian dari satu database yang memakai tabel akun_admin dan pelanggan yang sama.
