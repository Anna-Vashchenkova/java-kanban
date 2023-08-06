package store;

import model.Task;
import model.TaskType;

import java.util.Collection;

public interface TaskStore {
    Task getTaskById(int taskId);

    void saveTask(Task task);

    void removeTask(int taskId);

    Collection<Task> getAllTasksByType(TaskType taskType);
}
