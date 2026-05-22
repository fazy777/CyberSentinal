package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/cyber_incident_db");
                username = props.getProperty("db.username", "root");
                password = props.getProperty("db.password", "fazy@123");
            } else {
                url = "jdbc:mysql://localhost:3306/cyber_incident_db";
                username = "root";
                password = "fazy@123";
            }
        } catch (Exception e) {
            url = "jdbc:mysql://localhost:3306/cyber_incident_db";
            username = "root";
            password = "fazy@123";
        }
    }

    public static Connection Connection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }
}
