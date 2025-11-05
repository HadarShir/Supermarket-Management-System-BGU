package DataLayer;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static String url;
    private static String user;
    private static String password;
    private static DriverManagerDataSource dataSource;


    private DatabaseConnector() {}

    public static void initialize(String configFile) {
        try (InputStream input = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(input);

            url = props.getProperty("DB_URL");
            user = props.getProperty("DB_USER");
            password = props.getProperty("DB_PASSWORD");
            dataSource = new DriverManagerDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);


            Class.forName("org.postgresql.Driver");
            System.out.println("Database config loaded: " + url);

        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Database not initialized! Please call initialize() first.");
        }
        return DriverManager.getConnection(url, user, password);
    }
    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not initialized. Call initialize() first.");
        }
        return dataSource;
    }


}
