/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling database connections and queries. This class
 * provides methods for establishing a connection to the database and executing
 * common SQL queries such as INSERT, UPDATE, DELETE, and SELECT.
 *
 * @author Le Anh Khoa - CE190449
 */
public class DBContext {

    /**
     * Represents the database connection.
     */
    public Connection conn;

    private final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ss;encrypt=true;trustServerCertificate=true;user=sa;password=123456";
    private final String DB_USER = "sa";
    private final String DB_PWD = "123456";


    /**
     * Constructor that initializes the database connection. This constructor
     * loads the SQL Server JDBC driver and establishes a connection to the
     * database using the specified connection details.
     */
    public DBContext() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets the current database connection.
     *
     * @return the current database connection
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     *
     * @param query the SQL query to be executed
     * @param params the parameters to be set in the query
     * @return the number of rows affected by the query
     * @throws SQLException if an SQL error occurs
     */
    public int execQuery(String query, Object[] params) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(query);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pStatement.setObject(i + 1, params[i]);
            }
        }
        return pStatement.executeUpdate();
    }

    /**
     * Executes a SELECT query and returns the result set.
     *
     * @param query the SQL query to be executed
     * @param params the parameters to be set in the query
     * @return the result set of the query
     * @throws SQLException if an SQL error occurs
     */
    public ResultSet execSelectQuery(String query, Object[] params) throws SQLException {
        PreparedStatement pStatement = conn.prepareStatement(query);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pStatement.setObject(i + 1, params[i]);
            }
        }
        return pStatement.executeQuery();
    }

    /**
     * Executes a SELECT query without parameters and returns the result set.
     *
     * @param query the SQL query to be executed
     * @return the result set of the query
     * @throws SQLException if an SQL error occurs
     */
    public ResultSet execSelectQuery(String query) throws SQLException {
        return this.execSelectQuery(query, null);
    }
}
