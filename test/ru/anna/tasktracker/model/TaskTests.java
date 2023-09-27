package ru.anna.tasktracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTests {

    private static final LocalDateTime START_TIME = LocalDateTime.of(2023, 9, 20, 10, 0);

    @Test
    @DisplayName("Время завершения задачи должно совпадать со стартом, если длительность выполнения равна 0")
    public  void endTimeEqualsStartTimeByDuration0(){
        Task task = new Task(
                "прочитать книгу", "отдых", 1,
                START_TIME, 0);
        assertEquals(START_TIME,task.getEndTime());
    }

    @Test
    @DisplayName("Время завершения задачи должно быть через 10 минут после начала, если длительность задачи равна 10 минутам")
    public  void startTimeTo10MinutesByDuration10(){
        Task task = new Task(
                "прочитать книгу", "отдых", 1,
                START_TIME, 10);
        LocalDateTime end = LocalDateTime.of(2023, 9, 20,10, 10);
        assertEquals(end, task.getEndTime());
    }
}