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

public class HttpTaskManagerEpicsTest {

    HttpTaskServer server = new HttpTaskServer(HttpServer.create(new InetSocketAddress(8080), 0));
    Gson gson = BaseHttpHandler.getGson();
    Epic epic1;
    Subtask subtask1;


    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        server.getManager().deleteAllTasks();
        server.getManager().deleteAllSubtasks();
        server.getManager().deleteAllEpics();
        epic1 = new Epic("Epic1", "Description Epic1",
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2000, 1, 1, 0, 5, 0, 1));
        subtask1 = new Subtask(1, "Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(1999, 12, 31, 23, 55, 0, 0),
                Duration.ofMinutes(10), 0);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(server.getManager().getEpics());
        assertEquals(1, server.getManager().getEpics().size());
        assertEquals("Epic1", server.getManager().getEpics().get(0).getName());
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> addResponse = client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, addResponse.statusCode());
        assertEquals(1, server.getManager().getEpics().size());
        HttpRequest deleteRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/epics/0"))
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());
        assertEquals(0, server.getManager().getEpics().size());
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest addRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(url)
                .build();
        HttpResponse<String> addResponse = client.send(addRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, addResponse.statusCode());
        assertEquals(1, server.getManager().getEpics().size());
        HttpRequest getRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/epics/0"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, server.getManager().getEpics().size());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals("Epic1", jsonObject.get("name").getAsString());
    }
}