# Petstore API Endpoints

## Overview

The Petstore API provides a comprehensive REST interface for managing pet inventory, shopping carts, and orders. This documentation covers all available endpoints, request/response formats, and error handling.

**Base URL:** `http://localhost:8080/api`  
**Authentication:** None (for prototyping purposes)  
**Content-Type:** `application/json`

---

## Pet Inventory Endpoints

### GET /api/pets
**Description:** Retrieve all pets in the inventory

**Method:** `GET`  
**Path:** `/api/pets`  
**Parameters:** None

**Response (200 OK)**
```json
[
  {
    "id": "pet-001",
    "name": "Fluffy",
    "species": "cat",
    "breed": "Persian",
    "age": 3,
    "price": 199.99,
    "stock": 5,
    "description": "Friendly and playful cat"
  },
  {
    "id": "pet-002",
    "name": "Max",
    "species": "dog",
    "breed": "Golden Retriever",
    "age": 2,
    "price": 499.99,
    "stock": 2,
    "description": "Loyal and energetic dog"
  }
]
```

**Status Codes:** 200 OK, 500 Internal Server Error

---

### GET /api/pets/{id}
**Description:** Retrieve a specific pet by ID

**Method:** `GET`  
**Path:** `/api/pets/{id}`  
**Parameters:**
- `id` (path, required): The unique identifier of the pet

**Response (200 OK)**
```json
{
  "id": "pet-001",
  "name": "Fluffy",
  "species": "cat",
  "breed": "Persian",
  "age": 3,
  "price": 199.99,
  "stock": 5,
  "description": "Friendly and playful cat"
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Pet not found",
  "code": "PET_NOT_FOUND",
  "message": "No pet found with ID: pet-001",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 404 Not Found, 500 Internal Server Error

---

### GET /api/pets/species/{species}
**Description:** Retrieve all pets of a specific species

**Method:** `GET`  
**Path:** `/api/pets/species/{species}`  
**Parameters:**
- `species` (path, required): The species type (e.g., "cat", "dog", "bird", "fish")

**Response (200 OK)**
```json
[
  {
    "id": "pet-001",
    "name": "Fluffy",
    "species": "cat",
    "breed": "Persian",
    "age": 3,
    "price": 199.99,
    "stock": 5
  },
  {
    "id": "pet-003",
    "name": "Whiskers",
    "species": "cat",
    "breed": "Siamese",
    "age": 1,
    "price": 249.99,
    "stock": 3
  }
]
```

**Status Codes:** 200 OK, 400 Bad Request, 500 Internal Server Error

---

### GET /api/pets/inventory/low-stock
**Description:** Retrieve pets with low stock levels (stock < 3)

**Method:** `GET`  
**Path:** `/api/pets/inventory/low-stock`  
**Parameters:** None

**Response (200 OK)**
```json
[
  {
    "id": "pet-002",
    "name": "Max",
    "species": "dog",
    "breed": "Golden Retriever",
    "price": 499.99,
    "stock": 2,
    "status": "low_stock"
  }
]
```

**Status Codes:** 200 OK, 500 Internal Server Error

---

### POST /api/pets
**Description:** Create a new pet in the inventory

**Method:** `POST`  
**Path:** `/api/pets`

**Request Body**
```json
{
  "name": "Buddy",
  "species": "dog",
  "breed": "Labrador",
  "age": 2,
  "price": 549.99,
  "stock": 4,
  "description": "Friendly and intelligent dog"
}
```

**Response (201 Created)**
```json
{
  "id": "pet-004",
  "name": "Buddy",
  "species": "dog",
  "breed": "Labrador",
  "age": 2,
  "price": 549.99,
  "stock": 4,
  "description": "Friendly and intelligent dog"
}
```

**Error Response (400 Bad Request)**
```json
{
  "error": "Invalid request",
  "code": "VALIDATION_ERROR",
  "message": "Pet price must be greater than 0",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 201 Created, 400 Bad Request, 500 Internal Server Error

---

### PUT /api/pets/{id}
**Description:** Update an existing pet

**Method:** `PUT`  
**Path:** `/api/pets/{id}`  
**Parameters:**
- `id` (path, required): The unique identifier of the pet

**Request Body**
```json
{
  "name": "Fluffy",
  "species": "cat",
  "breed": "Persian",
  "age": 4,
  "price": 229.99,
  "stock": 6,
  "description": "Friendly and playful cat"
}
```

**Response (200 OK)**
```json
{
  "id": "pet-001",
  "name": "Fluffy",
  "species": "cat",
  "breed": "Persian",
  "age": 4,
  "price": 229.99,
  "stock": 6,
  "description": "Friendly and playful cat"
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Pet not found",
  "code": "PET_NOT_FOUND",
  "message": "No pet found with ID: pet-001",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 400 Bad Request, 404 Not Found, 500 Internal Server Error

---

### DELETE /api/pets/{id}
**Description:** Delete a pet from the inventory

**Method:** `DELETE`  
**Path:** `/api/pets/{id}`  
**Parameters:**
- `id` (path, required): The unique identifier of the pet

**Response (204 No Content)**
```
(No body)
```

**Error Response (404 Not Found)**
```json
{
  "error": "Pet not found",
  "code": "PET_NOT_FOUND",
  "message": "No pet found with ID: pet-999",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 204 No Content, 404 Not Found, 500 Internal Server Error

---

## Cart Management Endpoints

### GET /api/cart
**Description:** Retrieve the current shopping cart

**Method:** `GET`  
**Path:** `/api/cart`  
**Parameters:** None

**Response (200 OK)**
```json
{
  "cartId": "cart-user-123",
  "items": [
    {
      "cartItemId": "cart-item-001",
      "petId": "pet-001",
      "name": "Fluffy",
      "price": 199.99,
      "quantity": 1
    },
    {
      "cartItemId": "cart-item-002",
      "petId": "pet-002",
      "name": "Max",
      "price": 499.99,
      "quantity": 1
    }
  ],
  "itemCount": 2,
  "subtotal": 699.98,
  "tax": 55.99,
  "total": 755.97,
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 500 Internal Server Error

---

### GET /api/cart/total
**Description:** Get the cart total with breakdown

**Method:** `GET`  
**Path:** `/api/cart/total`  
**Parameters:** None

**Response (200 OK)**
```json
{
  "cartId": "cart-user-123",
  "itemCount": 2,
  "subtotal": 699.98,
  "tax": 55.99,
  "shipping": 10.00,
  "discount": 0.00,
  "total": 765.97
}
```

**Status Codes:** 200 OK, 500 Internal Server Error

---

### POST /api/cart/items
**Description:** Add a pet to the shopping cart

**Method:** `POST`  
**Path:** `/api/cart/items`

**Request Body**
```json
{
  "petId": "pet-001",
  "quantity": 1
}
```

**Response (201 Created)**
```json
{
  "cartItemId": "cart-item-003",
  "petId": "pet-001",
  "name": "Fluffy",
  "price": 199.99,
  "quantity": 1,
  "itemTotal": 199.99
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Pet not found",
  "code": "PET_NOT_FOUND",
  "message": "Pet with ID pet-001 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Error Response (400 Bad Request)**
```json
{
  "error": "Invalid request",
  "code": "INSUFFICIENT_STOCK",
  "message": "Only 5 units available in stock, requested 10",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 201 Created, 400 Bad Request, 404 Not Found, 500 Internal Server Error

---

### PUT /api/cart/items/{cartItemId}
**Description:** Update the quantity of an item in the cart

**Method:** `PUT`  
**Path:** `/api/cart/items/{cartItemId}`  
**Parameters:**
- `cartItemId` (path, required): The cart item identifier

**Request Body**
```json
{
  "quantity": 2
}
```

**Response (200 OK)**
```json
{
  "cartItemId": "cart-item-001",
  "petId": "pet-001",
  "name": "Fluffy",
  "price": 199.99,
  "quantity": 2,
  "itemTotal": 399.98
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Cart item not found",
  "code": "CART_ITEM_NOT_FOUND",
  "message": "Cart item with ID cart-item-001 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 400 Bad Request, 404 Not Found, 500 Internal Server Error

---

### DELETE /api/cart/items/{cartItemId}
**Description:** Remove an item from the shopping cart

**Method:** `DELETE`  
**Path:** `/api/cart/items/{cartItemId}`  
**Parameters:**
- `cartItemId` (path, required): The cart item identifier

**Response (204 No Content)**
```
(No body)
```

**Error Response (404 Not Found)**
```json
{
  "error": "Cart item not found",
  "code": "CART_ITEM_NOT_FOUND",
  "message": "Cart item with ID cart-item-999 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 204 No Content, 404 Not Found, 500 Internal Server Error

---

### DELETE /api/cart
**Description:** Clear all items from the shopping cart

**Method:** `DELETE`  
**Path:** `/api/cart`  
**Parameters:** None

**Response (204 No Content)**
```
(No body)
```

**Status Codes:** 204 No Content, 500 Internal Server Error

---

## Order Processing Endpoints

### GET /api/orders
**Description:** Retrieve all orders

**Method:** `GET`  
**Path:** `/api/orders`  
**Parameters:** None

**Response (200 OK)**
```json
[
  {
    "orderId": "order-001",
    "customerId": "cust-123",
    "status": "pending",
    "items": [
      {
        "petId": "pet-001",
        "name": "Fluffy",
        "price": 199.99,
        "quantity": 1
      }
    ],
    "subtotal": 199.99,
    "tax": 15.99,
    "shipping": 10.00,
    "total": 225.98,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
]
```

**Status Codes:** 200 OK, 500 Internal Server Error

---

### GET /api/orders/{id}
**Description:** Retrieve a specific order by ID

**Method:** `GET`  
**Path:** `/api/orders/{id}`  
**Parameters:**
- `id` (path, required): The unique identifier of the order

**Response (200 OK)**
```json
{
  "orderId": "order-001",
  "customerId": "cust-123",
  "status": "processing",
  "items": [
    {
      "petId": "pet-001",
      "name": "Fluffy",
      "price": 199.99,
      "quantity": 1
    },
    {
      "petId": "pet-002",
      "name": "Max",
      "price": 499.99,
      "quantity": 1
    }
  ],
  "subtotal": 699.98,
  "tax": 55.99,
  "shipping": 15.00,
  "total": 770.97,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T11:00:00Z"
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Order not found",
  "code": "ORDER_NOT_FOUND",
  "message": "Order with ID order-001 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 404 Not Found, 500 Internal Server Error

---

### GET /api/orders/status/{status}
**Description:** Retrieve orders filtered by status

**Method:** `GET`  
**Path:** `/api/orders/status/{status}`  
**Parameters:**
- `status` (path, required): Order status ("pending", "processing", "shipped", "delivered", "cancelled")

**Response (200 OK)**
```json
[
  {
    "orderId": "order-001",
    "customerId": "cust-123",
    "status": "processing",
    "itemCount": 2,
    "total": 770.97,
    "createdAt": "2024-01-15T10:30:00Z"
  },
  {
    "orderId": "order-002",
    "customerId": "cust-124",
    "status": "processing",
    "itemCount": 1,
    "total": 225.98,
    "createdAt": "2024-01-15T11:00:00Z"
  }
]
```

**Status Codes:** 200 OK, 400 Bad Request, 500 Internal Server Error

---

### POST /api/orders
**Description:** Create a new order from the current shopping cart

**Method:** `POST`  
**Path:** `/api/orders`

**Request Body**
```json
{
  "customerId": "cust-123",
  "shippingAddress": {
    "street": "123 Main St",
    "city": "Seattle",
    "state": "WA",
    "zipCode": "98101",
    "country": "USA"
  },
  "billingAddress": {
    "street": "123 Main St",
    "city": "Seattle",
    "state": "WA",
    "zipCode": "98101",
    "country": "USA"
  }
}
```

**Response (201 Created)**
```json
{
  "orderId": "order-003",
  "customerId": "cust-123",
  "status": "pending",
  "items": [
    {
      "petId": "pet-001",
      "name": "Fluffy",
      "price": 199.99,
      "quantity": 1
    }
  ],
  "shippingAddress": {
    "street": "123 Main St",
    "city": "Seattle",
    "state": "WA",
    "zipCode": "98101",
    "country": "USA"
  },
  "subtotal": 199.99,
  "tax": 15.99,
  "shipping": 10.00,
  "total": 225.98,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Error Response (400 Bad Request)**
```json
{
  "error": "Invalid request",
  "code": "EMPTY_CART",
  "message": "Cannot create order from empty cart",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 201 Created, 400 Bad Request, 500 Internal Server Error

---

### PUT /api/orders/{id}/status
**Description:** Update the status of an existing order

**Method:** `PUT`  
**Path:** `/api/orders/{id}/status`  
**Parameters:**
- `id` (path, required): The unique identifier of the order

**Request Body**
```json
{
  "status": "shipped",
  "notes": "Order has been dispatched"
}
```

**Response (200 OK)**
```json
{
  "orderId": "order-001",
  "customerId": "cust-123",
  "status": "shipped",
  "items": [...],
  "total": 225.98,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T11:30:00Z"
}
```

**Error Response (404 Not Found)**
```json
{
  "error": "Order not found",
  "code": "ORDER_NOT_FOUND",
  "message": "Order with ID order-001 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Error Response (400 Bad Request)**
```json
{
  "error": "Invalid status",
  "code": "INVALID_STATUS",
  "message": "Status 'invalid' is not valid. Allowed: pending, processing, shipped, delivered, cancelled",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 200 OK, 400 Bad Request, 404 Not Found, 500 Internal Server Error

---

### DELETE /api/orders/{id}
**Description:** Cancel an order

**Method:** `DELETE`  
**Path:** `/api/orders/{id}`  
**Parameters:**
- `id` (path, required): The unique identifier of the order

**Response (204 No Content)**
```
(No body)
```

**Error Response (404 Not Found)**
```json
{
  "error": "Order not found",
  "code": "ORDER_NOT_FOUND",
  "message": "Order with ID order-001 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Error Response (400 Bad Request)**
```json
{
  "error": "Cannot cancel order",
  "code": "ORDER_ALREADY_SHIPPED",
  "message": "Cannot cancel an order that has already been shipped",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Status Codes:** 204 No Content, 400 Bad Request, 404 Not Found, 500 Internal Server Error

---

## Error Responses

### Error Response Format

All error responses follow a consistent format:

```json
{
  "error": "Human-readable error type",
  "code": "MACHINE_READABLE_CODE",
  "message": "Detailed error message",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Common Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| `PET_NOT_FOUND` | 404 | The requested pet does not exist |
| `ORDER_NOT_FOUND` | 404 | The requested order does not exist |
| `CART_ITEM_NOT_FOUND` | 404 | The requested cart item does not exist |
| `VALIDATION_ERROR` | 400 | Request body validation failed |
| `INSUFFICIENT_STOCK` | 400 | Requested quantity exceeds available stock |
| `EMPTY_CART` | 400 | Cart is empty, cannot proceed with operation |
| `INVALID_STATUS` | 400 | Provided status is not valid |
| `ORDER_ALREADY_SHIPPED` | 400 | Cannot perform operation on shipped order |
| `INTERNAL_SERVER_ERROR` | 500 | Unexpected server error |

### Example Error Responses

**400 Bad Request**
```json
{
  "error": "Invalid request",
  "code": "VALIDATION_ERROR",
  "message": "Field 'price' is required and must be a positive number",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**404 Not Found**
```json
{
  "error": "Resource not found",
  "code": "PET_NOT_FOUND",
  "message": "Pet with ID 'pet-999' does not exist",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**500 Internal Server Error**
```json
{
  "error": "Server error",
  "code": "INTERNAL_SERVER_ERROR",
  "message": "An unexpected error occurred. Please try again later.",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## Quick Start Examples

### Example 1: Add Pet to Inventory

**Request:**
```bash
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tweety",
    "species": "bird",
    "breed": "Canary",
    "age": 1,
    "price": 49.99,
    "stock": 10,
    "description": "Beautiful yellow canary with lovely singing voice"
  }'
```

**Response (201 Created):**
```json
{
  "id": "pet-005",
  "name": "Tweety",
  "species": "bird",
  "breed": "Canary",
  "age": 1,
  "price": 49.99,
  "stock": 10,
  "description": "Beautiful yellow canary with lovely singing voice"
}
```

---

### Example 2: Add Pet to Cart and Checkout

**Step 1: Get available pets**
```bash
curl -X GET http://localhost:8080/api/pets
```

**Step 2: Add pet to cart**
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Content-Type: application/json" \
  -d '{
    "petId": "pet-001",
    "quantity": 1
  }'
```

**Step 3: View cart**
```bash
curl -X GET http://localhost:8080/api/cart
```

**Step 4: Create order**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust-123",
    "shippingAddress": {
      "street": "123 Main St",
      "city": "Seattle",
      "state": "WA",
      "zipCode": "98101",
      "country": "USA"
    },
    "billingAddress": {
      "street": "123 Main St",
      "city": "Seattle",
      "state": "WA",
      "zipCode": "98101",
      "country": "USA"
    }
  }'
```

**Step 5: Clear cart after order**
```bash
curl -X DELETE http://localhost:8080/api/cart
```

---

### Example 3: Track Order Status

**Get all pending orders:**
```bash
curl -X GET http://localhost:8080/api/orders/status/pending
```

**Get specific order details:**
```bash
curl -X GET http://localhost:8080/api/orders/order-001
```

**Update order status:**
```bash
curl -X PUT http://localhost:8080/api/orders/order-001/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "shipped",
    "notes": "Order dispatched via FedEx"
  }'
```

---

## API Response Headers

All responses include the following headers:

- `Content-Type: application/json`
- `X-Request-ID: <unique-request-id>` (for tracing)
- `X-Response-Time: <milliseconds>` (response time in milliseconds)

---

## Rate Limiting

Currently, the Petstore API does not implement rate limiting for prototyping purposes. Rate limiting will be added in future versions.

---

## Versioning

The current API version is **v1**. Version information may be included in future endpoints as `http://localhost:8080/api/v1/`.

---

*Last Updated: January 2024*  
*For support or questions, contact the Petstore API team*
