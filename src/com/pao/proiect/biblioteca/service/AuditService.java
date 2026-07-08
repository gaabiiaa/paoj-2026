package com.pao.proiect.biblioteca.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final Path AUDIT_FILE = Path.of("audit.csv");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private AuditService() {
        initializeFile();
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        String line = actionName + "," + LocalDateTime.now().format(FORMATTER) + System.lineSeparator();
        try {
            Files.writeString(AUDIT_FILE, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut scrie in audit.csv", e);
        }
    }

    private void initializeFile() {
        try {
            if (Files.notExists(AUDIT_FILE)) {
                Files.writeString(AUDIT_FILE, "nume_actiune,timestamp" + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut initializa audit.csv", e);
        }
    }
}
