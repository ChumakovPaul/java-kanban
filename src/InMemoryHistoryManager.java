import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static ArrayList<Task> history;
    private static Integer historyIndex;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
        historyIndex = 0;
    }

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            if (historyIndex == 10) {
                historyIndex = 0;
                history.set(historyIndex, task);
                historyIndex++;
            } else {
                history.set(historyIndex, task);
                historyIndex++;
            }
        } else {
            history.add(historyIndex, task);
            historyIndex++;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
