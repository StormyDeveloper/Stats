package de.stormydeveloper.playerstats.sql;

import java.sql.*;

public class MySQL {
    private static MySQL mysql = new MySQL();
    private Connection connection;

    public static MySQL getMysql() {
        return mysql;
    }

    private boolean isConnected() {return connection != null;}

    public void connect(final String host , final int port , final String database , final String user , final String password) throws SQLException {
        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database + "?autoReconnect=true" , user , password);

            if(isConnected()) {
                update("CREATE TABLE IF NOT EXISTS Stats (UUID TEXT, Kills INT , Deaths INT , FirstJoin DATETIME , LastOnline DATETIME, OnlineTime LONG)");
            }
        }

    }

    public void close() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(final String sql) {
        if(isConnected()) {
            try {
                connection.prepareStatement(sql).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(final String sql) {
        if(isConnected()) {
            try {
                connection.prepareStatement(sql).execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getResult(final String sql) {
        ResultSet rs = null;

        if(isConnected()) {
            try {
                final PreparedStatement ps = connection.prepareStatement(sql);
                ps.executeQuery();

                rs = ps.getResultSet();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }
}
