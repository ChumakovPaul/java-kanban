import java.util.List;

public interface HistoryManager<T> {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
