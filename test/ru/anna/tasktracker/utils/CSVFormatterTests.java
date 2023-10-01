package ru.anna.tasktracker.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.Epic;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.anna.tasktracker.model.TaskStatus.*;

public class CSVFormatterTests {

    private static LocalDateTime TIME_12 = LocalDateTime.of(2023, 1, 1, 12, 0);
    private static LocalDateTime TIME_13_30 = LocalDateTime.of(2023, 1, 1, 13, 30);
    private static LocalDateTime TIME_15 = LocalDateTime.of(2023, 1, 1, 15, 0);
    public static final int HOUR = 59;
    public static final int HALF_HOUR = 30;
    private final Epic ETALON_EPIC = new Epic("Дом", "Уборка дома", 5, new ArrayList<>(), TIME_12, 0);
    private final Task ETALON_TASK = new Task("Дом", "Уборка дома", 8, TIME_13_30, HALF_HOUR);
    private final SubTask ETALON_SUBTASK = new SubTask("Дом", "Уборка дома", 23, 2, TIME_15, HOUR);

    @BeforeEach
    public void initTasks() {
        ETALON_EPIC.setStatus(DONE);
        ETALON_TASK.setStatus(IN_PROGRESS);
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа TASK")
    public void formatTask() {
        String etalon = "TASK,Дом,Уборка дома,8,IN_PROGRESS,2023.01.01 13:30,30";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_TASK));
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа EPIC")
    public void formatEpic() {
        String etalon = "EPIC,Дом,Уборка дома,5,DONE,2023.01.01 12:00,0";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_EPIC));
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа SUB_TASK")
    public void formatSubTask() {
        String etalon = "SUB_TASK,Дом,Уборка дома,23,NEW,2023.01.01 15:00,59,2";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_SUBTASK));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа TASK")
    public void parseTask() {
        String str = "TASK,Дом,Уборка дома,8,IN_PROGRESS,2023.01.01 13:30,30";
        assertEquals(ETALON_TASK, CSVFormatter.parseTask(str));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа SUB_TASK")
    public void parseSubTask() {
        String str = "SUB_TASK,Дом,Уборка дома,23,NEW,2023.01.01 15:00,59,2";
        assertEquals(ETALON_SUBTASK, CSVFormatter.parseTask(str));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа EPIC")
    public void parseEpic() {
        String str = "EPIC,Дом,Уборка дома,5,DONE,2023.01.01 12:00,0";
        Task actual = CSVFormatter.parseTask(str);
        assertEquals(ETALON_EPIC, actual);
    }

    @Test
    @DisplayName("Необходимо сохранять пустую строку при сохранении пустого списка задач, если история пустая")
    public void shouldFormatEmptyHistory() {
        String etalon = "";
        String str = CSVFormatter.formatHistory(List.of());

        assertEquals(etalon, str);
    }

    @Test
    @DisplayName("Необходимо вернуть строку из 3х чисел, если переданы три числа")
    public void shouldFormatNotEmptyHistory() {
        String etalon = "5,8,23";
        String str = CSVFormatter.formatHistory(List.of(5, 8, 23));

        assertEquals(etalon, str);
    }

    @Test
    @DisplayName("Необходимо возвроащать пустой список, если пришла пустая строка на вход")
    public void shouldParseEmptyHistory() {
        String str = "";
        List<Integer> result = CSVFormatter.parseHistory(str);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Необходимо вернуть список из 3 чисел, если входная строка имеет 3 числа")
    public void shouldParseNotEmptyHistory() {
        String str = "5,8,23";
        List<Integer> result = CSVFormatter.parseHistory(str);

        assertEquals(3, result.size());
    }
}
