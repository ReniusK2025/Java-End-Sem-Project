# PetCareBook — Pet Vaccination Reminder System

A Spring Boot + Spring Data JPA + MySQL backend for the given "PetCareBook — Pet Vaccination Reminder System" assessment use case.

## 1. What this project implements

### Core requirements from the question

1. **Register a pet** with:
   - pet name
   - species
   - breed
   - date of birth
   - owner

2. **Log a vaccination event** with:
   - pet
   - vaccine type
   - vaccination date
   - notes

3. **Automatically calculate the next due date**
   - `nextDueDate = vaccinationDate + vaccineType.intervalDays`
   - the client does **not** send or control `nextDueDate`

4. **List vaccinations due in the next 7 days**
   - default endpoint: `GET /api/vaccinations/due?days=7`
   - includes records due today through the selected number of days

5. **View a pet's complete vaccination history**
   - `GET /api/pets/{petId}/vaccination-history`

### Architecture requested by the assessment

- Spring Boot
- Spring Web / MVC
- Spring Data JPA
- Bean Validation
- MySQL driver
- Layered architecture
- Entities: `Owner`, `Pet`, `VaccineType`, `VaccinationRecord`
- Repository interface for each entity
- Service layer containing business rules
- REST controllers
- Global exception handling with `@RestControllerAdvice`
- Basic Thymeleaf UI for demonstration
- Postman collection for endpoint testing

## 2. Tech stack

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Validation
- Thymeleaf
- MySQL
- Maven
- JUnit / Spring Boot Test

Spring Boot 4.1.1 is currently a stable Spring Boot release and requires at least Java 17, so Java 21 is a supported choice.

## 3. Project structure

```text
PetCareBook/
├── pom.xml
├── README.md
├── DATABASE_SETUP.sql
├── postman/
│   └── PetCareBook.postman_collection.json
├── src/
│   ├── main/
│   │   ├── java/com/example/PetCareBook/
│   │   │   ├── Config/
│   │   │   │   └── DataInitializer.java
│   │   │   ├── Controller/
│   │   │   │   ├── OwnerController.java
│   │   │   │   ├── PetController.java
│   │   │   │   ├── VaccineTypeController.java
│   │   │   │   ├── VaccinationController.java
│   │   │   │   ├── WebController.java
│   │   │   │   └── UiActionController.java
│   │   │   ├── DTO/
│   │   │   │   ├── OwnerRequest.java
│   │   │   │   ├── OwnerResponse.java
│   │   │   │   ├── PetRequest.java
│   │   │   │   ├── PetResponse.java
│   │   │   │   ├── VaccineTypeRequest.java
│   │   │   │   ├── VaccineTypeResponse.java
│   │   │   │   ├── VaccinationRequest.java
│   │   │   │   ├── VaccinationResponse.java
│   │   │   │   └── DueVaccinationResponse.java
│   │   │   ├── Exception/
│   │   │   │   ├── ApiError.java
│   │   │   │   ├── BusinessRuleException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── Model/
│   │   │   │   ├── Owner.java
│   │   │   │   ├── Pet.java
│   │   │   │   ├── VaccineType.java
│   │   │   │   └── VaccinationRecord.java
│   │   │   ├── Repository/
│   │   │   │   ├── OwnerRepository.java
│   │   │   │   ├── PetRepository.java
│   │   │   │   ├── VaccineTypeRepository.java
│   │   │   │   └── VaccinationRecordRepository.java
│   │   │   ├── Service/
│   │   │   │   ├── OwnerService.java
│   │   │   │   ├── PetService.java
│   │   │   │   ├── VaccineTypeService.java
│   │   │   │   └── VaccinationService.java
│   │   │   └── PetCareBookApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/css/style.css
│   │       └── templates/
│   │           ├── dashboard.html
│   │           ├── owners.html
│   │           ├── pets.html
│   │           ├── vaccines.html
│   │           └── pet-history.html
│   └── test/
│       └── java/com/example/PetCareBook/
└── ...
```

## 4. Database setup on your Mac

Install/start MySQL and create the database:

```sql
CREATE DATABASE IF NOT EXISTS petcarebook;
```

The application has:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petcarebook?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
```

If your MySQL password is not `root`, either edit `application.properties` or start the app with environment variables.

Example:

```bash
export DB_USERNAME=root
export DB_PASSWORD='YourMySQLPassword'
```

Then run the application.

## 5. Run in VS Code on MacBook Air M4

Open the project folder in VS Code.

Check Java:

```bash
java -version
```

You should use Java 21.

Check Maven:

```bash
mvn -version
```

Then:

```bash
mvn clean install
mvn spring-boot:run
```

Or run the generated jar:

```bash
java -jar target/PetCareBook-0.0.1-SNAPSHOT.jar
```

Open:

```text
http://localhost:8080/
```

The M4 does not require ARM-specific Java code; a normal Apple-Silicon JDK 21 is appropriate.

## 6. Default web pages

- `/` — dashboard
- `/owners` — owner registration/list
- `/pets` — pet registration and vaccination recording
- `/vaccines` — vaccine type and interval management
- `/pets/{id}` — complete vaccination history

## 7. REST API

### Owners

```text
POST   /api/owners
GET    /api/owners
GET    /api/owners/{id}
PUT    /api/owners/{id}
DELETE /api/owners/{id}
```

Example:

```json
{
  "ownerName": "Arun Kumar",
  "email": "arun@example.com",
  "phone": "9876543210"
}
```

### Pets

```text
POST   /api/pets
GET    /api/pets
GET    /api/pets/{id}
PUT    /api/pets/{id}
DELETE /api/pets/{id}
```

Example:

```json
{
  "petName": "Bruno",
  "species": "Dog",
  "breed": "Labrador",
  "dateOfBirth": "2022-04-10",
  "ownerId": 1
}
```

Search by pet name:

```text
GET /api/pets?name=Bruno
```

### Vaccine types

```text
POST   /api/vaccine-types
GET    /api/vaccine-types
GET    /api/vaccine-types/{id}
PUT    /api/vaccine-types/{id}
DELETE /api/vaccine-types/{id}
```

Example:

```json
{
  "vaccineName": "Rabies",
  "description": "Demo vaccine type",
  "intervalDays": 365
}
```

### Vaccination records

Record a vaccination:

```text
POST /api/pets/{petId}/vaccinations
```

Example:

```json
{
  "vaccinationDate": "2026-09-28",
  "vaccineTypeId": 1,
  "notes": "Annual vaccination"
}
```

The response contains:

```json
{
  "vaccinationId": 1,
  "petId": 1,
  "petName": "Bruno",
  "vaccineTypeId": 1,
  "vaccineName": "Rabies",
  "vaccinationDate": "2026-09-28",
  "nextDueDate": "2027-09-28",
  "notes": "Annual vaccination"
}
```

The important assessment rule is implemented in the service layer:

```text
nextDueDate = vaccinationDate + intervalDays
```

### Due reminders

```text
GET /api/vaccinations/due
GET /api/vaccinations/due?days=7
```

The default is 7 days.

### Complete vaccination history

```text
GET /api/pets/{petId}/vaccination-history
```

### Update/delete vaccination

```text
GET    /api/vaccinations/{id}
PUT    /api/vaccinations/{id}
DELETE /api/vaccinations/{id}
```

## 8. Business rules / edge cases

The backend intentionally handles the cases that can be demonstrated in Postman:

1. Missing required fields → `400 Bad Request`
2. Invalid email → `400 Bad Request`
3. Negative/zero IDs → `400 Bad Request`
4. Future pet DOB → `400 Bad Request`
5. Future vaccination date → `400 Bad Request`
6. Vaccination before pet DOB → `400 Bad Request`
7. Vaccine interval <= 0 → `400 Bad Request`
8. Duplicate owner email → `409 Conflict`
9. Duplicate vaccine type name → `409 Conflict`
10. Unknown owner/pet/vaccine/record ID → `404 Not Found`
11. Owner cannot be deleted while pets still belong to that owner → `400 Bad Request`
12. `nextDueDate` is never accepted from the request and is recalculated by the service
13. Due reminder window cannot be negative or greater than 365 days
14. Deleting a pet also deletes its vaccination records because they are child records of the pet

## 9. Why DTOs are used

The reference project directly binds entities to REST requests. This project separates request/response DTOs from JPA entities because it makes the API safer and avoids exposing relationship graphs or accidentally accepting fields such as `nextDueDate`.

This is still the same layered pattern as the reference:

```text
Controller → Service → Repository → MySQL
                  ↓
              Business Rules
```

## 10. Postman testing

Import:

```text
postman/PetCareBook.postman_collection.json
```

The collection contains:

- create owner
- list/get/update/delete owner
- create pet
- list/get/update/delete pet
- create/list/get/update/delete vaccine type
- record vaccination
- get vaccination by ID
- update/delete vaccination
- get vaccination history
- get due vaccinations
- negative/edge-case requests

Run the normal requests first so IDs are available. The collection also contains tests that expect `400`, `404`, and `409` for invalid cases.

## 11. Assessment/demo flow

For the demo, a clean sequence is:

1. Start MySQL.
2. Start Spring Boot.
3. Open `/`.
4. Create an owner.
5. Create a pet using that owner ID.
6. Create or use a vaccine type with a known interval.
7. Record a vaccination.
8. Show the returned `nextDueDate`.
9. Open the pet history and show the vaccination record.
10. Use `/api/vaccinations/due?days=7` to show the reminder query.
11. Use Postman to demonstrate validation and error handling.

## 12. Note about vaccine intervals

The seeded intervals are **demo data for the academic application**, not veterinary guidance. If your faculty expects particular intervals, edit the vaccine type records through the API or UI.

