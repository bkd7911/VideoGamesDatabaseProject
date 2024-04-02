import java.sql.*;
import java.util.*;

/**
 * This program is used to interface with our database of
 * video games and video game related information
 *
 * @author Group 32 of CSCI 320, Spring 2024
 * @author Sam Cordry, Caelen Naas, Pranav Sehgal,
 *         Bobby Dhanoolal, Cristian Malone
 */
public class Main {

    static String currentUID = null;

    public static void main(String[] args) throws SQLException {

        Connection conn = new Database().getConn();
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
                    Login.createUser(stmt, scanner);
                    break;
                case 2:
                    loggedIn = Login.loginUser(stmt, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    conn.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }

        }
        menuAccess(scanner,stmt,conn);
    }

    private static void menuAccess(Scanner scanner, Statement stmt, Connection conn) throws SQLException {
        VideoGames vg = new VideoGames(stmt, scanner, currentUID);
        Friends f = new Friends(stmt,scanner,currentUID);
        Collections c = new Collections(stmt,scanner,currentUID);
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
                    f.friendsMenu();
                    break;
                case 2:
                    int vgr = vg.VideoGameMenu();
                    if(vgr!=3)break;
                case 3:
                    c.collectionsMenu();
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

}
