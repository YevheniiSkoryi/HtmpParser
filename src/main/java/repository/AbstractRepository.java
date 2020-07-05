package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/test_post";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    protected Connection openConnection() throws SQLException {
        final Properties properties = new Properties();
        properties.put("user", USERNAME);
        properties.put("password", PASSWORD);
        final Connection connection = DriverManager.getConnection(URL, properties);
        connection.setAutoCommit(false);
        return connection;
    }

}
