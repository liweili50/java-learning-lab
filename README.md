# Java Learning Lab

从 TypeScript / Node.js / Express 转向 Java 后端的 6 周学习计划。

主线项目是一个**任务管理 API**：创建任务、按状态筛选、修改状态、查看状态变更历史。每周围绕同一个项目增加一层能力，最后用 `curl` 和自动化测试验收。

> 本仓库只覆盖 Java 后端。前端已掌握，不安排 Vue / React 联调。

---

## 目标与前提

**已有基础（不需要重学）**

- HTTP、REST、JSON、状态码
- 前后端分离、接口联调
- 异步、模块化、包管理的基本概念

**6 周后应当能够**

- 独立搭建并运行一个 Spring Boot 项目
- 用分层结构组织代码，并解释每层的职责
- 连接云 PostgreSQL，写出参数化 SQL 和事务
- 为业务逻辑和 API 写测试
- 看懂并审查 AI 生成的 Java 代码，而不是照单全收

**投入**：每周 6–8 小时，建议拆成 3 次、每次 2 小时左右。连续小步提交，胜过周末突击。

---

## 技术栈

| 类别 | 选择 | 说明 |
| --- | --- | --- |
| JDK | Eclipse Temurin 21 (LTS) | `brew install --cask temurin@21` |
| 编辑器 | VS Code + Extension Pack for Java | 需要 Spring 提示时再加 Spring Boot Extension Pack |
| 构建 | Maven（使用 `mvnw`） | 只学一种，暂时不碰 Gradle |
| 框架 | Spring Boot（Spring Initializr 选当前稳定版） | 教程多为 3.x，概念一致；遇到 API 差异以官方文档为准 |
| 数据库 | Neon 免费版 PostgreSQL | 不安装本地数据库，不用 Docker 跑数据库 |
| 迁移 | Flyway | 表结构进版本控制 |
| 测试 | JUnit 5 + Spring Boot Test | 单元测试 + MockMvc |
| CI | GitHub Actions | 默认流水线不连接数据库 |

**暂时不学**：登录鉴权、Redis、消息队列、微服务、Kubernetes、前端联调。

---

## 环境准备（第 1 周开始前完成）

```bash
java -version        # 应显示 21.x
mvn -v               # 可选；项目内统一用 ./mvnw
```

VS Code 安装扩展：

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
- 可选：[Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack)

第 4 周再注册 [Neon](https://console.neon.tech/signup)，创建免费项目。免费版无需信用卡，空闲时计算会自动挂起，首次连接可能慢几百毫秒，属于正常现象。

---

## 6 周路线

| 周次 | 主题 | 交付物 | 验收标准 |
| --- | --- | --- | --- |
| [1](#第-1-周java-基础) | Java 基础 | `labs/week-01-basics` 命令行任务列表 | 能新增、列出、按状态筛选；能用断点跟踪一次调用 |
| [2](#第-2-周java-工程化) | 工程化 | 接口 + 内存实现 + 单元测试 | `./mvnw test` 全绿，覆盖空标题、任务不存在 |
| [3](#第-3-周spring-boot) | Spring Boot | `backend/` REST API（仍用内存存储） | `curl` 与 MockMvc 覆盖成功、`400`、`404` |
| [4](#第-4-周云-postgresql) | 云 PostgreSQL | JDBC 仓储 + Flyway 迁移 | 空库能跑迁移；重启服务后数据仍在 |
| [5](#第-5-周jpa-与事务) | JPA 与事务 | JPA 持久化 + 状态历史 | 测试分支上验证：历史写入失败时状态更新回滚 |
| [6](#第-6-周后端交付) | 测试与交付 | README、API 示例、CI、毕业题 | CI 无需数据库密码即可通过 |

每周结束在 `notes/week-0X.md` 写复盘，并勾选下方进度。

---

## 第 1 周：Java 基础

**学**：类与构造器、`record`、`enum`、`List` / `Map`、`Optional`、基本类型与包装类型、`null` 的风险、包与访问修饰符。

**做**：在 `labs/week-01-basics/` 写一个命令行程序：

- `Task` 用 `record` 表示：`id`、`title`、`status`、`createdAt`
- `Status` 用 `enum`：`TODO`、`DOING`、`DONE`
- 支持新增、列出、按状态筛选
- 数据只放内存，不连数据库，不引入框架

**验收**

- [ ] 程序能运行，输出格式自己看得懂
- [ ] 在 VS Code 给筛选逻辑打断点，能说清每次循环的变量值
- [ ] 能用自己的话回答：`record` 和普通类有什么区别？`List<Task>` 和 TS 的 `Task[]` 哪里不一样？

**AI 用法**：让 AI 用 TS 类比解释概念，但**不写实现**。写完再让它审查。

> 常见坑：`==` 比较对象引用，字符串比较用 `equals`；`List.of()` 不可变；`int` 与 `Integer` 的区别会在后面 DTO 校验时再次出现。

---

## 第 2 周：Java 工程化

**学**：接口、泛型、受检异常与运行时异常、Maven 生命周期、JUnit 5、`assertThrows`。

**做**：把第 1 周的程序改造成可测试的结构：

- `TaskRepository` 接口 + `InMemoryTaskRepository` 实现
- `TaskService` 存放业务规则：标题不能为空、任务不存在时抛异常
- 自定义异常，例如 `TaskNotFoundException`
- 用 JUnit 覆盖正常路径与边界

**验收**

- [ ] `./mvnw test` 全绿
- [ ] 测试包含：正常创建、空标题、按状态筛选、任务不存在
- [ ] 能解释为什么 Service 依赖接口而不是具体实现

**AI 用法**：让它只列「应该测哪些边界」，你先写测试，再写实现。

---

## 第 3 周：Spring Boot

**学**：依赖注入、构造器注入、`@RestController`、`@Service`、DTO、Bean Validation、`@RestControllerAdvice`、MockMvc。

**做**：用 [Spring Initializr](https://start.spring.io/) 生成 `backend/`，依赖只选 Spring Web 和 Validation。把第 2 周的 Service 接成 REST API，存储仍用内存实现。

建议接口：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/api/tasks?status=TODO` | 列表，可选状态筛选 |
| `POST` | `/api/tasks` | 创建，标题必填 |
| `PATCH` | `/api/tasks/{id}/status` | 修改状态 |
| `GET` | `/api/tasks/{id}` | 详情 |

**验收**

- [ ] `curl` 创建成功返回 `201`
- [ ] 空标题返回 `400`，响应体有可读的错误信息
- [ ] 不存在的 id 返回 `404`
- [ ] 至少一条 MockMvc 测试
- [ ] 能对照 Express 说出：路由、参数解析、错误中间件分别对应 Spring 的什么

**AI 用法**：先自己写 Controller，卡住时只问「这个注解在这个位置的作用是什么」。

---

## 第 4 周：云 PostgreSQL

**学**：表结构设计、主键与外键、参数化查询、JDBC、连接池、Flyway 迁移。

**做**：

1. 在 Neon 控制台用 SQL Editor 手写 `tasks` 表并插入两行，先熟悉 SQL
2. 用 JDBC（`JdbcTemplate` 或原生 JDBC，二选一）实现 `TaskRepository`
3. 用 Flyway 管理 `V1__create_tasks.sql`
4. 数据库凭据全部走环境变量

```bash
# 示例，不要提交到仓库
export DB_URL='jdbc:postgresql://<host>/<db>?sslmode=require'
export DB_USER='<user>'
export DB_PASSWORD='<password>'
```

**验收**

- [ ] 在空数据库上启动应用，Flyway 自动建表
- [ ] 创建任务后重启服务，数据仍然存在
- [ ] 所有 SQL 都使用 `?` 占位符，没有字符串拼接
- [ ] 仓库中没有 `.env`、密码或连接串

**AI 用法**：让它帮你审查 SQL 与迁移脚本，尤其是指出哪些写法有注入风险或会在并发下出错。

---

## 第 5 周：JPA 与事务

**学**：实体与 DTO 的边界、Spring Data JPA、分页、`@Transactional`、事务传播的基本直觉。

**做**：

- 用 JPA 替换第 4 周的 JDBC 仓储
- 新增 `task_events` 表记录状态变更
- 修改状态时，同一事务内更新任务并写入一条历史
- 列表接口支持分页

**验收**

- [ ] 能解释为什么实体不直接当 API 返回值
- [ ] 在 Neon 建一个独立测试分支，人为让历史写入失败，确认状态更新也回滚
- [ ] 分页参数非法时返回 `400`

**AI 用法**：让它出 3 个「事务边界写错了」的场景题，你判断对错并说明理由。

> 注意：免费版 Neon 适合手动验证，不建议在 CI 里连接。CI 用 mock 或不依赖数据库的测试。

---

## 第 6 周：后端交付

**学**：集成测试的取舍、GitHub Actions、README 即文档。

**做**：

- 补 `backend/README.md`：启动方式、环境变量、接口示例、错误码说明
- 加 GitHub Actions：`./mvnw -B verify`，不配置任何数据库密钥
- 整理提交历史，确认没有敏感信息
- 完成毕业题

**毕业题（闭卷，不使用 AI）**

给任务增加 `priority` 字段（`LOW` / `NORMAL` / `HIGH`）：

1. 新增 Flyway 迁移
2. 更新实体与 DTO
3. 增加校验与默认值
4. 更新接口文档与测试

能独立完成，说明前 5 周的知识已经串起来了。

---

## 目录结构

```text
java-learning-lab/
├── README.md              # 本文件：路线、验收、AI 用法
├── notes/
│   ├── _template.md       # 每周复盘模板
│   └── week-01.md ...     # 每周一份
├── labs/
│   ├── week-01-basics/
│   └── week-02-engineering/
├── backend/               # 第 3 周创建
└── .gitignore
```

---

## 用 AI 学习的方式

固定流程：**问验收条件 → 自己写 20–30 分钟 → 卡住只要提示 → 跑测试 → 让 AI 审查 diff**。

原则：

- 不让 AI 一次生成整个功能
- 报错时提供失败的测试或脱敏日志，不给完整源码
- 每次审查后，用自己的话复述结论
- 毕业题必须闭卷

### 提示词 1：开始一个练习

```text
你是我的 Java 导师。我熟悉 TypeScript、Node.js、Express。
今天的任务是：【填写一个小目标】。

请先给出可验证的验收条件，然后用 TS/Express 类比解释相关概念，
并明确指出这个类比在哪里不成立。

不要修改文件，不要给完整实现。最多给一个 5 行以内的代码片段说明语法。
```

### 提示词 2：卡住时

```text
我卡在这个问题上：【现象】。
我已经尝试：【尝试】。
相关代码：【贴最小片段】。
失败输出：【贴测试或日志】。

不要直接给答案。先给我一个最可能成立的假设，
以及一个能在 2 分钟内验证它的实验。
```

### 提示词 3：审查 diff

```text
请审查本次改动，按严重程度列出最多两个问题，每个问题给出文件和行号。
然后指出一个我漏测的边界条件。
不要重写代码，只说明问题和验证方式。
```

### 提示词 4：自测理解

```text
根据这周的内容，问我 5 个递进的问题，从语法到设计取舍。
一次只问一个，等我回答后再问下一个，并纠正我的错误理解。
```

---

## GitHub 记录规范

**每周一个 Issue**，标题形如 `Week 3: Spring Boot REST API`，正文直接粘贴本周的验收清单。

**提交要小**，完成一个能运行的步骤就提交：

```text
feat: add in-memory task repository
test: cover empty title on create
docs: add week 02 retrospective
chore: configure flyway migration
```

**每周复盘**写进 `notes/week-0X.md`，复制 `notes/_template.md`。不要贴整段 AI 对话，只记录你自己的判断。

**安全**

- `.env`、连接串、密码永远不进仓库
- 不把凭据贴进 AI 对话；需要示例时用 `<password>` 占位
- 提交前跑一次 `git diff --cached` 自查

---

## 进度

- [ ] 第 1 周：Java 基础
- [ ] 第 2 周：Java 工程化
- [ ] 第 3 周：Spring Boot
- [ ] 第 4 周：云 PostgreSQL
- [ ] 第 5 周：JPA 与事务
- [ ] 第 6 周：后端交付与毕业题

---

## 参考资料

- [Java 21 API 文档](https://docs.oracle.com/en/java/javase/21/docs/api/)
- [VS Code Java 入门](https://code.visualstudio.com/docs/java/java-tutorial)
- [Spring Boot 官方指南](https://spring.io/guides)
- [Spring Data JPA 参考](https://docs.spring.io/spring-data/jpa/reference/)
- [Flyway 文档](https://documentation.red-gate.com/flyway)
- [Neon Java / JDBC 指南](https://neon.com/docs/guides/java)
- [OpenCode 文档](https://opencode.ai/docs/)
