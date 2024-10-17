package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    //create new account
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int generated_acount_id = (int) pkResultSet.getLong(1);
                return new Account(generated_acount_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return null;
    }
    //verify login
    public Account getAccountByUsernameAndPassword(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account.username = ? AND account.password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
}
