public class Subtask extends Task {
    private Integer epicID;

    public Subtask(String name, String description, Status status, Integer epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public Integer getEpicId() {
        return epicID;
    }
}
