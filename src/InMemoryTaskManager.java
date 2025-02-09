import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    public static Integer generatedId;
    public HashMap<Integer, Task> taskHashMap;
    public HashMap<Integer, Epic> epicHashMap;
    public HashMap<Integer, Subtask> subtaskHashMap;
    public Set<Task> sortedByDateTasks;

    public InMemoryTaskManager() {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        sortedByDateTasks = new TreeSet<>(new TaskDateComparator());
        generatedId = 0;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        taskHashMap.values().forEach(task -> list.add(task));
        return list;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> list = new ArrayList<>();
        epicHashMap.values().forEach(epic -> list.add(epic));
        return list;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> list = new ArrayList<>();
        subtaskHashMap.values().forEach(subtask -> list.add(subtask));
        return list;
    }

    @Override
    public void deleteAllTasks() {
        taskHashMap.values().forEach(task -> sortedByDateTasks.remove(task));
        taskHashMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtaskHashMap.values().forEach(subtask -> sortedByDateTasks.remove(subtask));
        subtaskHashMap.clear();
        epicHashMap.values().forEach(epic -> sortedByDateTasks.remove(epic));
        epicHashMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
        epicHashMap.values().stream().forEach(epic -> updateEpicStatus(epic.getId()));
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
        if (task.getId() == null) {
            task.setId(generatedId);
            generatedId++;
        }
        taskHashMap.put(task.getId(), task);
        if (task.getStartTime() != null) {
            addTaskToPrioritizedTasks(task);
        }
        return task.getId();
    }

    @Override
    public Integer createEpic(Epic epic) {
        if (epic.getId() == null) {
            epic.setId(generatedId);
            generatedId++;
        }
        epicHashMap.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            subtask.setId(generatedId);
            generatedId++;
        }
        subtaskHashMap.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
        if (subtask.getStartTime() != null) {
            addTaskToPrioritizedTasks(subtask);
        }
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
            addTaskToPrioritizedTasks(task);
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
            addTaskToPrioritizedTasks(subtask);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void deleteTask(Integer id) throws ManagerSaveException {
        if (historyManager.history.containsKey(id)) {
            historyManager.remove(id);
        }
        sortedByDateTasks.remove(taskHashMap.get(id));
        taskHashMap.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) throws ManagerSaveException {
        Integer epicId = subtaskHashMap.get(id).getEpicId();
        if (historyManager.history.containsKey(id)) {
            historyManager.remove(id);
        }
        sortedByDateTasks.remove(subtaskHashMap.get(id));
        subtaskHashMap.remove(id);
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteEpic(Integer id) throws ManagerSaveException {
        getEpicSubtasks(epicHashMap.get(id)).forEach(subtask -> {
            if ((historyManager.history.containsKey(subtask.getId()))) {
                historyManager.remove(subtask.getId());
            }
            deleteSubtask(subtask.getId());
        });
        epicHashMap.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Task epic) {
        ArrayList<Subtask> list = new ArrayList<>(subtaskHashMap.values().stream().filter(
                        subtask -> subtask.getEpicId().equals(epic.getId()))
                .toList());
        return list;
    }

    @Override
    public void updateEpicStatus(Integer id) throws ManagerSaveException {
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
        } else if (newCounter == 0 && inProgressCounter == 0 && doneCounter != 0) {
            epicHashMap.get(id).setStatus(Status.DONE);
        } else {
            epicHashMap.get(id).setStatus(Status.IN_PROGRESS);
        }
        determineEpicDuration(epicHashMap.get(id));
    }

    public void determineEpicDuration(Epic epic) {
        getEpicSubtasks(epic).forEach(subtask -> {
            if (epic.getStartTime() == null && subtask.getStartTime() != null) {
                epic.setStartTime(subtask.getStartTime());
                epic.setEndTime(subtask.getEndTime());
                return;
            }
            if (subtask.getStartTime() != null && epic.getStartTime().isAfter(subtask.getStartTime())) {
                epic.setStartTime(subtask.getStartTime());
            }
            if (subtask.getStartTime() != null && epic.getEndTime().isBefore(subtask.getEndTime())) {
                epic.setEndTime(subtask.getEndTime());
            }
        });
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(sortedByDateTasks);
    }

    public void addTaskToPrioritizedTasks(Task task) {
        if (getPrioritizedTasks().stream().noneMatch(prioritizedTask -> isIntersect(task, prioritizedTask))) {
            sortedByDateTasks.add(task);
            System.out.println("задача добавлена");
        } else {
            System.out.println("Добавляемая задача пересекается по времени с задачами из списка");
        }
    }

    public boolean isIntersect(Task task1, Task task2) {
        return (task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getStartTime()))
                || (task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getEndTime()));
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}