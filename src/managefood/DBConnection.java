package managefood;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public Connection mkDataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception or rethrow it as a runtime exception if needed
            e.printStackTrace(); // This prints the exception details to the console
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
