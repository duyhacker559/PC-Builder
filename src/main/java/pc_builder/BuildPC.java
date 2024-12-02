/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class BuildPC {
    public ArrayList<String> component;
    public double price = 0;
    public double performance = 0;

    public BuildPC(ArrayList<String> a) {
        this.component = a;
        double totalPrice = 0;
        for (String i: component) {
            Device item = Device.items.get(i);
            totalPrice += item.truePrice;
            performance += item.performance;
        }
        this.price = totalPrice;
    }
}
