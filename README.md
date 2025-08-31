# 📩 Notification Sender App

A Spring Boot microservice for sending email notifications (via Gmail) to users.  
This service was originally built as a companion microservice for [Trusty Wallet](https://github.com/kalin-angelov/trusty-wallet), enabling email alerts for transactions and other important updates.

---

## ✨ Features
- Send transactional emails to users using Gmail SMTP.
- Integrates seamlessly with **Trusty Wallet** or other services.
- Built with **Spring Boot** for fast, scalable development.
- Persists user and notification data in **MySQL**.
- Runs on **Java 17**.

---

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot**
- **MySQL**
- **Maven**

---

## 📦 Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL database running locally or in the cloud
- Gmail account with [App Passwords](https://support.google.com/accounts/answer/185833) enabled (for SMTP)

### Steps
1. Clone the repository:

   ```bash
   git clone https://github.com/kalin-angelov/notification-sender-app.git
   cd notification-sender-app
   ```

3. Configure database in `application.properties` :
   
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/notificationdb
    spring.datasource.username=your_db_user
    spring.datasource.password=your_db_password
    
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@gmail.com
    spring.mail.password=your_app_password
    ```

5. Build and run the app:
   
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

---

## 🚀 Usage

Once running, the service exposes REST endpoints to send notifications.
Example request (JSON payload):

  ```json
  {
    "to": "user@example.com",
    "subject": "Trusty Wallet Transaction",
    "body": "You have received 59.99 Euro in your wallet."
  }
  ```

Call the endpoint with:

   ```bash
   curl -X POST http://localhost:8080/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{"to":"user@example.com","subject":"Test","body":"Hello from Notification Sender!"}'
   ```

The endpoints can also be tested with `Postman`:

| Method | Endpoint                                                                | Description                               |
| ------ | ----------------------------------------------------------------------- | ----------------------------------------- |
| POST   | `/api/v1/notifications`                                                 | Send notification                         |
| POST   | `/api/v1/notifications/preferences?userId={userId}`                     | Create or update notification preference  |
| GET    | `/api/v1/notifications?userId={userId}`                                 | Get user notifications history            |
| GET    | `/api/v1/notifications/preferences`                                     | Get user notification preference          |
| PUT    | `/api/v1/notifications/preferences?userId={userId}&enabled={enabled}`   | Turn On/Off notifications                 |

