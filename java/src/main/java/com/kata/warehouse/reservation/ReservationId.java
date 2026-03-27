package com.kata.warehouse.reservation;

import java.util.Objects;

public final class ReservationId {
    private final String value;

    public ReservationId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ReservationId cannot be null or empty");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
