# The Pitstop Passport — BDD тестове с Cucumber

Проект за автоматизирано тестване на мобилното приложение **The Pitstop Passport**,
реализиран с Java и Cucumber (BDD подход).

---

## Структура на проекта

```
pitstop-passport-bdd/
├── pom.xml
└── src/
    └── test/
        ├── java/com/pitstop/
        │   ├── model/
        │   │   ├── User.java
        │   │   ├── DigitalPassport.java
        │   │   ├── Stamp.java
        │   │   ├── Pitstop.java
        │   │   └── Recipe.java
        │   ├── service/
        │   │   ├── UserService.java        ← регистрация, вход
        │   │   ├── LocationService.java    ← GPS логика, Haversine формула
        │   │   └── RecipeService.java      ← отключване на рецепти
        │   ├── stepdefs/
        │   │   ├── UserStepDefs.java
        │   │   ├── CheckInStepDefs.java
        │   │   └── RecipeStepDefs.java
        │   └── runner/
        │       └── RunCucumberTest.java
        └── resources/
            └── features/
                ├── 01_user_registration_login.feature   (7 сценария)
                ├── 02_gps_checkin.feature               (6 сценария)
                └── 03_unlock_recipes.feature            (6 сценария)
```

---

## Тествани функционалности

| # | Функционалност              | Feature файл                          | Сценарии |
|---|-----------------------------|---------------------------------------|----------|
| 1 | Регистрация и вход          | `01_user_registration_login.feature`  | 7        |
| 2 | GPS Чекиране на локация     | `02_gps_checkin.feature`              | 6        |
| 3 | Отключване на рецепти       | `03_unlock_recipes.feature`           | 6        |

---

## Изпълнение

```bash
mvn test
```

HTML репорт се генерира в: `target/cucumber-reports/report.html`

---

## Технологии

- Java 11
- Cucumber 7.14.0 (Gherkin сценарии на български)
- JUnit 5 / JUnit Platform Suite
- Maven
