package com.diego;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool
{
    private BasicDataSource bds;
    private static String URL = "jdbc:postgresql://localhost:5432/diego";
    private static ConnectionPool connectionPool;
    private ConnectionPool() //Constructor de la clase
    {
        bds = new BasicDataSource();
        bds.setDriverClassName("org.postgresql.Driver");
        bds.setUrl(URL);
        bds.setUsername("root");
        bds.setPassword("root");
        bds.setMinIdle(5);
        bds.setMinIdle(20);
        bds.setMaxWait(10000);
    }

    public static ConnectionPool getInstance() //Devuelve el objeto ConnectionPool
    {
        if(connectionPool==null)
            connectionPool = new ConnectionPool();
        return connectionPool;
    }

    public Connection getConnection()
    {
        Connection conn = null;
        try
        {
            conn = bds.getConnection();
        }
        catch(SQLException e){e.printStackTrace();}
        return conn;
    }

    public void closeConnection(Connection conn) throws SQLException {conn.close();}
}
