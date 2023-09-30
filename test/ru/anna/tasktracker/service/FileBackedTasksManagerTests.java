package ru.anna.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.*;
import ru.anna.tasktracker.store.TaskStore;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.anna.tasktracker.model.TaskType.*;

class FileBackedTasksManagerTests {

    private final static File TEST_FILE = new File("test-file.csv");
    private FileBackedTasksManager sut = new FileBackedTasksManager(TEST_FILE);
    private TaskStore ts;
    private HistoryManager historyStore;
    private static LocalDateTime TIME_11 = LocalDateTime.of(2023, 1, 1, 11, 0);
    private static LocalDateTime TIME_12 = LocalDateTime.of(2023, 1, 1, 12, 0);

    private final String ETALON_FILE_STRING = "type,name,description,id,status,startTime,duration,epic" + System.lineSeparator() +
            "TASK,Task1,Description task1,1,NEW,2023.01.01 11:00,45" + System.lineSeparator() +
            "EPIC,Epic2,Description epic2,2,DONE,2023.01.01 12:00,0" + System.lineSeparator() +
            "SUB_TASK,Sub Task2,Description sub task3,3,DONE,2023.01.01 12:00,0,2" + System.lineSeparator() +
            System.lineSeparator() +
            "2,3" + System.lineSeparator()
            ;

    @BeforeEach
    public void clearTest() throws NoSuchFieldException, IllegalAccessException {
        Field taskStoreField = InMemoryTaskManager.class.getDeclaredField("taskStore");
        taskStoreField.setAccessible(true);
        ts = (TaskStore) taskStoreField.get(sut);
        Field historyStoreField = InMemoryTaskManager.class.getDeclaredField("historyStore");
        historyStoreField.setAccessible(true);
        historyStore = (HistoryManager) historyStoreField.get(sut);

        if (TEST_FILE.exists()) {
            TEST_FILE.delete();
        }
    }

    @DisplayName("Необходимо запускать программу с пустой историей и без задач, если файла нет")
    @Test
    public void startWhenFileDoesntExists() {
        sut = new FileBackedTasksManager(TEST_FILE);
        assertAll(
                () -> assertTrue(ts.getAllTasksByType(TASK).isEmpty()),
                () -> assertTrue(ts.getAllTasksByType(TaskType.EPIC).isEmpty()),
                () -> assertTrue(ts.getAllTasksByType(TaskType.SUB_TASK).isEmpty()),
                () -> assertTrue(historyStore.getHistory().isEmpty())
        );
    }

    @DisplayName("Необходимо сохранить три задачи и два элемента истории")
    @Test
    public void shouldSave() throws IOException {
        sut.addTask(new Task("Task1", "Description task1", 1, TIME_11, 45));
        Epic epic = new Epic("Epic2", "Description epic2", 2, new ArrayList<>(), TIME_12, 0);
        epic.setStatus(TaskStatus.DONE);
        sut.addTask(epic);
        SubTask subTask = new SubTask("Sub Task2", "Description sub task3", 3, 2, TIME_12, 0);
        subTask.setStatus(TaskStatus.DONE);
        sut.addTask(subTask);
        sut.getTaskById(2);
        sut.getTaskById(3);


        String text = Files.readString(Paths.get(TEST_FILE.getAbsolutePath()));

        assertEquals(ETALON_FILE_STRING, text);
    }

    @DisplayName("Необходимо восстановить срисок задач при создании FileBackedTasksManager")
    @Test
    public void shouldRestoreTasks() throws IOException {
        try(PrintWriter writer = new PrintWriter(TEST_FILE)){
            writer.print(ETALON_FILE_STRING);
        } catch (IOException ignored) {

        }

        sut.restore();

        Task task = new Task("Task1", "Description task1", 1, TIME_11, 45);
        Epic epic = new Epic("Epic2", "Description epic2", 2, new ArrayList<>(), TIME_12, 0);
        epic.setStatus(TaskStatus.DONE);
        SubTask subTask = new SubTask("Sub Task2", "Description sub task3", 3, 2, TIME_12, 0);
        subTask.setStatus(TaskStatus.DONE);



        Collection<Task> tasks = ts.getAllTasksByType(TASK);
        Collection<Task> epics = ts.getAllTasksByType(EPIC);
        Collection<Task> subTasks = ts.getAllTasksByType(SUB_TASK);
        assertAll(
                () -> assertEquals(task, tasks.stream().findFirst().get()),
                () -> assertEquals(epic, epics.stream().findFirst().get()),
                () -> assertEquals(subTask, subTasks.stream().findFirst().get())
        );
    }

    @DisplayName("Необходимо восстановить историю при создании FileBackedTasksManager")
    @Test
    public void shouldRestoreHistory() throws IOException {
        try(PrintWriter writer = new PrintWriter(TEST_FILE)){
            writer.print(ETALON_FILE_STRING);
        } catch (IOException ignored) {

        }

        sut.restore();

        List<Task> history = historyStore.getHistory();
        assertAll(
                () -> assertEquals(2, history.get(0).getIdentificationNumber()),
                () -> assertEquals(3, history.get(1).getIdentificationNumber())
        );
    }

    @DisplayName("Поле lastId должно равняться максимальному идентификатору прочитанному из файла")
    @Test
    public void updateLastId() throws IOException {
        try(PrintWriter writer = new PrintWriter(TEST_FILE)){
            writer.print(ETALON_FILE_STRING);
        } catch (IOException ignored) {

        }

        sut.restore();

        assertEquals(4, sut.generateIdNumber());
    }

    @DisplayName("Необходимо выбрасывать RuntimeException при чтении файла, если переданный файл null")
    @Test
    public void throwExceprtionWhenFileIsNull(){
        RuntimeException aThrows = assertThrows(
                RuntimeException.class,
                () -> sut = new FileBackedTasksManager(null)
        );
        assertEquals("Нельзя восстановить состояние. Переданный файл - null", aThrows.getMessage());
    }

    @DisplayName("Необходимо выбрасывать RuntimeException при чтении файла, если переданный файл является директорией")
    @Test
    public void throwExceprtionWhenFileIsDirectory(){
        RuntimeException aThrows = assertThrows(
                RuntimeException.class,
                () -> sut = new FileBackedTasksManager(new File("."))
        );
        assertEquals("Нельзя восстановить состояние. Переданный файл - директория", aThrows.getMessage());
    }
}