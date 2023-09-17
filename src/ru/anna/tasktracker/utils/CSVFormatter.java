package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.model.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    public static String delimiter = ",";

    private CSVFormatter() {}

    public static String formatTaskToString(Task task) {
        if (task.getTaskType() == TaskType.SUB_TASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getTaskType() + delimiter + subTask.getTitle() + delimiter + subTask.getDescription()
                    + delimiter + subTask.getIdentificationNumber() + delimiter + subTask.getStatus()
                    + delimiter + subTask.getParentEpicId();
        } else {
            return task.getTaskType() + delimiter + task.getTitle() + delimiter + task.getDescription() + delimiter
                    + task.getIdentificationNumber() + delimiter + task.getStatus();
        }
    }


    public static Task parseTask(String str) {
        String[] strings = str.split(delimiter);
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

    public static String formatHistory(List<Integer> integers) {
        List<String> strings = new ArrayList<>();
        for (Integer element : integers) {
            strings.add("" + element);
        }
        return String.join(delimiter, strings);
    }

    public static List<Integer> parseHistory(String historyStr) {
        if (historyStr.isEmpty()) {
            return new ArrayList<>();
        }
        String[] strings = historyStr.split(delimiter);
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            String idString = strings[i];
            integers.add(Integer.parseInt(idString));
        }
        return integers;
    }
}
