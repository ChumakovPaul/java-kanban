import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    public static Integer generatedId;
    public HashMap<Integer, Task> taskHashMap;
    public HashMap<Integer, Epic> epicHashMap;
    public HashMap<Integer, Subtask> subtaskHashMap;

    public TaskManager() {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        generatedId = 0;
    }

    public List<Task> getTaskList() {
        List<Task> list = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            list.add(task);
        }
        return list;
    }

    public List<Epic> getEpicList() {
        List<Epic> list = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            list.add(epic);
        }
        return list;
    }

    public List<Subtask> getSubtaskList() {
        List<Subtask> list = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            list.add(subtask);
        }
        return list;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            updateEpicStatus(epic.getId());
        }
    }


    public Task getTask(Integer id) {
        if (taskHashMap.containsKey(id)) {
            return taskHashMap.get(id);
        }
        return null;
    }

    public Epic getEpic(Integer id) {
        if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id);
        }
        return null;
    }

    public Subtask getSubtask(Integer id) {
        if (subtaskHashMap.containsKey(id)) {
            return subtaskHashMap.get(id);
        }
        return null;
    }

    public Integer createTask(Task task) {
        task.setId(generatedId);
        taskHashMap.put(task.getId(), task);
        generatedId++;
        return task.getId();
    }

    public Integer createEpic(Epic epic) {
        epic.setId(generatedId);
        epicHashMap.put(epic.getId(), epic);
        generatedId++;
        return epic.getId();
    }

    public Integer createSubtask(Subtask subtask) {
        subtask.setId(generatedId);
        subtaskHashMap.put(subtask.getId(), subtask);
        generatedId++;
        updateEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.put(epic.getId(), epic);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Такой задачи нет");
        }

    }

    public void deleteTask(Integer id) {
        taskHashMap.remove(id);
    }

    public void deleteSubtask(Integer id) {
        Integer epicId = subtaskHashMap.get(id).getEpicId();
        subtaskHashMap.remove(id);
        updateEpicStatus(epicId);
    }

    public void deleteEpic(Integer id) {
        for (Subtask subtask : getSubtasksByEpic(epicHashMap.get(id))) {
            deleteSubtask(subtask.getId());
        }
        epicHashMap.remove(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            if (subtask.getEpicId().equals(epic.getId())) {
                list.add(subtask);
            }
        }
        return list;
    }

    private void updateEpicStatus(Integer id) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;
        for (Subtask subtask : getSubtasksByEpic(epicHashMap.get(id))) {
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
}
