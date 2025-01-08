import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager<T> implements HistoryManager {

    public Map<Integer, Node<T>> history;
    public Node<T> head;
    public Node<T> tail;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    public void removeNode(Node node) {
        if (history.size() == 1) {
            history.clear();
            head = null;
            tail = null;
            return;
        } else if (node.next == null) {
            tail = node.prev; //0
            tail.next = null;
        } else if (node.prev == null) {
            head = node.next;
            head.prev = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        history.remove(node);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

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
        history.put(task.getId(), newNode);
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

    public class Node<T> {
        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}

