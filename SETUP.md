# Setup IndiGym di laptop sendiri

Setiap anggota memakai MySQL lokal sendiri. Cukup dilakukan sekali.

## 1. Install tool
- JDK 21 (lebih baru juga bisa), Maven, Scene Builder.
- MySQL (macOS): `brew install mysql && brew services start mysql`
- Windows: install MySQL Community Server, lalu pakai `mysql -uroot -p` di langkah 2.

## 2. Siapkan database
Jalankan dari folder project:
```bash
mysql -uroot -e "CREATE DATABASE IF NOT EXISTS indigym"
mysql -uroot indigym < database/01_skema.sql
mysql -uroot indigym < database/02_data_awal.sql
```
Mau mengulang dari nol: `mysql -uroot -e "DROP DATABASE indigym"` lalu ulangi tiga perintah di atas.

## 3. Siapkan konfigurasi
```bash
cp config/db.example.properties config/db.properties
```
Kalau MySQL-mu memakai password, isi `db.password`. File `db.properties` tidak ikut di-commit.

## 4. Jalankan
```bash
mvn clean verify
mvn javafx:run
```
Login awal: username `superadmin`, password `admin123`.
