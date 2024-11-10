import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        historyManager.add(task1);
        assertEquals(historyManager.getHistory().get(0), task1);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        historyManager.add(task1);
        historyManager.add(task2);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());

    }
}