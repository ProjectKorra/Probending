package com.projectkorra.probending.libraries.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySQLDatabase extends AbstractDatabase {

    private HikariDataSource _source;

    public MySQLDatabase(Logger log, String ip, String port, String database, String username, String password) {
        super(log, "MySQL");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + database);
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTimeout(10000);

        _source = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() {
        try {
            return _source.getConnection();
        } catch (SQLException exception) {
            printErr("An Exception occurred while attempting to create a MySQL Connection!", true);
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        _source.close();
    }

    @Override
    public ResultSet executeQuery(String query, Object... values) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }

            try (ResultSet result = statement.executeQuery()) {
                return result;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void executeQuery(String query, Callback<ResultSet> callback, Object... values) {
        ResultSet rs = executeQuery(query, values);
        if (callback != null) {
            callback.run(rs);
        }
    }
}
