package service;

import model.Task;
import utils.CustomLinkedList;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_OF_HISTORY_TASKS = 10;
    private final List<Task> historyTasks = new ArrayList<>();
    private final CustomLinkedList<Task> newHistoryTasks= new CustomLinkedList<>();

    @Override
    public void addTaskToHistory(Task task) {
        if (task == null) {
            return;
        }
        if (historyTasks.size() == SIZE_OF_HISTORY_TASKS) {
            historyTasks.remove(0);
        }
        historyTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return newHistoryTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        newHistoryTasks.removeById(id);
    }

}
