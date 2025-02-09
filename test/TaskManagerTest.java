import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    private static InMemoryTaskManager taskManager;

    @Test
    void deleteAllTasksTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        Integer task1Id = taskManager.createTask(task1);
        Integer task2Id = taskManager.createTask(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteAllEpicsTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Epic epic2 = new Epic("Договориться о встрече с риелтором", "Определить свободное время у себя и риелтора");
        Integer epic1Id = taskManager.createEpic(epic1);
        Integer epic2Id = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Subtask subtask3 = new Subtask("Позвонить риелтору", "Позвонить риелтору в начале недели", Status.NEW, epic2Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        Integer subtask3Id = taskManager.createSubtask(subtask3);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteAllSubtasksTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Epic epic2 = new Epic("Договориться о встрече с риелтором", "Определить свободное время у себя и риелтора");
        Integer epic1Id = taskManager.createEpic(epic1);
        Integer epic2Id = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Subtask subtask3 = new Subtask("Позвонить риелтору", "Позвонить риелтору в начале недели", Status.NEW, epic2Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        Integer subtask3Id = taskManager.createSubtask(subtask3);
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void createTaskTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        assertEquals(taskManager.taskHashMap.get(task1Id), task1);
    }

    @Test
    void createEpicTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        assertEquals(taskManager.epicHashMap.get(epic1Id), epic1);
    }

    @Test
    void createSubtaskTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        assertEquals(taskManager.subtaskHashMap.get(subtask1Id), subtask1);
    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Уборка", "Помыть полы и вынести мусор", Status.IN_PROGRESS);
        Integer task1Id = taskManager.createTask(task1);
        task2.setId(task1Id);
        taskManager.updateTask(task2);
        assertEquals(taskManager.getTask(task1Id).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void updateEpicTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Epic epic2 = new Epic("Договориться о встрече с риелтором", "Определить свободное время у себя и риелтора");
        Integer epic1Id = taskManager.createEpic(epic1);
        epic2.setId(epic1Id);
        taskManager.updateEpic(epic2);
        assertEquals(taskManager.getEpic(epic1Id).getName(), "Договориться о встрече с риелтором");
        assertEquals(taskManager.getEpic(epic1Id).getDescription(), "Определить свободное время у себя и риелтора");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.IN_PROGRESS, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        subtask2.setId(subtask1Id);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getSubtask(subtask1Id).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void deleteTaskTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Integer task1Id = taskManager.createTask(task1);
        taskManager.deleteTask(task1Id);
        assertFalse(taskManager.taskHashMap.containsKey(task1Id));
    }

    @Test
    void deleteSubtaskTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        taskManager.deleteSubtask(subtask1Id);
        assertFalse(taskManager.subtaskHashMap.containsKey(subtask1Id));
    }

    @Test
    void deleteEpicTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        taskManager.deleteEpic(epic1Id);
        assertFalse(taskManager.epicHashMap.containsKey(epic1Id));
    }

    @Test
    void getEpicSubtasksTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        assertEquals(2, taskManager.getEpicSubtasks(epic1).size());
    }

    @Test
    void updateEpicStatusTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        assertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW);
        Subtask subtask2 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.DONE, epic1Id);
        subtask2.setId(subtask1Id);
        taskManager.updateSubtask(subtask2);
        assertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.DONE);
    }

    @Test
    void getHistoryTest() {
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Integer epic1Id = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        taskManager.getEpic(epic1Id);
        taskManager.getSubtask(subtask1Id);
        assertEquals(taskManager.historyManager.getHistory().get(0), epic1);
        assertEquals(taskManager.historyManager.getHistory().get(1), subtask1);
    }
}
