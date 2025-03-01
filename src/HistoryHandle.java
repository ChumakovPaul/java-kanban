import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class HistoryHandle extends BaseHttpHandler {
    public HistoryHandle(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            getHistory(httpExchange);
        } else {
            sendNotFound(httpExchange, "Эндпоинт не найден");
        }
    }

    public void getHistory(HttpExchange exchange) throws IOException {
        List<Task> historyTasks = manager.getHistory();
        String response = gson.toJson(historyTasks);
        sendText(exchange, response);
    }
}