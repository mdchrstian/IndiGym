# Diagram IndiGym (versi sederhana)

Diagram ini sesuai kode yang ada sekarang, bukan rancangan awal. Hanya atribut dan method penting yang ditampilkan.

## Alur antar lapisan

Satu arah saja, tidak boleh melompat lapisan.

**FXML → Controller → Service → DAO → MySQL**

| Lapisan | Tugas | Contoh |
| --- | --- | --- |
| FXML | Tata letak layar (dibuat di Scene Builder) | `pelanggan.fxml` |
| Controller | Ambil input, panggil service, tampilkan hasil | `PelangganController` |
| Service | Aturan bisnis, melempar `IllegalArgumentException` jika salah | `PelangganService` |
| DAO | Hanya SQL dengan `PreparedStatement` | `PelangganDAO` |
| Model | Objek data dan aturan kecil milik objek itu | `Pelanggan` |

## Class diagram

```mermaid
classDiagram
    class Manusia {
        <<Abstract>>
        -Long id
        -String nama
        +tampilkanInfo() String
    }
    class Admin {
        -String username
        -String passwordHash
        -boolean aktif
        +bolehKelolaAdmin() boolean
        +getRole() String
    }
    class SuperAdmin {
        +bolehKelolaAdmin() boolean
        +getRole() String
    }
    class Pelanggan {
        -String kode
        -String kontak
        -boolean diarsipkan
    }
    class PaketAkses {
        <<Abstract>>
        -String nama
        -BigDecimal harga
        +hitungTanggalAkhir(LocalDate) LocalDate
        +getJenis() String
    }
    class PaketHarian
    class PaketMembership {
        -int durasiBulan
    }
    class Cetak {
        <<Interface>>
        +cetakBukti() String
    }
    class PembayaranAkses {
        -String namaPaket
        -BigDecimal harga
        -LocalDate tanggalMulai
        -LocalDate tanggalAkhir
        +berlakuPada(LocalDate) boolean
    }
    class CheckIn {
        -String hasil
        -String alasan
        +diizinkan() boolean
    }
    class Produk {
        -String kode
        -BigDecimal harga
        -int stok
    }
    class Transaksi {
        -String status
        -String alasanBatal
        +hitungTotal() BigDecimal
    }
    class DetailTransaksi {
        -int jumlah
        -BigDecimal hargaSatuan
        +hitungSubtotal() BigDecimal
    }

    Manusia <|-- Admin
    Manusia <|-- Pelanggan
    Admin <|-- SuperAdmin
    PaketAkses <|-- PaketHarian
    PaketAkses <|-- PaketMembership
    Cetak <|.. PembayaranAkses
    Cetak <|.. Transaksi

    Pelanggan "1" -- "0..*" PembayaranAkses : membayar
    PaketAkses "1" -- "0..*" PembayaranAkses : dipilih
    Pelanggan "1" -- "0..*" CheckIn : tercatat
    PembayaranAkses "0..1" -- "0..*" CheckIn : menjadi dasar
    Pelanggan "0..1" -- "0..*" Transaksi : membeli
    Transaksi "1" *-- "1..*" DetailTransaksi : berisi
    Produk "1" -- "0..*" DetailTransaksi : dijual
```

## Konsep PBO dan lokasinya

| Konsep | Di mana |
| --- | --- |
| Abstraksi | `Manusia` dan `PaketAkses` (abstract, tidak bisa di-`new`) |
| Pewarisan | `Admin`/`Pelanggan` dari `Manusia`; `SuperAdmin` dari `Admin`; `PaketHarian`/`PaketMembership` dari `PaketAkses` |
| Polimorfisme | `bolehKelolaAdmin()` dan `hitungTanggalAkhir()` memberi hasil berbeda tergantung tipe objeknya; `MainApp.tampilkanBukti(Cetak)` menerima apa saja yang `Cetak` |
| Interface | `Cetak`, dipakai `PembayaranAkses` dan `Transaksi` |
| Enkapsulasi | Field `private`; `Produk.setStok` menolak nilai negatif |
| Komposisi | `Transaksi` memiliki daftar `DetailTransaksi` |

## ERD (8 tabel)

```mermaid
erDiagram
    akun_admin {
        bigint id PK
        varchar username UK
        varchar password_hash
        varchar role
        boolean aktif
    }
    pelanggan {
        bigint id PK
        varchar kode UK
        varchar nama
        varchar kontak "boleh NULL"
        boolean diarsipkan
    }
    paket_akses {
        bigint id PK
        varchar jenis
        varchar nama
        decimal harga
        int durasi_bulan "NULL untuk harian"
    }
    pembayaran_akses {
        bigint id PK
        bigint pelanggan_id FK
        bigint paket_id FK
        bigint admin_id FK
        varchar nama_paket "salinan"
        decimal harga "salinan"
        date tanggal_mulai
        date tanggal_akhir
    }
    checkin {
        bigint id PK
        bigint pelanggan_id FK
        bigint admin_id FK
        bigint pembayaran_id FK "NULL jika DITOLAK"
        varchar hasil
        varchar alasan
    }
    produk {
        bigint id PK
        varchar kode UK
        varchar nama
        decimal harga
        int stok
    }
    transaksi_produk {
        bigint id PK
        bigint pelanggan_id FK "NULL = pembeli umum"
        bigint admin_id FK
        decimal total
        varchar status
        varchar alasan_batal
    }
    detail_transaksi {
        bigint id PK
        bigint transaksi_id FK
        bigint produk_id FK
        varchar nama_produk "salinan"
        decimal harga_satuan "salinan"
        int jumlah
        decimal subtotal
    }

    akun_admin ||--o{ pembayaran_akses : mencatat
    akun_admin ||--o{ checkin : mencatat
    akun_admin ||--o{ transaksi_produk : mencatat
    pelanggan ||--o{ pembayaran_akses : membayar
    pelanggan ||--o{ checkin : berkunjung
    pelanggan |o--o{ transaksi_produk : membeli
    paket_akses ||--o{ pembayaran_akses : dipilih
    pembayaran_akses |o--o{ checkin : mendasari
    transaksi_produk ||--|{ detail_transaksi : berisi
    produk ||--o{ detail_transaksi : dijual
```

Dua tempat memakai transaksi database (`commit` dan `rollback`): **visit** (`PelangganDAO.simpanVisitDanCheckIn`) dan **checkout/pembatalan penjualan** (`PenjualanDAO.simpanTransaksi`, `batalkanTransaksi`).

## Yang sengaja tidak ada dibanding dokumen asli

Tabel `hak_akses` (digabung ke `pembayaran_akses`), `mutasi_stok`, pembatalan pembayaran akses, `requestId`/retry, `session_version`, `Task` di background, dan konversi zona waktu UTC.
