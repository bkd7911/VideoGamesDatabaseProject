
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
                    friendsMenu(stmt,scanner);
                    break;
                case 2:
                    int vgr = vg.VideoGameMenu(stmt, scanner, currentUID);
                    if(vgr != 1)
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

    private static void friendsMenu(Statement stmt, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("--Select Friends Action--");
            System.out.println("1. Find Friends");
            System.out.println("2. Follow User");
            System.out.println("3. Unfollow User");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    findUsersByEmail(stmt,scanner);
                    break;
                case 2:
                    followUser(stmt,scanner);
                    break;
                case 3:
                    unfollowUser(stmt,scanner);
                    break;
                case 4:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void findUsersByEmail(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Search Users By Email: ");
        String email = scanner.nextLine();
        String sql = "SELECT username, email FROM users as u\n" +
                "    INNER JOIN (SELECT uid, email FROM emails WHERE email LIKE '%" + email + "%') as e\n" +
                "    ON u.uid = e.uid";
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println("--Users Found--");
            String usernameStr = rs.getString("username");
            String emailStr = rs.getString("email");
            System.out.println("User: " + usernameStr + ", Email: " + emailStr);
            while (rs.next()) {
                usernameStr = rs.getString("username");
                emailStr = rs.getString("email");
                System.out.println("User: " + usernameStr + ", Email: " + emailStr);
            }
        } else {
            System.out.println("No Users Found With that Email.");
        }
        rs.close();
    }

    private static void followUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Username of User To Follow: ");
        String username = scanner.nextLine();

        String usrSql = "SELECT uid FROM users WHERE username = '" + username + "'";
        stmt.executeQuery(usrSql);
        ResultSet rsUsr = stmt.getResultSet();
        if (rsUsr.next()) {
            String fllwSql = "INSERT INTO friends (uid1, uid2) VALUES ('" + currentUID + "', '" + rsUsr.getString("uid") + "')";
            stmt.executeUpdate(fllwSql);
            System.out.println("Friend added successfully.");
        } else {
            System.out.println("User with Given Username Does Not Exist.");
        }
        rsUsr.close();
    }

    private static void unfollowUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Username of User To Unfollow: ");
        String username = scanner.nextLine();

        String usrSql = "SELECT uid FROM users WHERE username = '" + username + "'";
        stmt.executeQuery(usrSql);
        ResultSet rsUsr = stmt.getResultSet();
        if (rsUsr.next()) {
            String unfllwSql = "DELETE FROM friends WHERE uid1='" + currentUID + "'AND uid2= '" + rsUsr.getString("uid") + "'";
            stmt.executeUpdate(unfllwSql);
            System.out.println("Friend removed successfully.");
        } else {
            System.out.println("User with Given Username Does Not Exist.");
        }
        rsUsr.close();
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
                    viewCollections(stmt);
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

        if (rsCol.next()) {
            int cid = rsCol.getInt("cid");

            System.out.print("Enter Title of Video Game To Add: ");
            String vgName = scanner.nextLine();
            String vgSql = "SELECT vgid FROM videogame WHERE title='" + vgName + "'";
            ResultSet rsVG = stmt.executeQuery(vgSql);
            if (rsVG.next()) {

                int vgid = rsVG.getInt("vgid");

                String chkSql = "SELECT * FROM user_platform WHERE uid='" + currentUID + "' AND pid IN \n" +
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
        System.out.print("Enter Name of Collection To Delete From: ");
        String collectionName = scanner.nextLine();
        String sql = "SELECT cid FROM collections WHERE uid='" + currentUID + "' AND name='" + collectionName + "'";
        ResultSet rsCol = stmt.executeQuery(sql);
        if (rsCol.next()) {

            int cid = rsCol.getInt("cid");

            System.out.print("Enter Name of Video Game To Delete: ");
            String vgName = scanner.nextLine();
            String vgSql = "SELECT vgid FROM videogame WHERE title='" + vgName + "'";
            ResultSet rsVG = stmt.executeQuery(vgSql);
            if (rsVG.next()) {
                int vgid = rsVG.getInt("vgid");

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

    private static void viewCollections(Statement stmt) throws SQLException {
        System.out.println("--All Collections--");
        String sql = "SELECT name, COUNT(vgid) as numVideoGames,\n" +
                "       SUM((DATE_PART('day', sessionend - sessionstart) * 24) + DATE_PART('hour', sessionend - sessionstart)) as hourDiff,\n" +
                "       SUM(DATE_PART('minute', sessionend - sessionstart)) as minuteDiff\n" +
                "FROM\n" +
                "    (SELECT c.name, c.uid, c.cid, vgc.vgid, s.sessionstart, s.sessionend\n" +
                "     FROM collections AS c, video_game_collection AS vgc, session AS s\n" +
                "     WHERE c.uid = s.uid AND c.cid = vgc.cid AND vgc.vgid = s.vgid) as cv\n" +
                "WHERE uid = '" + currentUID + "'\n" +
                "GROUP BY cid, name\n" +
                "ORDER BY name ASC;";
        ResultSet rsCol = stmt.executeQuery(sql);
        while (rsCol.next()) {
            System.out.println("Collection Name: " + rsCol.getString("name"));
            System.out.println("\tTotal Games in Collection: " + rsCol.getString("numVideoGames"));
            int hours = rsCol.getInt("hourDiff");
            int minutes = rsCol.getInt("minuteDiff");

            if (minutes >= 60) {
                hours += Math.floorDiv(minutes, 60);
                minutes = minutes & 60;
            }

            String hoursStr = String.valueOf(hours);
            String minutesStr = String.valueOf(minutes);

            if (hours < 10) {
                hoursStr = '0' + String.valueOf(hours);
            }

            if (minutes < 10) {
                minutesStr = '0' + String.valueOf(minutes);
            }


            System.out.println("\tTotal Play Time of Games (HH:MM): " + hoursStr + ":" + minutesStr);

        }
        rsCol.close();
    }
}
