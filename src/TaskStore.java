import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskStore {
    /*1. Получать задачи по идентификатору.
    2. Выводить списки задач разных типов.*/

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

    public Collection<Task> getAllTasks() {
        return tasksList.values();
    }
}
