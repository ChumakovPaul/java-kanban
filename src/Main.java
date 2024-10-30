public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Уборка", "Помыть полы и вынести мусор", Status.NEW);
        Task task2 = new Task("Учеба", "Послушать лекцию и решить задачу", Status.IN_PROGRESS);
        Integer task1Id = taskManager.createTask(task1);
        Integer task2Id = taskManager.createTask(task2);

        Epic epic1 = new Epic("Подготовить подарок", "Купить подарок и подарочную упаковку");
        Epic epic2 = new Epic("Договориться о встрече с риелтором", "Определить свободное время у себя и риелтора");
        Integer epic1Id = taskManager.createEpic(epic1);
        Integer epic2Id = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Купить подарок", "Купить подарок в магазине", Status.NEW, epic1Id);
        Subtask subtask2 = new Subtask("Купить подарочную упаковку", "Найти магазин и купить понравившуюся", Status.IN_PROGRESS, epic1Id);
        Subtask subtask3 = new Subtask("Позвонить риелтору", "Позвонить риелтору в начале недели", Status.NEW, epic2Id);
        Integer subtask1Id = taskManager.createSubtask(subtask1);
        Integer subtask2Id = taskManager.createSubtask(subtask2);
        Integer subtask3Id = taskManager.createSubtask(subtask3);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        System.out.println("\n---------------\n");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        task2.setStatus(Status.DONE);
        taskManager.updateTask(task2);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        System.out.println("\n---------------\n");

        taskManager.deleteEpic(epic1Id);
        taskManager.deleteTask(task2Id);
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
    }
}
