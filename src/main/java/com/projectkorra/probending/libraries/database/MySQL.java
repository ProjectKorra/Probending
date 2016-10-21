package com.projectkorra.probending.libraries.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Connects to and uses a MySQL database
 * 
 * @author -_Husky_-
 * @author tips48
 */
public class MySQL extends Database {
    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    /**
     * Creates a new MySQL instance
     * 
     * @param plugin
     *            Plugin instance
     * @param hostname
     *            Name of the host
     * @param port
     *            Port number
     * @param database
     *            Database name
     * @param username
     *            Username
     * @param password
     *            Password
     */
    public MySQL(Logger log, String hostname, String port, String database, String username, String password) {
        super(log, "MySQL", database);
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        this.connection = open();
    }

    @Override
    public Connection open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
            this.printInfo("Database connection established.");
        } catch (SQLException e) {
            this.printInfo("Could not connect to MySQL server! because: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            this.printInfo("JDBC Driver not found!");
        }
        return connection;
    }
}