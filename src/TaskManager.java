import java.util.Collection;

public class TaskManager {
    TaskStore taskStore = new TaskStore();
    int lastId = 0;

    public void addTask(String title, String description, int identificationNumber) {
        Task task = new Task(title, description, identificationNumber);
        taskStore.saveTask(task);
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
         task.setStatus(status);
         taskStore.saveTask(task);
    }

    public void printTaskList() {
        Collection<Task> taskList = taskStore.getAllTasks();
        for (Task task1 : taskList) {
            System.out.println(task1);
        }
    }

    public int generateIdNumber() {
        lastId++;
        return lastId;
    }

    /*
    1 Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    2 Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    Получение списка всех задач.
    Удаление всех задач.
    Получение по идентификатору.
    Создание. Сам объект должен передаваться в качестве параметра.
    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        tasks.put(task.getId(), task))
    Удаление по идентификатору.
    3 Дополнительные методы:
    3/1 Получение списка всех подзадач определённого эпика.
    4/ Управление статусами осуществляется по следующему правилу:
    4/1 Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией
        о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    4/2 Для эпиков:
    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    во всех остальных случаях статус должен быть IN_PROGRESS.*/
}
