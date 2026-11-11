
-- Login awal: username superadmin, password admin123
INSERT INTO akun_admin (nama, username, password_hash, role, aktif)
VALUES ('Super Admin', 'superadmin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'SUPERADMIN', TRUE);

INSERT INTO paket_akses (jenis, nama, harga, durasi_bulan) VALUES
('HARIAN',     'Visit Harian',   30000.00,  NULL),
('MEMBERSHIP', 'Member 1 Bulan', 250000.00, 1),
('MEMBERSHIP', 'Member 12 Bulan', 2500000.00, 12);

INSERT INTO produk (kode, nama, harga, stok) VALUES
('PRD001', 'Air Mineral',   5000.00, 50),
('PRD002', 'Protein Bar',  25000.00, 20),
('PRD003', 'Handuk Gym',   40000.00, 10);
