import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Subtask getSubtask(Integer id);

    Integer createTask(Task task);

    Integer createEpic(Epic epic);

    Integer createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(Integer id);

    void deleteSubtask(Integer id);

    void deleteEpic(Integer id);

    ArrayList<Subtask> getEpicSubtasks(Task task);

    void updateEpicStatus(Integer id);

    List<Task> getHistory();
}

