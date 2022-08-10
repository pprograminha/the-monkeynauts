package com.themonkeynauts.game.adapter.out.persistence.tenant;

import com.themonkeynauts.game.adapter.in.web.rpc.tenant.TenantContext;
import com.themonkeynauts.game.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TenantAwareDatasourceTest {

    private TenantAwareDatasource tenantAwareDatasource;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    private UUID accountId;

    @BeforeEach
    public void setUp() {
        tenantAwareDatasource = new TenantAwareDatasource(dataSource);
        accountId = UUID.randomUUID();
        var account = new Account(accountId);
        TenantContext.setCurrentTenant(account);
    }

    @Test
    public void shouldSetTenantIdForConnectionWithoutUsernameAndPassword() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        tenantAwareDatasource.getConnection();

        verify(statement).execute("SET app.current_tenant TO '" + accountId + "'");
    }

    @Test
    public void shouldSetTenantIdForConnectionWithUsernameAndPassword() throws SQLException {
        when(dataSource.getConnection("username", "password")).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        tenantAwareDatasource.getConnection("username", "password");

        verify(statement).execute("SET app.current_tenant TO '" + accountId + "'");
    }

    @Test
    public void shouldClearTenantIdWhenClosingConnection() throws Throwable {
        when(connection.createStatement()).thenReturn(statement);

        InvocationHandler handler = tenantAwareDatasource.new TenantAwareInvocationHandler(connection);
        Method method = Connection.class.getDeclaredMethod("close");
        handler.invoke(connection, method, new Object[]{});

        verify(statement).execute("RESET app.current_tenant");
    }

}