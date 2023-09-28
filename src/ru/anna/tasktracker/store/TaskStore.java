package ru.anna.tasktracker.store;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskType;

import java.util.Collection;
import java.util.TreeSet;

public interface TaskStore {
    Task getTaskById(int taskId);

    void saveTask(Task task);

    void removeTask(int taskId);

    Collection<Task> getAllTasksByType(TaskType taskType);

    TreeSet<Task> getOrderedByTimeTasks();
}
