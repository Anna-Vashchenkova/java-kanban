import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    TaskManager taskManager = new TaskManager();

    public void printMenu() {
        System.out.println("Что вы хотите сделать? Выбирете команду: ");
        System.out.println("1 - добавить задачу.");
        System.out.println("2 - изменить статус задачи.");
        System.out.println("3 - удалить задачу.");
        System.out.println("4 - показать список задач.");
        System.out.println("5 - удалить все задачи.");
        System.out.println("6 - обновить задачу.");
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
                    taskManager.printTaskList();
                    break;
                case 5:
                    taskManager.removeAll();
                    break;
                case 6:
                    updateTask(scanner);
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
        System.out.println("Укажите тип задачи: 1 - TASK, 2 - EPIC, 3 - SUBTASK.");
        String taskTypeStr = scanner.nextLine();
        int taskType = Integer.parseInt(taskTypeStr);
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
        Epic epic = new Epic (title, description, identificationNumber, new ArrayList<>());
        return epic;
    }

    private SubTask createSubTask(Scanner scanner) {
        System.out.println("Введите название подзадачи.");
        String title = scanner.nextLine();
        System.out.println("Введите описание подзадачи.");
        String description = scanner.nextLine();
        System.out.println("Укажите номер эпика.");
        String epicIdStr = scanner.nextLine();
        int epicId = Integer.parseInt(epicIdStr);
        int identificationNumber = taskManager.generateIdNumber();
        SubTask subTask = new SubTask(title, description, identificationNumber, epicId);
        return subTask;
    }

    private void removeTask(Scanner scanner) {
        System.out.println("Введите номер идентификатора задачи: ");
        String taskIdString = scanner.nextLine();
        int taskId = Integer.parseInt(taskIdString);
        taskManager.removeTask(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена.");
    }

    private void changeStatus(Scanner scanner) {
        System.out.println("Введите номер идентификатора задачи: ");
        String taskIdString = scanner.nextLine();
        int taskId = Integer.parseInt(taskIdString);
        System.out.println("Введите данные о статусе задачи: NEW, IN_PROGRESS, DONE");
        String statusCommand = scanner.nextLine();
        TaskStatus status = TaskStatus.valueOf(statusCommand);
        taskManager.setStatus(taskId, status);
        System.out.println("Статус задачи " + taskId + " успешно изменён на " + status);
    }

    private void updateTask(Scanner scanner) {
        System.out.println("Введите номер идентификатора задачи:");
        String taskIdString = scanner.nextLine();
        int taskId = Integer.parseInt(taskIdString);
        Task task = taskManager.getTaskById(taskId);
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

}
