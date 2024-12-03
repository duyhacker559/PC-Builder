/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pc_builder;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.json.*;
/**
 *
 * @author Admin
 */
public class UserStorage{
    private static String DB_URL = "jdbc:mysql://localhost:3306/pc_builder";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "987choithoi";
    
    private static final String FILE_PATH = "Dat/users.json";
    private static final String primaryKey = "username";
    
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static JSONObject Sample() {
        String formatString = "ND%05d";
        int i = 0;
        while (idExists(formatString.formatted(i))) {
            i++;
        }
        
        JSONObject sample = new JSONObject();
        sample.put("id", formatString.formatted(i));
        sample.put("username", "N/A");
        sample.put("password", "N/A");
        sample.put("name", "N/A");
        sample.put("last_Name", "N/A");
        sample.put("birth", TimeHandler.getCurrentDay());
        sample.put("gender", "N/A");
        sample.put("email", "N/A");
        sample.put("address", "N/A");
        sample.put("balance", 0);
        sample.put("history", new JSONObject(){{
            this.put("0", new JSONObject(){{
                this.put("action", "Account created");
                this.put("date", TimeHandler.getCurrentDay());
                this.put("time", TimeHandler.getCurrentTime());
            }});
        }});
        
        return sample;
    }
    
    public static void initiate() {
        // Online data
        
        try (Connection conn = getConnection()) {
            if (!itemExists("root")) {
                JSONObject newUser = Sample();
                newUser.put("username", "root");
                newUser.put("password", "123");
                newUser.put("email", "root@gmail.com");
                newUser.put("name", "Root");
                newUser.put("last_Name", "Root");
                newUser.put("birth", TimeHandler.getCurrentDay());
                newUser.put("gender", "Other");
                newUser.put("address", "Root-123");
                newUser.put("admin", true);
                addItem(newUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            
        // Offline data
        String folderPath = "Dat"; // Folder will be created in the working directory

        // Create the folder
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Folder created at: " + folder.getAbsolutePath());
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder already exists: " + folder.getAbsolutePath());
        }

        // Create the json file
        if (!Files.exists(Paths.get(FILE_PATH))) {
            JSONArray newData = new JSONArray();
            try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(newData.toString(4));
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

        if (itemExists("root"));
        else {
            JSONObject newUser = UserStorage.Sample();
            newUser.put("username", "root");
            newUser.put("password", "123");
            newUser.put("email", "root@gmail");
            newUser.put("name", "Root");
            newUser.put("last_Name", "Root");
            newUser.put("birth", TimeHandler.getCurrentDay());
            newUser.put("gender", "Other");
            newUser.put("address", "Root-123");
            newUser.put("admin", true);
            UserStorage.addItem(newUser);
        }
    }

    // Load items from the JSON file
    public static JSONArray loadItems() {
        if (StorageSystem.online) {
            JSONArray items = new JSONArray();

            // SQL query to fetch user data
            String sql = "SELECT * FROM users";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // Process each record from the result set
                while (rs.next()) {
                    JSONObject item = new JSONObject();
                    item.put("id", rs.getString("id"));
                    item.put("username", rs.getString("username"));
                    item.put("password", rs.getString("password"));
                    item.put("name", rs.getString("name"));
                    item.put("last_Name", rs.getString("last_Name"));
                    item.put("birth", rs.getString("birth"));
                    item.put("gender", rs.getString("gender"));
                    item.put("email", rs.getString("email"));
                    item.put("address", rs.getString("address"));
                    item.put("balance", rs.getDouble("balance"));
                    item.put("history", new JSONObject (rs.getString("history")));
                    item.put("admin", rs.getBoolean("admin"));

                    // Add the item to the JSON array
                    items.put(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return items;
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
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, itemname);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
            String query = "SELECT COUNT(*) FROM users WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
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
        if (StorageSystem.online) {
            String query = "DELETE FROM users WHERE username = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, itemname);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Deleted item: " + itemname);
                } else {
                    System.out.println("Item not found: " + itemname);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JSONArray items = loadItems();
            boolean itemFound = false;

            // Cycle through and delete item
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
            String query = "INSERT INTO users (id, username, password, name, last_Name, birth, gender, email, address, balance, history, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                String hashedPassword = hashPassword(newItemData.getString("password"));
                stmt.setString(1, newItemData.getString("id"));
                stmt.setString(2, newItemData.getString("username"));
                stmt.setString(3, hashedPassword);
                stmt.setString(4, newItemData.getString("name"));
                stmt.setString(5, newItemData.getString("last_Name"));
                stmt.setString(6, newItemData.getString("birth"));
                stmt.setString(7, newItemData.getString("gender"));
                stmt.setString(8, newItemData.getString("email"));
                stmt.setString(9, newItemData.getString("address"));
                stmt.setDouble(10, newItemData.getDouble("balance"));
                stmt.setString(11, newItemData.get("history").toString());
                stmt.setBoolean(12, newItemData.getBoolean("admin"));
                stmt.executeUpdate();
                System.out.println("Item added successfully!");
            } catch (SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            String itemname, email, password;
            itemname = newItemData.getString("username");
            password = newItemData.getString("password");
            email = newItemData.getString("email");
            if (itemExists(itemname)) {
                System.out.println("Item already exists!");
                return;  // Don't add item if already exists
            }

            try {

                JSONArray items = loadItems();
                String hashedPassword = hashPassword(password);  // Hash password before saving

                JSONObject newItem = new JSONObject();
                for (String key : newItemData.keySet()) {
                    newItem.put(key, newItemData.get(key));  // Add new fields or update existing ones
                }
                newItem.put("password", hashedPassword);  // Store hashed password with salt

                items.put(newItem);

                // Save updated list to the JSON file
                try (FileWriter file = new FileWriter(FILE_PATH)) {
                    file.write(items.toString(4));  // Pretty-print the JSON
                }

                System.out.println("Item added successfully!");
            } catch (IOException | NoSuchAlgorithmException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    private static boolean userExists(String id, Connection conn) throws SQLException {
        String checkUserQuery = "SELECT 1 FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkUserQuery)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // If the user exists, result set will not be empty
            }
        }
    }
    
    // Save the updated list of items to the JSON file
    public static void saveItems(JSONArray items) {
        if (StorageSystem.online) {
                // SQL query to insert or update user data
            String sqlUpdate = "UPDATE users SET username = ?, password = ?, name = ?, last_Name = ?, birth = ?, gender = ?, email = ?, address = ?, balance = ?, history = ?, admin = ? WHERE id = ?";
            String sqlInsert = "INSERT INTO users (id, username, password, name, last_Name, birth, gender, email, address, balance, history, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare a statement for updating existing users
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                     PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

                    // Iterate through each item and save it
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String id = item.getString("id");
                        String username = item.getString("username");
                        String password = item.getString("password");
                        String name = item.getString("name");
                        String lastName = item.getString("last_Name");
                        String birth = item.getString("birth");
                        String gender = item.getString("gender");
                        String email = item.getString("email");
                        String address = item.getString("address");
                        double balance = item.getDouble("balance");
                        String history = item.get("history").toString();
                        boolean admin = item.getBoolean("admin");

                        // Check if the user exists in the database
                        if (userExists(id, conn)) {
                            // Update existing user record
                            stmtUpdate.setString(1, username);
                            stmtUpdate.setString(2, password);
                            stmtUpdate.setString(3, name);
                            stmtUpdate.setString(4, lastName);
                            stmtUpdate.setString(5, birth);
                            stmtUpdate.setString(6, gender);
                            stmtUpdate.setString(7, email);
                            stmtUpdate.setString(8, address);
                            stmtUpdate.setDouble(9, balance);
                            stmtUpdate.setString(10, history);
                            stmtUpdate.setBoolean(11, admin);
                            stmtUpdate.setString(12, id);

                            stmtUpdate.executeUpdate();
                        } else {
                            // Insert new user record
                            stmtInsert.setString(1, id);
                            stmtInsert.setString(2, username);
                            stmtInsert.setString(3, password);
                            stmtInsert.setString(4, name);
                            stmtInsert.setString(5, lastName);
                            stmtInsert.setString(6, birth);
                            stmtInsert.setString(7, gender);
                            stmtInsert.setString(8, email);
                            stmtInsert.setString(9, address);
                            stmtInsert.setDouble(10, balance);
                            stmtInsert.setString(11, history);
                            stmtInsert.setBoolean(12, admin);

                            stmtInsert.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
            String query = "UPDATE users SET username = ?, password = ?, name = ?, last_Name = ?, birth = ?, gender = ?, email = ?, address = ?, balance = ?, history = ?, admin = ? WHERE username = ?";
            try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newItemData.getString("username"));
                stmt.setString(2, newItemData.getString("password"));
                stmt.setString(3, newItemData.getString("name"));
                stmt.setString(4, newItemData.getString("last_Name"));
                stmt.setString(5, newItemData.getString("birth"));
                stmt.setString(6, newItemData.getString("gender"));
                stmt.setString(7, newItemData.getString("email"));
                stmt.setString(8, newItemData.getString("address"));
                stmt.setDouble(9, newItemData.getDouble("balance"));
                stmt.setString(10, newItemData.get("history").toString());
                stmt.setBoolean(11, newItemData.getBoolean("admin"));
                stmt.setString(12, itemname);
                stmt.executeUpdate();
                System.out.println("Item updated successfully!");
            } catch (SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
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
    public static JSONObject getItem(String itemname) {
        if (StorageSystem.online) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, itemname);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JSONObject item = new JSONObject();
                    item.put("id", rs.getString("id"));
                    item.put("username", rs.getString("username"));
                    item.put("password", rs.getString("password"));
                    item.put("name", rs.getString("name"));
                    item.put("last_Name", rs.getString("last_Name"));
                    item.put("birth", rs.getString("birth"));
                    item.put("gender", rs.getString("gender"));
                    item.put("email", rs.getString("email"));
                    item.put("address", rs.getString("address"));
                    item.put("balance", rs.getDouble("balance"));
                    item.put("history", new JSONObject (rs.getString("history")));
                    item.put("admin", rs.getBoolean("admin"));
                    return item;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            JSONArray items = loadItems();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.getString(primaryKey).equals(itemname)) {
                    return item;  // Return the item as a JSONObject
                }
            }
            return null;  // Return null if item is not found
        }
    }
    
    // Get different data of key in items
    public static TreeSet<Object> getDistinctValue(String key) {
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
