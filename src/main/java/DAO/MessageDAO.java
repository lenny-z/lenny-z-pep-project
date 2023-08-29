package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    public Message insertMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch)"
                + "VALUES (?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, message.getPosted_by());
        statement.setString(2, message.getMessage_text());
        statement.setLong(3, message.getTime_posted_epoch());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
            int messageID = (int) resultSet.getLong(1);

            return new Message(messageID, message.getPosted_by(), message.getMessage_text(),
                    message.getTime_posted_epoch());
        } else {
            throw new SQLException();
        }
    }

    public List<Message> selectAllMessages() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Message message = new Message(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getLong(4));

            messages.add(message);
        }

        return messages;
    }

    public Message selectMessageByID(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Message message = new Message(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getLong(4));

            return message;
        }

        return null;
    }

    public Message deleteMessageByID(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        Message message = selectMessageByID(id);

        if (message == null) {
            return null;
        } else {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            return message;
        }
    }

    public Message updateMessageByID(int id, String messageText) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, messageText);
        statement.setInt(2, id);
        statement.executeUpdate();
        return selectMessageByID(id);
    }

    public List<Message> selectMessagesByAccountID(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Message message = new Message(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getLong(4));

            messages.add(message);
        }

        return messages;
    }
}
