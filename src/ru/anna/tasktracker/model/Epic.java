package ru.anna.tasktracker.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {

    private List<SubTask> subTasks;

    public Epic(String title, String description, int identificationNumber, ArrayList<SubTask> listOfSubtasks, LocalDateTime startTime, int duration) {
        super(title, description, identificationNumber, startTime, duration);
        this.subTasks = listOfSubtasks;
        taskType = TaskType.EPIC;
    }

    public List<SubTask> getSubTasks() {
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

    @Override
    public LocalDateTime getStartTime() {
        if (!subTasks.isEmpty()) {
            startTime = subTasks.get(0).getStartTime();
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
