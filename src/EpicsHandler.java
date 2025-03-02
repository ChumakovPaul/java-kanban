import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        switch (requestMethod) {
            case "GET": {
                if (path.length == 2 && path[1].equals("epics")) {
                    getEpics(httpExchange);
                } else if (path.length == 3
                        && path[1].equals("epics")
                        && path[2].matches("\\d+")) {
                    getEpicByID(httpExchange, path);
                } else if (path.length == 4 && path[1].equals("epics")
                        && path[2].matches("\\d+")
                        && path[3].equals("subtasks")) {
                    getEpicSubtasks(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
            case "POST": {
                if (path.length == 2 && path[1].equals("epics")) {
                    createEpic(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
            }
            case "DELETE": {
                if (path.length == 3
                        && path[1].equals("epics")
                        && path[2].matches("\\d+")) {
                    deleteEpic(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }

        }
    }

    public void getEpics(HttpExchange httpExchange) throws IOException {
        List<Epic> epicsList = manager.getEpics();
        String response = gson.toJson(epicsList);
        sendText(httpExchange, response);
    }

    public void getEpicByID(HttpExchange httpExchange, String[] path) throws IOException {
        int epicId = Integer.parseInt(path[2]);
        try {
            String response = gson.toJson(manager.getEpic(epicId));
            sendText(httpExchange, response);
        } catch (NullTaskException e) {
            sendNotFound(httpExchange, "Эпик с заданным id не существует");
        }
    }

    public void getEpicSubtasks(HttpExchange httpExchange, String[] path) throws IOException {
        int epicId = Integer.parseInt(path[2]);
        try {
            List<Subtask> epicSubtaskList = manager.getEpicSubtasks(manager.getEpic(epicId));
            String response = gson.toJson(epicSubtaskList);
            sendText(httpExchange, response);
        } catch (NullTaskException e) {
            sendNotFound(httpExchange, "Эпик с заданным id не существует");
        }
    }

    public void createEpic(HttpExchange httpExchange, String[] path) throws IOException {
        String jsonEpic = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(jsonEpic, Epic.class);
        manager.createEpic(epic);
        sendCreated(httpExchange, "Эпик создан");
    }

    public void deleteEpic(HttpExchange httpExchange, String[] path) throws IOException {
        int epicId = Integer.parseInt(path[2]);
        manager.deleteEpic(epicId);
        sendText(httpExchange, "Эпик удален");
    }
}
