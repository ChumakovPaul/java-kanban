import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        setStatus(Status.NEW);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(getStartTime(), getEndTime());
    }

    @Override
    public String toString() {
        if (getStartTime() == null) {
            return getId() + "," + type.name() + "," + getName() + "," + getStatus().name() + "," + getDescription();
        } else {
            return getId() + "," + type.name() + "," + getName() + "," + getStatus().name() + "," + getDescription()
                    + "," + getStartTime() + "," + getDuration().toMinutes() + "," + getEndTime();
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}