# Viking Demo

Минимальный демонстрационный Maven-проект для практического занятия по связке Spring Web + REST API + Swagger/OpenAPI + Spring JDBC + H2.

## Что реализовано по заданию

1. Проект подготовлен как версия для форка оригинального репозитория.
2. Реализован метод добавления конкретного викинга:
   - `VikingService.addViking(Viking viking)`
   - `VikingStorage.save(Viking viking)`
   - `POST /api/vikings`
3. Реализован метод удаления викинга из таблицы:
   - `VikingService.deleteById(int id)`
   - `VikingStorage.deleteById(int id)`
   - `VikingTableModel.deleteVikingById(int id)`
   - `DELETE /api/vikings/{id}`
4. Реализован метод перезаписи параметров конкретного викинга:
   - `VikingService.updateViking(int id, Viking viking)`
   - `VikingStorage.updateById(int id, Viking viking)`
   - `VikingTableModel.updateVikingById(int id, Viking viking)`
   - `PUT /api/vikings/{id}`

## Модель викинга

У викинга есть:

- `id`
- `name`
- `age`
- `heightCm`
- `hairColor`
- `beardStyle`
- `equipment`

## Сборка и запуск

```bash
mvn clean spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

H2 Console:

```text
http://localhost:8080/h2-console
```

## Проверка через curl

Создание случайного викинга:

```bash
curl -X POST "http://localhost:8080/api/vikings/post"
```

Добавление конкретного викинга:

```bash
curl -X POST "http://localhost:8080/api/vikings" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ragnar",
    "age": 35,
    "heightCm": 184,
    "hairColor": "Blond",
    "beardStyle": "LONG",
    "equipment": [
      {"name": "Axe", "quality": "Rare"},
      {"name": "Shield", "quality": "Common"}
    ]
  }'
```

Перезапись параметров викинга:

```bash
curl -X PUT "http://localhost:8080/api/vikings/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bjorn",
    "age": 31,
    "heightCm": 182,
    "hairColor": "Red",
    "beardStyle": "BRAIDED",
    "equipment": [
      {"name": "Sword", "quality": "Legendary"},
      {"name": "Helmet", "quality": "Rare"}
    ]
  }'
```

Удаление викинга:

```bash
curl -X DELETE "http://localhost:8080/api/vikings/1"
```

## Дополнительное задание: сервис на лямбда-функциях

Добавлен новый сервис:

- `VikingLambdaService`

В нем операции выполняются через `Predicate`, `stream()`, `filter()`, `map()`, `sorted()`, `toArray()`, `Arrays.stream()` и другие лямбда-операции.

### Что реализовано

1. Оценка объема выборки викингов по условиям:
   - возраст больше заданного значения;
   - возраст меньше заданного значения;
   - возраст в диапазоне;
   - возраст вне диапазона;
   - форма бороды и цвет волос одновременно;
   - наличие одного или двух топоров.

2. Новая форма для вывода информации на экран:
   - `VikingLambdaInfoFrame`;
   - открывается кнопкой `Open lambda report` в основной Swing-форме;
   - выводит случайного викинга ростом выше 180;
   - выводит всех викингов с легендарным снаряжением;
   - выводит сортированный по возрасту список рыжебородых викингов.

3. Операции с массивом ID:
   - в сервисе явно создается массив `Integer[] ids`;
   - находится последняя запись через `max ID`;
   - выводятся все четные ID.

4. Массовая генерация викингов:
   - добавлен метод `VikingService.generateRandomVikings(int count)`;
   - метод использует фабрику `VikingFactory` и лямбда-операции;
   - вызывается только пользователем: через кнопку `Generate vikings` или REST endpoint.

## REST endpoints дополнительного задания

Массовая генерация:

```bash
curl -X POST "http://localhost:8080/api/vikings/generate/20"
```

Количество по возрасту больше значения:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/age/greater-than/30"
```

Количество по возрасту меньше значения:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/age/less-than/30"
```

Количество по возрасту в диапазоне:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/age/in-range?fromAge=25&toAge=40"
```

Количество по возрасту вне диапазона:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/age/out-of-range?fromAge=25&toAge=40"
```

Количество по форме бороды и цвету волос одновременно:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/appearance?beardStyle=LONG&hairColor=Red"
```

Количество викингов с одним или двумя топорами:

```bash
curl "http://localhost:8080/api/vikings/lambda/count/one-or-two-axes"
```

Случайный викинг ростом выше 180:

```bash
curl "http://localhost:8080/api/vikings/lambda/screen/random-higher-than-180"
```

Все викинги с легендарным снаряжением:

```bash
curl "http://localhost:8080/api/vikings/lambda/screen/legendary-equipment"
```

Рыжебородые викинги, отсортированные по возрасту:

```bash
curl "http://localhost:8080/api/vikings/lambda/screen/red-bearded-sorted-by-age"
```

Последняя запись по массиву ID:

```bash
curl "http://localhost:8080/api/vikings/lambda/ids/max"
```

Все четные ID:

```bash
curl "http://localhost:8080/api/vikings/lambda/ids/even"
```