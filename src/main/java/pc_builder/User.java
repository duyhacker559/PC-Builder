/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;
import pc_builder.UserStorage;

/**
 *
 * @author Admin
 */
public class User {
    public static TreeMap<String, User> items = new TreeMap<>();
    public static JSONArray dataArray;
    public static boolean itemChanged = false;
    
    public static void loadUsers(JSONArray usersData) {
        items.clear();
        dataArray = usersData;
        for (int i = 0; i < usersData.length(); i++) {
            JSONObject item = usersData.getJSONObject(i);
            User getUser = new User(item);
            items.put(getUser.username, getUser);
        }
    }
    
    public static void newUser(JSONObject newData) {
        JSONObject newUser = UserStorage.Sample();
        for (String key: newData.keySet()) {
            newUser.put(key, newData.get(key));
        }
        UserStorage.addItem(newUser);
        loadUsers(UserStorage.loadItems());
    }
    
    public static void deleteUser(String username) {
        UserStorage.deleteItem(username);
        loadUsers(UserStorage.loadItems());
    }
    
    public static User getUser(String username) {
        return items.get(username);
    }
    
    public static void updateUser(User user) {
        UserStorage.updateItem(user.username, user.data);
    }
    
    public static void addUser(JSONObject newUserData) {
        UserStorage.addItem(newUserData);
    }
    
    public static void saveUsers() {
        UserStorage.saveItems(dataArray);
    }
    
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        return UserStorage.hashPassword(password);
    }
    
    public static boolean verifyPassword(String storedPassword, String inputPassword) throws NoSuchAlgorithmException {
        return UserStorage.verifyPassword(storedPassword, inputPassword);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private String id;
    public String name;
    private String last_Name;
    private String email;
    public double balance;
    private String address;
    private String birth;
    private String gender;
    private JSONObject history;
    private boolean admin;
    
    private String username;
    private String password;
    
    public JSONObject data;
    
    public User(JSONObject data) {
        sync(data);
    }
    
    // Check LogIN
    public boolean logIn(String password) throws NoSuchAlgorithmException {
        return verifyPassword(this.password, password);
    }
    
    // Update to storage
    public void update() {
        data.put("username", this.username);
        data.put("password", this.password);
        
        data.put("id", this.id);
        data.put("email", this.email);
        data.put("name", this.name);
        data.put("last_Name", this.last_Name);
        data.put("gender", this.gender);
        data.put("birth", this.birth);
        data.put("balance", this.balance);
        data.put("admin", this.admin);
        data.put("history", this.history);
        
        updateUser(this);
    }
    
    // Get data from JSON data
    public void sync(JSONObject userData) {
        this.username = userData.getString("username");
        this.password = userData.getString("password");
        
        this.id = userData.getString("id");
        this.name = userData.getString("username");
        this.last_Name = userData.getString("last_Name");
        this.email = userData.getString("email");
        this.gender = userData.getString("gender");
        this.birth = userData.getString("birth");
        this.address = userData.getString("address");
        this.balance = userData.getDouble("balance");
        this.history = userData.getJSONObject("history");
        this.admin = userData.getBoolean("admin");
        
        this.data = userData;
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

    public boolean isAdmin() {
        return admin;
    }

    public String getUsername() {
        return username;
    }

    public JSONObject getHistory() {
        return history;
    }
    
}
