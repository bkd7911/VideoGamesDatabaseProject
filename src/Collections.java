import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Collections {

    String currentUID;

    public void collectionsMenu(Statement stmt, Scanner scanner, String currentUID) throws SQLException {

        this.currentUID = currentUID;

        while (true) {
            System.out.println("\n--Select Collections Action--");
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


    private void createCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter New Collection Name: ");
        String collectionName = scanner.nextLine();
        String sql = "INSERT INTO collections (uid, name) VALUES ('" + this.currentUID + "', '" + collectionName + "')";
        stmt.executeUpdate(sql);
        System.out.println("Collection created successfully.");
    }

    private void modifyCollectionName(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter Collection Name To Modify: ");
        String currCollectionName = scanner.nextLine();
        System.out.print("Enter New Collection Name: ");
        String newCollectionName = scanner.nextLine();
        String sql = "UPDATE collections SET name='" + newCollectionName + "' WHERE uid='" + this.currentUID + "' AND name='" + currCollectionName + "'";
        stmt.executeUpdate(sql);
        System.out.println("Collection created successfully.");
    }

    private void deleteCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter Name of Collection To Delete: ");
        String collectionName = scanner.nextLine();
        String sql = "DELETE FROM collections WHERE uid='" + this.currentUID + "' AND name='" + collectionName + "'";
        stmt.executeUpdate(sql);
        System.out.println("Collection deleted successfully.");
    }

    private void addVideoGameToCollection(Statement stmt, Scanner scanner) throws SQLException {

        System.out.print("\nEnter Name of Collection To Add To: ");
        String collectionName = scanner.nextLine();
        String sql = "SELECT cid FROM collections WHERE uid='" + this.currentUID + "' AND name='" + collectionName + "'";
        ResultSet rsCol = stmt.executeQuery(sql);

        if (rsCol.next()) {
            int cid = rsCol.getInt("cid");

            System.out.print("Enter Title of Video Game To Add: ");
            String vgName = scanner.nextLine();
            String vgSql = "SELECT vgid FROM videogame WHERE title='" + vgName + "'";
            ResultSet rsVG = stmt.executeQuery(vgSql);
            if (rsVG.next()) {

                int vgid = rsVG.getInt("vgid");

                String chkSql = "SELECT * FROM user_platform WHERE uid='" + this.currentUID + "' AND pid IN \n" +
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

    private void deleteVideoGameFromCollection(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("\nEnter Name of Collection To Delete From: ");
        String collectionName = scanner.nextLine();
        String sql = "SELECT cid FROM collections WHERE uid='" + this.currentUID + "' AND name='" + collectionName + "'";
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

    private void viewCollections(Statement stmt) throws SQLException {
        System.out.println("\n--All Collections--");
        String sql = "SELECT c.name, COUNT(vgid) as numVideoGames,\n" +
                "       SUM((DATE_PART('day', sessionend - sessionstart) * 24) + DATE_PART('hour', sessionend - sessionstart)) as hourDiff,\n" +
                "       SUM(DATE_PART('minute', sessionend - sessionstart)) as minuteDiff\n" +
                "FROM\n" +
                "    (SELECT name, cid FROM collections WHERE uid = '" + this.currentUID + "') AS c LEFT JOIN\n" +
                "    (SELECT c.name, c.uid, c.cid, vgc.vgid, s.sessionstart, s.sessionend\n" +
                "     FROM collections AS c, video_game_collection AS vgc, session AS s\n" +
                "     WHERE c.uid = s.uid AND c.cid = vgc.cid AND vgc.vgid = s.vgid) AS cv ON c.cid = cv.cid\n" +
                "GROUP BY c.cid, c.name\n" +
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
