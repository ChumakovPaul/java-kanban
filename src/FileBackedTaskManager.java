import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Integer createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public Integer createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicStatus(Integer id) {
        super.updateEpicStatus(id);
        save();
    }

    public Task fromString(String value) {
        String[] task = value.split(",");
        Task result = null;
        if (task.length != 1) {
            switch (task[1]) {
                case ("TASK"):
                    result = new Task(task[2], task[4], Status.valueOf(task[3]), LocalDateTime.parse(task[5]),
                            Duration.ofMinutes(Long.parseLong(task[6])));
                    break;
                case ("EPIC"):
                    result = new Epic(task[2], task[4]);
                    break;
                case ("SUBTASK"):
                    result = new Subtask(task[2], task[4], Status.valueOf(task[3]), LocalDateTime.parse(task[6]),
                            Duration.ofMinutes(Long.parseLong(task[7])), Integer.valueOf(task[5]));
                    break;
            }
            result.setId(Integer.valueOf(task[0]));
        }
        return result;
    }

    private void save() {
        String fileName = file.getName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("id,type,name,status,description,epic,startTime,duration,endTime\n");
            for (Task value : taskHashMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
            for (Epic value : epicHashMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
            for (Subtask value : subtaskHashMap.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    static FileBackedTaskManager loadFromFile(File loadFile) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(loadFile);
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(loadFile))) {
            reader.readLine();
            while (reader.ready()) {
                line = reader.readLine();
                Task task = manager.fromString(line);
                if (task == null) {
                    continue;
                }
                switch (task.getType().name()) {
                    case ("TASK"):
                        manager.createTask(task);
                        break;
                    case ("EPIC"):
                        manager.createEpic((Epic) task);
                        break;
                    case ("SUBTASK"):
                        manager.createSubtask((Subtask) task);
                        break;
                }

            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

        generatedId = maxId(manager);
        if (generatedId != 0) {
            generatedId++;
        }
        return manager;
    }

    private static int maxId(FileBackedTaskManager manager) {
        int maxId = 0;
        for (Task task : manager.getTasks()) {
            if (maxId < task.getId()) {
                maxId = task.getId();
            }
        }
        for (Epic epic : manager.getEpics()) {
            if (maxId < epic.getId()) {
                maxId = epic.getId();
            }
        }
        for (Subtask subtask : manager.getSubtasks()) {
            if (maxId < subtask.getId()) {
                maxId = subtask.getId();
            }
        }
        return maxId;
    }
}