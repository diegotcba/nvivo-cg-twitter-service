package com.nvivo.twitter.svc.dao.impl;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by DiegoT on 14/11/2015.
 */
public class DBConnection {

    private ResultSet rows;
    private Connection conn;
    private Statement stmt;
    private DataSource dataSource;


    public void openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                //Class.forName("com.mssql.jdbc.Driver");
                //Class.forName( "net.sourceforge.jtds.jdbc.Driver" );

                //conn = DriverManager.getConnection("jdbc:mssql://localhost/high-school", "diegotcba", "diegotcba");
                //conn = DriverManager.getConnection("jdbc:jtds:sqlserver://XPVIRTUAL/SisAveit","Java","JavaAveit");
                conn = dataSource.getConnection();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void executeCommand(String command) {
        try {
            if (!conn.isClosed() || conn != null) {
                stmt = conn.createStatement();
                stmt.executeUpdate(command);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getQuery(String query) {
        try {
            if (!conn.isClosed() || conn != null) {
                stmt = conn.createStatement();
                rows = stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return rows;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        DatabaseMetaData meta = null;
        try {
            if (!conn.isClosed() || conn != null) {
                meta = conn.getMetaData();
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            throw new SQLException(e.getCause());
        }
        return meta;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
