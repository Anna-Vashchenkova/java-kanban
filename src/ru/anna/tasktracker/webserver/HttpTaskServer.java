package ru.anna.tasktracker.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.anna.tasktracker.model.*;
import ru.anna.tasktracker.service.TaskManager;
import ru.anna.tasktracker.utils.LocalDateAdapter;
import ru.anna.tasktracker.utils.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String WRONG_OPERATION = "Не верная операция";
    private static final String WRONG_PARAMETERS = "Не правильные параметры для выполнения операции";
    private static final String TASK_NOT_FOUND = "Задача не найдена. Id = ";
    private final TaskManager taskManager = Managers.getDefault();

    public boolean start() {
        try {
            HttpServer httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler(taskManager));
            httpServer.start(); // запускаем сервер

            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
            return true;
        } catch (IOException e) {
            System.out.println("HTTP-сервер не можеь быть запущен на " + PORT + " порту!\n" + e.getMessage());
            return false;
        }

    }

    static enum Operation {
        ADD_TASK, GET_BY_ID, REMOVE_BY_ID, SET_STATUS, GET_BY_TYPE, REMOVE_BY_TYPE, UPDATE_TASK,
        GET_SUBTASKS, GET_HISTORY, UNKNOWN
    }

    static class TasksHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        TasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            gson = gsonBuilder.create();

        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Request request = getRequest(exchange);
            processRequest(request, exchange);
            exchange.close();
        }

        private void processRequest(Request request, HttpExchange exchange) throws IOException {
            final Optional requestBody = request.getBody();
            switch (request.operation) {
                case UNKNOWN -> {
                    writeResponse(exchange, WRONG_OPERATION, 404);
                }
                case ADD_TASK -> {
                    if(request.body.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        Object body = request.body.get();
                        Task task;
                        if (body instanceof AddTaskDto) {
                            AddTaskDto addTask = (AddTaskDto) body;
                            task = new Task(
                                    addTask.getTitle(),
                                    addTask.getDescription(),
                                    taskManager.generateIdNumber(),
                                    addTask.getStartTime(),
                                    addTask.getDuration());
                        } else if (body instanceof AddEpicDto) {
                            AddEpicDto addTask = (AddEpicDto) body;
                            task = new Epic(
                                    addTask.getTitle(),
                                    addTask.getDescription(),
                                    taskManager.generateIdNumber(),
                                    new ArrayList<>(),
                                    addTask.getStartTime(),
                                    addTask.getDuration());
                        } else {
                            AddSubTaskDto addTask = (AddSubTaskDto) body;
                            task = new SubTask(
                                    addTask.getTitle(),
                                    addTask.getDescription(),
                                    taskManager.generateIdNumber(),
                                    addTask.getEpicId(),
                                    addTask.getStartTime(),
                                    addTask.getDuration());
                        }
                        task = taskManager.addTask(task);
                        if (task == null) {
                            writeResponse(exchange, WRONG_PARAMETERS, 400);
                        } else {
                            writeResponse(exchange, "Задача сохранена с идентификатором " + task.getIdentificationNumber(), 201);
                        }
                    }
                }
                case SET_STATUS -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        UpdateStatusDto updateStatus = (UpdateStatusDto)requestBody.get();
                        try {
                            TaskStatus ts = TaskStatus.valueOf(updateStatus.getStatus());
                            taskManager.setStatus(updateStatus.getId(), ts);
                            writeResponse(exchange, "", 200);
                        } catch (IllegalArgumentException e) {
                            writeResponse(exchange, WRONG_PARAMETERS, 400);
                        }
                    }
                }
                case UPDATE_TASK -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        UpdateTaskDto updateTask = (UpdateTaskDto) requestBody.get();
                        Task taskById = taskManager.getTaskById(updateTask.getId());
                        if (taskById == null){
                            writeResponse(exchange, TASK_NOT_FOUND + updateTask.getId(), 404);
                        } else {
                            taskById.setTitle(updateTask.getTitle());
                            taskById.setDescription(updateTask.getDescription());
                            taskManager.updateTask(taskById);
                            writeResponse(exchange, "", 200);
                        }
                    }
                }
                case REMOVE_BY_ID -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        taskManager.removeTask((Integer) requestBody.get());
                        writeResponse(exchange, "", 200);
                    }
                }
                case REMOVE_BY_TYPE -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        taskManager.removeAllTasksByType((TaskType) requestBody.get());
                        writeResponse(exchange, "", 200);
                    }
                }
                case GET_BY_ID -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        Task taskById = taskManager.getTaskById((Integer) requestBody.get());
                        if (taskById == null) {
                            writeResponse(exchange, TASK_NOT_FOUND + requestBody.get(), 404);
                        } else {
                            writeResponse(exchange, gson.toJson(taskById), 200);
                        }
                    }

                }
                case GET_BY_TYPE ->  {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        List<Task> list = taskManager.getTaskListByType((TaskType) requestBody.get());
                        writeResponse(exchange, gson.toJson(list), 200);
                    }
                }
                case GET_HISTORY -> {
                    writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
                }
                case GET_SUBTASKS -> {
                    if(requestBody.isEmpty()) {
                        writeResponse(exchange, WRONG_PARAMETERS, 400);
                    } else {
                        writeResponse(exchange, gson.toJson(taskManager.getEpicSubtasks((Integer) requestBody.get())), 200);
                    }
                }
            }

/*
---         void removeTask(int taskId);                        // DELETE /tasks/{id}
---         int removeAllTasksByType(TaskType taskType);        // DELETE /tasks/[task|epic|subtask]
*/
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        }

        private Request getRequest(HttpExchange exchange) {
            String uri = exchange.getRequestURI().getPath();
            Map<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
            String method = exchange.getRequestMethod();
            InputStream body = exchange.getRequestBody();
            if ("GET".equals(method)) {
                return getRequestForGetMethod(uri);
            }
            if ("POST".equals(method)) {
                return getRequestForPostMethod(uri, params, body);
            }
            if ("DELETE".equals(method)) {
                return getRequestForDeleteMethod(uri);
            }
            return new Request(Operation.UNKNOWN);
        }

        Request getRequestForDeleteMethod(String uri) {
            String[] pathItems = uri.split("/");
            if ((pathItems.length < 2) || (!"".equals(pathItems[0])) || (!"tasks".equals(pathItems[1]))) {
                return new Request(Operation.UNKNOWN);
            }
            if(pathItems.length == 2) {
                return new Request(Operation.UNKNOWN);
            }
            OptionalInt idOpt = getIdFromPath(pathItems[2]);
            if ((pathItems.length == 3) && (idOpt.isPresent())) {
                return new Request(Operation.REMOVE_BY_ID, Optional.of(idOpt.getAsInt()));
            }
            if (pathItems.length == 3) {
                try {
                    String taskTypeStr = pathItems[2].toUpperCase().replace("SUBTASK", "SUB_TASK");
                    TaskType taskType = TaskType.valueOf(taskTypeStr);
                    return new Request(Operation.REMOVE_BY_TYPE, Optional.of(taskType));
                } catch (IllegalArgumentException e) {
                    return new Request(Operation.REMOVE_BY_TYPE, Optional.empty());
                }

            }
            return new Request(Operation.UNKNOWN);
        }

        Request getRequestForPostMethod(String uri, Map<String, String> params, InputStream bodyInputStream) {
            String[] pathItems = uri.split("/");
            if ((pathItems.length < 2) || (!"".equals(pathItems[0])) || (!"tasks".equals(pathItems[1]))) {
                return new Request(Operation.UNKNOWN);
            }
            if ((pathItems.length == 3) && ("task".equals(pathItems[2])) ){
                return new Request(Operation.ADD_TASK, getBodyObject(bodyInputStream, AddTaskDto.class));

            }
            if ((pathItems.length == 3) && ("epic".equals(pathItems[2])) ){
                return new Request(Operation.ADD_TASK, getBodyObject(bodyInputStream, AddEpicDto.class));

            }
            if ((pathItems.length == 3) && ("subtask".equals(pathItems[2])) ){
                return new Request(Operation.ADD_TASK, getBodyObject(bodyInputStream, AddSubTaskDto.class));
            }
            OptionalInt idOpt = getIdFromPath(pathItems[2]);
            if ((pathItems.length == 3) && (idOpt.isPresent()) && (params.isEmpty())){
                Optional<UpdateTaskDto> optional = getBodyObject(bodyInputStream, UpdateTaskDto.class);
                optional.ifPresent(updateTaskDto -> updateTaskDto.setId(idOpt.getAsInt()));
                return new Request(Operation.UPDATE_TASK, optional);
            }
            if ((pathItems.length == 3) && (idOpt.isPresent()) && (!params.isEmpty())){
                String status = params.get("status");
                Optional <UpdateStatusDto> body;
                if (status != null) {
                    body = Optional.of(new UpdateStatusDto(status, idOpt.getAsInt()));
                } else {
                    body = Optional.empty();
                }
                    return new Request(Operation.SET_STATUS, body);

            }
            return new Request(Operation.UNKNOWN);
        }

        Request getRequestForGetMethod(String uri) {
            String[] pathItems = uri.split("/");
            if ((pathItems.length < 2) || (!"".equals(pathItems[0])) || (!"tasks".equals(pathItems[1]))) {
                return new Request(Operation.UNKNOWN);
            }
            OptionalInt idOpt = getIdFromPath(pathItems[2]);
            if ((pathItems.length == 3) && (idOpt.isPresent())) {
                return new Request(Operation.GET_BY_ID, Optional.of(idOpt.getAsInt()));
            }
            if ((pathItems.length == 3) && ("history".equals(pathItems[2]))) {
                return new Request(Operation.GET_HISTORY, Optional.empty());
            }
            if (pathItems.length == 3) {
                try {
                    String taskTypeStr = pathItems[2].toUpperCase().replace("SUBTASK", "SUB_TASK");
                    TaskType taskType = TaskType.valueOf(taskTypeStr);
                    return new Request(Operation.GET_BY_TYPE, Optional.of(taskType));
                } catch (IllegalArgumentException e) {
                    return new Request(Operation.UNKNOWN, Optional.empty());
                }

            }
            if (pathItems.length == 5) {
                OptionalInt epicIdOpt = getIdFromPath(pathItems[3]);
                if (("epic".equals(pathItems[2])) && (epicIdOpt.isPresent()) && ("subtasks".equals(pathItems[4])) ) {
                    return new Request(Operation.GET_SUBTASKS, Optional.of(epicIdOpt.getAsInt()));
                }
            }

            return new Request(Operation.UNKNOWN, Optional.empty());
        }

        private OptionalInt getIdFromPath(String pathItem) {
            try {
                return OptionalInt.of(Integer.parseInt(pathItem));
            } catch (NumberFormatException e) {
                return OptionalInt.empty();
            }
        }

        private Optional getBodyObject(InputStream bodyInputStream, Class aClass) {
            try {
                JsonReader jsonReader = new JsonReader(new InputStreamReader(bodyInputStream, "UTF-8"));
                return Optional.of(gson.fromJson(jsonReader, aClass));
            } catch (Throwable e) {
                return Optional.empty();
            }
        }

        private Map<String, String> getParamsFromQuery(String query) {
            HashMap<String, String> result = new HashMap<>();
            if(query == null)
                return result;
            String[] paramsAndValues = query.split("&");
            for (String paramsAndValue : paramsAndValues) {
                String[] split = paramsAndValue.split("=");
                if (split.length > 1) {
                    result.put(split[0], split[1]);
                }
            }
            return result;
        }
    }

    static class Request {
        private final Operation operation;
        private final Optional body;

        Request(Operation operation) {
            this.operation = operation;
            body = Optional.empty();
        }

        public Request(Operation operation, Optional body) {
            this.operation = operation;
            this.body = body;
        }

        public Operation getOperation() {
            return operation;
        }

        public Optional getBody() {
            return body;
        }
    }
}
