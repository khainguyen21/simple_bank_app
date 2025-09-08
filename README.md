# Simple Bank App

Welcome to the **Simple Bank App**!  
This project is built for beginners to learn how a simple banking system works, both in terms of code and user interface.  
If you're new to Java or desktop application development, this guide will help you get started.

---

## 📌 What is this project?

**Simple Bank App** is a desktop application written in Java that simulates basic banking operations:
- **Deposit money**
- **Withdraw money**
- **Transfer money to other users**
- **View past transactions**

The application uses a graphical user interface (GUI) built with Java Swing, and a MySQL database for storing user and transaction information.

---

## 🛠️ Technology Stack

- **Java** (main programming language)
- **Swing** (for GUI)
- **MySQL** (for database)
- **JDBC** (to connect Java to MySQL)

---

## 🚀 How to Set Up

### 1. Prerequisites

- **Java JDK** (version 8 or higher). [Download here](https://www.oracle.com/java/technologies/downloads/)
- **MySQL Server** installed locally. [Download here](https://dev.mysql.com/downloads/mysql/)
- **A Java IDE** (e.g., IntelliJ IDEA, Eclipse, NetBeans) or use `javac`/`java` from command line.

### 2. Clone the Repository

```bash
git clone https://github.com/khainguyen21/simple_bank_app.git
cd simple_bank_app
```

### 3. Set Up the Database

1. **Start your MySQL server.**
2. **Create the `bankapp` database:**

   ```sql
   CREATE DATABASE bankapp;
   ```

3. **Create necessary tables:**  
   The code expects tables for users and transactions. (See `src/db_objs/MyJDBC.java` for table structure or ask for the SQL file if not present.)

4. **Configure your MySQL credentials:**  
   - Default values in the code (see `MyJDBC.java`):
     - **DB Username:** `root`
     - **DB Password:** `testing123@!`
     - **DB URL:** `jdbc:mysql://127.0.0.1:3306/bankapp`
   - If you use different credentials, update them in `src/db_objs/MyJDBC.java`.

### 4. Build and Run

- Compile the Java code using your IDE or with command line:

  ```bash
  javac -d bin src/**/*.java
  java -cp bin GUIS.MainGui
  ```

  *(Adjust the main class path as needed based on your project structure.)*

---

## 🧑‍💻 How to Use the App

1. **Login or Register**
   - On startup, you'll be prompted to login or register a new account.
   - Enter your username and password (new accounts require registration).

2. **Home Screen**
   - After login, you'll see your current balance and options:
     - Deposit
     - Withdraw
     - Transfer
     - View Past Transactions

3. **Making Transactions**
   - Click the desired operation.
   - Enter the amount (and username for transfer).
   - Confirm your action and see your balance update instantly.

4. **Logout**
   - Use the Logout button to safely exit the app.

---

## 🗄️ Project Structure

```
src/
├── GUIS/                 # GUI classes (BankingAppGui, BankingAppDialog, etc.)
├── db_objs/              # Database objects and MySQL connector (MyJDBC.java)
└── MainGui.java          # Main entry point (if present)
```

---

## ❓ FAQ

**Q: I get a 'Cannot connect to database' error!**
- Make sure MySQL is running, and your credentials in `MyJDBC.java` are correct.

**Q: I see blank screens or errors in the GUI!**
- Make sure you run the application using the main class (e.g., `MainGui.java`).  
- Ensure all dependencies are compiled.

**Q: How do I add users or reset passwords?**
- Use the registration screen to add users. Password reset is not implemented by default.

---

## 🏆 Credits

- Author: [khainguyen21](https://github.com/khainguyen21) , [Phil]

---

Happy coding! 🚀
