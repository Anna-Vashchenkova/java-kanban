package service;

import model.*;
import store.InMemoryTaskStore;

import java.util.ArrayList;
import java.util.Collection;

public class InMemoryTaskManager implements TaskManager {
    InMemoryTaskStore inMemoryTaskStore = new InMemoryTaskStore();
    int lastId = 0;

    @Override
    public void addTask(Task task) {
        switch (task.getTaskType()) {
            case SUB_TASK:
                SubTask st = (SubTask) task;
                Task taskById = inMemoryTaskStore.getTaskById(st.getParentEpicId());
                if ((taskById == null)||(taskById.getTaskType() != TaskType.EPIC)) {
                    System.out.println("Эпика с таким номером нет.");
                    return;
                }
                ((Epic)taskById).addSubTask(st);
                inMemoryTaskStore.saveTask(taskById);
            case EPIC:
            case TASK:
                inMemoryTaskStore.saveTask(task);
                System.out.println("Задача " + task.getTitle() + " успешно добавлена. Её номер - "
                        + task.getIdentificationNumber());
        }
    }


    @Override
    public Task getTaskById(int taskId) {
        Task task = inMemoryTaskStore.getTaskById(taskId);
        return task;
    }

    @Override
    public void removeTask(int taskId) {
        inMemoryTaskStore.removeTask(taskId);
    }

    @Override
    public void setStatus(int taskId, TaskStatus status) {
         Task task = inMemoryTaskStore.getTaskById(taskId);
         if (task == null) {
             System.out.println("Задачи с таким идентификатором нет.");
             return;
         }
         if (task.getTaskType() == TaskType.EPIC) {
             System.out.println("Невозможно изменить статус эпика.");
         } else  if (task.getTaskType() == TaskType.TASK) {
             task.setStatus(status);
             inMemoryTaskStore.saveTask(task);
             System.out.println("Статус задачи " + taskId + " успешно изменён на " + status);
         } else if (task.getTaskType() == TaskType.SUB_TASK) {
             task.setStatus(status);
             inMemoryTaskStore.saveTask(task);
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
        ArrayList<SubTask> subTasksList = ((Epic)taskById).getSubTasks();
        if (subTasksList.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (SubTask subTask : subTasksList) {
                if (subTask.getStatus() == TaskStatus.NEW) {
                    newCount++;
                } else  if (subTask.getStatus() == TaskStatus.DONE) {
                    doneCount++;
                }
            }
            if (newCount == subTasksList.size()) {
                taskById.setStatus(TaskStatus.NEW);
                System.out.println("Статус эпика " + epicId + " успешно изменён на " + taskById.getTaskType());
            }
            if (doneCount == subTasksList.size()) {
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
        Collection<Task> taskList = inMemoryTaskStore.getAllTasksByType(taskType);
        if (taskList.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (Task task1 : taskList) {
                System.out.println(task1);
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
        for (Task task : inMemoryTaskStore.getAllTasksByType(taskType)) {
            inMemoryTaskStore.removeTask(task.getIdentificationNumber());
            count++;
        }
        return  count;
    }

    @Override
    public void updateTask(Task task) {
        inMemoryTaskStore.saveTask(task);
    }

    @Override
    public void printSubTasks(int epicId) {
        Task taskById = getTaskById(epicId);
        if ((taskById == null) || (taskById.getTaskType() != TaskType.EPIC)) {
            System.out.println("Эпика с таким номером нет.");
            return;
        }
        ArrayList<SubTask> subTasksList = ((Epic)taskById).getSubTasks();
        if (subTasksList.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (SubTask subTask : subTasksList) {
                System.out.println(subTask);
            }
        }
    }
}
