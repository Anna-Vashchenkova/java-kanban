package service;

import model.*;
import store.HistoryManager;
import store.TaskStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final TaskStore taskStore;
    private final HistoryManager historyStore;
    private int lastId = 0;

    public InMemoryTaskManager(TaskStore taskStore, HistoryManager historyStore) {
        this.taskStore = taskStore;
        this.historyStore = historyStore;
    }

    @Override
    public void addTask(Task task) {
        switch (task.getTaskType()) {
            case SUB_TASK:
                SubTask st = (SubTask) task;
                Task taskById = taskStore.getTaskById(st.getParentEpicId());
                if ((taskById == null)||(taskById.getTaskType() != TaskType.EPIC)) {
                    System.out.println("Эпика с таким номером нет.");
                    return;
                }
                ((Epic)taskById).addSubTask(st);
                taskStore.saveTask(taskById);
            case EPIC:
            case TASK:
                taskStore.saveTask(task);
                System.out.println("Задача " + task.getTitle() + " успешно добавлена. Её номер - "
                        + task.getIdentificationNumber());
        }
    }


    @Override
    public Task getTaskById(int taskId) {
        Task task = taskStore.getTaskById(taskId);
        historyStore.addTaskToHistory(task);
        return task;
    }

    @Override
    public void removeTask(int taskId) {
        taskStore.removeTask(taskId);
    }

    @Override
    public void setStatus(int taskId, TaskStatus status) {
         Task task = getTaskById(taskId);
         if (task == null) {
             System.out.println("Задачи с таким идентификатором нет.");
             return;
         }
         if (task.getTaskType() == TaskType.EPIC) {
             System.out.println("Невозможно изменить статус эпика.");
         } else  if (task.getTaskType() == TaskType.TASK) {
             task.setStatus(status);
             taskStore.saveTask(task);
             System.out.println("Статус задачи " + taskId + " успешно изменён на " + status);
         } else if (task.getTaskType() == TaskType.SUB_TASK) {
             task.setStatus(status);
             taskStore.saveTask(task);
             System.out.println("Статус подзадачи " + taskId + " успешно изменён на " + status);
             SubTask st = (SubTask) task;
             changeEpicStatus(st.getParentEpicId());
         }
    }

    private void changeEpicStatus(int epicId) {
        int newCount = 0;
        int doneCount = 0;
        Task taskById = getTaskById(epicId);
        if ((taskById == null) || (taskById.getTaskType() != TaskType.EPIC)) {
            System.out.println("Эпика с таким номером нет.");
            return;
        }
        ArrayList<SubTask> subTasks = ((Epic)taskById).getSubTasks();
        if (subTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (SubTask subTask : subTasks) {
                if (subTask.getStatus() == TaskStatus.NEW) {
                    newCount++;
                } else  if (subTask.getStatus() == TaskStatus.DONE) {
                    doneCount++;
                }
            }
            if (newCount == subTasks.size()) {
                taskById.setStatus(TaskStatus.NEW);
                System.out.println("Статус эпика " + epicId + " успешно изменён на " + taskById.getTaskType());
            }
            if (doneCount == subTasks.size()) {
                taskById.setStatus(TaskStatus.DONE);
                System.out.println("Статус эпика " + epicId + " успешно изменён на " + taskById.getTaskType());
            } else {
                taskById.setStatus(TaskStatus.IN_PROGRESS);
                System.out.println("Статус эпика " + epicId + " успешно изменён на " + taskById.getTaskType());
            }
        }
    }

    @Override
    public void printTaskList(TaskType taskType) {
        Collection<Task> tasks = taskStore.getAllTasksByType(taskType);
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (Task task1 : tasks) {
                System.out.println(task1);
                historyStore.addTaskToHistory(task1);
            }
        }
    }

    @Override
    public int generateIdNumber() {
        lastId++;
        return lastId;
    }

    @Override
    public int removeAllTasksByType(TaskType taskType) {
        int count = 0;
        for (Task task : taskStore.getAllTasksByType(taskType)) {
            taskStore.removeTask(task.getIdentificationNumber());
            count++;
        }
        return  count;
    }

    @Override
    public void updateTask(Task task) {
        taskStore.saveTask(task);
    }

    @Override
    public void printSubTasks(int epicId) {
        Task taskById = getTaskById(epicId);
        if ((taskById == null) || (taskById.getTaskType() != TaskType.EPIC)) {
            System.out.println("Эпика с таким номером нет.");
            return;
        }
        ArrayList<SubTask> subTasks = ((Epic)taskById).getSubTasks();
        if (subTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (SubTask subTask : subTasks) {
                System.out.println(subTask);
            }
        }
    }

    @Override
    public void getHistory() {
        List<Task> printHistoryStore = historyStore.getHistory();
        if (printHistoryStore.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : printHistoryStore) {
                System.out.println(task.toString());
            }
        }
    }
}
