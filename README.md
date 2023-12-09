# Discord Bot

## Описание

Этот проект представляет собой Discord-бота, разработанного на языке Java с использованием библиотек JDA и LavaPlayer.
Бот способен выполнять различные функции в Discord-серверах, связанные с музыкой, управлением сервером и т.д.

## Установка

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/ichuvilin/discrod-bot
   ```
2. Убедитесь, что у вас установлен Java Development Kit (JDK).
3. Используйте среду разработки, например, IntelliJ IDEA или Eclipse, для открытия проекта.

## Использование

### Используя Java и Gradle

1. Добавьте вашего бота на свой Discord-сервер, получив токен
   от [Discord Developer Portal](https://discord.com/developers/applications).
2. Подставьте свои данные в файл [application.yml](src%2Fmain%2Fresources%2Fapplication.yml).
3. Собрать приложение с помощью команды:
   ```bash
     gradle build
   ```
4. Запустите бота из вашей IDE или с помощью команды:

   ```bash
   java -jar discord-bot.jar
   ```
5. Используйте префикс бота в Discord для доступа к командам, например /ping.

### Используя Dockerfile

1. Добавьте вашего бота на свой Discord-сервер, получив токен
   от [Discord Developer Portal](https://discord.com/developers/applications).
2. Подставьте свои данные в [Dockerfile](Dockerfile)
3. Собрать приложение
   ```bash
    docker build -t bot .
   ```
4. Запуск приложения
   ```bash
    docker run bot
   ```

### Используя Docker Compose

1. Добавьте вашего бота на свой Discord-сервер, получив токен
   от [Discord Developer Portal](https://discord.com/developers/applications).
2. Подставьте свои данные в [docker-compose.yml](docker-compose.yml)
3. Запуск приложения
   ```bash
    docker-copmose up
   ```