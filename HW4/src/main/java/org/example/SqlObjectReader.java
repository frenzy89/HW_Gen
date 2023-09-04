package org.example;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.nio.DoubleBuffer;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SqlObjectReader {
    public static void main(String[] args) {
        createUser();
        createAccount(4);
        topUpAccount(2);
        withdrawAccount(2);
    }

    public static void createUser() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Class found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Not found:" + ex.getMessage());
            return;
        }
        String sql = "INSERT INTO Users (name, address) VALUES ((?),(?))";
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql)) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type user's name");
            String userName = scanner.next();
            System.out.println("Type user's address");
            String userAddress = scanner.next();
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void createAccount(int userId) {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Class found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Not found:" + ex.getMessage());
            return;
        }
        String sql = "INSERT INTO ACCOUNTS (userId, balance, currency) VALUES ((?),(?),(?))";
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql)) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type balance");
            BigDecimal balance = scanner.nextBigDecimal().setScale(3);
            System.out.println("Type currency");
            String currency = scanner.next();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, balance);
            preparedStatement.setString(3, currency);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void topUpAccount(int accountId) {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Class found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Not found:" + ex.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type amount");
        BigDecimal amount = scanner.nextBigDecimal().setScale(3);
        String sql1 = "SELECT DISTINCT BALANCE FROM ACCOUNTS WHERE ACCOUNTID = (?)";
        BigDecimal accountBalance;
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql1)) {
            preparedStatement.setDouble(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            accountBalance = resultSet.getBigDecimal("balance");
            preparedStatement.close();
            String sql2 = "UPDATE accounts SET balance =(?) WHERE ACCOUNTID = (?)";
            try (PreparedStatement innerPreparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql2);) {
                double accountBalanceDouble = accountBalance.doubleValue();
                double amountDouble = amount.doubleValue();
                double updatedBalanceDouble = accountBalanceDouble + amountDouble;
                BigDecimal updatedBalance = new BigDecimal(updatedBalanceDouble).setScale(3, BigDecimal.ROUND_CEILING);
                innerPreparedStatement.setBigDecimal(1, updatedBalance);
                innerPreparedStatement.setInt(2, accountId);
                innerPreparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String sql = "INSERT INTO TRANSACTIONS (accountId, amount) VALUES ((?),(?))";
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void withdrawAccount(int accountId) {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Class found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Not found:" + ex.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type amount");
        BigDecimal amount = scanner.nextBigDecimal().setScale(3);
        String sql1 = "SELECT DISTINCT BALANCE FROM ACCOUNTS WHERE ACCOUNTID = (?)";
        BigDecimal accountBalance;
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql1)) {
            preparedStatement.setDouble(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            accountBalance = resultSet.getBigDecimal("balance");
            preparedStatement.close();
            String sql2 = "UPDATE accounts SET balance =(?) WHERE ACCOUNTID = (?)";
            try (PreparedStatement innerPreparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql2);) {
                double accountBalanceDouble = accountBalance.doubleValue();
                double amountDouble = amount.doubleValue();
                double updatedBalanceDouble = accountBalanceDouble - amountDouble;
                BigDecimal updatedBalance = new BigDecimal(updatedBalanceDouble).setScale(3, BigDecimal.ROUND_CEILING);
                innerPreparedStatement.setBigDecimal(1, updatedBalance);
                innerPreparedStatement.setInt(2, accountId);
                innerPreparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String sql = "INSERT INTO TRANSACTIONS (accountId, amount) VALUES ((?),(?))";
        try (PreparedStatement preparedStatement = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\max89\\BankOperations.db").prepareStatement(sql)) {
            double sumOfWithdrawalDouble = -amount.doubleValue();
            BigDecimal sumOfWithdrawal = new BigDecimal(sumOfWithdrawalDouble);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setBigDecimal(2, sumOfWithdrawal);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}