import java.util.Objects;

public class SubTask extends Task {
    protected final TaskType taskType = TaskType.SUB_TASK;
    private int parentEpicId = 0;

    public SubTask(String title, String description, int identificationNumber, Epic epic) {
        super(title, description, identificationNumber);
        this.parentEpicId = epic.getIdentificationNumber();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return parentEpicId == subTask.parentEpicId && taskType == subTask.taskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskType, parentEpicId);
    }
}
