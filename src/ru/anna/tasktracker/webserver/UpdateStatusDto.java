package ru.anna.tasktracker.webserver;

import java.util.Objects;

public class UpdateStatusDto {
    private String status;
    private int id;

    public UpdateStatusDto(String status, int id) {
        this.status = status;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateStatusDto that = (UpdateStatusDto) o;

        if (id != that.id) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "UpdateStatusDto{" +
                "status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
