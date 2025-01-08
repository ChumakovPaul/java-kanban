public class Epic extends Task {
    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        setStatus(Status.NEW);
    }
}
