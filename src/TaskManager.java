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
                System.out.println("Задача " + task.getTitle() + " успешно добавлена. Её номер - " + task.getIdentificationNumber());
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

    public void removeAll() {
        taskStore.removeAllTask();
    }

    public void updateTask(Task task) {
        taskStore.saveTask(task);
    }

    /*

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
