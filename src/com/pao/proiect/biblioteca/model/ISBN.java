package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public final class ISBN {
    private final String value;

    public ISBN(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN invalid");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISBN)) return false;
        ISBN isbn = (ISBN) o;
        return Objects.equals(value, isbn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
