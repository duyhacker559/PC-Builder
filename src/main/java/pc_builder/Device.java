/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;

import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;
import pc_builder.UserStorage;

/**
 *
 * @author Admin
 */
public class Device {
    public static TreeMap<String, Device> items = new TreeMap<>();
    public static JSONArray dataArray;
    public static boolean itemChanged = false;
    
    public static TreeSet<Object> getDistinctValue(String key) {
        TreeSet<Object> res = new TreeSet<>();
        JSONArray items = dataArray;
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.has(key)) {
                res.add(item.get(key));
            }
        }
        return res;
    }
    
    public static void loadDevices(JSONArray devicesData) {
        items.clear();
        dataArray = devicesData;
        for (int i = 0; i < devicesData.length(); i++) {
            JSONObject item = devicesData.getJSONObject(i);
            Device getDevice = new Device(item);
            items.put(getDevice.id, getDevice);
        }
    }
    
    public static void newDevice(JSONObject newData) {
        JSONObject newUser = DeviceStorage.Sample();
        for (String key: newData.keySet()) {
            newUser.put(key, newData.get(key));
        }
    }
    
    public static void deleteUser(String idString) {
        DeviceStorage.deleteItem(idString);
    }
    
    public static Device getDevice(String id) {
        return items.get(id);
    }
    
    public static void addDevice(JSONObject newDeviceData) {
        DeviceStorage.addItem(newDeviceData);
    }
    
    public static void saveDevices() {
        DeviceStorage.saveItems(dataArray);
    }
    
    private String id;
    private String name;
    private String type;
    private String brand;
    private double price;
    private String des;
    private String icon;
    private JSONObject history;
    private JSONObject attributes;
    private JSONObject info;
    private boolean forSale;
    private double sale;
    
    public double truePrice; 
    public double performance = 0;
    public String trueName;
    
    public JSONObject data;
    
    public Device(JSONObject data) {
        sync(data);
    }
    
    // Update to storage
    public void update() {
        data.put("id", this.id);
        data.put("name", this.name);
        data.put("type", this.type);
        data.put("brand", this.brand);
        data.put("price", this.price);
        data.put("des", this.des);
        data.put("icon", this.icon);
        data.put("sale", this.sale);
        data.put("forSale", this.forSale);
        data.put("info", this.info);
        data.put("attributes", this.attributes);
        data.put("history", this.history);
    }
    
    // Get data from JSON data
    public void sync(JSONObject deviceData) {        
        this.id = deviceData.getString("id");
        this.name = deviceData.getString("name");
        this.type = deviceData.getString("type");
        this.brand = deviceData.getString("brand");
        this.price = deviceData.getDouble("price");
        this.des = deviceData.getString("des");
        this.icon = deviceData.getString("icon");
        this.sale = deviceData.getDouble("sale");
        this.forSale = deviceData.getBoolean("forSale");
        this.info = deviceData.getJSONObject("info");
        this.attributes = deviceData.getJSONObject("attributes");
        this.history = deviceData.getJSONObject("history");
        
        this.truePrice = this.price;
        if (this.forSale) {
            this.truePrice *= 1 - this.sale;
        }
        
        if (this.attributes.has("performance")) {
            this.performance = this.attributes.getDouble("performance");
        }
        
        this.trueName = (this.brand + " - " + this.name + " - " + this.des).toLowerCase();
    }
    
    // Add an information to User history
    public void addRecord(String info) {
        int index = 0;
        while (history.has(index+"")) {
            index++;
        }
        
        JSONObject newRecord = new JSONObject(){{
            this.put("date", TimeHandler.getCurrentDay());
            this.put("time", TimeHandler.getCurrentTime());
            this.put("action", info);
        }};
        history.put(index+"", newRecord);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getDes() {
        return des;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isForSale() {
        return forSale;
    }

    public double getSale() {
        return sale;
    }

    public double getPerformance() {
        return performance;
    }
    
    
}
