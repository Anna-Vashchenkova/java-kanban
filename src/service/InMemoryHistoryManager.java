package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_OF_HISTORY_TASKS = 10;
    private final List<Task> historyTasks = new ArrayList<>();

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
        return List.copyOf(historyTasks);
    }

}
