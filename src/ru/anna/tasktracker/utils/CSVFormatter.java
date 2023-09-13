package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.model.*;

import java.util.ArrayList;

public class CSVFormatter {
    public CSVFormatter() {}
    public String formatTaskToString(Task task) {
        if (task.getTaskType() == TaskType.SUB_TASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getTaskType() + "," + subTask.getTitle() + "," + subTask.getDescription() + ","
                    + subTask.getIdentificationNumber() + "," + subTask.getStatus() + "," + subTask.getParentEpicId();
        } else {
            return task.getTaskType() + "," + task.getTitle() + "," + task.getDescription() + ","
                    + task.getIdentificationNumber() + "," + task.getStatus();
        }
    }


    public Task parseTask(String str) {
        String[] strings = str.split(",");
        if (TaskType.valueOf(strings[0]) == TaskType.SUB_TASK) {
            SubTask subTask = new SubTask(strings[1], strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[5]));
            subTask.setStatus(TaskStatus.valueOf(strings[4]));
            return subTask;
        } else if (TaskType.valueOf(strings[0]) == TaskType.EPIC) {
            Epic epic = new Epic(strings[1], strings[2], Integer.parseInt(strings[3]), new ArrayList<>());
            epic.setStatus(TaskStatus.valueOf(strings[4]));
            return epic;
        }
        Task task = new Task (strings[1], strings[2], Integer.parseInt(strings[3]));
        task.setStatus(TaskStatus.valueOf(strings[4]));
        return task;
    }
}
