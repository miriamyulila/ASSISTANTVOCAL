# TP Gestion

Java + SQLite starter project for an intelligent assistant and planner application.

## Features

- Open a browser search from the app
- Store reminders and agenda items in SQLite
- Track work sessions and calculate total time
- Simple console-driven interface to add reminders, view agenda, and log work

## Build

```bash
mvn clean package
```

## Run

```bash
mvn exec:java -Dexec.mainClass="com.assistantvocal.AssistantApp"
```

## Notes

The current starter includes the database schema, data access layer, console UI, and basic services for reminders and time tracking.
