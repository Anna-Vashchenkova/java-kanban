package ru.anna.tasktracker.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.Epic;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;
    private static TreeSet<Task> ORDERED_BY_TIME_TASKS = new TreeSet<>((Comparator.comparing(Task::getStartTime)));
    private static LocalDateTime TIME_11 = LocalDateTime.of(2023, 1, 1, 11, 0);
    private static LocalDateTime TIME_12 = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static LocalDateTime TIME_12_30 = LocalDateTime.of(2023, 1, 1, 12, 30);
    private static LocalDateTime TIME_13 = LocalDateTime.of(2023, 1, 1, 13, 0);
    private static LocalDateTime TIME_13_30 = LocalDateTime.of(2023, 1, 1, 13, 30);
    private static LocalDateTime TIME_14 = LocalDateTime.of(2023, 1, 1, 14, 0);
    private static LocalDateTime TIME_17_30 = LocalDateTime.of(2023, 1, 1, 17, 30);
    private static LocalDateTime TIME_18 = LocalDateTime.of(2023, 1, 1, 18, 0);
    private static LocalDateTime TIME_18_30 = LocalDateTime.of(2023, 1, 1, 18, 30);
    private static LocalDateTime TIME_19 = LocalDateTime.of(2023, 1, 1, 19, 0);


    public static final int HOUR = 59;
    public static final int HALF_HOUR = 30;
    public static final int EPIC_ID = 99;
    private static Task TASK_11 = new Task("прогулка", "--", 4, TIME_11, HALF_HOUR);
    private static SubTask TASK_12 = new SubTask("сделать покупки", "--", 1, EPIC_ID, TIME_12, HOUR);
    private static SubTask TASK_12_30 = new SubTask("прогулка", "--", 6, EPIC_ID, TIME_12_30, HOUR);
    private static SubTask TASK_13 = new SubTask("приготовить обед", "--", 2, EPIC_ID, TIME_13, HOUR);
    private static SubTask TASK_13_30 = new SubTask("прогулка", "--", 6, EPIC_ID, TIME_13_30, HOUR);
    private static SubTask TASK_14 = new SubTask("прогулка", "--", 5, EPIC_ID, TIME_14, HOUR);
    private static Task TASK_17_30 = new Task("прогулка", "--", 6, TIME_17_30, HOUR);
    private static Task TASK_18 = new Task("выучить урок", "--", 3, TIME_18, HOUR);
    private static Task TASK_18_30 = new Task("прогулка", "--", 6, TIME_18_30, HOUR);
    private static Epic EPIC_FOR_DAY = new Epic("Задачи на день", "Задачи на день", EPIC_ID, List.of(TASK_12, TASK_13, TASK_14, TASK_13_30), TIME_12, HOUR);
    private static Task EPIC_18_30 = new Epic("Задачи на вечер", "--", 6, List.of(), TIME_18_30, HOUR);
    private static Task TASK_19 = new Task("прогулка", "--", 7, TIME_19, HOUR);


    @BeforeAll
    public static void addTasks() {

        ORDERED_BY_TIME_TASKS.add(TASK_12);
        ORDERED_BY_TIME_TASKS.add(TASK_13);
        ORDERED_BY_TIME_TASKS.add(TASK_18);
    }

    @BeforeEach
    public void initContext() {
        taskManager = new InMemoryTaskManager();
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_12);
    }

    @Test
    @DisplayName("Задача сохранена если время начала добавляемой задачи позже начала и окончания единственной задачи в списке")
    public void startTimeAAfterStartTimeEndEndTimeB() {

        Task result = taskManager.addTask(TASK_14);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Задача не сохранена если время начала добавляемой задачи позже начала единственной задачи в списке и раньше ее окончания")
    public void startTimeAAfterStartTimeBEndBeforeEndTimeB() {

        Task result = taskManager.addTask(TASK_12_30);

        assertNull(result);
    }

    @Test
    @DisplayName("Задача не сохранена если добавляемая задача пересекается с единственной задачей в списке")
    public void startTimeABeforeStartTimeBEndEndTimeAAfterEndTimeB() {
        Task task11 = new Task("new2", "--", 2, TIME_11, 90);
        Task result = taskManager.addTask(task11);
        assertNull(result);
    }

    @Test
    @DisplayName("Задача сохранена если находится раньше единственной задачи")
    public void startTimeAEndEndTimeABeforeStartTimeB() {
        Task result = taskManager.addTask(TASK_11);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Можно добавить задачу если её окончание раньше начала первой задачи в списке")
    public void endTimeABeforeStartTimeB() {

        assertTrue(taskManager.canAddTask(TASK_11, ORDERED_BY_TIME_TASKS));
    }

    @Test
    @DisplayName("Можно добавить задачу в середину, если тестируюмая задача находится между задачами в списке")
    public void canAddBetweenOnlyStartTime() {

        assertTrue(taskManager.canAddTask(TASK_14, ORDERED_BY_TIME_TASKS));
    }

    @Test
    @DisplayName("Можно добавить задачу после всех задач в списке")
    public void canAddAfterAll() {

        assertTrue(taskManager.canAddTask(TASK_19, ORDERED_BY_TIME_TASKS));
    }

    @Test
    @DisplayName("Нельзя добавить задачу, если она пересекается с первой задачей в списке ")
    public void cantAddWenIntersectsWithFirst(){
        assertFalse(taskManager.canAddTask(TASK_12_30, ORDERED_BY_TIME_TASKS));

    }

    @Test
    @DisplayName("Нельзя добавить задачу, если она пересекается с последней задачей в списке ")
    public void cantAddWenIntersectsWithLast(){
        assertFalse(taskManager.canAddTask(TASK_13_30, ORDERED_BY_TIME_TASKS));
    }

    @Test
    @DisplayName("Нельзя добавить задачу, если она пересекается с задачей внутри списке ")
    public void cantAddWenIntersectsWithSomeInsideList(){
        assertFalse(taskManager.canAddTask(TASK_18_30, ORDERED_BY_TIME_TASKS));
    }

    @Test
    @DisplayName("Возможно добавить эпик, если он пересекается с одной из задач")
    public void canAddEpicWhenIntersectsWithSomeTask(){
        assertTrue(taskManager.canAddTask(EPIC_18_30, ORDERED_BY_TIME_TASKS));
    }

}