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
        Subtask subtask3 = new Subtask("Позвонить риелтору", "Позвонить риелтору в начале недели", Status.NEW, epic2Id);
        Integer subtask1Id = inMemoryTaskManager.createSubtask(subtask1);
        Integer subtask2Id = inMemoryTaskManager.createSubtask(subtask2);
        Integer subtask3Id = inMemoryTaskManager.createSubtask(subtask3);
        printAllTasks(inMemoryTaskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            manager.getTask(task.getId());
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            manager.getEpic(epic.getId());
            System.out.println(epic);

            for (Task subtask : manager.getEpicSubtasks(epic)) {
                manager.getSubtask(subtask.getId());
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            manager.getSubtask(subtask.getId());
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
