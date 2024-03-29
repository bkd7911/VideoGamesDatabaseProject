import java.sql.*;
import java.util.*;
public class VideoGames {
    Statement stmt;
    Scanner scanner;
    
    ArrayList<String> dirArr = new ArrayList<String>();
    ArrayList<String> sortArr = new ArrayList<String>();

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
    
    public int VideoGameMenu(Statement stmt,  Scanner scanner, String currentUID){
        try{
            int inp = -1;
            this.stmt = stmt;
            this.scanner = scanner;
            
            while(true){
                System.out.println("""
                    \n--Select action to continue--
                        1. Search for Games
                        2. Edit Sort Functionality
                        3. Play / Rate Game
                        4. View collections
                        5. Return to main menu
                    """);
                inp = getInput("Choose an option: ");
                switch(inp){
                    case 1: inp = PlayerSearchView();
                            if(inp == 1){return 0;}
                            break;
                    case 2: inp = PlayerSortView();
                            if(inp == 1){return 0;}
                            break;

                    case 3: inp = PlayerView(currentUID); break;
                    case 4: return 3;
                    case 5: sortArr.clear();
                            dirArr.clear();
                            return 0;
                }
            }
        }catch(Exception e){
            System.out.println("The following ERROR occured in VideoGame -->"+e);
            return 2;
        }
    }
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
    public int PlayerSearchView() throws SQLException{
        int inp = -1;
        String where = "";
        String order = getOrderString();
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
                    DisplayGame(stmt,"", where, order);
                    break;
                case 2:
                    System.out.print("Enter Rating to seach by( E/E10+/T/M/AO): ");
                    String rat = scanner.nextLine();rat = scanner.nextLine();
                    where = "WHERE videogame.esrb_rating = '"+rat+"'";
                    System.out.println("The following are rated : "+ rat);
                    DisplayGame(stmt, "",where, order);
                    break;
                case 3:
                    System.out.print("Enter Genre Name to search by: ");
                    String genre = scanner.nextLine();genre = scanner.nextLine();
                    where = "WHERE genre.name LIKE '%"+genre+"%'";
                    String additional_join = "";
                    System.out.println("The following games have the genre matching: "+ genre);
                    DisplayGame(stmt, additional_join,where, order);
                    break;
                case 4:
                    System.out.print("Enter Platform to seach for: ");
                    String plat = scanner.nextLine();plat = scanner.nextLine();
                    where = "WHERE platforms.name ='"+plat+"'";
                    System.out.println("The following are playable on the platform : "+ plat );
                    DisplayGame(stmt, "",where, order);
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
                    DisplayGame(stmt, "",where, order);
                    break;
                case 6:
                    System.out.print("Enter Develepor name to seach by: ");
                    String dev = scanner.nextLine();dev = scanner.nextLine();
                    if(dev.length()==0)dev = "ScipityScapady";
                    where = "WHERE devpub.name ='%"+dev+"%'";
                    System.out.println("The following games are made by : "+ dev);
                    DisplayGame(stmt, "",where, order);
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
                    DisplayGame(stmt, "",where, order);
                    break;
                case 8:
                    return 0;
                case 9:
                    return 1;
            }
        }
        return 0;
    }

    //Helps stop my eyes from bleeding
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
    //Helps stop my eyes from bleeding
    private String none_ify(String in){
        if(in.length()==4)
            return " None ";
        else
            return " "+in;
    }

    //Helps stop my eyes from bleeding
    private String cut_rat(String in, int ad){
        try{
            in = " ("+in.substring(0,4+ad)+")";     
        }catch(Exception e){
            return " (-)";
        }
        return in;
    }
    // Joins tables and queries it to print results based on given conditions
    private void DisplayGame(Statement stmt, String additional_join,String where, String order)throws SQLException{
        ResultSet res = stmt.executeQuery("""
            SELECT title,
                array_agg( release.curr_price) priceS,
                array_agg( genre.name ) genreS  ,
                array_agg( distinct concat(release.release_date )) dateS,
                array_agg( distinct concat(platforms.name )) platforms,
                array_agg( distinct concat(devpub.name )) devpubs,
                
                SUM(session.sessionend - session.sessionstart) playtime,
                AVG(video_game_rating.rating) rating
                FROM videogame

                LEFT JOIN release ON release.vgid = videogame.vgid
                LEFT JOIN platforms ON platforms.pid = release.pid
                LEFT JOIN published ON videogame.vgid = published.vgid
                LEFT JOIN devpub ON published.dpid = devpub.dpid
                LEFT JOIN session ON videogame.vgid = session.vgid
                LEFT JOIN video_game_rating ON videogame.vgid = video_game_rating.vgid
                LEFT JOIN video_game_genre ON videogame.vgid = video_game_genre.vgid
                LEFT JOIN genre ON genre.gid = video_game_genre.gid
                """
                +additional_join
                +where
                +" GROUP BY title "
                +order+";");
        while(res.next()){
            String pString = "\n\t-->) Title: '" + res.getString("title");
            pString += "'  Platforms:" + none_ify(res.getString("platforms"));
            pString += "  Devs/Pubs:"+ none_ify(res.getString("devpubs"));
            pString += "  Playtime:" + cut_rat(res.getString("playtime"),1);
            pString += "  Rating:" + cut_rat(res.getString("rating"),0);
            System.out.println(pString);
        }
        res.close();
        System.out.println("\n");
    }
    
    private String getOrderString(){
        String retVal="ORDER BY ";
        boolean first = true;
        if(sortArr.size()>0){
            for(int i = 0; i<sortArr.size();i++){
                if(!first)retVal+=" , ";
                retVal+= sortArr.get(i);
                if(!sortArr.get(i).equals("title")){
                    retVal+="S ";
                }
                retVal+= " "+dirArr.get(i) + " ";
                first = false;
            }
        }

        if(!sortArr.contains("title")){
            if(!first)retVal+=" , ";
            retVal += "title ASC ";
        }
        if(!sortArr.contains("date")) retVal += ", date ASC ";
        System.out.println(retVal);
        return retVal;
    }

    public int PlayerSortView(){
        while(true){
            System.out.println("""
            \n--Select action to continue--
                1. View Current Sorting Priority
                2. Add Sorting Criterea
                3. Remove Sorting Criteria
                4. Return to Video Game Menu
                5. Return to Main Menu
            """);
            int lind = -1;
            Set<String> allCrit = new HashSet<String>();
            allCrit.add("title");
            allCrit.add("price");
            allCrit.add("genre");
            allCrit.add("date");
            int inp = getInput("Choose an option: ");
            if(inp==-1){return 0;}
            switch(inp){
                case 1:
                    System.out.println("Current Sorting Priority : ");
                    lind = 1;
                    for(int i = 0; i<sortArr.size(); i++){
                        System.out.println("\t("+lind+") Video Game "+sortArr.get(i) + " \t( "+dirArr.get(i)+" )");
                        lind++;
                    }
                    if(!sortArr.contains("title")) System.out.println("\t("+lind++ +") " + "Video Game Name  \t( ASC )");
                    if(!sortArr.contains("date")) System.out.println("\t("+lind+") " + "Video Game Date \t( ASC )");
                    break;
                case 2:
                    System.out.println("Addable Sorting Criteria: ");
                    lind = 1;
                    for(String i:allCrit){
                        System.out.println("\t("+lind+") " + i);
                        lind++;
                    }
                    System.out.println("Enter criteria(name) to add: ");
                    String critName = scanner.nextLine().toLowerCase();critName = scanner.nextLine().toLowerCase();
                    System.out.println("Order By ASC or DESC? : ");
                    String critOrd = scanner.nextLine().toUpperCase();scanner.nextLine().toUpperCase();

                    if( !allCrit.contains(critName)){
                        System.out.println("Invalid Criterea name");
                        break;
                    }else if( ! (critOrd.equals("ASC")||critOrd.equals("DESC"))){
                        System.out.println("Invalid Filter direction");
                        break;
                    }

                    if(sortArr.contains(critName)){
                        dirArr.remove(sortArr.indexOf(critName));
                        sortArr.remove(critName);
                    }
                    dirArr.add(0, critOrd);
                    sortArr.add(0,critName);
                    System.out.println("New Sorting Criterea Added !!");
                    break;
                case 3:
                    System.out.println("Current Removable Sorting Priority : ");
                    lind = 1;
                    for(int i = 0; i<sortArr.size(); i++){
                        System.out.println("\t("+lind+") "+sortArr.get(i) + " ( "+dirArr.get(i)+" )");
                        lind++;
                    }
                    System.out.println("Enter criteria(name) to remove: ");
                    String remCrit = scanner.nextLine().toLowerCase();remCrit = scanner.nextLine().toLowerCase();
                    if( !allCrit.contains(remCrit)){
                        System.out.println("Invalid Criterea name");
                        break;
                    }else if(!sortArr.contains(remCrit)){
                        System.out.println("Criteria not added yet");
                        break;
                    }
                    dirArr.remove(sortArr.indexOf(remCrit));
                    sortArr.remove(remCrit);
                    System.out.println("Criteria Removed !!");
                    break;
                case 4:
                    return 0;
                case 5:
                    return 1;
            }
        }
    }

}
