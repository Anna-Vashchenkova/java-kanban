package service;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }
}
