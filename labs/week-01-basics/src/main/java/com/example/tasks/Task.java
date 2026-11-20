package com.example.tasks;

import java.time.Instant;

/**
 * 一条任务。record 自动生成构造器、访问器、equals、hashCode 和 toString。
 */
public record Task(
        long id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt) {

    public Task {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (status == null) {
            throw new IllegalArgumentException("状态不能为空");
        }
        description = description == null ? "" : description;
    }
}
