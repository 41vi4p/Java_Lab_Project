# 🎓 ECS Course Registration System

A comprehensive course registration management system built with Java Swing for college students and teachers, featuring a PostgreSQL database backend and modern UI design.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [Author](#author)

## 🌟 Overview

This Course Registration System was developed as part of a Java college lab project to demonstrate advanced Java programming concepts including:
- GUI development with Java Swing/AWT
- Database integration with PostgreSQL
- Object-oriented programming principles
- MVC design pattern
- DAO (Data Access Object) pattern

The system allows students to browse available courses, register for courses while respecting credit limits, and drop courses. Teachers can manage courses, view enrolled students, and track course capacity.

## ✨ Features

### Student Features
- **User Authentication**: Login with student ID or register as a new student
- **Course Browsing**: View courses organized by category:
  - Core Courses (PCC)
  - Program Electives (PEC)
  - Honours Courses
  - Open Electives
  - MDM Courses
- **Course Registration**: Add courses with real-time credit limit validation
- **Drop Courses**: Remove registered courses
- **Credit Tracking**: Monitor total credits and category-wise distribution
- **Course Details**: View detailed information about each course including:
  - Course name and instructor
  - Credits and maximum capacity
  - Current enrollment status
  - Prerequisites

### Teacher Features
- **Secure Login**: Authentication with username and password
- **Student Management**: View all registered students
- **Course Management**: Create, edit, and delete courses
- **Enrollment Tracking**: Monitor student enrollments
- **Dashboard**: Comprehensive view of system statistics

### System Features
- **Database Persistence**: All data stored in PostgreSQL database
- **Docker Support**: Easy database setup with Docker Compose
- **Modern UI**: Professional red-orange color scheme with intuitive design
- **Responsive Layout**: Organized grid layout for better user experience
- **Real-time Updates**: Instant feedback on all operations
- **Data Validation**: Comprehensive input validation and error handling

## 🛠️ Technology Stack

- **Language**: Java (JDK 8 or higher)
- **GUI Framework**: Java Swing/AWT
- **Database**: PostgreSQL 15
- **JDBC Driver**: PostgreSQL JDBC Driver 42.6.0
- **Containerization**: Docker & Docker Compose
- **Database Admin**: pgAdmin 4 (optional)

## 🏗️ Architecture

The system follows a layered architecture:

```
┌─────────────────────────────────────┐
│     Presentation Layer (GUI)        │
│  - CourseRegistrationSystem.java   │
│  - TeacherDashboard.java            │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│       Business Logic Layer          │
│  - CourseManager.java               │
│  - Student.java                     │
│  - Course.java                      │
│  - Teacher.java                     │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│       Data Access Layer (DAO)       │
│  - StudentDAO.java                  │
│  - CourseDAO.java                   │
│  - TeacherDAO.java                  │
│  - DatabaseConfig.java              │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Database Layer              │
│  - PostgreSQL Database              │
│  - Docker Container                 │
└─────────────────────────────────────┘
```

## 📦 Prerequisites

Before running this project, ensure you have:

- **Java Development Kit (JDK)** 8 or higher
  ```bash
  java -version
  ```

- **Docker and Docker Compose** (for database setup)
  ```bash
  docker --version
  docker-compose --version
  ```

- **PostgreSQL JDBC Driver** (included: `postgresql-42.6.0.jar`)

## 🚀 Installation

### Step 1: Clone or Download the Project

```bash
cd /path/to/project
cd AWT_project
```

### Step 2: Start the Database

Using Docker Compose (recommended):

```bash
# Start PostgreSQL and pgAdmin containers
docker-compose up -d

# Verify containers are running
docker ps
```

This will:
- Start PostgreSQL on port 5432
- Initialize the database with schema and sample data
- Start pgAdmin on port 8080 (optional)

### Step 3: Compile the Project

```bash
# Compile all Java files with PostgreSQL driver
javac -cp ".:postgresql-42.6.0.jar" *.java
```

### Step 4: Run the Application

```bash
# Option 1: Using the provided script
chmod +x run.sh
./run.sh

# Option 2: Manually run the application
java -cp ".:postgresql-42.6.0.jar" CourseRegistrationSystem
```

## 📖 Usage

### Student Login

1. Launch the application
2. Enter your student details:
   - **Student ID**: Unique identifier (e.g., "S001")
   - **Name**: Your full name
   - **Email**: Your email address
   - **Semester**: Current semester (1-8)
   - **Max Credits**: Maximum credits allowed (default: 18)
3. Click **"Student Login"** (creates new student if ID doesn't exist)

### Registering for Courses

1. Browse courses in different categories
2. Click on a course to view details
3. Click the **"Add"** button for the respective category
4. Monitor your credit usage in the top-right corner
5. System validates:
   - Available seats
   - Credit limit
   - Duplicate registrations

### Teacher Login

1. On the login screen, use teacher credentials:
   - **Username**: `admin`
   - **Password**: `admin123`
2. Click **"Teacher Login"**
3. Access the teacher dashboard

### Teacher Dashboard Operations

- **View Students**: See all registered students and their enrollments
- **Manage Courses**: Add, edit, or delete courses
- **Update Course Details**: Modify course information, capacity, instructor
- **Monitor Enrollments**: Track which students are enrolled in each course

## 📁 Project Structure

```
AWT_project/
├── Course.java                          # Course model class
├── CourseDAO.java                       # Course data access object
├── CourseManager.java                   # Course business logic manager
├── CourseRegistrationSystem.java        # Main student UI (primary version)
├── CourseRegistrationSystemEnhanced.java # Enhanced student UI
├── DatabaseConfig.java                  # Database connection configuration
├── Student.java                         # Student model class
├── StudentDAO.java                      # Student data access object
├── Teacher.java                         # Teacher model class
├── TeacherDAO.java                      # Teacher data access object
├── TeacherDashboard.java                # Teacher UI interface
├── docker-compose.yml                   # Docker configuration
├── postgresql-42.6.0.jar                # PostgreSQL JDBC driver
├── run.sh                               # Launch script
├── course_data.txt                      # File-based data backup
├── database/
│   └── init.sql                         # Database initialization script
└── README.md                            # This file
```

## 🗄️ Database Schema

The system uses three main tables:

### Students Table
```sql
CREATE TABLE students (
    student_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    semester INTEGER,
    max_credits INTEGER
);
```

### Courses Table
```sql
CREATE TABLE courses (
    course_id VARCHAR(50) PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    credits INTEGER NOT NULL,
    type VARCHAR(50),
    instructor VARCHAR(100),
    max_students INTEGER,
    enrolled_students INTEGER DEFAULT 0,
    prerequisites TEXT
);
```

### Teachers Table
```sql
CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    department VARCHAR(100),
    email VARCHAR(100)
);
```

### Enrollments Table
```sql
CREATE TABLE enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id VARCHAR(50) REFERENCES students(student_id),
    course_id VARCHAR(50) REFERENCES courses(course_id),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🎨 Design Highlights

### Color Scheme
The application features a professional red-orange theme:
- **Primary Color**: Deep Pink-Red (#DC267F)
- **Secondary Color**: Soft Pink Background (#FEF2F2)
- **Accent Color**: Fresh Green (#22C55E)
- **Warning Color**: Warm Orange (#FB9238)
- **Background**: Warm Cream-Orange (#FFF7ED)

### UI Components
- Card-based layout for better organization
- Hover effects on buttons for improved UX
- Real-time status updates with color-coded feedback
- Responsive grid layout for course categories
- Scroll panes for large data sets

## 🔧 Configuration

### Database Configuration

Edit `DatabaseConfig.java` to change database settings:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/ecs_courses";
private static final String DB_USER = "ecs_admin";
private static final String DB_PASSWORD = "ecs_password";
```

### Docker Configuration

Modify `docker-compose.yml` for custom database setup:

```yaml
environment:
  POSTGRES_DB: ecs_courses
  POSTGRES_USER: ecs_admin
  POSTGRES_PASSWORD: ecs_password
ports:
  - "5432:5432"
```

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Check if PostgreSQL container is running
docker ps | grep postgres

# View container logs
docker logs ecs_course_db

# Restart containers
docker-compose restart
```

### Compilation Errors
```bash
# Ensure JDBC driver is in the classpath
javac -cp ".:postgresql-42.6.0.jar" *.java

# Clean and recompile
rm *.class
javac -cp ".:postgresql-42.6.0.jar" *.java
```

### Port Conflicts
If port 5432 is already in use:
```bash
# Stop existing PostgreSQL service
sudo systemctl stop postgresql

# Or change port in docker-compose.yml
ports:
  - "5433:5432"  # Map to different port
```

## 📝 Default Credentials

### Teacher Account
- **Username**: `admin`
- **Password**: `admin123`

### Sample Student IDs
Students can be created on-the-fly during login. Example IDs:
- S001, S002, S003, etc.

## 🎯 Learning Outcomes

This project demonstrates:
1. **GUI Programming**: Complex Swing layouts and event handling
2. **Database Integration**: JDBC connection management and SQL operations
3. **Design Patterns**: Singleton, DAO, MVC patterns
4. **Object-Oriented Design**: Inheritance, encapsulation, polymorphism
5. **Error Handling**: Try-catch blocks and validation
6. **Docker**: Containerization and service orchestration
7. **User Experience**: Intuitive UI design with modern aesthetics

## 🚀 Future Enhancements

Potential improvements for the system:
- [ ] Add course prerequisite validation
- [ ] Implement waiting list functionality
- [ ] Add grade management system
- [ ] Generate course schedules (time slots)
- [ ] Export reports to PDF/Excel
- [ ] Add email notifications
- [ ] Implement course search and filters
- [ ] Add student performance analytics
- [ ] Multi-semester course planning

## 🤝 Contributing

This is a college lab project. If you want to extend or improve it:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project was created for educational purposes as part of a Java college lab assignment.

## 👨‍💻 Author

**David Porathur**
- College Lab Project - Java Programming
- Course: Advanced Java / AWT & Swing Programming

## 🙏 Acknowledgments

- Java Swing Documentation
- PostgreSQL Documentation
- Docker Community
- College Faculty and Lab Instructors

---

**Note**: This project demonstrates fundamental Java programming concepts and database integration. It was developed as part of academic coursework to showcase proficiency in Java GUI development and database management.

## 📞 Support

For questions or issues related to this project:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review the code comments for implementation details
3. Contact your lab instructor for academic guidance

---

*Last Updated: January 2026*
