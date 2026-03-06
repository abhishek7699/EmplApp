package com.EmplApp.EmplApp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.EmplApp.EmplApp.repo.query",
        entityManagerFactoryRef = "queryEntityManagerFactory",
        transactionManagerRef = "queryTransactionManager"
)
public class QueryDatasourceConfig {

    @Bean(name = "queryDataSource")
    public DataSource queryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5435/empl_db_query");
        dataSource.setUsername("admin_2");
        dataSource.setPassword("admin123");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Bean(name = "queryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean queryEntityManagerFactory(
            @Qualifier("queryDataSource") DataSource queryDataSource) {

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(queryDataSource);
        factory.setPackagesToScan("com.EmplApp.EmplApp.model");
        factory.setPersistenceUnitName("query");

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

    @Bean(name = "queryTransactionManager")
    public PlatformTransactionManager queryTransactionManager(
            @Qualifier("queryEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean queryEntityManagerFactory) {
        return new JpaTransactionManager(queryEntityManagerFactory.getObject());
    }
}