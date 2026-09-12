# Car Inventory (CIM)

> ⚠️ **Recovered project** — the original source code was lost in a disk format (May 2023 build, recovered September 2026). The Java sources below were **decompiled** from the shipped executable (`docs/original-release.exe`) using CFR 0.152. Decompiled code is functionally faithful but may contain artifacts (synthetic names, lost comments/formatting).

A JavaFX desktop **Car Inventory Management** application (school capstone project, May 2023).

## Features
- Login screen with user management (admin/user roles)
- Add / edit / view cars (VIN, brand, model, transmission, type, photo)
- Photos stored as BLOBs in MySQL
- Custom JavaFX styling (`cim-style.css`), Feather icons, FormsFX validation

## Tech stack
- Java 17, JavaFX 19 (OpenJFX)
- MySQL 8 / MariaDB (Connector/J 8.0.32)
- Maven (original `pom.xml` recovered intact from inside the exe's `META-INF`)

## Requirements
- JDK 17+
- Maven 3.8+
- MySQL/MariaDB running locally with database `car_inventory` and user `root` (empty password — matches the app's hardcoded config in `DB.java`; **do not use this in production**)

### Database setup
```sql
CREATE DATABASE car_inventory;
USE car_inventory;
SOURCE db/car_inventory.sql;   -- mysql client
```

## Build & run
```bash
mvn clean javafx:run          # run the app (dev mode)
mvn package                    # build fat jar -> target/CarInventory-1.0-SNAPSHOT-shaded.jar
```

## Structure
```
src/main/java/com/cim/carinventory/   decompiled sources (17 classes)
src/main/resources/com/cim/carinventory/  10 FXML views + stylesheet
db/car_inventory.sql                  full schema + data dump (phpMyAdmin, 2023-05-29)
docs/original-release.exe             original bundled Windows release (jpackage)
```

## Known artifacts from decompilation
- Inner classes appear as `loadingController$1.java` etc.
- Original comments, generics edge cases, and lambda sugar are not preserved.
- `Funcs_Class` naming is from the original code, not a decompiler artifact. 🙂

## License / status
Personal archive of a lost project — published for preservation. Credentials in `db/car_inventory.sql` are from the original dev database; treat them as burned.
