package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.service.*;

import java.io.File;
import java.io.IOException;

public class Managers {
    public final static String KV_SERVER_ADDRESS = "http://localhost:8078";
    private Managers(){

    }
    public static TaskManager getDefault() {
        try {
            return new HttpTaskManager(KV_SERVER_ADDRESS);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HistoryManager getDefaultHistoryStore() {
        return new InMemoryHistoryManager();
    }
}
