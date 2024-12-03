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
    public int tagPoint = 0;
    
    public BuildPC(ArrayList<String> a, ArrayList<String> tags) {
        this.component = a;
        double totalPrice = 0;
        for (String i: component) {
            int multiplier = 1;
            Device item = Device.items.get(i);
            if (item.getType().compareTo("Case")==0) {
                multiplier = 4;
            }
            totalPrice += item.truePrice;
            performance += item.performance;
            for (String j: tags) {
                if (item.trueName.contains(j)) {
                    tagPoint+=multiplier;
                }
            }
        }
        this.price = totalPrice;
    }
}
