import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    HttpTaskServer server = new HttpTaskServer(HttpServer.create(new InetSocketAddress(8080), 0));
    Gson gson = BaseHttpHandler.getGson();
    Task task1;
    Task task2;

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        task1 = new Task("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), Duration.ofMinutes(5));
        server.getManager().deleteAllTasks();
        server.getManager().deleteAllSubtasks();
        server.getManager().deleteAllEpics();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(server.getManager().getTasks(), "Задачи не возвращаются");
        assertEquals(1, server.getManager().getTasks().size(), "Некорректное количество задач");
        assertEquals("Test 2", server.getManager().getTasks().get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        HttpRequest deleteRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, server.getManager().getTasks().size());
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        HttpRequest getRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, server.getManager().getTasks().size());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals("Test 2", jsonObject.get("name").getAsString());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> addResponse = client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, addResponse.statusCode());
        assertEquals(1, server.getManager().getTasks().size());
        task2 = new Task(0, "Test 3", "Testing task 3",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        taskJson = gson.toJson(task2);
        HttpRequest updateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, updateResponse.statusCode());
        assertEquals(1, server.getManager().getTasks().size());
        assertEquals("Test 3", server.getManager().getTask(0).getName());
    }
}