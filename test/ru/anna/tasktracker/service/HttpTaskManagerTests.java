package ru.anna.tasktracker.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.kvserver.KVServer;
import ru.anna.tasktracker.model.Epic;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.utils.Managers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.anna.tasktracker.utils.Managers.KV_SERVER_ADDRESS;

class HttpTaskManagerTests {

    private KVServer kvServer;
    private KVTaskClient client;
    private HttpTaskManager taskManager;
    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        client = new KVTaskClient(KV_SERVER_ADDRESS);
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    protected void tearDown() {
        kvServer.stop();
    }

    @Test
    @DisplayName("При восстановлении менеджера с сервера получен корректный список эпиков, подзадач, задач")
    public void shouldRestore() {
        LocalDateTime time12 = LocalDateTime.of(2023,1,1,12,0);
        Task task12 = new Task("сделать покупки", "--", 1, time12, 59);
        taskManager.addTask(task12);

        LocalDateTime time14 = LocalDateTime.of(2023,1,1,14,0);
        Epic epic14 = new Epic("сделать уборку", "--", 2, new ArrayList<>(),  time14, 59);
        taskManager.addTask(epic14);

        LocalDateTime time15 = LocalDateTime.of(2023,1,1,15,0);
        SubTask subTask15 = new SubTask("уборка комнаты", "--", 3, 2,  time15, 59);
        taskManager.addTask(subTask15);

        taskManager = (HttpTaskManager) Managers.getDefault();

        assertEquals(task12, taskManager.getTaskById(task12.getIdentificationNumber()));
        assertEquals(epic14, taskManager.getTaskById(epic14.getIdentificationNumber()));
        assertEquals(subTask15, taskManager.getTaskById(subTask15.getIdentificationNumber()));
    }

}