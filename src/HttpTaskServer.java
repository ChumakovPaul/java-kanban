import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class HttpTaskServer {

    private final HttpServer httpServer;
    private final TaskManager manager;

    public HttpTaskServer(HttpServer httpServer) {
        this.httpServer = httpServer;
        this.manager = Managers.getDefault();
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/history", new HistoryHandle(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandle(manager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(HttpServer.create(new InetSocketAddress(8080), 0));
        Task task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10));
        Task task2 = new Task("Task2", "Description task2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 11, 0, 0),
                Duration.ofMinutes(10));
        Integer task1Id = server.manager.createTask(task1);
        Integer task2Id = server.manager.createTask(task2);
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        Epic epic2 = new Epic("Epic2", "Description Epic2");
        Integer epic1Id = server.manager.createEpic(epic1);
        Integer epic2Id = server.manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 22, 0, 0),
                Duration.ofMinutes(10), epic1Id);
        Subtask subtask2 = new Subtask("Subtask2", "Description Subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 44, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        Subtask subtask3 = new Subtask("Subtask3", "Description Subtask3", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 33, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        Integer subtask1Id = server.manager.createSubtask(subtask1);
        Integer subtask2Id = server.manager.createSubtask(subtask2);
        Integer subtask3Id = server.manager.createSubtask(subtask3);
        Random random = new Random();
        System.out.println(server.manager.getTask(random.nextInt(2)));
        System.out.println(server.manager.getEpic(random.nextInt(2, 4)));
        System.out.println(server.manager.getEpic(2));
        System.out.println(server.manager.getSubtask(4));
        System.out.println(server.manager.getSubtask(5));
        System.out.println(server.manager.getSubtask(6));

        server.start();
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public TaskManager getManager() {
        return manager;
    }
}
