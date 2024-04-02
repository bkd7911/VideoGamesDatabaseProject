import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class Login {
    public static void createUser(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = BCrypt.hashpw(scanner.nextLine(), BCrypt.gensalt(10));
        System.out.print("Enter first name: ");
        String first_name = scanner.nextLine();
        System.out.print("Enter last name: ");
        String last_name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String creationDate = new Date().toString();
        String lastAccessDate = new Date().toString();


        String sql = "INSERT INTO users (username, first_name, last_name, creation_date, last_access_date, password) " +
                "VALUES ('" + username + "', '" + first_name + "', '" + last_name + "', '" + creationDate + "', '" + lastAccessDate + "', '" + password + "')";
        stmt.executeUpdate(sql);
        String getUidSql = "SELECT uid FROM users WHERE username='" + username + "'";
        stmt.executeQuery(getUidSql);
        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            String emailSql = "INSERT INTO emails (email, uid) VALUES ('" + email + "', '" + rs.getString("uid") + "')";
            stmt.executeUpdate(emailSql);
        }
        System.out.println("Account created successfully.");
        rs.close();
    }

    public static boolean loginUser(Statement stmt, Scanner scanner) throws SQLException {
        boolean loggedIn = false;

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String sql = "SELECT * FROM users WHERE username='" + username + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
            System.out.println("Login successful. Welcome, " + username + "!");
            Main.currentUID = String.valueOf(rs.getInt("uid"));
            loggedIn = true;

        } else {
            System.out.println("Invalid username or password.");
        }
        rs.close();
        return loggedIn;
    }
}
