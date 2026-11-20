# Week 01：Java 基础

目标：不引入框架和数据库，用纯 Java 写一个能运行、能筛选的内存任务列表。

## 你要写的类

| 文件 | 内容 |
| --- | --- |
| `Status.java` | `enum`：`TODO`、`DOING`、`DONE` |
| `Task.java` | `record`：`id`、`title`、`status`、`createdAt` |
| `TaskStore.java` | 用 `List<Task>` 保存任务，提供新增、列出、按状态筛选 |
| `Main.java` | 菜单循环，读取控制台输入并调用 `TaskStore` |

`Main.java` 已建好空壳，其余类由你自己创建。

## 命令

```bash
# 编译
mvn -q compile

# 运行（在 module 根目录执行）
java -cp target/classes com.example.tasks.Main
```

在 VS Code 里也可以直接打开 `Main.java`，点 `Run`。

## 验收

- [ ] 新增任务后能立刻列出
- [ ] 输入 `TODO` / `DOING` / `DONE` 能筛出对应任务
- [ ] 输入不存在的状态时有可读提示，程序不崩溃
- [ ] 在筛选逻辑处打断点，能说清每次循环的变量值
- [ ] 能回答：`record` 和普通类有什么区别？`List<Task>` 和 `Task[]` 哪里不一样？

## 提示

- 字符串比较用 `equals`，不用 `==`
- `List.of(...)` 创建的是不可变列表，新增任务请用 `ArrayList`
- 时间可以用 `Instant.now()`，别急着格式化
- 先让程序跑通，再考虑拆方法；第 2 周才会引入接口
