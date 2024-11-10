import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    static Managers manager;

    @BeforeAll
    static void beforeAll() {
        manager = new Managers();
    }

    @Test
    void getDefaultTest() {
        InMemoryTaskManager taskManager = (InMemoryTaskManager) manager.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void getDefaultHistoryTest() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) manager.getDefaultHistory();
        assertNotNull(historyManager);
    }
}