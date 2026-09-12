# Car Inventory Management (CIM)

> **Recovered project.** The original working copy was lost in a disk format. The Java sources here were **decompiled** from the shipped 1.0 build (`Car Inventory 1.0.exe`, on the [Releases](../../releases) page) with CFR 0.152; the original `pom.xml` and the complete `pictures/` UI asset set were recovered from inside the same executable. Decompiled code is functionally faithful but loses comments and formatting.

A JavaFX desktop **Car Inventory Management** application — school capstone project, May 2023.

## Features
- Login screen with user management (Admin / Manager roles)
- Add / edit / view / list cars — VIN, brand, model, transmission, type, date created, photo
- Car photos stored as BLOBs in MySQL
- Custom JavaFX styling (`cim-style.css`), Feather icons via Ikonli, FormsFX / ValidatorFX forms
- About page with the project write-up and the team

## Tech stack
- Java 17, JavaFX 19 (OpenJFX 19.0.2.1)
- MySQL 8 / MariaDB + Connector/J 8.0.32
- Maven with the shade plugin (runnable fat jar)

## Requirements
- JDK 17+, Maven 3.8+
- MySQL/MariaDB with a database named `car_inventory`

## Database setup
```sql
CREATE DATABASE car_inventory;
USE car_inventory;
SOURCE car_inventory.sql;     -- schema + sample data, download from the Releases page
```

Seed accounts from the original development database:

| Username | Role |
| --- | --- |
| `Daniel` | Admin |
| `Fahd` | Admin |
| `Marvin` | Admin |
| `Joshua` | Manager |
| `Jeremie` | Manager |

> These are 2023 student-project credentials, stored in plain text (the passwords are the surnames recorded in the dump), and `src/main/java/com/cim/carinventory/DB.java` connects as `root` with an empty password on `localhost:3306`. Treat all of it as burned — change it before connecting to anything that matters.

## Build & run
```bash
mvn clean javafx:run     # run in dev mode
mvn package              # fat jar -> target/CarInventory-1.0-SNAPSHOT-shaded.jar
```

## Layout
```
src/main/java/com/cim/carinventory/            14 classes (decompiled)
src/main/resources/com/cim/carinventory/       10 FXML views + cim-style.css
src/main/resources/com/cim/carinventory/pictures/   18 UI assets (logos, backgrounds, team photos)
pom.xml                                        original Maven build, recovered from the release
```

## Downloads
| Asset | What it is |
| --- | --- |
| `Car Inventory 1.0.exe` | The original bundled Windows release, exactly as shipped in 2023 |
| `car_inventory.sql` | phpMyAdmin dump of the development database: schema, 10 sample cars (with their photo blobs), and the five accounts |

## Team — May 2023
| Name | Role |
| --- | --- |
| Daniel | Project Manager / Developer |
| Fahd | Designer |
| Marvin | Designer |
| Joshua | Admin |
| Jeremie | Admin |
| Genalyn | Advisor |

City College of San Jose del Monte — Computer Programming 2, BS EMC 1A. Created May 24, 2023.

## Known decompilation artifacts
- Inner classes appear as `SomeClass$1.java`.
- Comments, formatting, and lambda sugar are not preserved.
- `Funcs_Class` is the original class name, not a decompiler artifact.

## License
Licensed under the **Apache License, Version 2.0** — see [LICENSE](LICENSE). Copyright 2023 the Car Inventory Management project team; attribution details in [NOTICE](NOTICE).
