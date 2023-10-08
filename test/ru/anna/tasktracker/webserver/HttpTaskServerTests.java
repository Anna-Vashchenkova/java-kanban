package ru.anna.tasktracker.webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static ru.anna.tasktracker.model.TaskType.*;
import static ru.anna.tasktracker.webserver.HttpTaskServer.Operation.*;

class HttpTaskServerTests {

    private final static String WRONG_JSON = "{" +
            "\"title\":\"Название\"," +
            "\"description\"=\"description\"" +
            "\"startTime\":\"2023-03-21 12:34\"," +
            "\"duration\":5" +
            "_";
    private final static String ADD_TASK_JSON= "{" +
            "\"title\":\"Название\"," +
            "\"description\":\"description\"," +
            "\"startTime\":\"2023-03-21 12:34\"," +
            "\"duration\":5," +
            "\"epicId\":185" +
            "}";

    private final static String UPDATE_TASK_JSON= "{" +
            "\"title\":\"Название\"," +
            "\"description\":\"description\"" +
            "}";

    HttpTaskServer.TasksHandler tasksHandler = new HttpTaskServer.TasksHandler(null);

// Тесты для метода POST
    @DisplayName("POST. Необходимо возвращать UNKNOWN, если uri пустой")
    @Test
    public void unknownPostOperationWhenUriEmpty(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("", new HashMap<>(),
                null
        );
        assertEquals(UNKNOWN, request.getOperation());
    }
    @DisplayName("POST. Необходимо возвращать UNKNOWN, если первая часть uri не пустой")
    @Test
    public void unknownPostOperationWhenUriIncorrect1(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("asdsdf/", new HashMap<>(),
                null
        );
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("POST. Необходимо возвращать UNKNOWN, если первая часть uri пустая, а вторая не равна tasks")
    @Test
    public void unknownPostOperationWhenUriIncorrect2(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasksA", new HashMap<>(),
                null
        );
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK с объектом TaskDto, если запрос сформулирован корректно")
    @Test
    public void shouldAddTaskWithObject(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/task", new HashMap<>(),
                new ByteArrayInputStream(ADD_TASK_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertTrue(request.getBody().isPresent());
        AddTaskDto task = (AddTaskDto) request.getBody().get();
        AddTaskDto etalonTask = new AddTaskDto("Название", "description", 5, LocalDateTime.of(2023,3,21,12,34));
        assertEquals(etalonTask, task);
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK c пустым объектом, если передан некорректный JSON")
    @Test
    public void shouldAddTaskWithEmpty(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/task", new HashMap<>(),
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK с объектом EpicDto, если запрос сформулирован корректно")
    @Test
    public void shouldAddEpicWithObject(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/epic", new HashMap<>(),
                new ByteArrayInputStream(ADD_TASK_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertTrue(request.getBody().isPresent());
        AddEpicDto task = (AddEpicDto) request.getBody().get();
        AddEpicDto etalonTask = new AddEpicDto("Название", "description", 5, LocalDateTime.of(2023,3,21,12,34));
        assertEquals(etalonTask, task);
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK c пустым объектом, если передан некорректный JSON")
    @Test
    public void shouldEpicTaskWithEmpty(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/task", new HashMap<>(),
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK с объектом SubTaskDto, если запрос сформулирован корректно")
    @Test
    public void shouldAddSubtaskWithObject(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/subtask", new HashMap<>(),
                new ByteArrayInputStream(ADD_TASK_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertTrue(request.getBody().isPresent());
        AddSubTaskDto task = (AddSubTaskDto) request.getBody().get();
        AddSubTaskDto etalonTask = new AddSubTaskDto("Название", "description", 5, LocalDateTime.of(2023,3,21,12,34), 185);
        assertEquals(etalonTask, task);
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK c пустым объектом, если передан некорректный JSON")
    @Test
    public void shouldSubTaskTaskWithEmpty(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/subtask", new HashMap<>(),
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(ADD_TASK, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо возвращать операцию UPDATE_TASK с объектом UpdateTaskDto, если запрос сформулирован корректно")
    @Test
    public void shouldUpdateTaskWithObject(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/18", new HashMap<>(),
                new ByteArrayInputStream(UPDATE_TASK_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(UPDATE_TASK, request.getOperation());
        assertTrue(request.getBody().isPresent());
        UpdateTaskDto task = (UpdateTaskDto) request.getBody().get();
        UpdateTaskDto etalonTask = new UpdateTaskDto("Название", "description", 18);
        assertEquals(etalonTask, task);
    }
    @DisplayName("Необходимо возвращать операцию ADD_TASK c пустым объектом, если передан некорректный JSON")
    @Test
    public void shouldUpdateTaskTaskWithEmpty(){
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/18", new HashMap<>(),
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(UPDATE_TASK, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо возвращать операцию SET_STATUS с объектом UpdateStatusDto, если запрос сформулирован корректно")
        @Test
    public void shouldUpdateStatusWithObject(){
        HashMap<String, String> params = new HashMap<>();
        params.put("status", "DONE");
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/18", params,
                new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(SET_STATUS, request.getOperation());
        assertTrue(request.getBody().isPresent());
        UpdateStatusDto task = (UpdateStatusDto) request.getBody().get();
        UpdateStatusDto etalonTask = new UpdateStatusDto("DONE", 18);
        assertEquals(etalonTask, task);
    }
    @DisplayName("Необходимо возвращать операцию SET_STATUS c пустым объектом, если передан некорректный JSON")
    @Test
    public void shouldUpdateStatusWithEmpty(){
        HashMap<String, String> params = new HashMap<>();
        params.put("status12", "DONE");
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/18", params,
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(SET_STATUS, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо возвращать операцию UNKNOWN c пустым объектом, если невозможно определить операцию")
    @Test
    public void shouldUnknownWheWrongUri(){
        HashMap<String, String> params = new HashMap<>();
        HttpTaskServer.Request request = tasksHandler.getRequestForPostMethod("/tasks/dshfkjhdskfj", params,
                new ByteArrayInputStream(WRONG_JSON.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals(UNKNOWN, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }

//    Тесты для метода DELETE
    @DisplayName("DELETE. Необходимо возвращать UNKNOWN, если uri пустой")
    @Test
    public void unknownDeleteOperationWhenUriEmpty(){
    HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("");
    assertEquals(UNKNOWN, request.getOperation());
}
    @DisplayName("DELETE. Необходимо возвращать UNKNOWN, если первая часть uri не пустой")
    @Test
    public void unknownDeleteOperationWhenUriIncorrect1(){
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("asdsdf/");
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("DELETE. Необходимо возвращать UNKNOWN, если первая часть uri пустая, а вторая не равна tasks")
    @Test
    public void unknownDeleteOperationWhenUriIncorrect2(){
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasksA");
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("Необходимо возвращать операцию UNKNOWN c пустым объектом, если передан URI /tasks")
    @Test
    public void shouldRemoveAll() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks");

        assertEquals(UNKNOWN, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо вернуть операцию REMOVE_TASK и число, если передан URI /tasks/{int}")
    @Test
    public void shouldRemoveById() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/18");

        assertEquals(REMOVE_BY_ID, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(18, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию REMOVE_BY_TYPE и TaskType.TASK, если передан URI /tasks/task")
    @Test
    public void shouldRemoveAllTasks() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/task");

        assertEquals(REMOVE_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(TASK, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию REMOVE_BY_TYPE и TaskType.EPIC, если передан URI /tasks/epic")
    @Test
    public void shouldRemoveAllEpics() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/epic");

        assertEquals(REMOVE_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(EPIC, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию REMOVE_BY_TYPE и TaskType.SUB_TASKS, если передан URI /tasks/subtask")
    @Test
    public void shouldRemoveAllSubtasks() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/subtask");

        assertEquals(REMOVE_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(SUB_TASK, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию REMOVE_BY_TYPE и пустой объяект, если передан URI /tasks/{непонтяно_что}")
    @Test
    public void shouldRemoveAllWithEmpty() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/subtasks");

        assertEquals(REMOVE_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isEmpty());
    }
    @DisplayName("Необходимо вернуть операцию UNKNOWN и пустой объяект, если передан неверный URI")
    @Test
    public void shouldUnknownForWrongURI() {
        HttpTaskServer.Request request = tasksHandler.getRequestForDeleteMethod("/tasks/subtasks/sdds");

        assertEquals(UNKNOWN, request.getOperation());
        assertTrue(request.getBody().isEmpty());
    }
//    Тесты для метода GET
@DisplayName("GET. Необходимо возвращать UNKNOWN, если uri пустой")
@Test
public void unknownGetOperationWhenUriEmpty(){
    HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("");
    assertEquals(UNKNOWN, request.getOperation());
}
    @DisplayName("GET. Необходимо возвращать UNKNOWN, если первая часть uri не пустой")
    @Test
    public void unknownGetOperationWhenUriIncorrect1(){
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("asdsdf/");
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("GET. Необходимо возвращать UNKNOWN, если первая часть uri пустая, а вторая не равна tasks")
    @Test
    public void unknownGetOperationWhenUriIncorrect2(){
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasksA");
        assertEquals(UNKNOWN, request.getOperation());

    }
    @DisplayName("Необходимо вернуть операцию GET_BY_TYPE и TaskType.TASK, если передан URI /tasks/task")
    @Test
    public void shouldGetAllTasks() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/task");

        assertEquals(GET_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(TASK, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию GET_BY_TYPE и TaskType.EPIC, если передан URI /tasks/epic")
    @Test
    public void shouldGetAllEpics() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/epic");

        assertEquals(GET_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(EPIC, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию GET_BY_TYPE и TaskType.SUB_TASKS, если передан URI /tasks/subtask")
    @Test
    public void shouldGetAllSubtasks() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/subtask");

        assertEquals(GET_BY_TYPE, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(SUB_TASK, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию GET_BY_ID и идентификатор задачи, если передан URI /tasks/{id}")
    @Test
    public void shouldGetTaskById() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/15");

        assertEquals(GET_BY_ID, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(15, request.getBody().get());
    }
    @DisplayName("Необходимо вернуть операцию GET_HISTORY и пустой объект, если передан URI /tasks/history")
    @Test
    public void shouldGetHistory() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/history");

        assertEquals(GET_HISTORY, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо вернуть операцию UNKNOWNT и пустой объект, если передан URI /tasks/{что-то непонтяное}")
    @Test
    public void shouldGetUnknownWithTwoSlashUri() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/history2");

        assertEquals(UNKNOWN, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо вернуть операцию UNKNOWNT и пустой объект, если передан непонятный URI")
    @Test
    public void shouldGetUnknownWithIncorrectUri() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/epic/ххх/subtasks");

        assertEquals(UNKNOWN, request.getOperation());
        assertFalse(request.getBody().isPresent());
    }
    @DisplayName("Необходимо вернуть операцию UNKNOWNT и пустой объект, если передан непонятный URI")
    @Test
    public void shouldGetSubtasks() {
        HttpTaskServer.Request request = tasksHandler.getRequestForGetMethod("/tasks/epic/18/subtasks");

        assertEquals(GET_SUBTASKS, request.getOperation());
        assertTrue(request.getBody().isPresent());
        assertEquals(18, request.getBody().get());
    }
}