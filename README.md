# Supermarket Management System

## Overview
This project simulates a complete supermarket management system developed as part of an **academic group project** at **Ben-Gurion University of the Negev**.  
The system was built collaboratively by **Hadar Shir** and other team members.  
It supports the management of employees, shifts, suppliers, orders, deliveries, and inventory tracking.

The project is implemented in **Java**, using a **layered architecture** that separates the data, domain, and presentation layers to ensure scalability, maintainability, and clean design.

---

## 🧩 System Architecture

### **1. Domain Layer**
Contains the main entities of the system such as `Employee`, `Supplier`, `Delivery`, `Product`, and `Shift`.  
Each class represents a real-world object with its attributes and business logic.

### **2. Data Layer**
Handles all interactions with the **database** (SQL).  
Includes CRUD operations (Create, Read, Update, Delete) and manages data persistence.  
Responsible for maintaining data integrity and mapping between Java objects and database tables.

### **3. DTO (Data Transfer Objects)**
Includes intermediary classes used for passing data between layers efficiently and securely, without exposing internal logic.

### **4. Service Layer**
Implements the **business logic** of the system.  
Connects the data layer with the presentation layer, ensuring that business rules are applied correctly.

### **5. Presentation Layer**
The **user interface** and interaction layer of the program.  
Handles user input/output, commands, and communication with the service layer.

### **6. META-INF**
Contains configuration files required for project initialization and metadata.

---

## ⚙️ Technologies Used
- **Language:** Java  
- **Database:** SQL (relational structure with normalization)  
- **Architecture:** Layered (Domain, Data, Service, Presentation)  
- **Tools:** IntelliJ IDEA, GitHub  
- **Version Control:** Git  

---

## 🚀 Features
- Employee management (add, edit, and assign shifts)  
- Supplier and order management  
- Product inventory and stock tracking  
- Delivery scheduling and tracking  
- Database-driven backend with reusable code design  

---

## 👩‍💻 Authors
Developed as part of a university project by **Hadar Shir** and collaborators.  
**B.Sc. Information Systems and Software Engineering**  
**Ben-Gurion University of the Negev**

---

## 📚 Notes
This project demonstrates Object-Oriented Design, data persistence, teamwork, and system analysis principles, implemented through a multi-layered software architecture.
