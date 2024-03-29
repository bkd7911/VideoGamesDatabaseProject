
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Main {

    public static String getCurrentUID() {
        return currentUID;
    }

    static String currentUID = null;

    public static void main(String[] args) throws SQLException {

        Connection conn = new Database().getConn();
        VideoGames vg = new VideoGames();
        Friends f = new Friends();
        Collections c = new Collections();

        System.out.println("Database connection established");

        Statement stmt = conn.createStatement();

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n1. Create Account");
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
        menuAccess(scanner,stmt,vg,conn);



    }

    private static int menuAccess(Scanner scanner,Statement stmt, VideoGames vg, Connection conn) throws SQLException {
        while (true){
            System.out.println("\n--Select Menu To Access--");
            System.out.println("1. Friends");
            System.out.println("2. Video Games");
            System.out.println("3. Collections");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    f.friendsMenu(stmt,scanner,currentUID);
                    break;
                case 2:
                    int vgr = vg.VideoGameMenu(stmt, scanner, currentUID);
                    break;
                case 3:
                    c.collectionsMenu(stmt, scanner,currentUID);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    conn.close();
                    System.exit(0);
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

}
