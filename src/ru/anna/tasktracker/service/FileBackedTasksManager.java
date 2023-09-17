package ru.anna.tasktracker.service;

import ru.anna.tasktracker.exception.ManagerSaveException;
import ru.anna.tasktracker.model.SubTask;
import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;
import ru.anna.tasktracker.utils.CSVFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File backupFile;

    public FileBackedTasksManager(File file) {
        backupFile = file;
        restore();
    }

    public void restore() {
        if (backupFile == null) {
            throw new ManagerSaveException("Нельзя восстановить состояние. Переданный файл - null");
        } else if (backupFile.isDirectory()) {
            throw new ManagerSaveException("Нельзя восстановить состояние. Переданный файл - директория");
        }
        try {
            if (!backupFile.exists()) {
                backupFile.createNewFile();
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
            reader.readLine();
            while (reader.ready()) {
                String taskString = reader.readLine();
                if (!taskString.isEmpty()) {
                    Task parseTask = CSVFormatter.parseTask(taskString);
                    if (parseTask.getIdentificationNumber() >= lastId) {
                        lastId = parseTask.getIdentificationNumber();
                    }
                    taskStore.saveTask(parseTask);
                } else {
                    List<Integer> idTasks = CSVFormatter.parseHistory(reader.readLine());
                    for (Integer idTask : idTasks) {
                        getTaskById(idTask);
                    }
                    return;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter(backupFile);
        ) {
            writer.println("type,name,description,id,status,epic");
            for (int i = 0; i < TaskType.values().length; i++) {
                TaskType taskType = TaskType.values()[i];
                Collection<Task> tasks = taskStore.getAllTasksByType(taskType);
                for (Task task : tasks) {
                    String text = CSVFormatter.formatTaskToString(task);
                    writer.println(text);
                }
            }
            List<Task> history = historyStore.getHistory();
            List<Integer> idTasks = new ArrayList<>();
            for (Task task : history) {
                idTasks.add(task.getIdentificationNumber());
            }
            String textId = CSVFormatter.formatHistory(idTasks);
            writer.println();
            writer.println(textId);
        } catch (NullPointerException e) {
            System.out.printf("Произошла ошибка" + e.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

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
        //Task task = getTaskById(taskId);
        super.removeTask(taskId);
        save();
    }

    @Override
    public void setStatus(int taskId, TaskStatus status) {
        super.setStatus(taskId, status);
        save();
    }

    @Override
    public Collection<Task> getTaskListByType(TaskType taskType) {
        Collection<Task> tasks = super.getTaskListByType(taskType);
        save();
        return tasks;
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
    public List<SubTask> getEpicSubtasks(int epicId) {
        List<SubTask> subTasks = super.getEpicSubtasks(epicId);
        save();
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> printHistoryStore = super.getHistory();
        save();
        return  printHistoryStore;
    }
}
