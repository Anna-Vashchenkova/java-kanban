package ru.anna.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTests {
    private InMemoryTaskManager taskManager;
    private static LocalDateTime TIME_12 = LocalDateTime.of(2023,1,1,12,0);
    private static LocalDateTime TIME_13_30 = LocalDateTime.of(2023, 1, 1, 13, 30);
    private static LocalDateTime TIME_15 = LocalDateTime.of(2023, 1, 1, 15, 0);
    private static LocalDateTime TIME_16 = LocalDateTime.of(2023, 1, 1, 16, 0);

    private static final int HOUR = 59;
    private static final int EPIC_ID = 99;


    private static Task TASK_12 = new Task("сделать покупки", "--", 1, TIME_12, HOUR);
    private static SubTask TASK_13_30 = new SubTask("прогулка", "--", 2, EPIC_ID, TIME_13_30, HOUR);
    private static SubTask TASK_15 = new SubTask("уборка", "--", 3, EPIC_ID, TIME_15, HOUR);
    private static Epic EPIC_FOR_DAY = new Epic("Задачи на день", "Задачи на день", EPIC_ID, List.of(TASK_13_30), TIME_12, HOUR);


    @BeforeEach
    public void initContext() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Нельзя добавить не существующую задачу")
    public void canNotAddTaskNull() {
        assertNull(taskManager.addTask(null));
    }

    @Test
    @DisplayName("Нельзя добавить подзадачу если ранее не был сохранен эпик")
    public void canNotAddSubTaskWithoutEpic() {
        assertNull(taskManager.addTask(TASK_13_30));
    }

    @Test
    @DisplayName("Можно добавить подзадачу если есть эпик с нужным id")
    public void canAddSubTaskWithEpic() {
        taskManager.addTask(EPIC_FOR_DAY);
        Task result = taskManager.addTask(TASK_15);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Нельзя изменить статус не существующей задачи")
    public void canNotSetStatus() {
        assertNull(taskManager.setStatus(99999, TaskStatus.DONE));
    }

    @Test
    @DisplayName("Статус эпика новый если обе задачи в статусе новые")
    public void epicStatusIsNew() {
        SubTask subTask15 = new SubTask("уборка", "--", 30, 100, TIME_15, HOUR);
        SubTask subTask16 = new SubTask("стирка", "--", 40, 100, TIME_16, HOUR);
        Epic epic1 = new Epic("Задачи на день", "--", 100, List.of(subTask15, subTask16), TIME_12, 0);
        taskManager.addTask(epic1);
        taskManager.addTask(subTask15);
        taskManager.addTask(subTask16);
        assertEquals(TaskStatus.NEW, taskManager.setStatus(100, TaskStatus.DONE).getStatus());
    }

    @Test
    @DisplayName("Нельзя изменить статус эпика")
    public void canNotSetStatusToEpic() {
        SubTask subTask16nn = new SubTask("стирка", "--", 50, 101, TIME_16, HOUR);
        Epic epic2 = new Epic("Задачи на день", "--", 101, List.of(subTask16nn), TIME_12, 0);
        taskManager.addTask(epic2);
        taskManager.addTask(subTask16nn);
        assertEquals(TaskStatus.NEW, taskManager.setStatus(101, TaskStatus.DONE).getStatus());
    }

    @Test
    @DisplayName("Статус задачи меняется на заданный")
    public void canSetTasksStatus() {
        taskManager.addTask(TASK_12);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.setStatus(TASK_12.getIdentificationNumber(), TaskStatus.IN_PROGRESS).getStatus());
    }

    @Test
    @DisplayName("Статус подзадачи меняется на заданный")
    public void canSetSubTasksStatus() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.setStatus(TASK_13_30.getIdentificationNumber(), TaskStatus.IN_PROGRESS).getStatus());
    }

    @Test
    @DisplayName("Статус эпика меняется на в процессе если у одной из зподзадач статус в процессе")
    public void canSetEpicStatusInProgress() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);
        taskManager.setStatus(TASK_13_30.getIdentificationNumber(), TaskStatus.IN_PROGRESS);
         assertEquals(TaskStatus.IN_PROGRESS, EPIC_FOR_DAY.getStatus());
    }

    @Test
    @DisplayName("Статус эпика не меняется если у одной из подзадач статус завершена")
    public void canNotSetEpicStatusDone() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);
        taskManager.setStatus(TASK_13_30.getIdentificationNumber(), TaskStatus.IN_PROGRESS);
        taskManager.setStatus(TASK_15.getIdentificationNumber(), TaskStatus.DONE);

        assertEquals(TaskStatus.IN_PROGRESS, EPIC_FOR_DAY.getStatus());
    }

    @Test
    @DisplayName("Статус эпика меняется на завершенный если у обеих подзадач статус завершена")
    public void canSetEpicStatusDone() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);
        taskManager.setStatus(TASK_13_30.getIdentificationNumber(), TaskStatus.DONE);
        taskManager.setStatus(TASK_15.getIdentificationNumber(), TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, EPIC_FOR_DAY.getStatus());
    }

    @Test
    @DisplayName("Статус эпика меняется на в процессе если у одной из подзадач статус новая а у другой завершена")
    public void canSetEpicStatusInProgressIfSubtaskNewEndDone() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);
        taskManager.setStatus(TASK_13_30.getIdentificationNumber(), TaskStatus.NEW);
        taskManager.setStatus(TASK_15.getIdentificationNumber(), TaskStatus.NEW);
        taskManager.setStatus(TASK_15.getIdentificationNumber(), TaskStatus.DONE);

        assertEquals(TaskStatus.IN_PROGRESS, EPIC_FOR_DAY.getStatus());
    }

    @Test
    @DisplayName("Задача корректно удаляется из списка")
    public void taskIsRemove() {
        int size = taskManager.getHistory().size();
        taskManager.addTask(TASK_12);
        taskManager.removeTask(TASK_12.getIdentificationNumber());
        int result = taskManager.getHistory().size();

        assertEquals(size, result);
    }

    @Test
    @DisplayName("Можно получить список задач по типам")
    public  void canGetTasksByType() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_12);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);

        assertEquals(TaskType.TASK, taskManager.getTaskListByType(TASK_12.getTaskType()).stream().findFirst().get().getTaskType());
        assertEquals(TaskType.SUB_TASK, taskManager.getTaskListByType(TASK_13_30.getTaskType()).stream().findFirst().get().getTaskType());
        assertEquals(TaskType.EPIC, taskManager.getTaskListByType(EPIC_FOR_DAY.getTaskType()).stream().findFirst().get().getTaskType());
    }

    @Test
    @DisplayName("Можно удалить из списка задачи по типам")
    public  void canRemoveTasksByType() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_12);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);

        taskManager.removeAllTasksByType(TaskType.TASK);
        int count = taskManager.getTaskListByType(TaskType.TASK).size();
        assertEquals(0, count);

        taskManager.removeAllTasksByType(TaskType.SUB_TASK);
        int count2 = taskManager.getTaskListByType(TaskType.SUB_TASK).size();
        assertEquals(0, count2);

        taskManager.removeAllTasksByType(TaskType.EPIC);
        int count3 = taskManager.getTaskListByType(TaskType.EPIC).size();
        assertEquals(0, count3);
    }

    @Test
    @DisplayName("Можно получить историю просмотров из одной задачи если в списке одна задача")
    public void canGetOneTask() {
        taskManager.addTask(TASK_12);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    @DisplayName("Можно получить корректный список подзадач по номеру эпика")
    public void canGetEpicSubtasks() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);

        int size = taskManager.getEpicSubtasks(EPIC_ID).size();
        assertEquals(2, size);
    }

    @Test
    @DisplayName("Нельзя получить корректный список подзадач по не верному номеру эпика")
    public void canNotGetEpicSubtasks() {
        taskManager.addTask(EPIC_FOR_DAY);
        taskManager.addTask(TASK_13_30);
        taskManager.addTask(TASK_15);

        assertNull(taskManager.getEpicSubtasks(2000));
    }

}