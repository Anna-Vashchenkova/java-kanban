package ru.anna.tasktracker.model;


public class Task {
    protected TaskType taskType ;
    protected String title;
    protected String description;
    protected int identificationNumber;
    protected TaskStatus status;

    public Task(String title, String description, int identificationNumber) {
        this.title = title;
        this.description = description;
        this.identificationNumber = identificationNumber;
        status = TaskStatus.NEW;
        taskType = TaskType.TASK;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (identificationNumber != task.identificationNumber) return false;
        if (taskType != task.taskType) return false;
        if (!title.equals(task.title)) return false;
        if (!description.equals(task.description)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = taskType.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + identificationNumber;
        result = 31 * result + status.hashCode();
        return result;
    }
}
