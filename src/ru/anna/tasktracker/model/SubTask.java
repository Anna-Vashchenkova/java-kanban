package ru.anna.tasktracker.model;

public class SubTask extends Task {
    private int parentEpicId = 0;

    public SubTask(String title, String description, int identificationNumber, int parentEpicId ) {
        super(title, description, identificationNumber);
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return parentEpicId == subTask.parentEpicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + parentEpicId;
        return result;
    }
}
