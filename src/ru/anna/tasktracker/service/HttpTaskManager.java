package ru.anna.tasktracker.service;

import com.google.gson.*;
import ru.anna.tasktracker.model.*;
import ru.anna.tasktracker.utils.LocalDateAdapter;
import ru.anna.tasktracker.utils.TaskStatusAdapter;
import ru.anna.tasktracker.utils.TaskTypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends InMemoryTaskManager {
    public static final String TASKS_KEY = "tasks";
    public static final String EPICS_KEY = "epics";
    public static final String SUBTASKS_KEY = "subtasks";
    public static final String HISTORY_KEY = "history";

    private final KVTaskClient client;

    public HttpTaskManager() throws IOException, InterruptedException {
        client = new KVTaskClient();
        restore();
    }

    public void restore() {
        List<Task> tasks = (List<Task>) client.load(TASKS_KEY, Task.class);
        List<Epic> epics = (List<Epic>) client.load(EPICS_KEY, Epic.class);
        List<SubTask> subTasks = (List<SubTask>) client.load(SUBTASKS_KEY, SubTask.class);
        List<Integer> history = (List<Integer>) client.load(HISTORY_KEY, Integer.class);
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

    public void save() {
        client.put(TASKS_KEY, taskStore.getAllTasksByType(TaskType.TASK));
        client.put(EPICS_KEY, taskStore.getAllTasksByType(TaskType.EPIC));
        client.put(SUBTASKS_KEY, taskStore.getAllTasksByType(TaskType.SUB_TASK));
        client.put(HISTORY_KEY, historyStore.getHistory().stream().map(Task::getIdentificationNumber).toList());
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

    private class KVTaskClient {
        private final static String KV_SERVER_ADDRESS = "http://localhost:8078";
        private final static String GET_TOKEN_URI = "/register";
        private final static String SAVE_TOKEN_URI = "/save/";
        private final static String LOAD_TOKEN_URI = "/load/";
        private final HttpClient client = HttpClient.newHttpClient();
        private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        private final String token;
        private final Gson gson;

        public KVTaskClient() throws IOException, InterruptedException {
            HttpRequest request = requestBuilder
                    .GET()
                    .uri(URI.create(KV_SERVER_ADDRESS + GET_TOKEN_URI))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "text/html")
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            // отправляем запрос и получаем ответ от сервера
            HttpResponse<String> response = client.send(request, handler);
            token = response.body();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            gsonBuilder.registerTypeAdapter(TaskType.class, new TaskTypeAdapter());
            gsonBuilder.registerTypeAdapter(TaskStatus.class, new TaskStatusAdapter());
            gson = gsonBuilder.create();
        }

        public void put(String key, Object value) {
            try {
                HttpRequest request = requestBuilder
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(value)))
                        .uri(URI.create(KV_SERVER_ADDRESS + SAVE_TOKEN_URI + key + "?API_TOKEN=" + token))
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "text/html")
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public List load(String key, Class aClass) {
            LinkedList result = new LinkedList();
            try {
                String str = KV_SERVER_ADDRESS + LOAD_TOKEN_URI + key + "?API_TOKEN=" + token;
                HttpRequest request = requestBuilder
                        .GET()
                        .uri(URI.create(str))
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "text/html")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                System.out.println("body = " + body);
                if ("".equals(body))
                    return result;
                JsonArray asJsonArray = JsonParser.parseString(body).getAsJsonArray();
                for (JsonElement jsonElement : asJsonArray) {
                    result.add(gson.fromJson(jsonElement, aClass));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result;
        }
    }
}
