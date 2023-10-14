package ru.anna.tasktracker.model;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    /*Возможно ли оставить TreeSet?
    Я использую специфические методы first и  last, код более читабельный по сравнению со стримами.
    */
    private transient TreeSet<SubTask> subTasks;

    public Epic() {
        super();
        subTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    public Epic(String title, String description, int identificationNumber, List<SubTask> listOfSubtasks, LocalDateTime startTime, int duration) {
        super(title, description, identificationNumber, startTime, duration);
        subTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        this.subTasks.addAll(listOfSubtasks);
        taskType = TaskType.EPIC;
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskType=" + taskType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", subtasks=" + subTasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (subTasks != null ? subTasks.hashCode() : 0);
        return result;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!subTasks.isEmpty()) {
            startTime = subTasks.first().startTime;
        }
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime;
        if (!subTasks.isEmpty()) {
            endTime = subTasks.last().getEndTime();
        }
        return endTime;
    }

    @Override
    public int getDuration() {
        int epicDuration = 0;
        for (SubTask subTask : subTasks) {
            epicDuration += subTask.getDuration();
        }
        return epicDuration;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }


}
