/*
 * Decompiled with CFR 0.152.
 */
package com.cim.carinventory;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    public static boolean isSQLInstalled() {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName("localhost");
            dataSource.setUser("root");
            dataSource.setDatabaseName("car_inventory");
            dataSource.setPortNumber(3306);
            dataSource.setPassword("");
            dataSource.getConnection().close();
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    public static Connection getConnection() throws SQLNotInstalledException {
        Connection connection;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setDatabaseName("car_inventory");
        dataSource.setPortNumber(3306);
        dataSource.setPassword("");
        try {
            connection = dataSource.getConnection();
            String sql = "SET GLOBAL max_allowed_packet = 1024*1024*128";
            try (PreparedStatement statement = connection.prepareStatement(sql);){
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            throw new SQLNotInstalledException("SQL is not installed.");
        }
        return connection;
    }

    public static class SQLNotInstalledException
    extends Exception {
        public SQLNotInstalledException(String message) {
            super(message);
        }
    }
}
