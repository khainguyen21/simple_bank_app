package db_objs;

import java.math.BigDecimal;
import java.sql.*;

/*
    This class will be the only class that directly interacts with our MySQL Database to perform activities such as
    retrieving and updating our database
 */
public class MyJDBC {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Khai211004@";

    // if valid return an object with the user's information
    public static User validateLogin(String username, String password) {
        try {

            // Establish a connection to the database using configurations
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // create SQL query
            PreparedStatement preparedStatement = con.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            // replace the ? with values
            // parameter index referring to the iteration of the ? so 1 is the first ? and 2 is second ?
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // execute query and store into a result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // .next() returns true or false
            // true - query returned data and result set now points to the first row
            // false - query returned no data and result set equals to null
            if (resultSet.next()) {
                // success
                // get id
                int userID = resultSet.getInt("id");

                // get current balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // return user object
                return new User(userID, username, password, currentBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // not valid user
        return null;
    }

    // registers new user to the database
    // true - register success
    // false - register fail
    public static boolean register(String username, String password) {
        try {
            // first we will need to check if the username has already been taken
            if (!checkUser(username)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users (username, password, current_balance)" +
                                "VALUES(?, ?, ?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, 0);
                preparedStatement.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // check if username already exists in the database
    // true - user exists
    // false - user doesn't exist
    private static boolean checkUser(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public static boolean addTransactionToDataBase(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT transactions(user_id, transaction_amount, transaction_type, transaction_date)" +
                            "VALUE(?, ?, ?, NOW())"
            );

            // NOW() will put in the current date
            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setBigDecimal(2, transaction.getTransactionAmount());
            insertTransaction.setString(3, transaction.transactionType());

            // execute
            insertTransaction.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
