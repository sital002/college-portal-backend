# 🎓 College Portal Backend

This is a **Spring Boot** backend for a College Portal system that manages user authentication, student data, and other college-related functionalities.

## 📌 Features
- **User Authentication**
    - Sign-up with validation
    - Login with JWT-based authentication
- **Student Management**
    - Add, update, and retrieve student information
- **Admin Features**
    - Manage users, courses, and assignments
- **REST API**
    - Well-structured API endpoints for frontend integration

---

## 🚀 Tech Stack
- **Java 17+**
- **Spring Boot 3+**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Lombok**
- **Jakarta Validation**

---

## 🔧 Installation & Setup

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/your-username/college-portal-backend.git
cd college-portal-backend
```
#### Update the ```application.properties``` file 
```sh
spring.datasource.url=jdbc:mysql://localhost:3306/college_portal
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
### Run the application
```shell
./gradlew bootRun
```