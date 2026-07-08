package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.hasNextInt() ? scanner.nextInt() : 0;
        List<Record> records = new ArrayList<>();

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                byte status = 0; // PENDING

                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                out.write(idBytes);
                out.write(sumaBytes);

                byte[] dataBytes = new byte[10];
                byte[] rawData = data.getBytes();
                System.arraycopy(rawData, 0, dataBytes, 0, Math.min(rawData.length, dataBytes.length));
                out.write(dataBytes);

                out.write(tip == TipTranzactie.CREDIT ? 0 : 1);
                out.write(status);
                out.write(new byte[8]);

                records.add(new Record(id, suma, data, tip, status));
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();
                switch (command) {
                    case "READ" -> {
                        int idx = scanner.nextInt();
                        Record record = readRecord(raf, idx);
                        if (record != null) {
                            System.out.println(record.format(idx));
                        }
                    }
                    case "UPDATE" -> {
                        int idx = scanner.nextInt();
                        String statusText = scanner.next();
                        byte statusByte = switch (statusText) {
                            case "PENDING" -> 0;
                            case "PROCESSED" -> 1;
                            case "REJECTED" -> 2;
                            default -> 0;
                        };
                        long position = (long) idx * RECORD_SIZE + 23;
                        raf.seek(position);
                        raf.write(statusByte);
                        System.out.println("Updated [" + idx + "]: " + statusText);
                    }
                    case "PRINT_ALL" -> {
                        for (int idx = 0; idx < n; idx++) {
                            Record record = readRecord(raf, idx);
                            if (record != null) {
                                System.out.println(record.format(idx));
                            }
                        }
                    }
                    default -> {
                        // ignore unknown commands
                    }
                }
            }
        }
    }

    private static Record readRecord(RandomAccessFile raf, int idx) throws IOException {
        long position = (long) idx * RECORD_SIZE;
        raf.seek(position);
        byte[] buffer = new byte[RECORD_SIZE];
        int read = raf.read(buffer);
        if (read != RECORD_SIZE) {
            return null;
        }

        int id = ByteBuffer.wrap(buffer, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        double suma = ByteBuffer.wrap(buffer, 4, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
        String data = new String(buffer, 12, 10).trim();
        byte tipByte = buffer[22];
        byte statusByte = buffer[23];

        TipTranzactie tip = tipByte == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        String statusText = switch (statusByte) {
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "PENDING";
        };

        return new Record(id, suma, data, tip, statusByte, statusText);
    }

    private static class Record {
        private final int id;
        private final double suma;
        private final String data;
        private final TipTranzactie tip;
        private final byte status;
        private final String statusText;

        Record(int id, double suma, String data, TipTranzactie tip, byte status) {
            this(id, suma, data, tip, status, status == 1 ? "PROCESSED" : status == 2 ? "REJECTED" : "PENDING");
        }

        Record(int id, double suma, String data, TipTranzactie tip, byte status, String statusText) {
            this.id = id;
            this.suma = suma;
            this.data = data;
            this.tip = tip;
            this.status = status;
            this.statusText = statusText;
        }

        String format(int idx) {
            return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                    idx, id, data, tip, suma, statusText);
        }
    }
}
