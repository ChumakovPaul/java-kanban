import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
        assertNotNull(historyManager.getTasks());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.head.data);
        assertEquals(task2, historyManager.tail.data);
    }

    @Test
    void removeNodeTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        Task task3 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task4 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        Task task5 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task6 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        task4.setId(3);
        task5.setId(4);
        task6.setId(5);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        assertEquals(task1, historyManager.head.data);
        assertEquals(task6, historyManager.tail.data);
        historyManager.remove(0);
        historyManager.remove(3);
        historyManager.remove(5);
        assertEquals(task2, historyManager.head.data);
        assertEquals(task5, historyManager.tail.data);
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void getNullHistoryTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        task1.setId(0);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(0);
        assertThrows(NullPointerException.class, () -> historyManager.getHistory());
    }

    @Test
    void getHistoryWithDuplicationTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());
    }
}