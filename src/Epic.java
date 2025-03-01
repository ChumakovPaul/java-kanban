import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description, startTime, Duration.between(startTime, endTime));
        this.type = TaskType.EPIC;
        this.endTime = endTime;
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

    public void setDuration() {
        if (getStartTime() != null & getEndTime() != null) {
            this.duration = Duration.between(getStartTime(), getEndTime());
        }
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