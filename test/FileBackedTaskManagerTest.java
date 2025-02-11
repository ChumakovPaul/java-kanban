import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

class FileBackedTaskManagerTest {

    private static FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("test", ".csv");
        String fileName = file.getName();
        manager = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    void saveEmptyFileTest() {
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getSubtasks().size());
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void saveSomeTasksTest() {
        Task task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(500));
        Integer task1Id = manager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        Integer epic1Id = manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(500), epic1Id);
        Integer subtask1Id = manager.createSubtask(subtask1);
        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    void loadSomeTasksTest() throws IOException {
        Task task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(500));
        Integer task1Id = manager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        Integer epic1Id = manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(500), epic1Id);
        Integer subtask1Id = manager.createSubtask(subtask1);
        File file1 = new File(file.getName());
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file1);
        assertEquals(1, manager1.getTasks().size());
        assertEquals(1, manager1.getSubtasks().size());
        assertEquals(1, manager1.getEpics().size());
    }

    @Test
    void shouldThrowManagerSaveException() {
        File file1 = new File("new_file");
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file1);
        }, "Файл с таким именем не найден");
    }

    @Test
    void shouldNotThrowManagerSaveException() {
        Assertions.assertDoesNotThrow(() -> {
            FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file);
        });
    }
}