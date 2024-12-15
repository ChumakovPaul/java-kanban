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
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        Node<Task> node1 = new Node<>(task1);
        Node<Task> node2 = new Node<>(task2);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(node1.data, historyManager.getHistory().get(0));
        assertEquals(node2.data, historyManager.getHistory().get(1));
        assertNotNull(historyManager.history.getTasks());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.history.head.data);
        assertEquals(task2, historyManager.history.tail.data);
    }

    @Test
    void removeNodeTest() {
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(0);
        assertEquals(task2, historyManager.history.head.data);
        assertEquals(task2, historyManager.history.tail.data);
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


}