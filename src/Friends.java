import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Friends {

    String currentUID;

    public void friendsMenu(Statement stmt, Scanner scanner, String currentUID) throws SQLException {

        this.currentUID = currentUID;

        while (true) {
            System.out.println("\n--Select Friends Action--");
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

    private void findUsersByEmail(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nSearch Users By Email: ");
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

    private void followUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter Username of User To Follow: ");
        String username = scanner.nextLine();

        String usrSql = "SELECT uid FROM users WHERE username = '" + username + "'";
        stmt.executeQuery(usrSql);
        ResultSet rsUsr = stmt.getResultSet();
        if (rsUsr.next()) {
            String fllwSql = "INSERT INTO friends (uid1, uid2) VALUES ('" + this.currentUID + "', '" + rsUsr.getString("uid") + "')";
            stmt.executeUpdate(fllwSql);
            System.out.println("Friend added successfully.");
        } else {
            System.out.println("User with Given Username Does Not Exist.");
        }
        rsUsr.close();
    }

    private void unfollowUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter Username of User To Unfollow: ");
        String username = scanner.nextLine();

        String usrSql = "SELECT uid FROM users WHERE username = '" + username + "'";
        stmt.executeQuery(usrSql);
        ResultSet rsUsr = stmt.getResultSet();
        if (rsUsr.next()) {
            String unfllwSql = "DELETE FROM friends WHERE uid1='" + this.currentUID + "'AND uid2= '" + rsUsr.getString("uid") + "'";
            stmt.executeUpdate(unfllwSql);
            System.out.println("Friend removed successfully.");
        } else {
            System.out.println("User with Given Username Does Not Exist.");
        }
        rsUsr.close();
    }
}
