/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class Login {
    static Connection conn = null;
    private String username;
    private String password;
    
    private static Connection getJDBCConnection() {
        String url = "jdbc:mysql://127.0.0.1:3306/pc_builder";
        String user = "root";
        String password = "987choithoi";
        try {
            conn = DriverManager.getConnection(url, user, password);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
        
        return null;
    }
    
    public void Login(String username, String password) {
        
    }
    
    public void getAdminAccountList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from admin");
        HashMap<String,String> adminAccount = new HashMap<>();
        while (rs.next()) {
            adminAccount.put(rs.getString("username"), rs.getString("password"));
        }
        for (String key: adminAccount.keySet()) {
            System.out.printf("User: %s Password: %s\n",key,adminAccount.get(key));
        }
    }
    
    public static void main(String[] args) {
        if (conn == null) {
            conn = getJDBCConnection();
            if (conn != null) {
                System.out.println("Thanh Cong!");
            }
            else System.out.println("That Bai!");
        }
    }
}
