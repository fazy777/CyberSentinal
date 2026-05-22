package DAO;

import Model.User;
import util.DBConnection;

import java.sql.*;

public class UserDAO {
    public static User login(String username, String password) throws ClassNotFoundException, SQLException {
        User user = new User(username, password);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt("user_id"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }
        System.out.println("Login Failed! Please check your username and password.");
        return null;
    }
}
