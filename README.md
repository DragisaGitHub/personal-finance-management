
# Personal Finance Management System

Welcome to the **Personal Finance Management System**! This robust platform allows users to manage their personal finances efficiently, set savings goals, and track transactions seamlessly. With advanced features, user-friendly dashboards, and secure authentication, this system is built to help individuals take control of their financial future.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Architecture Overview](#architecture-overview)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Contributing](#contributing)
7. [License](#license)

---

## Features

- **Transaction Management**: Add, edit, delete, and categorize financial transactions with multi-currency support.
- **Savings Goals & Alerts**: Set financial goals, track progress, and receive alerts when you're on or off track.
- **User Profile**: Manage personal profiles with Keycloak authentication.
- **Country-based Currency Selection**: Choose currency options (Dollar, Euro, Dinar) during transaction creation.
- **Admin Dashboard**: Powerful admin dashboard to oversee user activities and monitor financial trends.
- **Responsive UI**: Built with Angular and Tailwind CSS for a seamless experience across devices.
- **Authentication**: Secure login and role-based access using Keycloak.
- **Containerized Setup**: Easily deploy the system using Docker and Docker Compose for consistent development environments.

---

## Tech Stack

This project leverages modern technologies to deliver a high-performance, scalable, and secure platform:

- **Backend**: Spring Boot 3 (Gradle)
    - REST API built with Spring Boot for optimal performance.
    - PostgreSQL for persistent and reliable data storage.
    - Keycloak for user authentication and role-based access control.
    - Custom `HelperUtils` class to handle Keycloak token extraction.

- **Frontend**: Angular 18 (Yarn)
    - Built with Angular Material and Tailwind CSS for a responsive and modern interface.
    - Transaction and goal management views that integrate seamlessly with the backend.
    - Directives like `aipShowForRoles` ensure UI components are shown based on user roles.

- **Containerization**: Docker & Docker Compose
    - All services are containerized for easy setup and deployment.

---

## Architecture Overview

The Personal Finance Management System follows a modular architecture to ensure separation of concerns, scalability, and maintainability. Below is a high-level breakdown:

- **Frontend (Angular)**: The user-facing interface that interacts with the backend via REST APIs.
- **Backend (Spring Boot)**: Processes user requests, handles business logic, and communicates with the database.
- **Database (PostgreSQL)**: Stores transaction records, user profiles, and savings goals.
- **Authentication (Keycloak)**: Provides authentication and authorization using OAuth2 standards.
- **Containerization (Docker)**: Ensures consistent environments for development, testing, and production.

---

## Installation

To get this system up and running locally, follow these steps:

### Prerequisites
- Docker and Docker Compose
- Java 17
- Node.js (for Angular)
- Yarn package manager

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/personal-finance-management.git
   cd personal-finance-management
   ```

2. Run the backend:
   ```bash
   ./gradlew bootRun
   ```

3. Run the frontend:
   ```bash
   cd frontend
   yarn install
   yarn start
   ```

4. Start Docker for PostgreSQL and Keycloak:
   ```bash
   docker-compose up
   ```

5. Access the application at:
   ```
   Frontend: http://localhost:4200
   Backend: http://localhost:8080
   Keycloak: http://localhost:8081/auth
   ```

---

## Usage

### User Management:
- Register and login securely via Keycloak.
- Access the admin dashboard for user-specific data.

### Transaction Management:
- Easily add new transactions with real-time currency selection.
- Track your expenses and manage categories for better visibility.

### Savings Goals:
- Create goals and visualize progress with intuitive charts.
- Get alerts when you're nearing your goal or off track.

---

## Contributing

We welcome contributions from the community! If you'd like to contribute, please follow these guidelines:

1. Fork the repository.
2. Create a new feature branch: `git checkout -b feature/your-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature/your-feature`.
5. Submit a pull request.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

### Let's start managing your finances like a pro!