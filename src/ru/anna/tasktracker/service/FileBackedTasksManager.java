package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;

import java.io.File;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public FileBackedTasksManager(File file) {
        restore(); //восстанавление
    }

    private void restore() {

    }

    private void save() {

    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task resultTask = super.getTaskById(taskId);
        save();
        return resultTask;
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void setStatus(int taskId, TaskStatus status) {
        super.setStatus(taskId, status);
        save();
    }

    @Override
    public void printTaskList(TaskType taskType) {
        super.printTaskList(taskType);
        save();
    }

    @Override
    public int removeAllTasksByType(TaskType taskType) {
        int countTask = super.removeAllTasksByType(taskType);
        save();
        return countTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void printSubTasks(int epicId) {
        super.printSubTasks(epicId);
        save();
    }

    @Override
    public void printHistory() {
        super.printHistory();
        save();
    }
}
