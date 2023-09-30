package ru.anna.tasktracker.model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int parentEpicId = 0;

    public SubTask(String title, String description, int identificationNumber, int parentEpicId, LocalDateTime startTime, int duration) {
        super(title, description, identificationNumber, startTime, duration);
        this.parentEpicId =parentEpicId;
        taskType = TaskType.SUB_TASK;
    }

    public int getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString() {
        return "Task.SubTask{" +
                "parentEpicId=" + parentEpicId +
                ", taskType=" + taskType +
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
        if (!super.equals(o)) return false;

        SubTask subTask = (SubTask) o;

        if (parentEpicId != subTask.parentEpicId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parentEpicId;
        return result;
    }
}
