package store;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryStore implements HistoryStore{
    public List<Task> historyTasks = new ArrayList<>();

    @Override
    public void addTaskToHistory(Task task) {
        if (historyTasks.size() < 10) {
            historyTasks.add(task);
        } else  if (historyTasks.size() == 10) {
            historyTasks.remove(0);
            historyTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

}
