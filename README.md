## Stack
Java 18 + Spring Boot + H2 in-memory DB

## How to Run Locally
Build and run JAR using Java 18 or later. Web server accepts API requests at http://localhost:8080/api

Demo products catalog is created on startup. Data does not persist between restarts.

## REST API Endpoints
| Method | URL               | Description                            |
|--------|-------------------|----------------------------------------|
| GET    | /cart             | Get shopping cart                      |
| PUT    | /cart             | Update shopping cart                   |
| POST   | /cart/submit      | Submit shopping cart to complete order |
| GET    | /orders           | Get all orders                         |
| GET    | /orders/new       | Get new orders                         |
| GET    | /orders/completed | Get completed orders                   |
| GET    | /orders/{id}      | Get order by id                        |
| DELETE | /orders/{id}      | Delete order                           |
| GET    | /products         | Get all products                       |
| GET    | /products/{id}    | Get product by id                      |
| POST   | /products         | Create product                         |
| PUT    | /products         | Update product                         |
| DELETE | /products/{id}    | Delete product                         |

## TODO
* Unit & integration tests
* Users & authentication (so far all users are anonymous and share same shopping cart)
* Authorization & permissions
* JSR 380 validation (for DTO's & entities)
* Handling corner cases (like adding same product more than one time to the cart etc...)