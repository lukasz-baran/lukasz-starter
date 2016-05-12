package com.lukaszbaran;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {


    public static void main(String[] args) {

        Connection con = null;
        try {
//            Connection conn = dataSource.getConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT ID FROM USERS");

            // Load the Driver class.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // If you are using any other database then load the right driver here.
            con = DriverManager.getConnection(Constants.DB_CONNECTION);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select name from przystanek");

            while (rs.next()) {
                //System.out.println("Name= " + rs.getString("Name") + " CountryCode= " + rs.getString("CountryCode"));
                System.out.println("Name= " + rs.getString("Name") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
