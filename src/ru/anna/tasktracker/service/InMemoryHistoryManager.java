package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList newHistoryTasks= new CustomLinkedList();

    @Override
    public void addTaskToHistory(Task task) {
        if (task == null) {
            return;
        }
        newHistoryTasks.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return newHistoryTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        newHistoryTasks.removeById(id);
    }

}
