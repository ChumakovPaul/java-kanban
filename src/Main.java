import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Description task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                Duration.ofMinutes(10));
        Task task2 = new Task("Task2", "Description task2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 11, 0, 0),
                Duration.ofMinutes(10));
        Integer task1Id = manager.createTask(task1);
        Integer task2Id = manager.createTask(task2);
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        Epic epic2 = new Epic("Epic2", "Description Epic2");
        Integer epic1Id = manager.createEpic(epic1);
        Integer epic2Id = manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask1", "Description Subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 22, 0, 0),
                Duration.ofMinutes(10), epic1Id);
        Subtask subtask2 = new Subtask("Subtask2", "Description Subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 5, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        Subtask subtask3 = new Subtask("Subtask3", "Description Subtask3", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 33, 0, 0),
                Duration.ofMinutes(10), epic2Id);
        Integer subtask1Id = manager.createSubtask(subtask1);
        Integer subtask2Id = manager.createSubtask(subtask2);
        Integer subtask3Id = manager.createSubtask(subtask3);

        System.out.println("\nНепересекаемые по времени задачи:");
        manager.getPrioritizedTasks().forEach(System.out::println);
        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        Random random = new Random();

        System.out.println("\nВызываем таски:");
        System.out.println(manager.getTask(random.nextInt(2)));
        System.out.println(manager.getSubtask(random.nextInt(4, 7)));
        System.out.println(manager.getSubtask(random.nextInt(4, 7)));
        System.out.println(manager.getTask(random.nextInt(2)));
        System.out.println(manager.getTask(random.nextInt(2)));
        System.out.println(manager.getTask(random.nextInt(2)));
        System.out.println(manager.getEpic(random.nextInt(2, 4)));
        System.out.println(manager.getEpic(2));
        System.out.println(manager.getSubtask(4));
        System.out.println(manager.getSubtask(5));
        System.out.println(manager.getSubtask(6));
        System.out.println(manager.getEpic(random.nextInt(2, 4)));
        System.out.println(manager.getSubtask(random.nextInt(4, 7)));
        System.out.println(manager.getSubtask(random.nextInt(4, 7)));
        System.out.println(manager.getSubtask(random.nextInt(4, 7)));
        System.out.println(manager.getTask(random.nextInt(2)));
        System.out.println("-------------");

        System.out.println("\nИстория:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-------------");

        manager.deleteEpic(2);

        System.out.println("\nИстория после удаления Эпика с id 2:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-------------\n");
    }
}