# AShot Visual Testing Framework

Комплексный Java фреймворк для автоматизированного тестирования пользовательского интерфейса с акцентом на визуальное регрессионное тестирование. Использует Selenium WebDriver и библиотеку AShot для точного сравнения скриншотов, обеспечивая надежное обнаружение визуальных изменений в веб-приложениях.

<div align="center">

## Преимущества проекта

</div>

- **Надежное визуальное тестирование**: Автоматическое сравнение скриншотов с базовыми изображениями для выявления регрессий в пользовательском интерфейсе.
- **Высокая производительность**: Параллельное выполнение тестов и оптимизированные скриншоты для быстрого анализа.
- **Гибкая конфигурация**: Настраиваемые параметры через properties-файлы, поддержка различных браузеров и режимов.
- **Детальная отчетность**: Интеграция с Allure для визуальных отчетов с прикрепленными скриншотами, базовыми изображениями и диффами.
- **Стабилизация страниц**: Автоматическая стабилизация контента перед снятием скриншотов для точности.
- **Модульная архитектура**: Разделение логики базовых тестов, создания базовых скриншотов и сравнения для удобства сопровождения.

<div align="center">

## Возможности

</div>

<div align="center">
<img src="https://img.shields.io/badge/Visual_Regression-FF6B6B?style=for-the-badge&logo=visual-studio-code&logoColor=white" alt="Visual Regression"/>
<img src="https://img.shields.io/badge/Page_Object_Model-4CAF50?style=for-the-badge&logo=java&logoColor=white" alt="Page Object Model"/>
<img src="https://img.shields.io/badge/Parallel_Execution-2196F3?style=for-the-badge&logo=parallel&logoColor=white" alt="Parallel Execution"/>
<img src="https://img.shields.io/badge/Allure_Reporting-9C27B0?style=for-the-badge&logo=allure&logoColor=white" alt="Allure Reporting"/>
</div>

- Визуальное регрессионное тестирование с точным сравнением скриншотов
- Реализация паттерна Page Object Model для структурированного кода
- Параллельное выполнение тестов для ускорения
- Интеграция с Allure для детальных визуальных отчетов
- Поддержка контейнеризации Docker
- Стабилизация страниц с использованием аспектов для надежности скриншотов

<div align="center">

## Технологии

</div>

<div align="center">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
<img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white" alt="Selenium"/>
<img src="https://img.shields.io/badge/AShot-FF6B6B?style=for-the-badge&logo=visual-studio-code&logoColor=white" alt="AShot"/>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"/>
<img src="https://img.shields.io/badge/Allure-9C27B0?style=for-the-badge&logo=allure&logoColor=white" alt="Allure"/>
</div>

- Java 11
- Selenium
- AShot
- JUnit 5 
- Gradle 
- Allure 
- AspectJ

## Быстрый старт

1. Клонируйте репозиторий:
```bash
git clone https://github.com/GitMrRabbit/framework-testing-UI.git
cd framework-testing-UI
```

2. Запустите тесты:
```bash
./gradlew test
```

3. Сгенерируйте отчет Allure:
```bash
./gradlew allureReport
./gradlew allureServe
```

## Структура проекта

```
src/
├── main/java/com/ashot/
│   ├── annotations/            # Аннотации для стабилизации
│   ├── aspects/                # Аспекты для стабилизации страниц
│   ├── base/                   # Базовые классы тестов (BaseTest, BaselineBaseTest)
│   ├── config/                 # Классы конфигурации (TestConfig, BaselineConfig)
│   ├── pojo/                   # Классы Page Object
│   └── utils/                  # Утилитарные классы (ScreenshotUtils, ScreenshotComparisonUtils и др.)
├── main/resources/             # Ресурсы (test.properties, baseline.properties)
└── test/java/com/ashot/tests/  # Тестовые классы (FormFillingTest, BaselineCreator)
```

## Конфигурация

Настройте параметры тестирования в файле `src/main/resources/test.properties`:

```properties
base.url=https://demoqa.com/
browser=chrome
headless=true
timeout=10
screenshot.dir=screenshots
baseline.dir=baselines
diff.dir=diff
stabilization.timeout=5000
```

Для создания базовых скриншотов используйте `src/main/resources/baseline.properties`:

```properties
base.url=https://demoqa.com/
browser=chrome
headless=true
baseline.dir=baselines
```

## Создание базовых скриншотов

Запустите класс `BaselineCreator` для генерации эталонных изображений:

```bash
./gradlew test --tests BaselineCreator
```

## CI/CD

Проект не включает готовые конфигурации CI/CD, но поддерживает интеграцию с системами автоматизации. Для настройки пайплайнов можно использовать GitHub Actions, Jenkins или другие инструменты, выполняющие тесты в параллельном режиме и генерирующие отчеты Allure.

<div align="right">
 <img style="display: inline-block; vertical-align: middle;" src="logo/SL.png" alt="Author" width="70">
</div>
