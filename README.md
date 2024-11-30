# Online Book Rental System - Spring Boot Security

This is a simplified version of an online book rental system, built using Spring Boot Security and MySQL. It includes user registration, login functionality, book management, and rental management, with authentication and authorization mechanisms using Basic Authentication.

## Key Features

#### 1. User Registration and Login
* Users can register using their email and password.
* Passwords are hashed using BCrypt before storing them in the database.
* Roles: USER (default) and ADMIN.
* Registered users can log in using their email and password.

#### 2. Book Management
* Books have the following fields: Title, Author, Genre, and Availability Status.
* Availability Status indicates whether the book is available for rent.
* Any user can browse all available books.
* Only ADMIN users can create, update, or delete books.

#### 3. Rental Management
* Users can rent books, but they can only have two active rentals at a time.
* Users can return books that they have rented.

#### 4. Authentication and Authorization
* Basic Authentication is used.
* Users with the ADMIN role have the ability to perform all actions (create, update, delete books).
* USER role can only read books and rent/return books.

## Technologies Used
* Spring Boot (for RESTful APIs)
* Spring Security (for Basic Authentication and Authorization)
* MySQL (for persistence)
* JPA/Hibernate (for ORM)
* BCrypt (for password hashing)
* Maven (for project management)
* JUnit (for unit testing)

## API Endpoints

| S.No. | Access     | Method | Endpoint                        | Description                          |
|-------|------------|--------|---------------------------------|--------------------------------------|
| 1     | Everyone   | POST   | /rent-read/v1/user/register     | Register a new user.                 |
| 2     | Everyone   | PUT    | /rent-read/v1/user/login        | Login user with credentials.         |
| 3     | Admin      | POST   | /rent-read/v1/book/add          | Add new book in system.              |
| 4     | Admin/User | GET    | /rent-read/v1/book              | Retrieve a list of all books.        |
| 5     | Admin      | GET    | /rent-read/v1/book/{id}         | Retrieve details of a specific book. |
| 6     | Admin      | PUT    | /rent-read/v1/book/{id}         | Update details of a specific book.   |
| 7     | Admin      | DELETE | /rent-read/v1/book/{id}         | Remove a specific book.              |
| 8     | Admin/User | POST   | /rent-read/v1/books/{id}/rent   | Rent a specific book.                |
| 9     | Admin/User | POST   | /rent-read/v1/books/{id}/return | Return a rented a book.              |


## Pre-requisites
1. Java 21+
2. Gradle (or use the Gradle wrapper provided in the project)
3. SQL

## Installation
##### 1. Clone the repository:
```bash
git clone https://github.com/divii98/rent-read-application.git
cd your-repo-name
```

#### 2. Configure Database Connection: 
Make sure to set up your database (e.g., MySQL) and update the database configurations in application.properties.
```
spring.datasource.url=jdbc:mysql://localhost:3306/rent_read
spring.datasource.username=root
spring.datasource.password=password
```

##### 4. Build and Run the Service:

Use Gradle to build and run the application
``
./gradlew bootRun
``

##### 5. Access the Application:

By default, the application will run at:
> http://localhost:8080


## Postman Collections

For testing the API endpoints, you can use the Postman collection provided in the link below:

[Rent Read Service API Collection](https://www.postman.com/apicollections-7830/apicollections/collection/vaivei7/rent-read-apis?action=share&creator=28961073)