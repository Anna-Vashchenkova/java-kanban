package ru.anna.tasktracker.webserver;

import java.time.LocalDateTime;
import java.util.Objects;

public class AddSubTaskDto {
    private String title;
    private String description;
    private int duration;
    private LocalDateTime startTime;
    private int epicId;

    public AddSubTaskDto(String title, String description, int duration, LocalDateTime startTime, int epicId) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.epicId = epicId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddSubTaskDto that = (AddSubTaskDto) o;

        if (duration != that.duration) return false;
        if (epicId != that.epicId) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(description, that.description)) return false;
        return Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + epicId;
        return result;
    }

    @Override
    public String toString() {
        return "SubTaskDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", epicId=" + epicId +
                '}';
    }
}
