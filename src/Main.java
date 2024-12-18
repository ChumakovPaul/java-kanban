import java.util.Random;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        Integer task1Id = inMemoryTaskManager.createTask(task1);
        Integer task2Id = inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Epic epic2 = new Epic("Договориться о встрече с риелтором", "Определить свободное время у себя и риелтора");
        Integer epic1Id = inMemoryTaskManager.createEpic(epic1);
        Integer epic2Id = inMemoryTaskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Subtask subtask3 = new Subtask("Позвонить риелтору", "Позвонить риелтору в начале недели", Status.NEW, epic1Id);
        Integer subtask1Id = inMemoryTaskManager.createSubtask(subtask1);
        Integer subtask2Id = inMemoryTaskManager.createSubtask(subtask2);
        Integer subtask3Id = inMemoryTaskManager.createSubtask(subtask3);
        printAllTasks(inMemoryTaskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        Random random = new Random();

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

        System.out.println("\nИстория:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("-------------");
    }
}
