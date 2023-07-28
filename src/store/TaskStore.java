package store;

import model.Task;
import model.TaskType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskStore {

    HashMap<Integer, Task> tasksList = new HashMap<>();

    public Task getTaskById(int taskId) {
         return tasksList.get(taskId);
    }

    public void saveTask(Task task) {
        tasksList.put(task.getIdentificationNumber(), task);
    }

    public void removeTask(int taskId) {
        tasksList.remove(taskId);
    }

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
