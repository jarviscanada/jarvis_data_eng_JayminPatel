# Introduction

The JDBC Application allows users' manipulating a PostgreSQL database through Java programming. The application is equipped with the ability to perform CRUD (Create, Read, Update, Delete) operations on Customer and Order data. The Data Access Object (DAO) pattern is implemented to manipulate Data Transfer Objects (DTO). The following include some of the key technologies used in this implementation:

1. Java 8
2. JDBC API
3. PostgreSQL
4. Maven

# Implementation

ER Diagram


# Design Patterns

### Data Access Object (DAO) Pattern
DAO pattern is an interface for data persistence with a focus on atomicity of relational operations and delegation of logic to SQL queries. The DAO pattern links the business logic and corresponding relational data. It operates with the object-oriented code through the Data Transfer Objects (DTOs) that represent the state of some business entities and maps them to the relations (tables) in the database.

The DAO is selected in this implementation, since the database has normalized relations which allow to perform SQL operations efficiently. To follow the DAO pattern, a DTO class is been defined for each relation in the table (Customer, Order, OrderItem, etc.). Then, the database operations are implemented in CustomerDAO and OrderDAO classes.

### Repository Pattern
The Repository pattern is another approach to the data persistence. While from the user's perspective it can be similar to the DAO pattern, the Repository pattern is often used in the distributed systems. The Repository is usually implemented as a single large table containing for a class and performs the business logic directly in code, rather then through SQL queries.

The Repository pattern is most applicable in database constrained applications that benefit from horizontal scaling. In such case, the data tables are often denormalized and can be sharded and distributed accordingly, which can mean higher availability and partition tolerance. However, these benefits come at the cost of potential data inconsistency, since atomic operations become hard or even impossible.

# Test

A test database is set up in PostgreSQL locally and populated with lines code of data across 5 different tables. Manual testing was conducted in Java and subsequently verified through CLI access of the PostgreSQL test database.
