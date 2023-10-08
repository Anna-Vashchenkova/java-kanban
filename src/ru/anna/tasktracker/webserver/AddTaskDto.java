package ru.anna.tasktracker.webserver;

import java.time.LocalDateTime;
import java.util.Objects;

public class AddTaskDto {
    private String title;
    private String description;
    private int duration;
    private LocalDateTime startTime;

    public AddTaskDto(String title, String description, int duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddTaskDto addTaskDto = (AddTaskDto) o;

        if (duration != addTaskDto.duration) return false;
        if (!Objects.equals(title, addTaskDto.title)) return false;
        if (!Objects.equals(description, addTaskDto.description)) return false;
        return Objects.equals(startTime, addTaskDto.startTime);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
