package ru.anna.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    @BeforeEach
    public void initContext() {
        taskManager = new InMemoryTaskManager();
        LocalDateTime tasksStartTime1 = LocalDateTime.of(2023, 1, 1, 12, 00);
        Task taskB = new Task("new", "--", 1, tasksStartTime1, 120);
        taskManager.addTask(taskB);
    }

    @Test
    @DisplayName("Задача а сохранена если время начала задачи a позже начала и окончания задачи b")
    public void startTimeAAfterStartTimeEndEndTimeB() {
        LocalDateTime tasksStartTime2 = LocalDateTime.of(2023, 1, 1, 14, 15);
        Task taskA = new Task("new2", "--", 2, tasksStartTime2, 60);
        taskManager.addTask(taskA);

        assertEquals(taskA, taskManager.addTask(taskA));
    }

    @Test
    @DisplayName("Задача а не сохранена если время начала задачи a позже начала задачи в и раньше окончания b")
    public void startTimeAAfterStartTimeBEndBeforeEndTimeB() {
        LocalDateTime tasksStartTime2 = LocalDateTime.of(2023, 1, 1, 13, 15);
        Task taskA = new Task("new2", "--", 2, tasksStartTime2, 60);
        taskManager.addTask(taskA);

        assertNotEquals(taskA, taskManager.addTask(taskA));
    }

    @Test
    @DisplayName("Задача а не сохранена если время начала задачи a раньше начала задачи в и время окончания а позже окончания b")
    public void startTimeABeforeStartTimeBEndEndTimeAAfterEndTimeB() {
        LocalDateTime tasksStartTime2 = LocalDateTime.of(2023, 1, 1, 11, 00);
        Task taskA = new Task("new2", "--", 2, tasksStartTime2, 90);
        taskManager.addTask(taskA);

        assertNotEquals(taskA, taskManager.addTask(taskA));
    }

    @Test
    @DisplayName("Задача а сохранена если время начала и окончания задачи a раньше начала задачи в")
    public void startTimeAEndEndTimeABeforeStartTimeB() {
        LocalDateTime tasksStartTime2 = LocalDateTime.of(2023, 1, 1, 11, 00);
        Task taskA = new Task("new2", "--", 2, tasksStartTime2, 45);
        taskManager.addTask(taskA);

        assertEquals(taskA, taskManager.addTask(taskA));
    }
}