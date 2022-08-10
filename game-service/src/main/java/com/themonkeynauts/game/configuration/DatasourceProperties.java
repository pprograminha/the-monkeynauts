package com.themonkeynauts.game.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource")
@Getter
@Setter
public class DatasourceProperties {

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.driver.class:org.postgresql.Driver}")
    private String driverClass;

    @Value("${datasource.app.schema:themonkeynauts}")
    private String appSchema;

    @Value("${datasource.app.username:application}")
    private String appUsername;

    @Value("${datasource.app.password:welcome}")
    private String appPassword;

    @Value("${datasource.schema.username}")
    private String schemaUsername;

    @Value("${datasource.schema.password}")
    private String schemaPassword;
}
