package com.example.tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 内存任务仓库。第 1 周只关心增、查、筛选，不涉及数据库。
 */
public class TaskStore {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    /**
     * 新增任务，状态默认是 TODO。
     *
     * @throws IllegalArgumentException 标题为空时
     */
    public Task add(String title, String description) {
        Task task = new Task(nextId++, title, description, TaskStatus.TODO, Instant.now());
        tasks.add(task);
        return task;
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public List<Task> findByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.status() == status)
                .toList();
    }
}
