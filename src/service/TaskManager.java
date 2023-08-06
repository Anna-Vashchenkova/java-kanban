package service;

import model.Task;
import model.TaskStatus;
import model.TaskType;

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
}
