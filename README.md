# M3Bank — E2E-приложение для перевода денег

Тестовое задание: Java разработчик  
*Литов Н., Владимир*

## Что реализовано

- Регистрация и аутентификация пользователей
- Работа с несколькими банковскими счетами
- P2P-переводы между счетами пользователей
- Валидация счетов
- Уведомления о поступлении средств
- Администрирование пользователей

## Особенности реализации

### Архитектура и стек

- Backend: Spring Boot, REST, WebSocket, Kafka
- БД: PostgreSQL, `JdbcTemplate`
- Frontend: Freemarker + Bootstrap + JS
- Миграции: Flyway
- Тесты: JUnit, Mockito, MockMvc, TestContainers

### Уведомления (Kafka + WebSocket)

- Kafka используется для доставки уведомлений о переводах
- Kafka consumer сохраняет уведомление и пушит его через WebSocket
- Клиент получает уведомление мгновенно, без опроса

### Аудит через AOP

- Создана аннотация `@Auditable`, логирующая действия пользователя
- В лог попадают имя пользователя, действие и время

### Обработка ошибок

- Глобальный `@ControllerAdvice`
- Единый JSON-формат ошибок: статус, сообщение, путь, время

### Admin
За создание админа отвественнен компонент DevUserInitializer. Полагаю, что можно было бы через configReader сделать, но в принципе и так сойдёт. Вход в панель осуществляется по пути "сервер:порт/admin".
Как это выглядит:
![image](https://github.com/user-attachments/assets/43248d03-6254-4d34-8c15-fd57671b1630)

## Использование LLM
В основном использовалась для генерации юнит-тестов, html-документов, создании собственной аннотации и поиска багов

