package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;

public interface TaskManager {
    void addTask(Task task);

    Task getTaskById(int taskId);

    void removeTask(int taskId);

    void setStatus(int taskId, TaskStatus status);

    void printTaskList(TaskType taskType);

    int generateIdNumber();

    int removeAllTasksByType(TaskType taskType);

    void updateTask(Task task);

    void printSubTasks(int epicId);

    void printHistory();
}
