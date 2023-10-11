package ru.anna.tasktracker.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.anna.tasktracker.model.TaskStatus;
import ru.anna.tasktracker.model.TaskType;

import java.io.IOException;


public class TaskStatusAdapter extends TypeAdapter<TaskStatus> {
    @Override
    public void write(final JsonWriter jsonWriter, final TaskStatus taskStatus) throws IOException {
        jsonWriter.value(taskStatus.toString());
    }

    @Override
    public TaskStatus read(final JsonReader jsonReader) throws IOException {
        String text = jsonReader.nextString();
        return TaskStatus.valueOf(text);
    }
}

