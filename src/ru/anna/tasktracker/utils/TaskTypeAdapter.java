package ru.anna.tasktracker.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.anna.tasktracker.model.TaskType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TaskTypeAdapter extends TypeAdapter<TaskType> {
    @Override
    public void write(final JsonWriter jsonWriter, final TaskType taskType) throws IOException {
        jsonWriter.value(taskType.toString());
    }

    @Override
    public TaskType read(final JsonReader jsonReader) throws IOException {
        String text = jsonReader.nextString();
        return TaskType.valueOf(text);
    }
}

