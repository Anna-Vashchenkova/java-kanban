package store;

import model.Task;
import model.TaskType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryTaskStore implements TaskStore {

    HashMap<Integer, Task> tasksList = new HashMap<>();

    @Override
    public Task getTaskById(int taskId) {
         return tasksList.get(taskId);
    }

    @Override
    public void saveTask(Task task) {
        tasksList.put(task.getIdentificationNumber(), task);
    }

    @Override
    public void removeTask(int taskId) {
        tasksList.remove(taskId);
    }

    @Override
    public Collection<Task> getAllTasksByType(TaskType taskType) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task value : tasksList.values()) {
            if (taskType == value.getTaskType()) {
                result.add(value);
            }
        }
         return result;
    }
}
