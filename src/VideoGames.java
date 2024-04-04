import java.sql.*;
import java.util.*;
import java.util.Date;
public class VideoGames {
    Statement stmt;
    Scanner scanner;
    String currentUID = "";

    private int getInput(String ques){
        System.out.print(ques);
        int option = noMisInput(scanner);
        return option;
    }
    public int noMisInput(Scanner scanner){
        try{
            return scanner.nextInt();
        }catch(Exception e){
            System.out.println("Invalid Input, try again: ");
            return -1;
        }
    }
    // Class Constructor
    public VideoGames(Statement stmt,  Scanner scanner, String currentUID){
        this.stmt = stmt;
            this.scanner = scanner;
            this.currentUID = currentUID;
    }
    
    // Main VideoGameMenu Functionality
    public int VideoGameMenu(){
        try{
            int inp = -1;
            while(true){
                System.out.println("""
                    \n--Select action to continue--
                        1. Search for Games
                        2. <New Thing>
                        3. Play / Rate Game
                        4. View collections
                        5. Return to main menu
                    """);
                inp = getInput("Choose an option: ");
                switch(inp){
                    case 1: inp = PlayerSearchView();
                            if(inp == 1){return 0;}
                            break;
                    case 2: 
                            break;

                    case 3: inp = PlayerView(currentUID); break;
                    case 4: return 3;
                    case 5: 
                            return 0;
                }
            }
        }catch(Exception e){
            System.out.println("The following ERROR occured in VideoGame -->"+e);
            return 2;
        }
    }
    
    // Menu Functionality to play and rate games
    public int PlayerView(String currentUID) throws SQLException{
        int inp = -1;
        ResultSet res;
        while(inp!=5){
            System.out.println("""
                \n--Select action to continue--
                    1. Play Game
                    2. Rate Game
                    3. Return to Video Game menu
                 """);
            inp = getInput("Choose an option: ");
            switch(inp){

                case 1:
                    System.out.print("Enter ID of game: (Enter 0 for a random game) ");
                    String query = scanner.nextLine();
                    query = scanner.nextLine();
                    int vgid = Integer.parseInt(query);
                    if(vgid == 0){
                        res = stmt.executeQuery("SELECT * FROM videogame ORDER BY RANDOM() LIMIT 1;");
                        if(res.next()){
                            vgid = res.getInt("vgid");
                        }
                    }
                    res = stmt.executeQuery("SELECT * FROM videogame WHERE vgid ="+vgid+";");
                    if (res.next()){
                        Date sessionStart = new Date();
                        System.out.println("How long did you play the game for? (in minutes)");
                        query = scanner.nextLine();
                        int sessionLength = Integer.parseInt(query);
                        // Calculate session end time based on the start time and length of session
                        Date sessionEnd = new Date(sessionStart.getTime() +( sessionLength * 60000L));

                        stmt.executeUpdate("INSERT INTO session (uid, vgid, sessionstart, sessionend) VALUES ("+currentUID+","+vgid+",'"+sessionStart+"','"+sessionEnd+"');");
                    }
                    else {
                        System.out.println("Invalid game ID");
                    }
                    break;
                case 2:
                    System.out.print("Enter ID of game: ");
                    String ratingVGID = scanner.nextLine();
                    ratingVGID = scanner.nextLine();
                    int ratingVGIDNum = Integer.parseInt(ratingVGID);
                    res = stmt.executeQuery("SELECT * FROM videogame WHERE vgid ="+ratingVGIDNum+";");
                    if (res.next()){
                        System.out.print("Enter Rating of game: ");
                        ratingVGID = scanner.nextLine();
                        int rating = Integer.parseInt(ratingVGID);
                        stmt.executeUpdate("INSERT INTO video_game_rating (uid,vgid, rating) VALUES ("+currentUID+","+ratingVGIDNum+", "+rating+");");
                    }
                    else {
                        System.out.println("Invalid game ID");
                    }
                    break;
                case 3:
                    return 0;
            }
        }
        return 0;
    }
    
    // Menu Functionality to search and sort games
    public int PlayerSearchView() throws SQLException{
        int inp = -1;
        String where = "";
        while(inp!=7){
            System.out.println("""
            \n--Select action to continue--
                1. Search games by Name  
                2. Search games by ESRB_rating
                3. Search games by Genre
                4. Search games by Platform
                5. Search games by Release Date
                6. Search games by Developers
                7. Search games by Price
                8. Return to VideoGame Menu
                9. Return to Main Menu
            """);
            inp = getInput("Choose an option: ");
            switch(inp){
                case 1:
                    System.out.print("Enter name of game (partial or full): ");
                    String title = scanner.nextLine();title = scanner.nextLine();
                    where = "WHERE videogame.title LIKE '%"+title+"%'";
                    System.out.println("The following games match the name : "+ title);
                    searchAndSortGame(stmt, where);
                    break;
                case 2:
                    System.out.print("Enter Rating to seach by( E/E10+/T/M/AO): ");
                    String rat = scanner.nextLine();rat = scanner.nextLine();
                    where = "WHERE videogame.esrb_rating = '"+rat+"'";
                    System.out.println("The following are rated : "+ rat);
                    searchAndSortGame(stmt, where);
                    break;
                case 3:
                    System.out.print("Enter Genre Name to search by: ");
                    String genre = scanner.nextLine();genre = scanner.nextLine();
                    where = "WHERE genre.name LIKE '%"+genre+"%'";
                    System.out.println("The following games have the genre matching: "+ genre);
                    searchAndSortGame(stmt,where);
                    break;
                case 4:
                    System.out.print("Enter Platform to seach for: ");
                    String plat = scanner.nextLine();plat = scanner.nextLine();
                    where = "WHERE platforms.name ='"+plat+"'";
                    System.out.println("The following are playable on the platform : "+ plat );
                    searchAndSortGame(stmt, where);
                    break;
                case 5:                    
                    System.out.print("Enter year of release: ");int year = noMisInput(scanner);
                    System.out.print("Enter month of release: ");int month = noMisInput(scanner);
                    System.out.print("Enter date of release: ");int date = noMisInput(scanner);
                    String dateVal=dateFormat(year, month, date);

                    System.out.println("""
                        \n--Select Date Filteration Type--
                            1. Released BEFORE Date
                            2. Released AFTER  Date
                            3. Released  ON    Date
                        \nFilteration Type: """);
                    int choi = noMisInput(scanner);
                    where = "WHERE release.release_date ='"+dateVal+"'";
                    switch (choi) {
                        case 1:
                            where = "WHERE release.release_date <='"+dateVal+"'";
                            break;
                        case 2:
                            where = "WHERE release.release_date >='"+dateVal+"'";
                            break;
                    }
                    System.out.println("The following are filtered games : ");
                    searchAndSortGame(stmt, where);
                    break;
                case 6:
                    System.out.print("Enter Develepor name to seach by: ");
                    String dev = scanner.nextLine();dev = scanner.nextLine();
                    if(dev.length()==0)dev = "ScipityScapady";
                    where = "WHERE devpub.name ='%"+dev+"%'";
                    System.out.println("The following games are made by : "+ dev);
                    searchAndSortGame(stmt, where);
                    break;
                case 7:
                    System.out.print("Enter price: ");float price = scanner.nextFloat();
                    System.out.println("""
                        \n--Select Date Filteration Type--
                            1. Price is less than (inclusive)
                            2. Price is greater than (inclusive)
                            3. Price is exactly
                        \nFilteration Type: """);
                    choi = noMisInput(scanner);
                    where = "WHERE release.curr_price ="+price;
                    switch (choi) {
                        case 1:
                            where = "WHERE release.curr_price <='"+price+"'";
                            break;
                        case 2:
                            where = "WHERE release.curr_price >='"+price+"'";
                            break;
                    }
                    System.out.println("The following are filtered games : ");
                    searchAndSortGame(stmt, where);
                    break;
                case 8:
                    return 0;
                case 9:
                    return 1;
            }
        }
        return 0;
    }

    // Formats Playtime
    private String dateFormat(int y, int m, int d){
        String retVal = "";
        String ys = Integer.toString(y);
        String ms = Integer.toString(m);
        String ds = Integer.toString(d);
        if(ys.length()<2){ys="0"+ys;}
        if(ms.length()<2){ms="0"+ms;}
        if(ds.length()<2){ds="0"+ds;}
        retVal += ys+ms+ds;
        return retVal+" 00:00:00";
    }
    
    // Formats publishers/platforms
    private String none_ify(String in){
        if(in.length()==4)
            return " None ";
        else
            return " "+in;
    }
    
    // Formats Playtime and Ratings
    private String cut_rat(String in, int ad){
        try{
            in = " ("+in.substring(0,4+ad)+")";     
        }catch(Exception e){
            return " (-)";
        }
        return in;
    }
    
    // Prints games search resultset
    private void printResultSet(ResultSet res) throws SQLException{
        while(res.next()){
                String pString = "\n\t-->) Title: '" + res.getString("title");
                pString += "'  Platforms:" + none_ify(res.getString("platforms"));
                pString += "  Devs/Pubs:"+ none_ify(res.getString("devpubs"));
                pString += "  Playtime:" + cut_rat(res.getString("playtime"),1);
                pString += "  Rating:" + cut_rat(res.getString("rating"),0);
                System.out.println(pString);
        }
    }
    
    // Joins tables and queries it to print results based on given conditions
    private void searchAndSortGame(Statement stmt, String where )throws SQLException{
        try {
            stmt.executeUpdate("DROP TABLE tempSortTable"+currentUID+";");
        }catch (Exception e) {
            System.out.println("\n");
        }
        String queryString = "SELECT * FROM tempSortTable"+currentUID+";";
        String updateString = """
            SELECT 
                title,
                array_agg( distinct concat(release.curr_price)) priceS,
                array_agg( distinct concat(genre.name) ) genreS  ,
                array_agg( distinct concat(release.release_date )) dateS,
                array_agg( distinct concat(platforms.name )) platforms,
                array_agg( distinct concat(devpub.name )) devpubs,

                SUM(play_video_game.sessionend - play_video_game.sessionstart) playtime,
                AVG(video_game_rating.rating) rating

            INTO tempSortTable"""+
            currentUID
            +
            """
            
            FROM videogame
                LEFT JOIN release ON release.vgid = videogame.vgid
                LEFT JOIN platforms ON platforms.pid = release.pid
                LEFT JOIN published ON videogame.vgid = published.vgid
                LEFT JOIN devpub ON published.dpid = devpub.dpid
                LEFT JOIN play_video_game ON videogame.vgid = play_video_game.vgid
                LEFT JOIN video_game_rating ON videogame.vgid = video_game_rating.vgid
                LEFT JOIN video_game_genre ON videogame.vgid = video_game_genre.vgid
                LEFT JOIN genre ON genre.gid = video_game_genre.gid
            """
            +where
            +" GROUP BY title "
            +" ORDER BY title ASC, dateS ASC;";
        
        stmt.executeUpdate(updateString);
        ResultSet res = stmt.executeQuery(queryString);
        printResultSet(res);
        res.close();
        int inp = getInput("""
            Would You Like to sort this table?
            1. Yes
            2. No

            Choose an option( numerical ) : """);
        
        if(inp==1){
            PlayerSortView(stmt);
        }
        stmt.executeUpdate(" DROP table tempSortTable"+currentUID+";");
        System.out.println("\n");
    }

    public int PlayerSortView(Statement stmt) throws SQLException{
        System.out.println("""
        \n--Select category to sort by--
            1. Title
            2. Price
            3. Genre
            4. Release Date
            5. Exit Sort Menu
        """);
        int inp = getInput("Choose an option: "); 
        String sortQuery = "SELECT * FROM tempSortTable"+currentUID;
            
        switch (inp) {
            case 1:
                sortQuery += " ORDER BY title ";
                break;
            case 2:
                sortQuery += " ORDER BY priceS ";
                break;

            case 3:
                sortQuery += " ORDER BY genreS ";
                break;
            case 4:
                sortQuery += " ORDER BY dateS ";
                break;  
            case 5:
                System.out.println("Okie Dokie, Exiting Menu");
                return 1;
            default:
                System.out.println("Invalid Input! ");
                return 1;
        }
        System.out.println("""
        \n--Would you like to sort by--
            1. ASC
            2. DESC
        """);
        inp = getInput("Choose sort option: ");  
        switch (inp) {
            case 1:
                sortQuery += "ASC; ";
                break;
            case 2:
                sortQuery += "DESC; ";
                break;
            default:
                sortQuery += "ASC; ";
                System.out.println("Invalid input, using default method ASC");
                break;
        }
        ResultSet res = stmt.executeQuery(sortQuery);  
        
        
        printResultSet(res);
        return 0;
    }


}
