package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.service.*;

import java.io.File;
import java.io.IOException;

public class Managers {
    private Managers(){

    }
    public static TaskManager getDefault() {
        try {
            return new HttpTaskManager();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }
}
