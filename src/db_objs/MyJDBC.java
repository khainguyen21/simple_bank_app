package db_objs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/*
    This class will be the only class that directly interacts with our MySQL Database to perform activities such as
    retrieving and updating our database
 */
public class MyJDBC {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "testing123@!";

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
                preparedStatement.setBigDecimal(3, new BigDecimal(0));
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
                    "INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date)" +
                            "VALUE(?, ?, ?, NOW())"
            );

            // NOW() will put in the current date
            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());

            // execute
            insertTransaction.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // true - update balance successful
    // false - update balance fail
    public static boolean updateCurrentBalance(User user)
    {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?");


            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());

            updateBalance.executeUpdate();

            return true;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean transfer(User user, String transferredUsername, float transferAmount)
    {
        try
        {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");

            queryUser.setString(1, transferredUsername);
            ResultSet result = queryUser.executeQuery();

            while (result.next())
            {
                // perform transfer
                User transferredUser = new User(
                        result.getInt("id"),
                        transferredUsername,
                        result.getString("password"),
                        result.getBigDecimal("current_balance")
                );

                // create transaction
                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer Out",
                        new BigDecimal(-transferAmount),
                        null
                );

                // this transaction will belong to the received user
                Transaction receiveTransaction = new Transaction(
                        result.getInt("id"),
                        "Transfer In",
                        new BigDecimal(transferAmount),
                        null
                );

                // update received user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(new BigDecimal(transferAmount)));
                updateCurrentBalance(transferredUser);

                // update sent user
                user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(transferAmount)));
                updateCurrentBalance(user);

                // add these transactions to database
                addTransactionToDataBase(transferTransaction);
                addTransactionToDataBase(receiveTransaction);

                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    // get all transactions (used for past transaction)
    public static ArrayList<Transaction> getPastTransaction(User user)
    {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();

        try{

            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );

            selectAllTransaction.setInt(1, user.getId());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            // iterate through the results (if any)
            while (resultSet.next())
            {
                Transaction transaction = new Transaction(
                        resultSet.getInt("user_id"),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                // store into array list
                pastTransactions.add(transaction);
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return pastTransactions;
    }
}
