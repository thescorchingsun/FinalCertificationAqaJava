# Проект автоматизации тестирования UI и API

Полный проект автоматизации тестирования, объединяющий:
- **UI автотесты** для сайта [saucedemo.com](https://www.saucedemo.com)
- **API автотесты** для сервиса [innopolispython.onrender.com](https://innopolispython.onrender.com) с покрытием модуля employee.

Реализован на **Java + Maven + JUnit5**, визуальной отчётностью в **Allure** и CI/CD-запуском через **GitHub Actions**.

## Инструменты

| Инструмент                | Назначение                                          |
|---------------------------|-----------------------------------------------------|
| **Java 17**               | Язык разработки автотестов                          |
| **Maven**                 | Сборка и управление зависимостями                   |
| **Selenium WebDriver**    | Автоматизация действий в браузере                   |
| **WebDriverManager**      | Управление драйверами браузеров                     |
| **Rest-Assured**          | Тестирование REST API                               |
| **JUnit 5 (Jupiter)**     | Фреймворк  для выполнения UI и API тестов           |
| **Allure Report**         | Формирование отчётов с шагами, скриншотами и логами |
| **AspectJ**               | Перехват шагов тестов для Allure                    |
| **SLF4J + SimpleLogger**  | Логирование                                         |
| **Lombok**                | Сокращение шаблонного кода                          |
| **AssertJ**               | Читаемые проверки                                   |
| **Instancio / JavaFaker** | Генерация тестовых данных                           |
| **Ashot**                 | Скриншотные сравнения                               |
| **PostgreSQL JDBC**       | Проверка данных на уровне БД                        |
| **Jackson / Gson**        | Библиотеки для сериализации и десериализации JSON   |
| **GitHub**                | Хранение и совместная работа с кодом                |
| **GitHub Actions**        | CI/CD-пайплайн для автоматического запуска тестов   |


## Описание проекта
Проект объединяет тесты трёх уровней:

1. **UI-тесты**

+ Реализованы с использованием Selenium WebDriver и Page Object Pattern

+ Поддерживается запуск в браузерах Chrome и Firefox

+ Cкриншотное сравнение страниц через Ashot

+ При падении тестов автоматически добавляются в Allure отчет:
  + Скриншоты страницы
  + HTML-код в момент ошибки
  + Логи браузера (только для Chrome)

2. **API-тесты**

+ Разделены на:

  + **Контрактные тесты** — проверяют корректность структуры и статусов API-ответов

  + **Бизнес-тесты** — проверяют бизнес-логику и интеграцию модулей

+ Используется Rest-Assured и Allure Rest Plugin

+ Для логов запросов/ответов применяются .tpl-шаблоны для лучшей читаемости в отчётах Allure

+ Для логов бизнес тестов в Allure записывается SQL-запросы. Отправленные и полученные данные.

3. **Отчётность и инфраструктура**

 + Генерация отчётов через Allure Report

+ CI/CD-запуск через GitHub Actions

+ Данные скрыты через конфигурационный файл в env.properties

## Запуск проекта локально
> Требования: **Java 17+**, **Maven**, браузеры **Google Chrome** и/или **Firefox**.
> 

1. **Настройка браузера**
 + По умолчанию UI автотесты запускаются в браузере firefox. Чтобы запустить в Chrome — изменить параметр в
autotests.ui.BaseTest ⭢ @BeforeEach

 + Для запуска в видимом режиме (не headless), в config.BrowserSettings закомментировать строку: 

> firefoxOptions.addArguments("--headless");

2. **Запуск тестов через терминал**
> mvn clean test               # Запуск всех тестов
>
> mvn clean test -Dtags=smoke  # Запуск тестов для дымового тестирования
> 
> mvn clean test -Dtags=regress    # Запуск тестов для регрессионного тестирования
>
3. **Генерация отчета**
> mvn allure:serve 


## Структура проекта
```text
FinalCertificationAqaJava/
│
├── .github/
│   └── workflows/
│       └── maven.yml               # CI/CD pipeline GitHub Actions для запуска тестов
│
├── src/
│   └── test/
│       └── java/
│           ├── autotests/
│           │   ├── apiBusiness/            # API-тесты бизнес-логики
│           │   │   ├── BaseTest.java
│           │   │   ├── CreateEmployeeBusinessTest.java
│           │   │   ├── DeleteEmployeeBusinessTest.java
│           │   │   ├── EndToEndTest.java
│           │   │   ├── GetEmployeeByIdBusinessTest.java
│           │   │   ├── GetEmployeeByNameBusinessTest.java
│           │   │   ├── GetEmployeesBusinessTest.java
│           │   │   └── UpdateEmployeeBusinessTest.java
│           │   │
│           │   ├── apiContract/            # Контрактные API-тесты
│           │   │   ├── BaseTest.java
│           │   │   ├── CreateEmployeeContractTest.java
│           │   │   ├── DeleteEmployeeContractTest.java
│           │   │   ├── GetEmployeeByIdContractTest.java
│           │   │   ├── GetEmployeeByNameContractTest.java
│           │   │   ├── GetEmployeesContractTest.java
│           │   │   └── UpdateEmployeeContractTest.java
│           │   │
│           │   └── ui/                     # UI-тесты
│           │       ├── BaseTest.java
│           │       ├── ScreenTest.java     # Скриншотные тесты
│           │       └── UiTest.java
│           │
│           ├── config/                     # Конфигурации для браузеров
│           │   └── BrowserSettings.java
│           │
│           ├── entities/                   # POJO-классы и модели данных (JSON, объекты БД)
│           │   ├── EmployeeRequest.java
│           │   ├── EmployeeResponse.java
│           │   ├── User.java
│           │   └── ValidationErrorResponse.java
│           │
│           ├── helper/                     # Вспомогательные классы и утилиты
│           │   ├── AbstractHelper.java
│           │   ├── AuthHelper.java
│           │   ├── EmployeeHelper.java
│           │   ├── EmployeeHelperDB.java
│           │   ├── EnvHelper.java          
│           │   ├── HttpCode.java
│           │   └── MethodForScreen.java
│           │
│           ├── listener/                   # Слушатели и расширения для Allure
│           │   ├── AllureLogsAttachment.java
│           │   ├── AllureLogsExtension.java
│           │   ├── AllureTestWatcher.java
│           │   └── CustomTpl.java
│           │
│           ├── pages/                      # Реализация Page Object для UI-тестов
│           │   ├── BasePage.java
│           │   ├── CartPage.java
│           │   ├── CheckoutCompletePage.java
│           │   ├── CheckoutOnePage.java
│           │   ├── CheckoutTwoPage.java
│           │   ├── LoginPage.java
│           │   └── ProductsPage.java
│           │
│           └── resources/                  # Ресурсы проекта
│               ├── screens/                # Эталонные скриншоты для сравнения
│               │
│               ├── tpl/                    # Шаблоны для лучшей читаемости запросов/ответов в Allure отчете 
│               │   ├── request.ftl
│               │   ├── response.ftl
│               │   └── response.zip
│               │
│               └── env.properties          # Настройки окружения (URL, креды, и т.п.)
│
├── .gitignore                              # Исключения для Git (артефакты, логи и отчёты)
├── pom.xml                                 # Maven-конфигурация проекта (зависимости, плагины)
└── README.md                               # Документация проекта
```

## Пример отчета Allure

После выполнения тестов формируется интерактивный отчёт:

+ Статистика по прогонам

+ Скриншоты и HTML-артефакты при падении

+ Запросы и ответы API в формате .tpl

+ SQL-логирование для API-тестов

<p align="center">
  <img src="resources/screens/allureApiBusniessTest.png" alt="Успешный API тест" width="600"/>
  <br>
  <em>Успешно пройденный API тест</em>
</p>

<p align="center">
  <img src="resources/screens/allureFailedUiTest.png" alt="Падение UI теста" width="600"/>
  <br>
  <em>Пример отчёта об упавшем UI тесте</em>
</p>



