package service;

import store.HistoryStore;
import store.InMemoryHistoryStore;
import store.InMemoryTaskStore;
import store.TaskStore;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(ge4tDefaultTaskStore(), ge4tDefaultHistoryStore() );
    }

    private static HistoryStore ge4tDefaultHistoryStore() {
        return new InMemoryHistoryStore();
    }

    private static TaskStore ge4tDefaultTaskStore() {
        return new InMemoryTaskStore();
    }
}
