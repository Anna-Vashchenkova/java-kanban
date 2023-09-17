package ru.anna.tasktracker.service;

import ru.anna.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {
    private Map<Integer, Node> idToNode = new HashMap<>();
    private Node first = null;
    private Node last = null;

    public void linkLast(Task task) {
        if (idToNode.containsKey(task.getIdentificationNumber())) {
            removeById(task.getIdentificationNumber());
        }
        Node lastNode = last;
        Node newNode = new Node(lastNode, task, null);
        idToNode.put(task.getIdentificationNumber(), newNode);
        last = newNode;
        if (lastNode == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node temp = first;
        while (temp != null) {
            tasks.add(temp.getTask());
            temp = temp.next;
        }
        return tasks;
    }

    public void removeById(int id) {
        Node node = idToNode.get(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
        idToNode.remove(id);
    }

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }
    }
}
