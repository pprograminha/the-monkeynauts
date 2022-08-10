package com.themonkeynauts.game.configuration;

import com.themonkeynauts.game.adapter.out.persistence.tenant.TenantAwareDatasource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
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
@EnableJpaRepositories(
        basePackages = "com.themonkeynauts.game.adapter.out.persistence",
        entityManagerFactoryRef = "appEntityManagerFactory",
        transactionManagerRef = "appTransactionManager"
)
public class AppDatasourceConfiguration {

    private final DatasourceProperties datasourceProperties;

    @Primary
    @Bean(name = "appDataSource")
    @DependsOn("schemaDatasource")
    public DataSource appDataSource() {
        HikariDataSource datasource = createDataSource(datasourceProperties);
        datasource.setUsername(datasourceProperties.getAppUsername());
        datasource.setPassword(datasourceProperties.getAppPassword());
        datasource.setMaximumPoolSize(5);
        datasource.setMinimumIdle(5);
        return new TenantAwareDatasource(datasource);
    }

    @Primary
    @Bean(name = "appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(appDataSource());
        entityManagerFactoryBean.setPersistenceUnitName("app");
        entityManagerFactoryBean.setPackagesToScan("com.themonkeynauts.game.adapter.out.persistence");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);

        final Map<String, String> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);
        return entityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "appTransactionManager")
    public PlatformTransactionManager appTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(appEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private HikariDataSource createDataSource(DatasourceProperties datasourceProperties) {
        HikariDataSource datasource = new HikariDataSource();
        datasource.setJdbcUrl(datasourceProperties.getUrl());
        datasource.setDriverClassName(datasourceProperties.getDriverClass());
        datasource.addDataSourceProperty("reWriteBatchedInserts", "true");
        datasource.setSchema(datasourceProperties.getAppSchema());
        datasource.setUsername(datasourceProperties.getAppUsername());
        datasource.setPassword(datasourceProperties.getAppPassword());
        //If gssEncryption is not disabled you will not be able to connect to pgpoolII and
        //the error you get will be 100% useless, simply stating it cant connect
        datasource.addDataSourceProperty("gssEncMode", "disable");
        datasource.setAutoCommit(false);
        return datasource;
    }
}
