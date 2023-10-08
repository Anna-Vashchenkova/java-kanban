package ru.anna.tasktracker.webserver;

import java.util.Objects;

public class UpdateTaskDto {
    private String title;
    private String description;
    private int id;

    public UpdateTaskDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public UpdateTaskDto(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateTaskDto that = (UpdateTaskDto) o;

        if (id != that.id) return false;
        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "UpdateTaskDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
