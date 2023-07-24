import java.util.HashMap;

public class TaskStore {
    /*1. Получать задачи по идентификатору.
    2. Выводить списки задач разных типов.*/

    HashMap<Integer, Task> tasksList = new HashMap<>();

    public Task getTaskById(int taskId) {
         return tasksList.get(taskId);
    }

    public void saveTask(int taskId, Task task) {
        tasksList.put(taskId, task);
    }

    public void removeTask(int taskId) {
        tasksList.remove(taskId);
    }
}
