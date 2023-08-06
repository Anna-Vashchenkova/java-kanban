package store;

import model.Task;

import java.util.List;

public interface HistoryStore {
    void addTaskToHistory(Task task);

    List<Task> getHistory();

}
