package service;

import store.HistoryManager;
import store.InMemoryHistoryManager;
import store.InMemoryTaskStore;
import store.TaskStore;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultTaskStore(), getDefaultHistoryStore() );
    }

    private static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }

    private static TaskStore getDefaultTaskStore() {
        return new InMemoryTaskStore();
    }
}
