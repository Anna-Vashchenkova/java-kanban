import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> listOfSubTasks;

    public Epic(String title, String description, int identificationNumber, ArrayList<SubTask> listOfSubtasks) {
        super(title, description, identificationNumber);
        this.listOfSubTasks = listOfSubtasks;
        taskType = TaskType.EPIC;
    }

    public ArrayList<SubTask> getListOfSubTasks() {
        return listOfSubTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskType=" + taskType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                '}';
    }

    public void addSubTask(SubTask subTask) {
        listOfSubTasks.add(subTask);
    }
}
