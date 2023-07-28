package logic;

import model.*;
import store.TaskStore;

import java.util.ArrayList;
import java.util.Collection;

public class TaskManager {
    TaskStore taskStore = new TaskStore();
    int lastId = 0;

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


    public Task getTaskById(int taskId) {
        Task task = taskStore.getTaskById(taskId);
        return task;
    }

    public void removeTask(int taskId) {
        taskStore.removeTask(taskId);
    }

    public void setStatus(int taskId, TaskStatus status) {
         Task task = taskStore.getTaskById(taskId);
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

    public void printTaskList(TaskType taskType) {
        Collection<Task> taskList = taskStore.getAllTasksByType(taskType);
        if (taskList.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            for (Task task1 : taskList) {
                System.out.println(task1);
            }
        }
    }

    public int generateIdNumber() {
        lastId++;
        return lastId;
    }

    public int removeAllTasksByType(TaskType taskType) {
        int count = 0;
        for (Task task : taskStore.getAllTasksByType(taskType)) {
            taskStore.removeTask(task.getIdentificationNumber());
            count++;
        }
        return  count;
    }

    public void updateTask(Task task) {
        taskStore.saveTask(task);
    }

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
