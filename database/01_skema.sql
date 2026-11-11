
CREATE TABLE akun_admin (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama          VARCHAR(100) NOT NULL,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL,
    role          VARCHAR(20)  NOT NULL,           -- ADMIN atau SUPERADMIN
    aktif         BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE pelanggan (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    kode       VARCHAR(20) UNIQUE,                 -- diisi "PLG"+id setelah insert
    nama       VARCHAR(100) NOT NULL,
    kontak     VARCHAR(50) NULL,
    diarsipkan BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE paket_akses (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    jenis       VARCHAR(20)  NOT NULL,             -- HARIAN atau MEMBERSHIP
    nama        VARCHAR(100) NOT NULL,
    harga       DECIMAL(12,2) NOT NULL,
    durasi_bulan INT NULL,                         -- NULL untuk HARIAN
    diarsipkan  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE pembayaran_akses (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    pelanggan_id  BIGINT NOT NULL,
    paket_id      BIGINT NOT NULL,
    admin_id      BIGINT NOT NULL,
    nama_paket    VARCHAR(100)  NOT NULL,          -- salinan saat transaksi
    harga         DECIMAL(12,2) NOT NULL,          -- salinan saat transaksi
    waktu_bayar   DATETIME NOT NULL,
    tanggal_mulai DATE NOT NULL,
    tanggal_akhir DATE NOT NULL,
    FOREIGN KEY (pelanggan_id) REFERENCES pelanggan(id),
    FOREIGN KEY (paket_id)     REFERENCES paket_akses(id),
    FOREIGN KEY (admin_id)     REFERENCES akun_admin(id)
);

CREATE TABLE checkin (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    pelanggan_id  BIGINT NOT NULL,
    admin_id      BIGINT NOT NULL,
    pembayaran_id BIGINT NULL,                     -- NULL jika DITOLAK
    waktu         DATETIME NOT NULL,
    hasil         VARCHAR(20) NOT NULL,            -- DIIZINKAN atau DITOLAK
    alasan        VARCHAR(200) NULL,
    FOREIGN KEY (pelanggan_id)  REFERENCES pelanggan(id),
    FOREIGN KEY (admin_id)      REFERENCES akun_admin(id),
    FOREIGN KEY (pembayaran_id) REFERENCES pembayaran_akses(id)
);

CREATE TABLE produk (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    kode       VARCHAR(20) NOT NULL UNIQUE,
    nama       VARCHAR(100) NOT NULL,
    harga      DECIMAL(12,2) NOT NULL,
    stok       INT NOT NULL DEFAULT 0,
    diarsipkan BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE transaksi_produk (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    pelanggan_id BIGINT NULL,                      -- NULL = pembeli umum
    admin_id     BIGINT NOT NULL,
    waktu        DATETIME NOT NULL,
    total        DECIMAL(12,2) NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'LUNAS',  -- LUNAS atau BATAL
    alasan_batal VARCHAR(200) NULL,
    FOREIGN KEY (pelanggan_id) REFERENCES pelanggan(id),
    FOREIGN KEY (admin_id)     REFERENCES akun_admin(id)
);

CREATE TABLE detail_transaksi (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaksi_id BIGINT NOT NULL,
    produk_id    BIGINT NOT NULL,
    nama_produk  VARCHAR(100) NOT NULL,            -- salinan saat transaksi
    harga_satuan DECIMAL(12,2) NOT NULL,           -- salinan saat transaksi
    jumlah       INT NOT NULL,
    subtotal     DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (transaksi_id) REFERENCES transaksi_produk(id),
    FOREIGN KEY (produk_id)    REFERENCES produk(id)
);
