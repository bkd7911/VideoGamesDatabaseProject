import java.sql.*;
import java.util.*;

public class Main {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/your_database";
    static final String USER = "username";
    static final String PASS = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
        if (rs.next()) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid username or password.");
        }
        rs.close();
    }
}
