package com.example.tasks;

/**
 * 任务状态。用 enum 而不是字符串常量，拼错的状态在编译期就会报错。
 */
public enum TaskStatus {
    TODO,
    DOING,
    DONE
}
