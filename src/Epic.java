import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> listOfSubtasks;

    public Epic(String title, String description, int identificationNumber, ArrayList<SubTask> listOfSubtasks) {
        super(title, description, identificationNumber);
        this.listOfSubtasks = listOfSubtasks;
        taskType = TaskType.EPIC;
    }

    public ArrayList<SubTask> setListOfSubtasks(SubTask subTask) {
        listOfSubtasks.add(subTask);
        return listOfSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskType=" + taskType +
                ", listOfSubtasks=" + listOfSubtasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                '}';
    }

    public void addSubTask(SubTask subTask) {
        listOfSubtasks.add(subTask);
    }
}
