package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account insertAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account(username, password) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getPassword());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
            int accountID = (int) resultSet.getLong(1);
            return new Account(accountID, account.getUsername(), account.getPassword());
        } else {
            throw new SQLException();
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT EXISTS(SELECT 1 FROM account WHERE username = ?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getBoolean(1);
        } else {
            throw new SQLException();
        }
    }

    public boolean accountIDExists(int accountID) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT EXISTS(SELECT 1 FROM account WHERE account_id = ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public Account selectAccountByUsernameAndPassword(String username, String password) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        // try {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"));
        }
        // } catch (SQLException e) {
        // System.out.println(e.getMessage());
        // }

        return null;
    }
}