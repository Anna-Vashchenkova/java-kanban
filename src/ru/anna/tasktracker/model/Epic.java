package ru.anna.tasktracker.model;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private TreeSet<SubTask> subTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public Epic(String title, String description, int identificationNumber, List<SubTask> listOfSubtasks, LocalDateTime startTime, int duration) {
        super(title, description, identificationNumber, startTime, duration);
        this.subTasks.addAll(listOfSubtasks);
        taskType = TaskType.EPIC;
    }

    public Collection<SubTask> getSubTasks() {
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
                ", startTime=" + startTime +
                ", duration=" + duration +
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
        LocalDateTime  endTime = null;
        List<LocalDateTime> timesOfSubTasks = new ArrayList<>();
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : subTasks) {
                timesOfSubTasks.add(subTask.getEndTime());
            }
            endTime = timesOfSubTasks.stream().max(LocalDateTime::compareTo).get();
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
