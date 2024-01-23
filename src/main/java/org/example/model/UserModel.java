package org.example.model;


import org.example.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserModel {

    public boolean register(String userName, String pass) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO user (userName,password) VALUES(?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, pass);
        return pstm.executeUpdate() > 0;
    }
    public boolean exists (String userName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE userName = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, userName);
        return pstm.executeQuery().next();
    }
    public boolean login(String userName, String pass) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE userName = ? AND password = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, pass);
        return pstm.executeQuery().next();
    }
}
