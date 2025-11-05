/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ADMIN
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QL_PhanCongCongViec;encrypt=true;"
            + "trustServerCertificate=true";
    private static final String USER = "sa"; 
    private static final String PASSWORD = "sa";
    public static Connection getConnection()
    {
        Connection conn=null;
        try {
            //nap driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // 2. Kết nối CSDL
             conn = DriverManager.getConnection(URL, USER, PASSWORD);        
        } catch (Exception ex) {
            System.out.println("Loi: " + ex.toString());
        }
        return conn;
    }
    
    public static void main(String[] args) {
        System.out.println(DBConnection.getConnection());
    }
}
