import java.util.Scanner;

public class Menu {
    TaskManager taskManager = new TaskManager();

    public void printMenu() {
        System.out.println("Что вы хотите сделать? Выбирете команду: ");
        System.out.println("1 - добавить задачу.");
        System.out.println("2 - изменить статус задачи.");
        System.out.println("3 - удалить задачу.");
        System.out.println("4 - показать список задач.");
        System.out.println("0 - выход.");
    }
    
    public void getTaskManager() {
        while (true) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            String commandString = scanner.nextLine();
            int command = Integer.parseInt(commandString);
            if (command == 1) {
                createTask(scanner, taskManager);
            } else  if (command == 2) {
                changeStatus(scanner, taskManager);
            } else  if (command == 3) {
                removeTask(scanner, taskManager);
            } else  if (command == 4) {
                taskManager.printTaskList();
            } else if (command == 0) {
                System.out.println("Выход");
                scanner.close();
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    private void removeTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите номер идентификатора задачи: ");
        String taskIdString = scanner.nextLine();
        int taskId = Integer.parseInt(taskIdString);
        taskManager.removeTask(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена.");
    }

    private void changeStatus(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите номер идентификатора задачи: ");
        String taskIdString = scanner.nextLine();
        int taskId = Integer.parseInt(taskIdString);
        System.out.println("Введите данные о статусе задачи: NEW, IN_PROGRESS, DONE");
        String statusCommand = scanner.nextLine();
        TaskStatus status = TaskStatus.valueOf(statusCommand);
        taskManager.setStatus(taskId, status);
        System.out.println("Статус задачи " + taskId + " успешно изменён на " + status);
    }

    private void createTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите название задачи.");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи.");
        String description = scanner.nextLine();
        int identificationNumber = taskManager.generateIdNumber();
        taskManager.addTask(title, description, identificationNumber);
        System.out.println("Задача " + title + " успешно добавлена. Её номер - " + identificationNumber);
    }

    /*public TaskStatus getStatus(int command) {
        TaskStatus taskStatus;
        switch (command) {
            case 1:
                return TaskStatus.NEW;
            case 2:
                return TaskStatus.IN_PROGRESS;
            case 3:
                return TaskStatus.DONE;
            default:
                System.out.println("Такой команды нет.");
                return  null;
        }
    }*/
}
