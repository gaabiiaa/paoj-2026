package com.pao.proiect.biblioteca.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final Properties properties = new Properties();

    private DatabaseConnection() {
        loadProperties();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                );
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Nu s-a putut deschide conexiunea la baza de date", e);
        }
    }

    private void loadProperties() {
        Path path = Path.of("resources", "db.properties");
        try (InputStream inputStream = Files.exists(path)
                ? Files.newInputStream(path)
                : DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("Fisierul db.properties nu a fost gasit");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut citi db.properties", e);
        }
    }
}
