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

public class HttpTaskManagerSubtasksTest {

    HttpTaskServer server = new HttpTaskServer(HttpServer.create(new InetSocketAddress(8080), 0));
    Gson gson = BaseHttpHandler.getGson();
    Epic epic1;
    Integer epic1Id;
    Subtask subtask1;
    Subtask subtask2;

    public HttpTaskManagerSubtasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        server.getManager().deleteAllTasks();
        server.getManager().deleteAllSubtasks();
        server.getManager().deleteAllEpics();
        epic1 = new Epic("Epic1", "Description Epic1");
        epic1Id = server.getManager().createEpic(epic1);
        subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 22, 0, 0),
                Duration.ofMinutes(10), epic1Id);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(server.getManager().getSubtasks());
        assertEquals(1, server.getManager().getSubtasks().size());
        assertEquals("Subtask1", server.getManager().getSubtasks().get(0).getName());
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        HttpRequest deleteRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, server.getManager().getSubtasks().size());
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        HttpRequest getRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, server.getManager().getSubtasks().size());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals(200, response.statusCode());
        assertEquals("Subtask1", jsonObject.get("name").getAsString());
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> addResponse = client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, addResponse.statusCode());
        assertEquals(1, server.getManager().getSubtasks().size());

        subtask2 = new Subtask(1, "Subtask2", "Description Subtask2", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 22, 0, 0),
                Duration.ofMinutes(10), epic1Id);
        taskJson = gson.toJson(subtask2);
        HttpRequest updateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, updateResponse.statusCode());
        assertEquals(1, server.getManager().getSubtasks().size());
        assertEquals("Subtask2", server.getManager().getSubtask(1).getName());
    }
}