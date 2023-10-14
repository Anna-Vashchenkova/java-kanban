package ru.anna.tasktracker.model;


import java.time.LocalDateTime;

public class Task {

    protected TaskType taskType ;
    protected String title;
    protected String description;
    protected int identificationNumber;
    protected TaskStatus status;
    protected LocalDateTime startTime;
    protected int duration;

    public Task() {
    }

    public Task(String title, String description, int identificationNumber, LocalDateTime startTime, int duration) {
        this.title = title;
        this.description = description;
        this.identificationNumber = identificationNumber;
        status = TaskStatus.NEW;
        taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }
    public int getIdentificationNumber() {
        return identificationNumber;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "taskType=" + taskType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (identificationNumber != task.identificationNumber) return false;
        if (duration != task.duration) return false;
        if (taskType != task.taskType) return false;
        if (!title.equals(task.title)) return false;
        if (!description.equals(task.description)) return false;
        if (status != task.status) return false;
        return startTime.equals(task.startTime);
    }

    @Override
    public int hashCode() {
        int result = taskType.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + identificationNumber;
        result = 31 * result + status.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + duration;
        return result;
    }
}
