import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicID;

    public Subtask(String name, String description, Status status, Integer epicID) {
        super(name, description, status);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, Status status, LocalDateTime startTime, Duration duration, Integer epicID) {
        super(name, description, status, startTime, duration);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(Integer id, String name, String description, Status status, LocalDateTime startTime, Duration duration, Integer epicID) {
        super(name, description, status, startTime, duration);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
        setId(id);
    }

    public Integer getEpicId() {
        return epicID;
    }

    @Override
    public String toString() {
        return getId() + "," + type.name() + "," + getName() + "," + getStatus().name() + "," + getDescription()
                + "," + getEpicId() + "," + getStartTime() + "," + getDuration().toMinutes() + "," + getEndTime();
    }
}