/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package pc_builder;
import UI.Login;
import java.sql.SQLException;
import java.util.*;
import org.json.JSONObject;
/**
 *
 * @author Admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        UserStorage.initiate();
        DeviceStorage.initiate();
        Login GUI = new Login();
        GUI.setLocationRelativeTo(null);
        GUI.setVisible(true);
    }
}
