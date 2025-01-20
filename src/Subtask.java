public class Subtask extends Task {
    private Integer epicID;

    public Subtask(String name, String description, Status status, Integer epicID) {
        super(name, description, status);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
    }

    public Integer getEpicId() {
        return epicID;
    }

    @Override
    public String toString() {
        return getId() + "," + type.name() + "," + getName() + "," + getStatus().name()  + "," + getDescription() + "," + getEpicId();
    }

}
