package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String urlJDBC = "jdbc:mysql://localhost:3306/taskjdbc?useSSL=false";
    private static final String userName = "root";
    private static final String userPassword = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlJDBC, userName, userPassword);
    }
}
