import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    public InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    public static Integer generatedId;
    public HashMap<Integer, Task> taskHashMap;
    public HashMap<Integer, Epic> epicHashMap;
    public HashMap<Integer, Subtask> subtaskHashMap;

    public InMemoryTaskManager() {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        generatedId = 0;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            list.add(task);
        }
        return list;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> list = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            list.add(epic);
        }
        return list;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> list = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            list.add(subtask);
        }
        return list;
    }

    @Override
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public Task getTask(Integer id) {
        if (taskHashMap.containsKey(id)) {
            historyManager.add(taskHashMap.get(id));
            return taskHashMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (epicHashMap.containsKey(id)) {
            historyManager.add(epicHashMap.get(id));
            return epicHashMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        if (subtaskHashMap.containsKey(id)) {
            historyManager.add(subtaskHashMap.get(id));
            return subtaskHashMap.get(id);
        }
        return null;
    }

    @Override
    public Integer createTask(Task task) {
        task.setId(generatedId);
        taskHashMap.put(task.getId(), task);
        generatedId++;
        return task.getId();
    }

    @Override
    public Integer createEpic(Epic epic) {
        epic.setId(generatedId);
        epicHashMap.put(epic.getId(), epic);
        generatedId++;
        return epic.getId();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        subtask.setId(generatedId);
        subtaskHashMap.put(subtask.getId(), subtask);
        generatedId++;
        updateEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.put(epic.getId(), epic);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void deleteTask(Integer id) {
        if (historyManager.history.map.containsKey(id)) {
            historyManager.remove(id);
        }
        taskHashMap.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        Integer epicId = subtaskHashMap.get(id).getEpicId();
        if (historyManager.history.map.containsKey(id)) {
            historyManager.remove(id);
        }
        subtaskHashMap.remove(id);
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteEpic(Integer id) {
        for (Subtask subtask : getEpicSubtasks(epicHashMap.get(id))) {
            if ((historyManager.history.map.containsKey(subtask.getId()))) {
                historyManager.remove(subtask.getId());
            }
            deleteSubtask(subtask.getId());
        }
        if (historyManager.history.map.containsKey(id)) {
            historyManager.remove(id);
        }
        epicHashMap.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Task epic) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            if (subtask.getEpicId().equals(epic.getId())) {
                list.add(subtask);
            }
        }
        return list;
    }

    @Override
    public void updateEpicStatus(Integer id) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;
        for (Subtask subtask : getEpicSubtasks(epicHashMap.get(id))) {
            if (subtask.getStatus().equals(Status.NEW)) {
                newCounter++;
            } else if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                inProgressCounter++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                doneCounter++;
            }
        }
        if (inProgressCounter == 0 && doneCounter == 0) {
            epicHashMap.get(id).setStatus(Status.NEW);
        } else if (inProgressCounter != 0) {
            epicHashMap.get(id).setStatus(Status.IN_PROGRESS);
        } else if (newCounter == 0 && inProgressCounter == 0 && doneCounter != 0) {
            epicHashMap.get(id).setStatus(Status.DONE);
        }

    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
