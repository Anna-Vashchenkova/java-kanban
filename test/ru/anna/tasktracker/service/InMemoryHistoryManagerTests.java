package ru.anna.tasktracker.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTests {
    private InMemoryHistoryManager historyManager;
    private static final LocalDateTime TIME_12 = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static final LocalDateTime TIME_12_30 = LocalDateTime.of(2023, 1, 1, 12, 30);
    private static final LocalDateTime TIME_13 = LocalDateTime.of(2023, 1, 1, 13, 0);
    private static final LocalDateTime TIME_17_30 = LocalDateTime.of(2023, 1, 1, 17, 30);
    public static final int HOUR = 59;
    public static final int HALF_HOUR = 30;
    private static final Task TASK_12 = new Task("сделать покупки", "--", 1, TIME_12, HOUR);
    private static final Task TASK_12_30 = new Task("прогулка", "--", 2, TIME_12_30, HALF_HOUR);
    private static final Task TASK_13 = new Task("приготовить обед", "--", 3, TIME_13, HOUR);
    private static final Task TASK_17_30 = new Task("прогулка", "--", 4, TIME_17_30, HOUR);

    @BeforeEach
    public void initContext() {
        historyManager = new InMemoryHistoryManager();
        historyManager.addTaskToHistory(TASK_12);
        historyManager.addTaskToHistory(TASK_12_30);
        historyManager.addTaskToHistory(TASK_13);
    }

    @Test
    @DisplayName("При добавлении null в историю размер списка истории просмотров не меняется")
    public void canNotAddNull() {
        int initialLength = historyManager.getHistory().size();
        historyManager.addTaskToHistory(null);
        int result = historyManager.getHistory().size();
        assertEquals(initialLength, result);
    }

    @Test
    @DisplayName("При добавлении новой задачи в список её id добавится в конец истории просмотров")
    public void newTasksIdToEndHistory() {
        historyManager.addTaskToHistory(TASK_17_30);
        List<Task> history = historyManager.getHistory();
        Task actual = history.get(history.size() - 1);
        assertEquals(4, actual.getIdentificationNumber());
    }

    @Test
    @DisplayName("При добавлении существующей задачи в список её id встретится один раз в конце истории просмотров")
    public void tasksIdToEndHistory() {
        historyManager.addTaskToHistory(TASK_12_30);
        List<Task> history = historyManager.getHistory();
        Task actual = history.get(history.size() - 1);
        long count = history.stream().filter(task -> task.getIdentificationNumber() == 2).count();

        assertEquals(1, count);
        assertEquals(2, actual.getIdentificationNumber());
    }

    @Test
    @DisplayName("При удалении задачи из списка её id удаляется из истории просмотров")
    public void removeTasksIdFromHistory() {
        historyManager.remove(TASK_12_30.getIdentificationNumber());
        List<Task> history = historyManager.getHistory();
        long count = history.stream().filter(task -> task.getIdentificationNumber() == 2).count();

        assertEquals(0, count);
    }
}