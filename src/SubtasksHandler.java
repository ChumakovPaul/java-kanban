import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        switch (requestMethod) {
            case "GET": {
                if (path.length == 2) {
                    getSubtasks(httpExchange);
                } else if (path.length == 3) {
                    getSubtaskByID(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
            case "POST": {
                if (path.length == 2) {
                    createSubtask(httpExchange, path);
                } else if (path.length == 3) {
                    updateSubtask(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
            case "DELETE": {
                if (path.length == 3) {
                    deleteSubtask(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
        }
    }

    public void getSubtasks(HttpExchange httpExchange) throws IOException {
        List<Subtask> subtasksList = manager.getSubtasks();
        String response = gson.toJson(subtasksList);
        sendText(httpExchange, response);
    }

    public void getSubtaskByID(HttpExchange httpExchange, String[] path) throws IOException {
        int subtaskId = Integer.parseInt(path[2]);
        try {
            String response = gson.toJson(manager.getSubtask(subtaskId));
            sendText(httpExchange, response);
        } catch (NullPointerException e) {
            sendNotFound(httpExchange, "Сабтаск с заданным id не существует");
        }
    }

    public void createSubtask(HttpExchange httpExchange, String[] path) throws IOException {
        String jsonSubtask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
            manager.createSubtask(subtask);
            sendCreated(httpExchange, "Сабтаск создан");
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, "Добавляемая задача пересекается по времени с задачами из списка");
        }
    }

    public void updateSubtask(HttpExchange httpExchange, String[] path) throws IOException {
        String jsonSubtask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
            manager.updateSubtask(subtask);
            sendCreated(httpExchange, "Сабтаск обновлен");
        } catch (ManagerSaveException e) {
            sendNotFound(httpExchange, "Такой задачи нет");
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, "Добавляемая задача пересекается по времени с задачами из списка");
        }
    }

    public void deleteSubtask(HttpExchange httpExchange, String[] path) throws IOException {
        int subtaskId = Integer.parseInt(path[2]);
        manager.deleteSubtask(subtaskId);
        sendText(httpExchange, "Сабтаск удален");
    }
}
