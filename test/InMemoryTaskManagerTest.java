import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void equalsTasksWithSameId() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        Task task2 = taskManager.getTask(task1Id);
        assertEquals(task1, task2);
    }

    @Test
    void equalsEpicsWithSameId() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Epic epic2 = taskManager.getEpic(epic1Id);
        assertEquals(epic1, epic2);
    }

    @Test
    void equalsSubtasksWithSameId() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = taskManager.getSubtask(subtask1Id);
        assertEquals(subtask1, subtask2);
    }

    @Test
    void shouldErrorToAddEpicAsSubtask() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        assertNotEquals("Subtask", epic1.getClass().getSimpleName(), "Нельзя добавить Epic в качестве Subtask");
    }

    @Test
    void shouldErrorToAddSubtaskAsEpic() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        assertNotEquals("Epic", subtask1.getClass().getSimpleName(), "Нельзя добавить Subtask в качестве Epic");
    }

    @Test
    void shouldFindDifferentTasksById() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        assertNotNull(taskManager.getEpics());
        assertNotNull(taskManager.getSubtasks());
        assertNotNull(taskManager.getTasks());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        assertEquals(task1, taskManager.getTask(task1Id));
    }

    @Test
    void shouldNotConflictBetweenDifferentKindsOfId() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        assertNotEquals(subtask1Id, subtask1.getEpicId());
    }

    @Test
    void shouldNoDifferenceBetweenTaskAndTaskInManager() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        Task task = taskManager.getTask(task1Id);
        assertEquals(task.getName(), taskManager.getTask(task1Id).getName());
        assertEquals(task.getDescription(), taskManager.getTask(task1Id).getDescription());
        assertEquals(task.getId(), taskManager.getTask(task1Id).getId());
        assertEquals(task.getStatus(), taskManager.getTask(task1Id).getStatus());
    }

    @Test
    void shouldNoDifferenceBetweenTaskAndTaskInHistoryManager() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        Task task = taskManager.getTask(task1Id);
        taskManager.historyManager.add(task1);
        assertEquals(task.getName(), taskManager.getTask(task1Id).getName());
        assertEquals(task.getDescription(), taskManager.getTask(task1Id).getDescription());
        assertEquals(task.getId(), taskManager.getTask(task1Id).getId());
        assertEquals(task.getStatus(), taskManager.getTask(task1Id).getStatus());
    }

    @Test
    void shouldErrorToChangeSubtaskId() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        taskManager.getEpic(epic1Id);
        taskManager.getSubtask(subtask1Id);
        taskManager.getSubtask(subtask2Id);
        List<Task> list1 = taskManager.getHistory();
        List<Task> list2 = taskManager.getHistory();
        assertEquals(list2, list1);
    }

    @Test
    void shouldShowNewEpicStatus() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся",
                Status.NEW,
                LocalDateTime.of(2000, 1, 1, 1, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void shouldShowDoneEpicStatus() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.DONE,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся",
                Status.DONE,
                LocalDateTime.of(2000, 1, 1, 1, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void shouldShowInProgressEpicStatusWithDifferentSubtasks() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся",
                Status.DONE,
                LocalDateTime.of(2000, 1, 1, 1, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        taskManager.updateEpicStatus(epic1Id);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void shouldShowInProgressEpicStatusWithInProgressSubtasks() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся",
                Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 1, 11, 0, 0),
                Duration.ofMinutes(10),
                epic1Id);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
        assertEquals(epic1Id, subtask1.getEpicId());
    }

    @Test
    void shouldShowIntersectionBetweenTasks() {
        Task task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10));
        Task task2 = new Task("Task2", "Description task2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 5, 0, 0),
                Duration.ofMinutes(10));
        Integer task1Id = taskManager.createTask(task1);
        Assertions.assertThrows(IntersectionException.class, () -> {
            Integer task2Id = taskManager.createTask(task2);
        });
    }
}