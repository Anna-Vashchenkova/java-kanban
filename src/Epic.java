import java.util.ArrayList;

public class Epic extends Task {
    protected final TaskType taskType = TaskType.EPIC;
    private ArrayList<SubTask> listOfSubtasks = new ArrayList<SubTask>();

    public Epic(String title, String description, int identificationNumber, ArrayList<SubTask> listOfSubtasks) {
        super(title, description, identificationNumber);
        this.listOfSubtasks = listOfSubtasks;
    }

    public ArrayList<SubTask> setListOfSubtasks(SubTask subTask) {
        listOfSubtasks.add(subTask);
        return listOfSubtasks;
    }
}
