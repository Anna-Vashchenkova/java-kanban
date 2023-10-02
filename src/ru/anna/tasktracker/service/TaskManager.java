package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    Task addTask(Task task);

    Task getTaskById(int taskId);

    void removeTask(int taskId);

    Task setStatus(int taskId, TaskStatus status);

    Collection<Task> getTaskListByType(TaskType taskType);

    int generateIdNumber();

    int removeAllTasksByType(TaskType taskType);

    void updateTask(Task task);

    Set<SubTask> getEpicSubtasks(int epicId);

    List<Task> getHistory();
}
