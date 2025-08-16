# Event Notification System (Multi-Module)

Modules:
- `api-ext` – DTOs & Service interface
- `impl` – Business logic, queues, processors
- `rest` – Spring Boot app & controllers

## Build
```bash
mvn clean package
```

## Run (Docker)
```bash
docker compose up --build
# API: http://localhost:8080/api/events
```

## Example request
```bash
curl -X POST http://localhost:8080/api/events -H "Content-Type: application/json" -d '{
  "eventType": "EMAIL",
  "payload": { "recipient":"user@example.com", "message":"Welcome!" },
  "callbackUrl": "https://webhook.site/your-id"
}'
```