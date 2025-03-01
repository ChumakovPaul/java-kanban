import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerPrioritizedTest {
    HttpTaskServer server = new HttpTaskServer(HttpServer.create(new InetSocketAddress(8080), 0));
    Gson gson = BaseHttpHandler.getGson();
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    public HttpTaskManagerPrioritizedTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        server.getManager().deleteAllTasks();
        server.getManager().deleteAllSubtasks();
        server.getManager().deleteAllEpics();
        task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10));
        task2 = new Task("Task2", "Description task2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 11, 0, 0),
                Duration.ofMinutes(10));
        Integer task1Id = server.getManager().createTask(task1);
        Integer task2Id = server.getManager().createTask(task2);
        epic1 = new Epic("Epic1", "Description Epic1");
        epic2 = new Epic("Epic2", "Description Epic2");
        Integer epic1Id = server.getManager().createEpic(epic1);
        Integer epic2Id = server.getManager().createEpic(epic2);
        subtask1 = new Subtask("Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 22, 0, 0),
                Duration.ofMinutes(10), epic1Id);
        subtask2 = new Subtask("Subtask2", "Description Subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 44, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        subtask3 = new Subtask("Subtask3", "Description Subtask3", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 33, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        Integer subtask1Id = server.getManager().createSubtask(subtask1);
        Integer subtask2Id = server.getManager().createSubtask(subtask2);
        Integer subtask3Id = server.getManager().createSubtask(subtask3);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    void getPrioritizedTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
        assertEquals(server.getManager().getPrioritizedTasks().size(), 5);
    }
}