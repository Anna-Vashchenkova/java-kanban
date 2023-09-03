package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.service.HistoryManager;
import ru.anna.tasktracker.service.InMemoryHistoryManager;
import ru.anna.tasktracker.service.InMemoryTaskManager;
import ru.anna.tasktracker.service.TaskManager;

public class Managers {
    private Managers(){

    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }
}
