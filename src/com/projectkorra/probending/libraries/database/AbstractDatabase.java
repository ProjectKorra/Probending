package com.projectkorra.probending.libraries.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDatabase {

    protected final Logger _log;
    protected final String _dbType;

    public AbstractDatabase(Logger log, String dbType) {
        _log = log;
        _dbType = dbType;
    }

    /**
     * Print information to console
     *
     * @param message The string to print to console
     */
    protected void printInfo(String message) {
        _log.log(Level.INFO, "[{0}" + "]" + " {1}", new Object[]{_dbType, message});
    }

    /**
     * Print error to console
     *
     * @param message The string to print to console
     * @param severe If true print an error, else print a warning
     */
    protected void printErr(String message, boolean severe) {
        if (severe) {
            _log.log(Level.SEVERE, "{0}" + " {1}", new Object[]{_dbType, message});
        } else {
            _log.log(Level.WARNING, "{0}" + " {1}", new Object[]{_dbType, message});
        }
    }

    /**
     * Returns a Connection to this database
     */
    public abstract Connection getConnection();

    /**
     * Closes this database
     */
    public abstract void close();

    /**
     * Executes a query
     *
     * @param query - The SQL Query to execute
     * @param callback - The callback to receive the results of this query
     * @param values - The data to be used in this query
     */
    public abstract void executeQuery(String query, Callback<ResultSet> callback, Object... values);
    
    /**
     * Executes an update (CREATE, INSERT, UPDATE, DELETE)
     *
     * @param query - The SQL Update to execute
     * @param values - The data to be used in this update
     */
    public abstract void executeUpdate(String query, Object... values);

    /**
     * Check database to see if a table exists
     *
     * @param table Table name to check
     * @return True if table exists, else false
     */
    public abstract boolean tableExists(String table);
}
