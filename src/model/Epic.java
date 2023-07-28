package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String title, String description, int identificationNumber, ArrayList<SubTask> listOfSubtasks) {
        super(title, description, identificationNumber);
        this.subTasks = listOfSubtasks;
        taskType = TaskType.EPIC;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
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
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return subTasks.equals(epic.subTasks);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + subTasks.hashCode();
        return result;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }
}
