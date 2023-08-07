package store;

import model.Task;
import model.TaskType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryTaskStore implements TaskStore {

    HashMap<Integer, Task> tasks = new HashMap<>();

    @Override
    public Task getTaskById(int taskId) {
         return tasks.get(taskId);
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(task.getIdentificationNumber(), task);
    }

    @Override
    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public Collection<Task> getAllTasksByType(TaskType taskType) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (taskType == value.getTaskType()) {
                result.add(value);
            }
        }
         return result;
    }
}
