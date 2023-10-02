package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.service.CustomLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    private static CustomLinkedList emptyList = new CustomLinkedList();
    private static final Task task1 = new Task("Название_1", "Описание_1", 1, null, 0);
    private static final Task task2 = new Task("Название_2", "Описание_2", 2, null, 0);
    private static final Task task3 = new Task("Название_3", "Описание_3", 3, null, 0);
    private static final Task task4 = new Task("Название_4", "Описание_4", 4, null, 0);
    private static CustomLinkedList filledList = new CustomLinkedList();

    @BeforeEach
    public void initContext() {
        emptyList = new CustomLinkedList();
        filledList = new CustomLinkedList() {{
            linkLast(task1);
            linkLast(task2);
            linkLast(task3);
            linkLast(task4);
        }};
    }

    @Test
    @DisplayName("Необходимо вернуть пустой массив, если в списке нет ни одного элемента")
    public void shouldReturnEmptyList(){
        var tasks = emptyList.getTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("Необходимо возвращать список из одного элемента, если в пустой список добавили одну задачу")
    public void shouldReturnListWithSingleTask(){
        emptyList.linkLast(task1);

        var tasks = emptyList.getTasks();
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Необходимо корректно обрабатывать операцию удаления по несуществующему ID")
    public void removeByWrongId(){
        emptyList.removeById(0);
    }
    @Test
    @DisplayName("Если задача была ранее в истории и добавлеа снова, задача должна возвращаться последенецй в списке")
    public void shouldReturnTaskInLastPlace() {
        filledList.linkLast(task2);
        var tasks = filledList.getTasks();

        Task task = tasks.get(tasks.size() - 1);
        assertAll(
                () -> assertEquals(task2, task),
                () -> assertEquals(4, tasks.size())
        );
    }
    @Test
    @DisplayName("Необходимо удалить элемент из списка, когда передан ID сужествующей задачи")
    public void shouldRemoveTask(){
        var initialSize = filledList.getTasks().size();
        filledList.removeById(task2.getIdentificationNumber());
        assertEquals(initialSize -1, filledList.getTasks().size());
    }
    @Test
    @DisplayName("Необходимо корректно обрабатывать повторное уждаление элемента из списка")
    public void shouldRemoveTaskTwice(){
        var initialSize = filledList.getTasks().size();
        filledList.removeById(task2.getIdentificationNumber());
        filledList.removeById(task2.getIdentificationNumber());
        assertEquals(initialSize -1, filledList.getTasks().size());
    }
}