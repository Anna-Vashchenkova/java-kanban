package ru.anna.tasktracker.store;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskType;

import java.util.*;

public class InMemoryTaskStore implements TaskStore {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private TreeSet<Task> orderedByTimeTasks = new TreeSet<>((Comparator.comparing(Task::getStartTime)));


    @Override
    public Task getTaskById(int taskId) {
         return tasks.get(taskId);
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(task.getIdentificationNumber(), task);
        if (task.getTaskType() != TaskType.EPIC) {
            orderedByTimeTasks.add(task);
        }
    }

    @Override
    public void removeTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            orderedByTimeTasks.remove(task);
        }
        tasks.remove(taskId);
    }

    @Override
    public List<Task> getAllTasksByType(TaskType taskType) {
        List<Task> result = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (taskType == value.getTaskType()) {
                result.add(value);
            }
        }
         return result;
    }

    @Override
    public Set<Task> getOrderedByTimeTasks() {
        return orderedByTimeTasks;
    }
}
