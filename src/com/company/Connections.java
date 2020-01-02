package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {
    private static Connection con=null;

    private Connections() throws SQLException{
        con = DriverManager.getConnection(
                "jdbc:oracle:thin:@//recre.cmz0hahq3ngb.us-west-2.rds.amazonaws.com:1522/ORCL", "ora_p3l0b", "a58055154");
    }

    public static Connection getConnection() throws SQLException{
        if(con == null){
            new Connections();
        }
        return con;

    }
}
