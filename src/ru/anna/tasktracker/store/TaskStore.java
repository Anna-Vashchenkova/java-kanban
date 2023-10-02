package ru.anna.tasktracker.store;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskType;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskStore {
    Task getTaskById(int taskId);

    void saveTask(Task task);

    void removeTask(int taskId);

    List<Task> getAllTasksByType(TaskType taskType);

    Set<Task> getOrderedByTimeTasks();
}
