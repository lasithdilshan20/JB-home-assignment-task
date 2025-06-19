# Online Shop Backend API

This is a minimalistic backend web application for an online shop. The application provides CRUD operations for Customers, Shop Item Categories, Shop Items, and Orders.

## Technologies Used

- Java 11
- Spring Boot 2.7.14
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5 for testing

## Data Entities

The application operates with the following data entities:

1. **Customer**
   - ID (Long)
   - Name (String)
   - Surname (String)
   - Email (String)

2. **ShopItemCategory**
   - ID (Long)
   - Title (String)
   - Description (String)

3. **ShopItem**
   - ID (Long)
   - Title (String)
   - Description (String)
   - Price (Float)
   - Categories (List of ShopItemCategory)

4. **OrderItem**
   - ID (Long)
   - ShopItem (ShopItem)
   - Quantity (Integer)

5. **Order**
   - ID (Long)
   - Customer (Customer)
   - Items (List of OrderItem)
   - CreatedAt (LocalDateTime)

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup and Run

### Clone the Repository

```bash
git clone <repository-url>
cd Task1-BackendAPI
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080. You can access the API at `http://localhost:8080/api/`.

### Access H2 Console

You can access the H2 database console at `http://localhost:8080/h2-console` with the following credentials:
- JDBC URL: `jdbc:h2:mem:shopdb`
- Username: `sa`
- Password: `password`

## API Endpoints

### Customer Endpoints

- **GET /api/customers** - Get all customers
- **GET /api/customers/{id}** - Get customer by ID
- **POST /api/customers** - Create a new customer
- **PUT /api/customers/{id}** - Update an existing customer
- **DELETE /api/customers/{id}** - Delete a customer

### Shop Item Category Endpoints

- **GET /api/categories** - Get all categories
- **GET /api/categories/{id}** - Get category by ID
- **POST /api/categories** - Create a new category
- **PUT /api/categories/{id}** - Update an existing category
- **DELETE /api/categories/{id}** - Delete a category

### Shop Item Endpoints

- **GET /api/items** - Get all shop items
- **GET /api/items/{id}** - Get shop item by ID
- **GET /api/items/category/{categoryId}** - Get shop items by category
- **GET /api/items/price?maxPrice={maxPrice}** - Get shop items with price less than or equal to maxPrice
- **POST /api/items** - Create a new shop item
- **PUT /api/items/{id}** - Update an existing shop item
- **DELETE /api/items/{id}** - Delete a shop item
- **POST /api/items/{itemId}/categories/{categoryId}** - Add a category to a shop item
- **DELETE /api/items/{itemId}/categories/{categoryId}** - Remove a category from a shop item

### Order Endpoints

- **GET /api/orders** - Get all orders
- **GET /api/orders/{id}** - Get order by ID
- **GET /api/orders/customer/{customerId}** - Get orders by customer
- **GET /api/orders/date-range?start={start}&end={end}** - Get orders created within a specific time range
- **POST /api/orders** - Create a new order
- **PUT /api/orders/{id}** - Update an existing order
- **DELETE /api/orders/{id}** - Delete an order
- **POST /api/orders/{orderId}/items** - Add an item to an order
- **DELETE /api/orders/{orderId}/items/{itemId}** - Remove an item from an order

## Running Tests

To run the tests, execute the following command:

```bash
mvn test
```

The tests include endpoint autotests for each controller to verify the functionality of the API.

## Sample Data

The application initializes with sample data for testing purposes:
- 2 customers (John Doe and Jane Smith)
- 3 categories (Electronics, Books, Clothing)
- 5 shop items (Laptop, Smartphone, Novel, T-Shirt, Jeans)
- 2 orders with various items

## Error Handling

The API includes global exception handling for consistent error responses:
- 404 Not Found for entity not found errors
- 400 Bad Request for validation errors and illegal arguments
- 500 Internal Server Error for unexpected errors