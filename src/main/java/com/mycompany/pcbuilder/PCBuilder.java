/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pcbuilder;

import UI.Login;
import pc_builder.Device;
import pc_builder.DeviceStorage;
import pc_builder.User;
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
        User.loadUsers(UserStorage.loadItems());
        Device.loadDevices(DeviceStorage.loadItems());
        Login ui = new Login();
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }
}
