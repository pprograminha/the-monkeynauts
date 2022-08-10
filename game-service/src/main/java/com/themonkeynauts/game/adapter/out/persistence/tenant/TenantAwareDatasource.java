package com.themonkeynauts.game.adapter.out.persistence.tenant;

import com.themonkeynauts.game.adapter.in.web.rpc.tenant.TenantContext;
import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TenantAwareDatasource extends DelegatingDataSource {

    public TenantAwareDatasource(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        final Connection connection = getTargetDataSource().getConnection();
        setTenantId(connection);
        return getTenantAwareConnectionProxy(connection);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        final Connection connection = getTargetDataSource().getConnection(username, password);
        setTenantId(connection);
        return getTenantAwareConnectionProxy(connection);
    }

    protected Connection getTenantAwareConnectionProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class[] {ConnectionProxy.class},
                new TenantAwareDatasource.TenantAwareInvocationHandler(connection));
    }

    private void setTenantId(Connection connection) throws SQLException {
        try (Statement sql = connection.createStatement()) {
            var account = TenantContext.currentTenant();
            if (account != null) {
                sql.execute("SET app.current_tenant TO '" + account.getId() + "'");
            }
        }
    }

    private void clearTenantId(Connection connection) throws SQLException {
        try (Statement sql = connection.createStatement()) {
            sql.execute("RESET app.current_tenant");
        }
    }

    protected class TenantAwareInvocationHandler implements InvocationHandler {
        private final Connection target;

        public TenantAwareInvocationHandler(Connection target) {
            this.target = target;
        }

        @Nullable
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            switch (method.getName()) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return "Tenant-aware proxy for target Connection [" + this.target.toString() + "]";
                case "unwrap":
                    if (((Class) args[0]).isInstance(proxy)) {
                        return proxy;
                    } else {
                        return method.invoke(target, args);
                    }
                case "isWrapperFor":
                    if (((Class) args[0]).isInstance(proxy)) {
                        return true;
                    } else {
                        return method.invoke(target, args);
                    }
                case "getTargetConnection":
                    return target;
                default:
                    if (method.getName().equals("close")) {
                        clearTenantId(target);
                    }
                    return method.invoke(target, args);
            }
        }
    }
}
