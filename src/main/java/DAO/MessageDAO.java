package DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

import static org.mockito.ArgumentMatchers.refEq;

import java.sql.*;


public class MessageDAO {
    //create new message
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            
            ResultSet pkrs = preparedStatement.getGeneratedKeys();
            if(pkrs.next())
            {
                int generated_message_id = (int) pkrs.getLong(1);
                return new Message(generated_message_id,
                                message.getPosted_by(),
                                message.getMessage_text(),
                                message.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return null;
    }

    //Checks if an account exists by account ID
    public boolean userExists(int accountID){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT 1 FROM account WHERE account.account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountID);

            ResultSet rs = preparedStatement.executeQuery();
            
            return rs.next();
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return false;
    }

    //retrieve all messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return messages;
    }
    //retrieve messages by ID
    public Message getMessageByID(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message.message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageID);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
                return new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    //delete message by ID
    public Message deleteMessageByID(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //check if string exists
            String sql = "SELECT * FROM message WHERE message.message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageID);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                String deleteSQL = "DELETE FROM message WHERE message.message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL);

                deleteStatement.setInt(1, messageID);

                deleteStatement.executeUpdate();

                return new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    //update message by ID
    public Message patchMessageByID(int messageID, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message.message_text = ? WHERE message.message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageID);

            preparedStatement.executeUpdate();

    
        }catch(SQLException e){
            System.out.print(e);
        }
        
        return getMessageByID(messageID);
    }

    //retrieve all messages from specified user
    public List<Message> getAllMessagesByUser(int accountID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE message.posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return messages;
    }
        
}   
