import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        switch (requestMethod) {
            case "GET": {
                if (path.length == 2 && path[1].equals("tasks")) {
                    getTasks(httpExchange);
                } else if (path.length == 3
                        && path[1].equals("tasks")
                        && path[2].matches("\\d+")) {
                    getTaskByID(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
            case "POST": {
                if (path.length == 2 && path[1].equals("tasks")) {
                    createTask(httpExchange, path);
                } else if (path.length == 3
                        && path[1].equals("tasks")
                        && path[2].matches("\\d+")) {
                    updateTask(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
            case "DELETE": {
                if (path.length == 3
                        && path[1].equals("tasks")
                        && path[2].matches("\\d+")) {
                    deleteTask(httpExchange, path);
                } else {
                    sendNotFound(httpExchange, "Эндпоинт не найден");
                }
                break;
            }
        }
    }

    public void getTasks(HttpExchange httpExchange) throws IOException {
        List<Task> tasksList = manager.getTasks();
        String response = gson.toJson(tasksList);
        sendText(httpExchange, response);
    }

    public void getTaskByID(HttpExchange httpExchange, String[] path) throws IOException {
        int taskId = Integer.parseInt(path[2]);
        try {
            String response = gson.toJson(manager.getTask(taskId));
            sendText(httpExchange, response);
        } catch (NullTaskException e) {
            sendNotFound(httpExchange, "Таск с заданным id не существует");
        }
    }

    public void createTask(HttpExchange httpExchange, String[] path) throws IOException {
        String jsonTask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Task task = gson.fromJson(jsonTask, Task.class);
            manager.createTask(task);
            sendCreated(httpExchange, "Таск создан");
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, "Добавляемая задача пересекается по времени с задачами из списка");
        }
    }

    public void updateTask(HttpExchange httpExchange, String[] path) throws IOException {
        String jsonTask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Task task = gson.fromJson(jsonTask, Task.class);
            manager.updateTask(task);
            sendCreated(httpExchange, "Таск обновлен");
        } catch (ManagerSaveException e) {
            sendNotFound(httpExchange, "Такой задачи нет");
        } catch (IntersectionException e) {
            sendHasInteractions(httpExchange, "Добавляемая задача пересекается по времени с задачами из списка");
        }
    }

    public void deleteTask(HttpExchange httpExchange, String[] path) throws IOException {
        int taskId = Integer.parseInt(path[2]);
        manager.deleteTask(taskId);
        sendText(httpExchange, "Таск удален");
    }
}


