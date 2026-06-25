# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

- **Build:** `mvn clean compile` (or `mvn clean package -DskipTests`)
- **Run:** `mvn spring-boot:run` — starts on port 8081 (configured in `application.properties`)
- **Test:** `mvn test`
- **Single test:** `mvn test -Dtest=HelloApplicationTests`
- **Package:** `mvn clean package` (produces executable JAR in `target/`)

## Tech Stack

- Java 17, Spring Boot 3.3.2
- MyBatis 3.0.3 (XML mappers under `resources/mapper/`)
- PageHelper 1.4.7 (pagination)
- MySQL (external, configured in `application.properties`)

## Architecture

Standard layered REST API (no security/auth):

```
Controller → Service (interface + impl) → Mapper (interface) → XML SQL
```

### Layers

- **`controller/`** — REST endpoints. `DeptController` (`/depts`), `EmpController` (`/emps`). Constructor injection only.
- **`service/`** — Interfaces (`DeptService`, `EmpService`) + implementations in `impl/`. `EmpServiceImpl` uses `@Transactional` for write operations and cascades `EmpExpr` (work experience) CRUD alongside `Emp`.
- **`mapper/`** — MyBatis interfaces. SQL is in `resources/mapper/*.xml`. `@MapperScan("com.example.hello.mapper")` on the main application class.
- **`entity/`** — POJOs: `Dept`, `Emp`, `EmpExpr`. `Emp` has a `@Transient` `exprList` for joined work-experience data.
- **`dto/`** — Query params: `EmpQuery` (name, gender, date range, page/pageSize).
- **`common/`** — `Result<T>` (unified JSON response: `code`=1 success / 0 fail), `PageResult<T>` (pagination: `total` + `rows`).

### Key Patterns

- Unified response: all controllers return `Result<T>`.
- Pagination: `PageHelper.startPage()` + `PageInfo` wrapper, returns `PageResult`.
- DELETE accepts comma-separated IDs (e.g. `DELETE /emps?ids=1,2,3`), parsed in service layer.
- `EmpServiceImpl.updateExprList()` implements smart sync: deletes removed `EmpExpr` rows, inserts new ones, updates existing ones.
- `Emp` default password is `"123456"` when not provided.
- Server port: 8081 (not the default 8080).
