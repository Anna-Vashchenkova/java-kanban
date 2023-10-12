package ru.anna.tasktracker.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.kvserver.KVServer;
import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.utils.Managers;

import java.io.IOException;
import java.time.LocalDateTime;
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

        //как проверить, что задача есть на сервере, достать ее оттуда? разбирать джейсон и взять id?
        //типа
        String tasksStr = client.load("tasks");
        List tasks = taskManager.getListFromString(tasksStr, Task.class);
        assertEquals(1, tasks.size());
    }

/*
    @Test
    @DisplayName("При сохранении задачи поле lastId увеличивается на 1")

    @Test
    @DisplayName("При сохранении задачи, эпика, подзадачи они успешно добавлены на сервер")

*/

}