import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class Main {

    static String currentUID = null;

    public static void main(String[] args) throws SQLException {

        Connection conn = new Database().getConn();

        System.out.println("Database connection established");

        Statement stmt = conn.createStatement();

        Scanner scanner = new Scanner(System.in);

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
