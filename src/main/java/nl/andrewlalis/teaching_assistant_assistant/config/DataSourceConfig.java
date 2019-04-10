package nl.andrewlalis.teaching_assistant_assistant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Configures the data source for this application.
 */
@Configuration
public class DataSourceConfig {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DB_HOST = "localhost";
    private static final String DB_NAME = "teaching_assistant_assistant";

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .url("jdbc:h2:~/" + DB_NAME)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

}
