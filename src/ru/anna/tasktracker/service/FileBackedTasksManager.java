package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.Task;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;
import ru.anna.tasktracker.utils.CSVFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    CSVFormatter csvFormatter = new CSVFormatter();
    private final File backupFile;

    public FileBackedTasksManager(File file) {
        backupFile = file;
        if (backupFile == null) {
            throw new RuntimeException("Нельзя восстановить состояние. Переданный файл - null");
        } else if (backupFile.isDirectory()) {
            throw new RuntimeException("Нельзя восстановить состояние. Переданный файл - директория");
        }
        try {
            if (!backupFile.exists()) {
                backupFile.createNewFile();
            }
            restore(); //восстанавление
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void restore() {
        try (FileReader fileReader = new FileReader(backupFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String taskString = bufferedReader.readLine();
                if (!taskString.isEmpty()) {
                    Task parseTask = csvFormatter.parseTask(taskString);
                    taskStore.saveTask(parseTask);
                } else {
                    List<Integer> idTasks = csvFormatter.parseHistory(bufferedReader.readLine());
                    for (Integer idTask : idTasks) {
                        getTaskById(idTask);
                    }
                    return;
                }
            }
        } catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                    String text = csvFormatter.formatTaskToString(task);
                    writer.println(text);
                }
            }
            List<Task> history = historyStore.getHistory();
            List<Integer> idTasks = new ArrayList<>();
            for (Task task : history) {
                idTasks.add(task.getIdentificationNumber());
            }
            String textId = csvFormatter.formatHistory(idTasks);
            writer.println();
            writer.println(textId);
        } catch (NullPointerException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
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
