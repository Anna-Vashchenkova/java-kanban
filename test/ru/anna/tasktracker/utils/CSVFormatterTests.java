package ru.anna.tasktracker.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.model.Epic;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.anna.tasktracker.model.TaskStatus.*;

public class CSVFormatterTests {

    private final Epic ETALON_EPIC = new Epic("Дом", "Уборка дома", 5, new ArrayList<>(), null, 0);
    private final Task ETALON_TASK = new Task("Дом", "Уборка дома", 8, null, 0);
    private final SubTask ETALON_SUBTASK = new SubTask("Дом", "Уборка дома", 23, 2, null, 0);

    @BeforeEach
    public void initTasks() {
        ETALON_EPIC.setStatus(DONE);
        ETALON_TASK.setStatus(IN_PROGRESS);
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа TASK")
    public void formatTask() {
        String etalon = "TASK,Дом,Уборка дома,8,IN_PROGRESS";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_TASK));
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа EPIC")
    public void formatEpic() {
        String etalon = "EPIC,Дом,Уборка дома,5,DONE";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_EPIC));
    }

    @Test
    @DisplayName("Необходимо корректно формировать строку csv если передана задача типа SUB_TASK")
    public void formatSubTask() {
        String etalon = "SUB_TASK,Дом,Уборка дома,23,NEW,2";
        assertEquals(etalon, CSVFormatter.formatTaskToString(ETALON_SUBTASK));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа TASK")
    public void parseTask() {
        String str = "TASK,Дом,Уборка дома,8,IN_PROGRESS";
        assertEquals(ETALON_TASK, CSVFormatter.parseTask(str));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа SUB_TASK")
    public void parseSubTask() {
        String str = "SUB_TASK,Дом,Уборка дома,23,NEW,2";
        assertEquals(ETALON_SUBTASK, CSVFormatter.parseTask(str));
    }

    @Test
    @DisplayName("Необходимо корректно разобрать строку в объект типа EPIC")
    public void parseEpic() {
        String str = "EPIC,Дом,Уборка дома,5,DONE";
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
