package com.themonkeynauts.game.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SchemaDatasourceConfiguration {

    private final DatasourceProperties datasourceProperties;

    @Value("${flyway.locations}")
    private String flywayLocations;

    @Bean(name = "schemaDatasource")
    @FlywayDataSource
    public DataSource schemaDatasource() {
        HikariDataSource dataSource = createDataSource(datasourceProperties);
        dataSource.setUsername(datasourceProperties.getSchemaUsername());
        dataSource.setPassword(datasourceProperties.getSchemaPassword());
        dataSource.setMaximumPoolSize(1);
        applyFlywayMigration(dataSource);
        return dataSource;
    }

    @Bean(name = "schemaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean schemaEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(schemaDatasource());
        entityManagerFactoryBean.setPackagesToScan("com.themonkeynauts.game.adapter.out.persistence");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);

        final Map<String, String> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);
        return entityManagerFactoryBean;
    }

    @Bean(name = "schemaTransactionManager")
    public PlatformTransactionManager schemaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(schemaEntityManagerFactory().getObject());
        return transactionManager;
    }

    public void applyFlywayMigration(DataSource dataSource) {
        HashMap<String, String> flywayPlaceholders = new HashMap<>();
        flywayPlaceholders.put("user_name", datasourceProperties.getAppUsername());
        flywayPlaceholders.put("user_password", datasourceProperties.getAppPassword());
        flywayPlaceholders.put("superuser_name", datasourceProperties.getSchemaUsername());

        Flyway.configure()
                .dataSource(dataSource)
                .schemas(datasourceProperties.getAppSchema())
                .cleanDisabled(false)
                .placeholders(flywayPlaceholders)
                .locations(flywayLocations)
                .load()
                .migrate();
    }

    private HikariDataSource createDataSource(DatasourceProperties datasourceProperties) {
        HikariDataSource datasource = new HikariDataSource();
        datasource.setJdbcUrl(datasourceProperties.getUrl());
        datasource.setDriverClassName(datasourceProperties.getDriverClass());
        datasource.addDataSourceProperty("reWriteBatchedInserts", "true");
        datasource.setSchema(datasourceProperties.getAppSchema());
        //If gssEncryption is not disabled you will not be able to connect to pgpoolII and
        //the error you get will be 100% useless, simply stating it cant connect
        datasource.addDataSourceProperty("gssEncMode", "disable");
        datasource.setAutoCommit(false);
        return datasource;
    }
}
