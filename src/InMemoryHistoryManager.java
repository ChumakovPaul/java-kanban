import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager<T> implements HistoryManager {

    public CustomList<Task> history;

    public InMemoryHistoryManager() {
        history = new CustomList<>();
    }

    @Override
    public void add(Task task) {
        if (history.map.containsKey(task.getId())) {
            remove(task.getId());
        }
        history.linkLast(task);
    }

    public void removeNode(Node node) {
        if (history.map.size() == 1) {
            history.map.clear();
            history.head = null;
            history.tail = null;
            return;
        } else if (node.next == null) {
            history.tail = node.prev; //0
            history.tail.next = null;
        } else if (node.prev == null) {
            history.head = node.next;
            history.head.prev = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        history.map.remove(node);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.getTasks());
    }

    @Override
    public void remove(int id) {
        removeNode(history.map.get(id));
    }

    class CustomList<T> {
        public Map<Integer, Node<T>> map = new HashMap<>();
        public Node<T> head;
        public Node<T> tail;

        public void linkLast(Task task) {
            Node newNode = new Node(task);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            map.put(task.getId(), newNode);
        }

        public List<Task> getTasks() {
            ArrayList<Task> list = new ArrayList<>();
            Node task = head;
            while (task.next != null) {
                list.add((Task) task.data);
                task = task.next;
            }
            list.add((Task) tail.data);
            return list;

        }

    }
}

