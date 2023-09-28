package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.*;
import ru.anna.tasktracker.store.InMemoryTaskStore;
import ru.anna.tasktracker.store.TaskStore;
import ru.anna.tasktracker.utils.Managers;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected final TaskStore taskStore = new InMemoryTaskStore();
    protected final HistoryManager historyStore = Managers.getDefaultHistoryStore();
    protected int lastId = 0;
    @Override
    public Task addTask(Task task) {
        if (!canAddTask(task)) {
            return  null;
        }
        if (task.getTaskType() == TaskType.SUB_TASK) {
            SubTask subTask = (SubTask) task;
            Task taskById = getTaskById(subTask.getParentEpicId());
            if ((taskById == null) || (taskById.getTaskType() != TaskType.EPIC)) {
                System.out.println("Эпика с таким номером нет.");
                return null;
            }
            ((Epic) taskById).addSubTask(subTask);
            taskStore.saveTask(taskById);
        }
            taskStore.saveTask(task);
            System.out.println("Задача " + task.getTitle() + " успешно добавлена. Её номер - "
                        + task.getIdentificationNumber());
            return task;
    }

    private boolean canAddTask(Task task) {
        TreeSet<Task> orderedByTimeTasks = taskStore.getOrderedByTimeTasks();
        if (!orderedByTimeTasks.isEmpty()) {
            Task first = orderedByTimeTasks.first();
            if ((task.getStartTime().isAfter(first.getStartTime()))&&(task.getStartTime().isBefore(first.getEndTime()))) {
                System.out.println("Задача не добавлена.");
                return false;
            } else if ((task.getStartTime().isBefore(first.getStartTime()))&&(task.getEndTime().isAfter(first.getStartTime()))) {
                System.out.println("Задача не добавлена.");
                return false;
            } else if ((task.getStartTime().isBefore(first.getStartTime()))&&(task.getEndTime().isBefore(first.getStartTime()))) {
                return true;
            }
        }
        return true;
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
        historyStore.remove(taskId);
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
             SubTask subTask = (SubTask) task;
             changeEpicStatus(subTask.getParentEpicId());
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
        Collection<SubTask> subTasks = ((Epic)taskById).getSubTasks();
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
    public Collection<Task> getTaskListByType(TaskType taskType) {
        Collection<Task> tasks = taskStore.getAllTasksByType(taskType);
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (Task task1 : tasks) {
                System.out.println(task1);
                historyStore.addTaskToHistory(task1);
            }
        }
        return tasks;
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
        return count;
    }

    @Override
    public void updateTask(Task task) {
        taskStore.saveTask(task);
    }

    @Override
    public Collection<SubTask> getEpicSubtasks(int epicId) {
        Task taskById = getTaskById(epicId);
        if ((taskById == null) || (taskById.getTaskType() != TaskType.EPIC)) {
            System.out.println("Эпика с таким номером нет.");
            return null;
        }
        Collection<SubTask> subTasks = ((Epic)taskById).getSubTasks();
        if (subTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (SubTask subTask : subTasks) {
                System.out.println(subTask);
            }
        }
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> printHistoryStore = historyStore.getHistory();
        if (printHistoryStore.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : printHistoryStore) {
                System.out.println(task.toString());
            }
        }
        return printHistoryStore;
    }
}
