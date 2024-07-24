# SolveMe Recruitment Platform

SolveMe is a comprehensive recruitment platform designed to help companies streamline their hiring process through automated MCQs and coding assessments. This platform allows admins to set up various types of questions, manage candidates, and handle a high volume of simultaneous requests efficiently.

## Features

- **MCQ and Coding Questions:** Admins can create and manage multiple-choice and coding questions.
- **Scalable Backend:** The backend endpoint can handle more than 200 requests at once, ensuring smooth operation even during peak times.
- **User Management:** Allows management of candidates taking the assessments.
- **Result Analysis:** Provides detailed analysis of candidates' performance.
- **Security:** The result submitted by the user is first analyzed by antivirus software in docker for malicious activity, including any generated executable files.

## Technologies Used

- **Backend:** Spring Boot
- **Frontend:** Angular
- **Database:** MySQL
- **Authentication:** JWT
- **Others:** HTML, CSS, REST APIs, Docker

## Installation

### Prerequisites

- JDK 11 or higher
- Node.js and Angular CLI
- MySQL

### Backend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/divinefrancis948/COMPILER-PROJECT.git
   cd COMPILER-PROJECT/WebApp-Compiler-Backend

2. **Configure the database:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_databasespring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

3. **Build and run the backend:**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run

Use your favorite IDE to update the maven project and run the SpringBoot Application.


### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd COMPILER-PROJECT/WebApp-Compiler-Frontend

2. **Install dependencies:**
      ```bash
      npm i
3. **Run Frontend:**
   ```bash
   ng serve
