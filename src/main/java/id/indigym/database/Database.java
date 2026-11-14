package id.indigym.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    public static Connection getConnection() throws SQLException {
        Properties pengaturan = bacaPengaturan();
        String url = pengaturan.getProperty("db.url");
        String user = pengaturan.getProperty("db.user");
        String password = pengaturan.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties bacaPengaturan() throws SQLException {
        Properties pengaturan = new Properties();
        try (FileInputStream file = new FileInputStream("config/db.properties")) {
            pengaturan.load(file);
        } catch (IOException e) {
            throw new SQLException("File config/db.properties tidak ditemukan. Salin dari db.example.properties.");
        }
        return pengaturan;
    }
}