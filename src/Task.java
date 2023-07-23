public class Task {
    protected final TaskType taskType = TaskType.TASK;
    private String title;
    private String description;
    private int identificationNumber;
    private TaskStatus status;

    public Task(String title, String description, int identificationNumber) {
        this.title = title;
        this.description = description;
        this.identificationNumber = identificationNumber;
        TaskStatus status = TaskStatus.NEW;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(int identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

}
