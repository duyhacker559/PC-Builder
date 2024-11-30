/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class StorageSystem_NotNeeded {
    static final String FILE_PATH = "Dat/items.json";
    static String primaryKey = "id";
    
    public static JSONObject Sample() {
        String formatString = "ND%05d";
        int i = 0;
        while (idExists(formatString.formatted(i))) {
            i++;
        }
        
        JSONObject sample = new JSONObject();
        sample.put("id", formatString.formatted(i));
        sample.put("itemname", "N/A");
        sample.put("password", "N/A");
        sample.put("name", "N/A");
        sample.put("last_Name", "N/A");
        sample.put("birth", new JSONObject(){{
            this.put("day", 1);
            this.put("month", 1);
            this.put("year", 2000);
        }});
        sample.put("gender", "N/A");
        sample.put("email", "N/A");
        sample.put("address", "N/A");
        sample.put("balance", 0);
        sample.put("history", new JSONObject(){{
            this.put("0", new JSONObject(){{
                this.put("action", "Account created");
                this.put("time", TimeHandler.getCurrentDay());
            }});
        }});
        
        return sample;
    }
    
    public static void initiate() {       
        // Create the json file
        if (!Files.exists(Paths.get(FILE_PATH))) {
            JSONArray newData = new JSONArray();  // No items, return empty array
            try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(newData.toString(4));  // Pretty-print the JSON
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    // Load items from the JSON file
    public static JSONArray loadItems() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return new JSONArray();  // No items, return empty array
            }
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            return new JSONArray(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new JSONArray();
        }
    }

    // Check if a item with the same itemname already exists
    public static boolean itemExists(String itemname) {
        JSONArray items = loadItems();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.getString(primaryKey).equals(itemname)) {
                return true;  // Item exists
            }
        }
        return false;  // Item does not exist
    }

    public static boolean idExists(String id) {
        JSONArray items = loadItems();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.getString("id").equals(id)) {
                return true;  // Item exists
            }
        }
        return false;  // Item does not exist
    }
    
    // Method to hash a password with salt using SHA-256
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Generate salt
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        // Combine password and salt
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        // Convert the hash and salt to base64 to store in JSON
        String base64Salt = Base64.getEncoder().encodeToString(salt);
        String base64Hash = Base64.getEncoder().encodeToString(hashedPassword);

        return base64Salt + ":" + base64Hash;  // Store salt and hash together
    }

    // Verify password by comparing hashes
    public static boolean verifyPassword(String storedPassword, String inputPassword) throws NoSuchAlgorithmException {
        String[] parts = storedPassword.split(":");
        String storedSalt = parts[0];
        String storedHash = parts[1];

        // Hash the input password with the stored salt
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] inputHash = md.digest(inputPassword.getBytes());

        // Compare hashes (stored hash vs. input hash)
        String inputHashBase64 = Base64.getEncoder().encodeToString(inputHash);
        return inputHashBase64.equals(storedHash);
    }
    
    // Delete a item by itemname
    public static void deleteItem(String itemname) {
        JSONArray items = loadItems();
        boolean itemFound = false;

        // Duyệt qua danh sách và xóa người dùng
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.getString(primaryKey).equals(itemname)) {
                items.remove(i);
                itemFound = true;
                break;
            }
        }

        if (itemFound) {
            saveItems(items);
            System.out.println("Deleted item: " + itemname);
        } else {
            System.out.println("Item not exist: " + itemname);
        }
    }
    
    // Add a new item if they don't exist
    public static void addItem(JSONObject newItemData) {
        String itemname;
        itemname = newItemData.getString(primaryKey);
        if (itemExists(itemname)) {
            System.out.println("Item already exists!");
            return;  // Don't add item if already exists
        }

        try {
            JSONArray items = loadItems();

            JSONObject newItem = new JSONObject();
            for (String key : newItemData.keySet()) {
                newItem.put(key, newItemData.get(key));  // Add new fields or update existing ones
            }

            items.put(newItem);

            // Save updated list to the JSON file
            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(items.toString(4));  // Pretty-print the JSON
            }

            System.out.println("Item added successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Save the updated list of items to the JSON file
    public static void saveItems(JSONArray items) {
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(items.toString(4));  // Pretty-print the JSON
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // Update a item's information, including optional fields
    public static void updateItem(String itemname, JSONObject newItemData) {
        JSONArray items = loadItems();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.getString(primaryKey).equals(itemname)) {
                // Update item with new data, including optional fields
                for (String key : newItemData.keySet()) {
                    item.put(key, newItemData.get(key));  // Add new fields or update existing ones
                }
                saveItems(items);
                System.out.println("Item updated successfully!");
                return;
            }
        }
        System.out.println("Item not found!");
    }
    
    // Get a item by their itemname
    public static JSONObject getItem(String id) {
        JSONArray items = loadItems();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.getString(primaryKey).equals(id)) {
                return item;  // Return the item as a JSONObject
            }
        }
        return null;  // Return null if item is not found
    }
}
