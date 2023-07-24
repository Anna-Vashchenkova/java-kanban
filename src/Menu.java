import java.util.Scanner;

public class Menu {
    public void printMenu() {
        System.out.println("Что вы хотите сделать? Выбирете команду: ");
        System.out.println("1 - добавить задачу.");
        System.out.println("2 - изменить статус задачи.");
        System.out.println("3 - удалить задачу.");
        System.out.println("0 - выход.");

        getTaskManager();
    }
    
    public void getTaskManager() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            TaskManager taskManager = new TaskManager();
            int command = scanner.nextInt();
            if (command == 1) {
                taskManager.addTask();
            } else  if (command == 2) {
                System.out.println("Введите данные о статусе задачи: 1 = NEW, 2 = IN_PROGRESS, 3 = DONE");
                int statusCommand = scanner.nextInt();
                int taskId = scanner.nextInt();
                TaskStatus status = getStatus(statusCommand);
                taskManager.setStatus(taskId, status);
                if (taskManager == null) {
                    System.out.println("Вы ввели неверное значение. Введите число от 1 до 3.");
                }
            } else  if (command == 3) {
                System.out.println("Введите номер идентификатора задачи: ");
                int taskId = scanner.nextInt();
                taskManager.removeTask(taskId);
            } else if (command == 0) {
                System.out.println("Выход");
                scanner.close();
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    public TaskStatus getStatus(int command) {
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
    }
}
