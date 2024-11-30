/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pcbuilder;

import UI.Login;
import pc_builder.DeviceStorage;
import pc_builder.UserStorage;

/**
 *
 * @author Admin
 */
public class PCBuilder {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        UserStorage.initiate();
        DeviceStorage.initiate();
        Login ui = new Login();
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }
}
