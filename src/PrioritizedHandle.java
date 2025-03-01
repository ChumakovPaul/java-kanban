import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandle extends BaseHttpHandler {
    public PrioritizedHandle(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            getPrioritized(exchange);
        } else {
            sendNotFound(exchange, "Эндпоинт не найден");
        }
    }

    public void getPrioritized(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        String response = gson.toJson(prioritizedTasks);
        sendText(exchange, response); // Отправляем ответ
    }
}
