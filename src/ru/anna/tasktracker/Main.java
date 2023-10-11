package ru.anna.tasktracker;

import ru.anna.tasktracker.kvserver.KVServer;
import ru.anna.tasktracker.webserver.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        new HttpTaskServer().start();
        System.out.println("Поехали!");
    }
}
