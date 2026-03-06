package com.EmplApp.EmplApp.config;

import org.springframework.beans.factory.annotation.Qualifier;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.EmplApp.EmplApp.repo.command",
        entityManagerFactoryRef = "commandEntityManagerFactory",
        transactionManagerRef = "commandTransactionManager"
)
public class CommandDatasourceConfig {



    @Primary
    @Bean(name = "commandDataSource")
    public DataSource commandDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5434/empl_db_command");
        dataSource.setUsername("admin_1");
        dataSource.setPassword("admin123");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }



    @Primary
    @Bean(name = "commandEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commandEntityManagerFactory(
            @Qualifier("commandDataSource") DataSource commandDataSource) {

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(commandDataSource);
        factory.setPackagesToScan("com.EmplApp.EmplApp.model");
        factory.setPersistenceUnitName("command");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        factory.setJpaVendorAdapter(adapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        factory.setJpaPropertyMap(properties);

        return factory;
    }

    @Primary
    @Bean(name = "commandTransactionManager")
    public PlatformTransactionManager commandTransactionManager(
            @Qualifier("commandEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean commandEntityManagerFactory) {
        return new JpaTransactionManager(commandEntityManagerFactory.getObject());
    }
}