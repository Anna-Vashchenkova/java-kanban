package ru.anna.tasktracker.service;

import ru.anna.tasktracker.exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


class KVTaskClient {
    private final static String GET_TOKEN_URI = "/register";
    private final static String SAVE_TOKEN_URI = "/save/";
    private final static String LOAD_TOKEN_URI = "/load/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private final String token;
    String path;

    public KVTaskClient(String path) throws IOException, InterruptedException {
        this.path = path;
        HttpRequest request = requestBuilder
                .GET()
                .uri(URI.create(path + GET_TOKEN_URI))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);
        token = response.body();
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = requestBuilder
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .uri(URI.create(path + SAVE_TOKEN_URI + key + "?API_TOKEN=" + token))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "text/html")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не ожидаемый код " + response.statusCode());
            }
        } catch (ManagerSaveException|IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        try {
            String str = path + LOAD_TOKEN_URI + key + "?API_TOKEN=" + token;
            HttpRequest request = requestBuilder
                    .GET()
                    .uri(URI.create(str))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "text/html")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не ожидаемый код " + response.statusCode());
            }
            String body = response.body();
            System.out.println("body = " + body);
            return body;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
