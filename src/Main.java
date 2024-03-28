import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Main {

    static String currentUID = null;

    public static void main(String[] args) throws SQLException {

        Connection conn = new Database().getConn();

        System.out.println("Database connection established");

        Statement stmt = conn.createStatement();

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
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
                    loggedIn = loginUser(stmt, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    conn.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }

        }

        while (true) {
            System.out.println("--Select Menu To Access--");
            System.out.println("1. Friends");
            System.out.println("2. Video Games");
            System.out.println("3. Collections");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    collectionsMenu(stmt, scanner);
                    break;
                case 4:
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
        String password = BCrypt.hashpw(scanner.nextLine(), BCrypt.gensalt(10));
        System.out.print("Enter first name: ");
        String first_name = scanner.nextLine();
        System.out.print("Enter last name: ");
        String last_name = scanner.nextLine();

        String creationDate = new Date().toString();
        String lastAccessData = new Date().toString();


        String sql = "INSERT INTO users (username, first_name, last_name, creation_date, last_access_date, password) " +
                "VALUES ('" + username + "', '" + first_name + "', '" + last_name + "', '" + creationDate + "', '" + lastAccessData + "', '" + password + "')";
        stmt.executeUpdate(sql);
        System.out.println("Account created successfully.");
    }

    private static boolean loginUser(Statement stmt, Scanner scanner) throws SQLException {
        // TODO Change where the user input is gotten from
        boolean loggedIn = false;

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String sql = "SELECT * FROM users WHERE username='" + username + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next() && BCrypt.checkpw(password,rs.getString("password"))) {
                System.out.println("Login successful. Welcome, " + username + "!");
                currentUID = String.valueOf(rs.getInt("uid"));
                loggedIn = true;

        } else {
            System.out.println("Invalid username or password.");
        }
        rs.close();
        return loggedIn;
    }

    private static void collectionsMenu(Statement stmt, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("--Select Collections Action--");
            System.out.println("1. View Your Collections");
            System.out.println("2. Create Collection");
            System.out.println("3. Modify Collection Name");
            System.out.println("4. Delete Collection");
            System.out.println("5. Add Game to Collection");
            System.out.println("6. Delete Game from Collection");
            System.out.println("7. Return to Main Menu");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    viewCollections(stmt,scanner);
                    break;
                case 2:
                    createCollection(stmt,scanner);
                    break;
                case 3:
                    modifyCollectionName(stmt,scanner);
                    break;
                case 4:
                    deleteCollection(stmt,scanner);
                    break;
                case 5:
                    addVideoGameToCollection(stmt,scanner);
                    break;
                case 6:
                    deleteVideoGameFromCollection(stmt,scanner);
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    private static void createCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter New Collection Name: ");
        String collectionName = scanner.nextLine();
        String sql = "INSERT INTO collections (uid, name) VALUES ('" + currentUID + "', '" + collectionName + "')";
        stmt.executeUpdate(sql);
        System.out.println("Collection created successfully.");
    }

    private static void modifyCollectionName(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Collection Name To Modify: ");
        String currCollectionName = scanner.nextLine();
        System.out.print("Enter New Collection Name: ");
        String newCollectionName = scanner.nextLine();
        String sql = "UPDATE collections SET name='" + newCollectionName + "' WHERE uid='" + currentUID + "' AND name='" + currCollectionName + "'";
        stmt.executeUpdate(sql);
        System.out.println("Collection created successfully.");
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
        String sql = "SELECT cid FROM collections WHERE uid='" + currentUID + "' AND name='" + collectionName + "'";
        ResultSet rsCol = stmt.executeQuery(sql);
        int cid = rsCol.getInt("cid");

        if (cid != 0) {

            System.out.print("Enter Name of Video Game To Add: ");
            String vgName = scanner.nextLine();
            String vgSql = "SELECT vgid FROM videogame WHERE name='" + vgName + "'";
            ResultSet rsVG = stmt.executeQuery(vgSql);
            int vgid = rsVG.getInt("vgid");

            if (vgid != 0) {

                String chkSql = "SELECT * FROM user_platform WHERE uid='" + currentUID + "' AND pid IS IN " +
                        "(SELECT pid FROM release WHERE vgid='" + vgid + "')";
                ResultSet rsChk = stmt.executeQuery(chkSql);

                if (!rsChk.next()) {
                    System.out.println("--This Video Game is Not Available on a Platform You Own--");
                    System.out.print("Are you sure you want to proceed? (y/n): ");
                    String warningAnswer = scanner.nextLine();

                    if (warningAnswer.charAt(0) == 'n') {
                        System.out.println("Aborting collection addition.");
                        return;
                    }

                }

                String addSql = "INSERT INTO video_game_collection (cid, vgid) VALUES ('" + cid + "', '" + vgid + "')";
                stmt.executeUpdate(addSql);
                System.out.println("Successfully added " + vgName + " to Collection " + collectionName);
                rsChk.close();
            }
            else {
                System.out.println("Invalid video game name.");
            }
            rsVG.close();
        } else {
            System.out.println("Invalid collection name.");
        }
        rsCol.close();
    }

    private static void deleteVideoGameFromCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Name of Collection To Add To: ");
        String collectionName = scanner.nextLine();
        String sql = "SELECT cid FROM collections WHERE uid='" + currentUID + "' AND name='" + collectionName + "'";
        ResultSet rsCol = stmt.executeQuery(sql);
        int cid = rsCol.getInt("cid");
        if (cid != 0) {
            System.out.print("Enter Name of Video Game To Add: ");
            String vgName = scanner.nextLine();
            String vgSql = "SELECT vgid FROM videogame WHERE name='" + vgName + "'";
            ResultSet rsVG = stmt.executeQuery(vgSql);
            int vgid = rsVG.getInt("vgid");
            if (vgid != 0) {
                String addSql = "DELETE FROM video_game_collection WHERE cid='" + cid + "' AND vgid='" + vgid + "'";
                stmt.executeUpdate(addSql);
                System.out.println("Successfully deleted " + vgName + " from Collection " + collectionName);
            }
            else {
                System.out.println("Invalid video game name.");
            }
            rsVG.close();
        } else {
            System.out.println("Invalid collection name.");
        }
        rsCol.close();
    }

    private static void viewCollections(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("--All Collections--");
        String sql = "SELECT name FROM collections WHERE uid='" + currentUID + "'";
        ResultSet rsCol = stmt.executeQuery(sql);
        while (rsCol.next()) {
            System.out.println(rsCol.getString("name"));
        }
        rsCol.close();
    }
}
