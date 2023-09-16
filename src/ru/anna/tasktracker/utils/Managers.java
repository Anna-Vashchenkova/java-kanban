package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.service.*;

import java.io.File;

public class Managers {
    private Managers(){

    }
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("kanban-backup.csv"));
    }

    public static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }
}
