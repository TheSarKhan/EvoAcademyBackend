package az.evoacademy.backend.config;


import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "az.evoacademy.backend.repository.content",
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager"
)

public class ContentDbConfig {

    @Value("${spring.datasource.second.url}")
    private String secondDbUrl;

    @Value("${spring.datasource.second.username}")
    private String secondDbUsername;

    @Value("${spring.datasource.second.password}")
    private String secondDbPassword;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String secondDbDdlAuto;


    @Bean(name = "secondDataSource")
    public DataSource secondDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(secondDbUrl);
        builder.username(secondDbUsername);
        builder.password(secondDbPassword);
        return builder.build();
    }


    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(secondDataSource())
                .packages("az.evoacademy.backend.model.content")
                .persistenceUnit("second")
                .properties(hibernateProperties())
                .build();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory secondEntityManagerFactory) {
        return new JpaTransactionManager(secondEntityManagerFactory);
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", secondDbDdlAuto);
        return properties;
    }
}