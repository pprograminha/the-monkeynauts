package com.themonkeynauts.game;

import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.AccountRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.EquipmentRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.MonkeynautRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.ShipRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import io.grpc.ManagedChannel;
import net.devh.boot.grpc.server.config.GrpcServerProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationContainerTest {

    public static final int RANDOM_GRPC_PORT = 0;

    private static final String DATABASE_NAME = "themonkeynauts";
    private static final String DATABASE_USERNAME = "test-root-user";
    private static final String DATABASE_PASSWORD = "test-root-pass";

    protected ManagedChannel channel;

    @Autowired
    protected GrpcServerProperties properties;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MonkeynautRepository monkeynautRepository;

    @Autowired
    protected EquipmentRepository equipmentRepository;

    @Autowired
    protected ShipRepository shipRepository;

    static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer(DockerImageName.parse("postgres:13.5"))
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USERNAME)
            .withPassword(DATABASE_PASSWORD);

    static {
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("datasource.app.username", () -> "test-application-user");
        registry.add("datasource.app.password", () -> "test-application-pass");
        registry.add("datasource.schema.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("datasource.schema.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @AfterAll
    void teardown() {
        try {
            if (!Objects.isNull(channel)) {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
