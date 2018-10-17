package com.epam.training.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.extern.log4j.Log4j;

@Log4j
public class DBConnector {
    public static Connection getConnection() throws SQLException {
        String url = null;
        String user = null;
        String pass = null;
        try {
            ResourceBundle resource = ResourceBundle.getBundle("db");
            url = resource.getString("db.url");
            user = resource.getString("db.user");
            pass = resource.getString("db.password");
        } catch (MissingResourceException e) {
            log.fatal("Invalid properties for database", e);
        }
        return DriverManager.getConnection(url, user, pass);
    }
}