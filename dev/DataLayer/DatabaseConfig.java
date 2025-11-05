package DataLayer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan(basePackages = {"DataLayer"})
public class DatabaseConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(DatabaseConnector.getDataSource());
    }
}
