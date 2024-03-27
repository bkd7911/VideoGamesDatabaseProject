import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class Main {

    static String currentUID = null;

    public static void main(String[] args) {

        int lport = 5432;
        String rhost = "starbug.cs.rit.edu";
        int rport = 5432;

        JSONParser parser = new JSONParser();
        Scanner scanner = new Scanner(System.in);

        Statement stmt = null;
        Session session;
        Connection conn = null;

        try {

            Object obj = parser.parse(new FileReader("credentials.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            String user = (String) jsonObject.get("Username");
            String password = (String) jsonObject.get("Password");
            String databaseName = "p320_32";

            String driverName = "org.postgresql.Driver";

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "127.0.0.1", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://127.0.0.1:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");

            stmt = conn.createStatement();

            while (true) {
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1:
                        createUser(stmt, scanner);
                        break;
                    case 2:
                        loginUser(stmt, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }


        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static void createUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String sql = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "')";
        stmt.executeUpdate(sql);
        System.out.println("Account created successfully.");
    }

    private static void loginUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String sql = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        int userId = rs.getInt("uid");
        if (userId != 0) {
            currentUID = String.valueOf(userId);
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid username or password.");
        }
        rs.close();
    }

    private static void createCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter New Collection Name: ");
        String collectionName = scanner.nextLine();
        String sql = "INSERT INTO collections (uid, name) VALUES ('" + currentUID + "', '" + collectionName + "')";
        stmt.executeUpdate(sql);
        System.out.println("Colection created successfully.");
    }

    private static void modifyCollectionName(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Collection Name To Modify: ");
        String currCollectionName = scanner.nextLine();
        System.out.print("Enter New Collection Name: ");
        String newCollectionName = scanner.nextLine();
        String sql = "UPDATE collections SET name='" + newCollectionName + "' WHERE uid='" + currentUID + "' AND name='" + currCollectionName + "'";
        stmt.executeUpdate(sql);
        System.out.println("Colection created successfully.");
    }

    private static void deleteCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Name of Collection To Delete: ");
        String collectionName = scanner.nextLine();
        String sql = "DELETE FROM collections WHERE uid='" + currentUID + "' AND name='" + collectionName + "'";
        stmt.executeUpdate(sql);
        System.out.println("Collection deleted successfully.");
    }

    private static void addVideoGameToCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Name of Collection To Add To: ");
        String collectionName = scanner.nextLine();
//        String sql = "DELETE FROM collections WHERE name='" + username + "'";
//        stmt.executeUpdate(sql);
        System.out.println("Collection deleted successfully.");
    }
}
