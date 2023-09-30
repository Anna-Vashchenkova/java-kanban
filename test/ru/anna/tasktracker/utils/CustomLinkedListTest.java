package ru.anna.tasktracker.utils;

import ru.anna.tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.anna.tasktracker.service.CustomLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    private CustomLinkedList EMPTY_LIST = new CustomLinkedList();
    private final Task task1 = new Task("Название_1", "Описание_1", 1, null, 0);
    private final Task task2 = new Task("Название_2", "Описание_2", 2, null, 0);
    private final Task task3 = new Task("Название_3", "Описание_3", 3, null, 0);
    private final Task task4 = new Task("Название_4", "Описание_4", 4, null, 0);
    private CustomLinkedList FILLED_LIST = new CustomLinkedList();

    @BeforeEach
    public void reinit() {
        EMPTY_LIST = new CustomLinkedList();
        FILLED_LIST = new CustomLinkedList() {{
            linkLast(task1);
            linkLast(task2);
            linkLast(task3);
            linkLast(task4);
        }};
    }

    @Test
    @DisplayName("Необходимо вернуть пустой массив, если в списке нет ни одного элемента")
    public void shouldReturnEmptyList(){
        var tasks = EMPTY_LIST.getTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("Необходимо возвращать список из одного элемента, если в пустой список добавили одну задачу")
    public void shouldReturnListWithSingleTask(){
        EMPTY_LIST.linkLast(task1);

        var tasks = EMPTY_LIST.getTasks();
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Необходимо корректно обрабатывать операцию удаления по несуществующему ID")
    public void removeByWrongId(){
        EMPTY_LIST.removeById(0);
    }
    @Test
    @DisplayName("Если задача была ранее в истории и добавлеа снова, задача должна возвращаться последенецй в списке")
    public void shouldReturnTaskInLastPlace() {
        FILLED_LIST.linkLast(task2);
        var tasks = FILLED_LIST.getTasks();

        Task task = tasks.get(tasks.size() - 1);
        assertAll(
                () -> assertEquals(task2, task),
                () -> assertEquals(4, tasks.size())
        );
    }
    @Test
    @DisplayName("Необходимо удалить элемент из списка, когда передан ID сужествующей задачи")
    public void shouldRemoveTask(){
        var initialSize = FILLED_LIST.getTasks().size();
        FILLED_LIST.removeById(task2.getIdentificationNumber());
        assertEquals(initialSize -1, FILLED_LIST.getTasks().size());
    }
    @Test
    @DisplayName("Необходимо корректно обрабатывать повторное уждаление элемента из списка")
    public void shouldRemoveTaskTwice(){
        var initialSize = FILLED_LIST.getTasks().size();
        FILLED_LIST.removeById(task2.getIdentificationNumber());
        FILLED_LIST.removeById(task2.getIdentificationNumber());
        assertEquals(initialSize -1, FILLED_LIST.getTasks().size());
    }
}