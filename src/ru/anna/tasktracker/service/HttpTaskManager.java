package ru.anna.tasktracker.service;

import com.google.gson.*;
import ru.anna.tasktracker.model.*;
import ru.anna.tasktracker.utils.LocalDateAdapter;
import ru.anna.tasktracker.utils.TaskStatusAdapter;
import ru.anna.tasktracker.utils.TaskTypeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    public static final String TASKS_KEY = "tasks";
    public static final String EPICS_KEY = "epics";
    public static final String SUBTASKS_KEY = "subtasks";
    public static final String HISTORY_KEY = "history";

    private KVTaskClient client; //убрала final
    private final Gson gson;


    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super(null);
        client = new KVTaskClient(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(TaskType.class, new TaskTypeAdapter());
        gsonBuilder.registerTypeAdapter(TaskStatus.class, new TaskStatusAdapter());
        gson = gsonBuilder.create();
        restore();
    }

    @Override
    public void restore() {
        if (client == null) {
            return;
        }
        List<Task> tasks = getListFromString(client.load(TASKS_KEY), Task.class);
        List<Epic> epics = getListFromString(client.load(EPICS_KEY), Epic.class);
        List<SubTask> subTasks = getListFromString(client.load(SUBTASKS_KEY), SubTask.class);
        List<Integer> history = getListFromString(client.load(HISTORY_KEY), Integer.class);
        tasks.forEach(this::saveTaskAndChangeLastId);
        epics.forEach(this::saveTaskAndChangeLastId);
        subTasks.forEach(this::saveTaskAndChangeLastId);
        history.forEach(this::getTaskById);
    }

    private void saveTaskAndChangeLastId(Task task) {
        taskStore.saveTask(task);
        if (lastId < task.getIdentificationNumber()) {
            lastId = task.getIdentificationNumber();
        }
        if (task.getTaskType() == TaskType.SUB_TASK) {
            Task epic = taskStore.getTaskById(((SubTask) task).getParentEpicId());
            if (epic != null)
                ((Epic) epic).addSubTask((SubTask) task);
        }

    }

    @Override
    public void save() {
        client.put(TASKS_KEY, gson.toJson(taskStore.getAllTasksByType(TaskType.TASK)));
        client.put(EPICS_KEY, gson.toJson(taskStore.getAllTasksByType(TaskType.EPIC)));
        client.put(SUBTASKS_KEY, gson.toJson(taskStore.getAllTasksByType(TaskType.SUB_TASK)));
        client.put(HISTORY_KEY, gson.toJson(historyStore.getHistory().stream().map(Task::getIdentificationNumber).toList()));
    }

    @Override
    public Task addTask(Task task) {
        Task result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task resultTask = super.getTaskById(taskId);
        save();
        return resultTask;
    }

    @Override
    public void removeTask(int taskId) {
        //Task task = getTaskById(taskId);
        super.removeTask(taskId);
        save();
    }

    @Override
    public Task setStatus(int taskId, TaskStatus status) {
        super.setStatus(taskId, status);
        save();
        Task resultTask = super.getTaskById(taskId);
        return resultTask;
    }

    @Override
    public List<Task> getTaskListByType(TaskType taskType) {
        List<Task> tasks = super.getTaskListByType(taskType);
        save();
        return tasks;
    }

    @Override
    public int removeAllTasksByType(TaskType taskType) {
        int countTask = super.removeAllTasksByType(taskType);
        save();
        return countTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Set<SubTask> getEpicSubtasks(int epicId) {
        Set<SubTask> subTasks = super.getEpicSubtasks(epicId);
        save();
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> printHistoryStore = super.getHistory();
        save();
        return printHistoryStore;
    }

    public List getListFromString(String str, Class aClass) {
        List result = new ArrayList<>();
        if (str.isEmpty()) {
            return new ArrayList<>();
        }
        JsonArray asJsonArray = JsonParser.parseString(str).getAsJsonArray();
            for (JsonElement jsonElement : asJsonArray) {
                result.add(gson.fromJson(jsonElement, aClass));
            }
            return result;
    }

}
