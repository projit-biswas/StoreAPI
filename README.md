# 🏪 Store API

A Spring Boot-based RESTful API for managing products, customers, carts, orders and payments — ideal for online store or
e-commerce platforms.

## 🔧 Technology Stack

- **Java 21+**
- **Spring Boot** (Web, Data JPA, Security)
- **Spring Data JPA** for ORM
- **Spring Security** for authentication and authorization
- **MySQL** for relational database
- **Flyway** for database migrations
- **JWT** for secure token-based authentication
- **Lombok** to reduce boilerplate code
- **Swagger UI** for API documentation and testing
- **Maven** for project management

---

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based login system
- Role-based access (Admin & Customer)
- Secure password handling

### 👤 User Management
- Register a new customer
- Admin user support
- Update/delete user profiles

### 📦 Product Management
- Admin: Create, update, delete products
- Customer: View and filter products

### 🛒 Cart Operations
- Add/remove products from cart
- Update quantities
- View cart details
- Clear cart

### 📦 Order Management
- Place orders from cart
- Cancel orders
- View order history (Customer/Admin)
- Track order status

### 💳 Payment Processing

- Integrate with payment gateways (mocked for demo)
- Process payments for orders
- Stripe integration for payment processing
- View payment history
- Handle payment status updates

### 🗺️ Address Management
- Add/update/remove address linked to users
- Associate address during order placement

### ⚙️ Additional Highlights
- Centralized exception handling
- Data Transfer Objects (DTOs) for clean separation
- Feature-based architecture

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21 or higher
- Maven 3.x
- MySQL Server

### 📁 Clone the Repo

```bash
git clone https://github.com/projit-biswas/store-api.git
cd store-api
