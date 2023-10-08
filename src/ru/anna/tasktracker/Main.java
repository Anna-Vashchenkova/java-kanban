package ru.anna.tasktracker;

import ru.anna.tasktracker.webserver.HttpTaskServer;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        new HttpTaskServer().start();
/*
        Menu menu = new Menu();
        menu.getTaskManager();
*/

    }
}
