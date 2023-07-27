public class Task {
    protected TaskType taskType ;
    protected String title;
    protected String description;
    protected int identificationNumber;
    protected TaskStatus status;

    public Task(String title, String description, int identificationNumber) {
        this.title = title;
        this.description = description;
        this.identificationNumber = identificationNumber;
        status = TaskStatus.NEW;
        taskType = TaskType.TASK;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType=" + taskType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", identificationNumber=" + identificationNumber +
                ", status=" + status +
                '}';
    }
}
