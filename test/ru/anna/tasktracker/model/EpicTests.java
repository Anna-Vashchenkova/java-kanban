package ru.anna.tasktracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTests {

    @Test
    @DisplayName("Дата начала эпика равна дате переданной в конструктор, если список подзадач пустой")
    public  void startTimeEqualsTimeFromConstructor(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);

        assertEquals(startTime, epic.getStartTime());
    }

    @Test
    @DisplayName("Длительность эпика равна 0, при любом значении переданном в конструктор, если список подзадач пустой")
    public  void durationEquals0ByEmptyListSubTasks(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 500);

        assertEquals(0, epic.getDuration());
    }

    @Test
    @DisplayName("Дата начала эпика равна дате подзадачи, если список подзадач состоит из одной подзадачи")
    public  void startTimeEqualsTimeOfSubTask(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime = LocalDateTime.of(2023, 9, 20, 11, 0);

        ArrayList<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime, 0);
        subTasks.add(subTask1);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);

        assertEquals(subTasksStartTime, epic.getStartTime());
    }

    @Test
    @DisplayName("Длиельность эпика равна длительностт подзадачи, если список подзадач состоит из одной задачи")
    public  void durationEqualsSubTaskDuration(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime = LocalDateTime.of(2023, 9, 20, 11, 0);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime, 500);
        subTasks.add(subTask1);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);

        assertEquals(500, epic.getDuration());
    }

    @Test
    @DisplayName("Время заврешения эпика равно времени заверщшения подзадачи, если список подзадач состоит из одной задачи")
    public  void epicEndTimeEqualsSubtaskEndTime(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime = LocalDateTime.of(2023, 9, 20, 11, 30);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime, 60);
        subTasks.add(subTask1);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);
        LocalDateTime subTasksEndTime = LocalDateTime.of(2023, 9, 20, 12, 30);

        assertEquals(subTasksEndTime, epic.getEndTime());
    }

    @Test
    @DisplayName("Дата начала эпика равна дате начала самой ранней подзадачи, если список подзадач состоит из нескольких подзадач")
    public  void epicStartTimeEqualsSubtaskStartTime(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime1 = LocalDateTime.of(2023, 9, 20, 11, 30);
        LocalDateTime subTasksStartTime2 = LocalDateTime.of(2023, 9, 20, 10, 15);
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime1, 60);
        SubTask subTask2 = new SubTask("стены", "возвести", 3, 1, subTasksStartTime2, 60);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);

        assertEquals(subTasksStartTime2, epic.getStartTime());
    }

    @Test
    @DisplayName("Длиельность эпика равна сумме длительностей подзадач, если список подзадач состоит из нескольких подзадач")
    public  void durationEqualsSumSubTaskDuration(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime = LocalDateTime.of(2023, 9, 20, 11, 30);
        LocalDateTime subTasksStartTime2 = LocalDateTime.of(2023, 9, 20, 10, 15);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime, 60);
        SubTask subTask2 = new SubTask("стены", "возвести", 3, 1, subTasksStartTime2, 60);
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);

        assertEquals(120, epic.getDuration());
    }

    @Test
    @DisplayName("Время заврешения эпика равно времени заверщшения самой поздней подзадачи, если список подзадач состоит из нескольких подзадач")
    public  void epicEndTimeEqualsLastSubtaskEndTime(){
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 20, 10, 0);
        LocalDateTime subTasksStartTime1 = LocalDateTime.of(2023, 9, 20, 11, 30);
        LocalDateTime subTasksStartTime2 = LocalDateTime.of(2023, 9, 20, 10, 15);
        SubTask subTask1 = new SubTask("фундамент", "вырыть залить", 2, 1, subTasksStartTime1, 60);
        SubTask subTask2 = new SubTask("стены", "возвести", 3, 1, subTasksStartTime2, 60);
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        Epic epic = new Epic("строю дом", "этапы строительства", 1, subTasks, startTime, 0);
        LocalDateTime lastSubTaskEndTime = LocalDateTime.of(2023, 9, 20, 12, 30);

        assertEquals(lastSubTaskEndTime, epic.getEndTime());
    }

}