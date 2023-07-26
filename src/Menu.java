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
                    createTask(scanner);
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

    private void createTask(Scanner scanner) {
        System.out.println("Введите название задачи.");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи.");
        String description = scanner.nextLine();
        int identificationNumber = taskManager.generateIdNumber();
        Task task = new Task(title, description, identificationNumber);
        taskManager.addTask(task);
        System.out.println("Задача " + title + " успешно добавлена. Её номер - " + identificationNumber);
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
