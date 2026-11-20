package com.example.tasks;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final TaskStore STORE = new TaskStore();

    public static void main(String[] args) {
        System.out.println("=== 任务列表 ===");

        boolean running = true;
        while (running && SCANNER.hasNextLine()) {
            printMenu();
            String choice = SCANNER.nextLine().trim();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> printTasks("全部任务", STORE.findAll());
                case "3" -> filterTasks();
                case "4" -> running = false;
                default -> System.out.println("无效选项，请输入 1-4。");
            }
        }

        System.out.println("已退出。");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. 新增任务");
        System.out.println("2. 列出全部任务");
        System.out.println("3. 按状态筛选");
        System.out.println("4. 退出");
        System.out.print("请选择：");
    }

    private static void addTask() {
        System.out.print("标题：");
        String title = SCANNER.nextLine().trim();

        System.out.print("描述（可留空）：");
        String description = SCANNER.nextLine().trim();

        try {
            Task task = STORE.add(title, description);
            System.out.println("已添加 #" + task.id() + " " + task.title());
        } catch (IllegalArgumentException e) {
            System.out.println("添加失败：" + e.getMessage());
        }
    }

    private static void filterTasks() {
        System.out.print("状态（TODO / DOING / DONE）：");
        String input = SCANNER.nextLine().trim();

        try {
            TaskStatus status = TaskStatus.valueOf(input.toUpperCase(Locale.ROOT));
            printTasks("状态为 " + status + " 的任务", STORE.findByStatus(status));
        } catch (IllegalArgumentException e) {
            System.out.println("无效状态：" + input + "，可选值：TODO、DOING、DONE。");
        }
    }

    private static void printTasks(String heading, List<Task> tasks) {
        System.out.println("--- " + heading + "（" + tasks.size() + "）---");
        if (tasks.isEmpty()) {
            System.out.println("没有任务。");
            return;
        }

        for (Task task : tasks) {
            System.out.println("#" + task.id() + " [" + task.status() + "] " + task.title());
            if (!task.description().isBlank()) {
                System.out.println("    " + task.description());
            }
        }
    }
}
