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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Admin
 */
public class DeviceStorage {
    public static String DB_URL = "jdbc:mysql://localhost:3306/pc_builder";
    public static String DB_USER = "root";
    public static String DB_PASSWORD = "987choithoi";
    
    private static final String FILE_PATH = "Dat/devices.json";
    private static final String primaryKey = "id";

    public static JSONObject Sample() {
        String formatString = "TB%05d";
        int i = 1;
        while (idExists(formatString.formatted(i))) {
            i++;
        }
        
        JSONObject sample = new JSONObject();
        sample.put("id", formatString.formatted(i));
        sample.put("name", "N/A");
        sample.put("type", "N/A");
        sample.put("brand", "N/A");
        sample.put("price", 0);
        sample.put("des", "N/A");
        sample.put("forSale", false);
        sample.put("sale", 0);
        sample.put("icon", 0);
        sample.put("attributes", new JSONObject(){{
            
        }});
        sample.put("info", new JSONObject(){{
            
        }});
        sample.put("history", new JSONObject(){{
            this.put("0", new JSONObject(){{
                this.put("action", "Device created");
                this.put("date", TimeHandler.getCurrentDay());
                this.put("time", TimeHandler.getCurrentTime());
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
        if (StorageSystem.online) {
            JSONArray items = new JSONArray();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM devices";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        JSONObject item = new JSONObject();
                        item.put("id", rs.getString("id"));
                        item.put("name", rs.getString("name"));
                        item.put("type", rs.getString("type"));
                        item.put("brand", rs.getString("brand"));
                        item.put("price", rs.getDouble("price"));
                        item.put("des", rs.getString("description"));
                        item.put("forSale", rs.getBoolean("forSale"));
                        item.put("sale", rs.getDouble("sale"));
                        item.put("icon", rs.getString("icon"));  // Assuming it's stored as base64 string
                        item.put("attributes", new JSONObject(rs.getString("attributes")));  // Assuming attributes is a JSON string
                        item.put("info", new JSONObject(rs.getString("info")));  // Assuming info is a JSON string
                        item.put("history", new JSONObject(rs.getString("history")));  // Assuming history is a JSON string

                        items.put(item);  // Add the device to the JSON array
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error loading items: " + e.getMessage());
            }

        return items;  // Return the list of items
        } else {
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
    }

    // Check if a item with the same itemname already exists
    public static boolean itemExists(String itemname) {
        if (StorageSystem.online) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT COUNT(*) FROM devices WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, itemname);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error checking if item exists: " + e.getMessage());
            }
            return false;
        } else {
            JSONArray items = loadItems();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.getString(primaryKey).equals(itemname)) {
                    return true;  // Item exists
                }
            }
            return false;  // Item does not exist
        }
    }

    public static boolean idExists(String id) {
        if (StorageSystem.online) {
            return itemExists(id);
        } else {
            JSONArray items = loadItems();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.getString("id").equals(id)) {
                    return true;  // Item exists
                }
            }
            return false;  // Item does not exist
        }
    }
    
    // Delete a item by itemname
    public static void deleteItem(String itemname) {
        if (StorageSystem.online) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM devices WHERE id = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, itemname);
                    stmt.executeUpdate();
                    System.out.println("Item deleted successfully!");
                }
            } catch (SQLException e) {
                System.err.println("Error deleting item from database: " + e.getMessage());
            }
        } else {
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
    }
    
    // Add a new item if they don't exist
    public static void addItem(JSONObject newItemData) {
        if (StorageSystem.online) {
            String itemname = newItemData.getString(primaryKey);

            if (itemExists(itemname)) {
                System.out.println("Item already exists!");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO devices (id, name, type, brand, price, description, forSale, sale, icon, attributes, info, history) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    // Set the parameters for the SQL query
                    stmt.setString(1, newItemData.getString("id"));
                    stmt.setString(2, newItemData.getString("name"));
                    stmt.setString(3, newItemData.getString("type"));
                    stmt.setString(4, newItemData.getString("brand"));
                    stmt.setDouble(5, newItemData.getDouble("price"));
                    stmt.setString(6, newItemData.getString("des"));
                    stmt.setBoolean(7, newItemData.getBoolean("forSale"));
                    stmt.setDouble(8, newItemData.getDouble("sale"));
                    stmt.setString(9, newItemData.get("icon").toString());  // Store the icon as BLOB
                    stmt.setString(10, newItemData.getJSONObject("attributes").toString());
                    stmt.setString(11, newItemData.getJSONObject("info").toString());
                    stmt.setString(12, newItemData.getJSONObject("history").toString());

                    // Execute the update
                    stmt.executeUpdate();
                    System.out.println("Item added successfully!");
                }
            } catch (SQLException e) {
                System.err.println("Error inserting into database: " + e.getMessage());
            }
        } else {
        
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
    }

    // Save the updated list of items to the JSON file
    public static void saveItems(JSONArray items) {
        if (StorageSystem.online) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Start a transaction to ensure atomic operations
                conn.setAutoCommit(false);

                String insertSQL = "INSERT INTO devices (id, name, type, brand, price, description, forSale, sale, icon, attributes, info, history) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String updateSQL = "UPDATE devices SET name = ?, type = ?, brand = ?, price = ?, description = ?, forSale = ?, sale = ?, "
                        + "icon = ?, attributes = ?, info = ?, history = ? WHERE id = ?";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String id = item.getString("id");

                        // Check if the item already exists (based on id)
                        if (itemExists(id)) {
                            // Update existing item
                            updateStmt.setString(1, item.getString("name"));
                            updateStmt.setString(2, item.getString("type"));
                            updateStmt.setString(3, item.getString("brand"));
                            updateStmt.setDouble(4, item.getDouble("price"));
                            updateStmt.setString(5, item.getString("des"));
                            updateStmt.setBoolean(6, item.getBoolean("forSale"));
                            updateStmt.setDouble(7, item.getDouble("sale"));
                            updateStmt.setString(8, item.get("icon").toString());
                            updateStmt.setString(9, item.get("attributes").toString());
                            updateStmt.setString(10, item.get("info").toString());
                            updateStmt.setString(11, item.get("history").toString());
                            updateStmt.setString(12, id);
                            updateStmt.addBatch();
                        } else {
                            // Insert new item
                            insertStmt.setString(1, id);
                            insertStmt.setString(2, item.getString("name"));
                            insertStmt.setString(3, item.getString("type"));
                            insertStmt.setString(4, item.getString("brand"));
                            insertStmt.setDouble(5, item.getDouble("price"));
                            insertStmt.setString(6, item.getString("des"));
                            insertStmt.setBoolean(7, item.getBoolean("forSale"));
                            insertStmt.setDouble(8, item.getDouble("sale"));
                            insertStmt.setString(9, item.get("icon").toString());
                            insertStmt.setString(10, item.get("attributes").toString());
                            insertStmt.setString(11, item.get("info").toString());
                            insertStmt.setString(12, item.get("history").toString());
                            insertStmt.addBatch();
                        }
                    }

                    // Execute the batch
                    insertStmt.executeBatch();
                    updateStmt.executeBatch();

                    // Commit the transaction
                    conn.commit();

                    System.out.println("Items saved successfully!");
                } catch (SQLException e) {
                    conn.rollback();
                    System.err.println("Error saving items: " + e.getMessage());
                } finally {
                    conn.setAutoCommit(true);  // Reset auto commit
                }
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
            }
        } else {
            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(items.toString(4));  // Pretty-print the JSON
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }
    
    // Update a item's information, including optional fields
    public static void updateItem(String itemname, JSONObject newItemData) {
        if (StorageSystem.online) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "UPDATE devices SET name = ?, type = ?, brand = ?, price = ?, description = ?, forSale = ?, sale = ?, icon = ?, attributes = ?, info = ?, history = ? WHERE id = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    String base64Icon = newItemData.getString("icon");
                    byte[] iconData = base64Icon.isEmpty() ? null : Base64.getDecoder().decode(base64Icon);

                    stmt.setString(1, newItemData.getString("name"));
                    stmt.setString(2, newItemData.getString("type"));
                    stmt.setString(3, newItemData.getString("brand"));
                    stmt.setDouble(4, newItemData.getDouble("price"));
                    stmt.setString(5, newItemData.getString("des"));
                    stmt.setBoolean(6, newItemData.getBoolean("forSale"));
                    stmt.setDouble(7, newItemData.getDouble("sale"));
                    stmt.setString(8, newItemData.get("icon").toString());  // Store the icon as BLOB
                    stmt.setString(9, newItemData.getJSONObject("attributes").toString());
                    stmt.setString(10, newItemData.getJSONObject("info").toString());
                    stmt.setString(11, newItemData.getJSONObject("history").toString());
                    stmt.setString(12, itemname);

                    stmt.executeUpdate();
                    System.out.println("Item updated successfully!");
                }
            } catch (SQLException e) {
                System.err.println("Error updating item in database: " + e.getMessage());
            }
        } else {
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
    }
    
    // Get a item by their itemname
    public static JSONObject getItem(String id) {
        if (StorageSystem.online) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM devices WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        JSONObject item = new JSONObject();
                        item.put("id", rs.getString("id"));
                        item.put("name", rs.getString("name"));
                        item.put("type", rs.getString("type"));
                        item.put("brand", rs.getString("brand"));
                        item.put("price", rs.getDouble("price"));
                        item.put("des", rs.getString("description"));
                        item.put("forSale", rs.getBoolean("forSale"));
                        item.put("sale", rs.getDouble("sale"));

                        // Retrieve and convert the icon from BLOB to Base64
                        byte[] iconData = rs.getBytes("icon");
                        if (iconData != null) {
                            String base64Icon = Base64.getEncoder().encodeToString(iconData);
                            item.put("icon", rs.getString("icon"));  // Add icon as Base64 string
                        } else {
                            item.put("icon", "");  // No icon available
                        }

                        item.put("attributes", new JSONObject(rs.getString("attributes")));
                        item.put("info", new JSONObject(rs.getString("info")));
                        item.put("history", new JSONObject(rs.getString("history")));

                        return item;
                    } else {
                        System.out.println("Item not found!");
                        return null;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving item from database: " + e.getMessage());
                return null;
            }
        } else {
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
    
    // Get different data of key in items
    public static TreeSet<Object> getDistinctValue(String key) {
        if (StorageSystem.online) {
            TreeSet<Object> distinctValues = new TreeSet<>();

            // Define the SQL query to get distinct values for the specified column
            String sql = "SELECT DISTINCT " + key + " FROM devices";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // Iterate through the result set and add each distinct value to the TreeSet
                while (rs.next()) {
                    distinctValues.add(rs.getObject(1));  // Get the first (and only) column of each row
                }

            } catch (SQLException e) {
                System.err.println("Error fetching distinct values: " + e.getMessage());
            }

            return distinctValues;
        } else {
            TreeSet<Object> res = new TreeSet<>();
            JSONArray items = loadItems();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has(key)) {
                    res.add(item.get(key));
                }
            }
            return res;  // Return set
        }
    }
}
