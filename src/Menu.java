import service.InMemoryTaskManager;
import model.SubTask;
import model.Task;
import model.Epic;
import model.TaskType;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public void printMenu() {
        System.out.println("Что вы хотите сделать? Выбирете команду: ");
        System.out.println("1 - добавить задачу.");
        System.out.println("2 - изменить статус задачи.");
        System.out.println("3 - удалить задачу.");
        System.out.println("4 - показать список задач.");
        System.out.println("5 - удалить все задачи.");
        System.out.println("6 - обновить задачу.");
        System.out.println("7 - показать список всех подзадач для эпика.");
        System.out.println("0 - выход.");
    }

    public void getTaskManager() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String commandString = scanner.nextLine();
            int command = Integer.parseInt(commandString);
            switch (command) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    changeStatus(scanner);
                    break;
                case 3:
                    removeTask(scanner);
                    break;
                case 4:
                    printTasks(scanner);
                    break;
                case 5:
                    removeAllTasks(scanner);
                    break;
                case 6:
                    updateTask(scanner);
                    break;
                case 7:
                    printSubTasks(scanner);
                    break;
                case 0:
                    System.out.println("Выход");
                    scanner.close();
                    return;
                default:
                    System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    private void addTask(Scanner scanner) {
        int taskType = readIntFromScanner(scanner, "Укажите тип задачи: 1 - TASK, 2 - EPIC, 3 - SUBTASK.");
        Task task;
        switch (taskType) {
            case 1:
                task = createTask(scanner);
                break;
            case 2:
                task = createEpic(scanner);
                break;
            case 3:
                task = createSubTask(scanner);
                break;
            default:
                System.out.println("Введенный тип указан не верно.");
                return;
        }
        taskManager.addTask(task);
    }

    private Task createTask(Scanner scanner) {
        System.out.println("Введите название задачи.");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи.");
        String description = scanner.nextLine();
        int identificationNumber = taskManager.generateIdNumber();
        Task task = new Task(title, description, identificationNumber);
        return task;
    }

    private Epic createEpic(Scanner scanner) {
        System.out.println("Введите название эпика.");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпика.");
        String description = scanner.nextLine();
        int identificationNumber = taskManager.generateIdNumber();
        Epic epic = new Epic(title, description, identificationNumber, new ArrayList<>());
        return epic;
    }

    private SubTask createSubTask(Scanner scanner) {
        System.out.println("Введите название подзадачи.");
        String title = scanner.nextLine();
        System.out.println("Введите описание подзадачи.");
        String description = scanner.nextLine();
        int epicId = readIntFromScanner(scanner, "Укажите номер эпика.");
        int identificationNumber = taskManager.generateIdNumber();
        SubTask subTask = new SubTask(title, description, identificationNumber, epicId);
        return subTask;
    }

    private void changeStatus(Scanner scanner) {
        int taskId = readIntFromScanner(scanner, "Введите номер идентификатора задачи: ");
        System.out.println("Введите данные о статусе задачи: NEW, IN_PROGRESS, DONE");
        String statusCommand = scanner.nextLine();
        TaskStatus status = TaskStatus.valueOf(statusCommand);
        taskManager.setStatus(taskId, status);
    }

    private int readIntFromScanner(Scanner scanner, String s) {
        System.out.println(s);
        String taskIdString = scanner.nextLine();
        return Integer.parseInt(taskIdString);
    }

    private void removeTask(Scanner scanner) {
        int taskId = readIntFromScanner(scanner, "Введите номер идентификатора задачи: ");
        if (taskManager.getTaskById(taskId) == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        taskManager.removeTask(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена.");
    }

    private void printTasks(Scanner scanner) {
        TaskType taskType = getTaskType(scanner);
        if (taskType == null) return;
        taskManager.printTaskList(taskType);
    }

    private TaskType getTaskType(Scanner scanner) {
        TaskType taskType;
        int taskTypeNumber = readIntFromScanner(scanner, "Укажите тип задачи: 1 - TASK, 2 - EPIC, 3 - SUBTASK.");
        switch (taskTypeNumber) {
            case 1:
                taskType = TaskType.TASK;
                break;
            case 2:
                taskType = TaskType.EPIC;
                break;
            case 3:
                taskType = TaskType.SUB_TASK;
                break;
            default:
                System.out.println("Введенный тип указан не верно.");
                taskType = null;
        }
        return taskType;
    }

    private void removeAllTasks(Scanner scanner) {
        int taskTypeNumber = readIntFromScanner(scanner, "Укажите тип задач: 1 - TASK, 2 - EPIC, 3 - SUBTASK.");
        TaskType taskType;
        switch (taskTypeNumber) {
            case 1:
                taskType = TaskType.TASK;
                break;
            case 2:
                taskType = TaskType.EPIC;
                break;
            case 3:
                taskType = TaskType.SUB_TASK;
                break;
            default:
                System.out.println("Введенный тип указан не верно.");
                return;
        }
        System.out.println("Удалено задач: " + taskManager.removeAllTasksByType(taskType));
    }

    private void updateTask(Scanner scanner) {
        int taskId = readIntFromScanner(scanner, "Введите номер идентификатора задачи:");
        Task task = taskManager.getTaskById(taskId);
        if (task == null) {
            System.out.println("Задача не найдена.");
            return;
        }
        System.out.println("Укажите новое название. Текущее значение = " + task.getTitle());
        if (scanner.hasNextLine()) {
            String newTitle = scanner.nextLine();
            if (!newTitle.isEmpty()) {
                task.setTitle(newTitle);
            }
        }
        System.out.println("Укажите новое описание задачи. Текущее значение = " + task.getDescription());
        if (scanner.hasNextLine()) {
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                task.setDescription(newDescription);
            }
        }
        taskManager.updateTask(task);
    }

    private void printSubTasks(Scanner scanner) {
        int epicId = readIntFromScanner(scanner, "Введите номер эпика:");
        taskManager.printSubTasks(epicId);
    }
}
