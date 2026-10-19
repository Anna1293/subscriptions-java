# Subscriptions API (Java)

![CI](https://github.com/Anna1293/subscriptions-java/actions/workflows/ci.yml/badge.svg)

Java/Spring Boot порт [em-subscriptions](https://github.com/Anna1293/em-subscriptions): тот же REST-контракт, PostgreSQL, агрегация суммы за период, Docker.

Коммерческий стек на работе — Java. Этот репозиторий показывает публичный сервис на том же API, что и Go-версия.

## Стек

Java 17 · Spring Boot 3 · Spring Web · JPA · Flyway · PostgreSQL · springdoc · Docker · GitHub Actions

## Запуск

```bash
docker compose up --build
```

API: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html  
Health: http://localhost:8080/actuator/health

Postgres снаружи: `localhost:5433`

## Примеры

Создать подписку:

```bash
curl -X POST http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{"service_name":"Yandex Plus","price":400,"user_id":"60601fee-2bf1-4721-ae6f-7636e79a0cba","start_date":"07-2025"}'
```

Список:

```bash
curl "http://localhost:8080/api/subscriptions?user_id=60601fee-2bf1-4721-ae6f-7636e79a0cba"
```

Сумма за период (`price *` число месяцев пересечения):

```bash
curl "http://localhost:8080/api/subscriptions/total?from=07-2025&to=12-2025&user_id=60601fee-2bf1-4721-ae6f-7636e79a0cba"
```

## Локально без Docker

Нужен Postgres (`DATABASE` как в `.env.example`) и JDK 17:

```bash
./mvnw spring-boot:run
```

Windows: `mvnw.cmd spring-boot:run`

## Тесты

```bash
./mvnw test
```
