import java.sql.*;
import java.util.*;
public class VideoGames {
    Statement stmt;
    Scanner scanner;

    private int getInput(String ques){
        System.out.print(ques);
        int option = scanner.nextInt();
        return option;
    } 
    public int VideoGameMenu(Statement stmt,  Scanner scanner){
        try{
            int inp = -1;
            this.stmt = stmt;
            boolean cont = true;
            this.scanner = scanner;
            
            while(cont){
                System.out.println("""
                    \n--Select action to continue--
                        1. View as Player  
                        2. View as Develepor/Publiser 
                        3. View collections
                        4. Return to main menu
                    """);
                inp = getInput("Choose an option: ");
                switch(inp){
                    case 1: inp = PlayerView();
                    case 2: inp = DPView();
                    case 3: return 1;
                    case 4: return 0;
                }
            }
        }catch(Exception e){
            System.out.println("THe following ERROR occured in VideoGame -->"+e);
            return 2;
        }
        return 0;
    }

    public int PlayerView() throws SQLException{
        int inp = -1;
        ResultSet res;
        while(inp!=5){
            System.out.println("""
                \n--Select action to continue--
                    1. Search games by name  
                    2. Search games by rating
                    3. Search games by genre
                    4. Play Game
                    5. Rate Game
                    6. Return to main menu
                 """);
            inp = getInput("Choose an option: ");
            switch(inp){
                case 1:
                    System.out.print("Enter name of game (partial or full): ");
                    String title = scanner.nextLine();
                    title = scanner.nextLine();
                    res = stmt.executeQuery("SELECT * FROM videogame WHERE title LIKE '%"+title+"%';");
                    
                    System.out.println("The following games match the name : "+ title);
                    while(res.next()){
                        String pString = "\tUID: " + res.getInt("vgid");
                        pString += "\tTitle: '" + res.getString("title");
                        pString += "'\tESRB-Rating: " + res.getString("esrb_rating");
                        System.out.println(pString);
                    }
                    System.out.println("\n");
                    res.close();
                    break;
                case 2:
                    System.out.print("Enter Rating to seach by( E/E10+/T/M/AO): ");
                    String rat = scanner.nextLine();
                    rat = scanner.nextLine();
                    System.out.println("The following are rated : "+ rat);
                    
                    res = stmt.executeQuery("SELECT * FROM videogame WHERE esrb_rating ='"+rat+"';");
                    while(res.next()){
                        String pString = "\tUID: " + res.getInt("vgid");
                        pString += "\tTitle: '" + res.getString("title");
                        pString += "'\tESRB-Rating: " + res.getString("esrb_rating");
                        System.out.println(pString);
                    }
                    System.out.println("\n");
                    res.close();
                    break;
                case 3:
                    res = stmt.executeQuery("SELECT * FROM genre;");
                    System.out.println("\nThe following genres are avalible:");
                    while(res.next()){
                        System.out.println("\t" + res.getString("gid")+":"+res.getString("name"));
                    }
                    res.close();
                    System.out.print("Enter Id of Genre to seach by: ");
                    int gid = scanner.nextInt();
                    System.out.println("The following games belong to the genre of "+ gid);
                    
                    res = stmt.executeQuery("""
                        SELECT videogame.vgid, videogame.title, videogame.esrb_rating FROM videogame
                        JOIN video_game_genre
                        ON videogame.vgid = video_game_genre.vgid
                        JOIN genre
                        ON genre.gid = video_game_genre.gid;""");
                    while(res.next()){
                        System.out.println("\t-->" + res.getString("vgid")+":"+res.getString("title"));
                    }
                    res.close();
                    break;
                case 4:
                    
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
        }
        return 0;
    }

    public int DPView(){
        System.out.println("""
                --Select action to continue--
                    1. Search games by name  
                    2. Search games by rating
                    3. TODO: Search games by genre
                    4. Play Game
                    5. Return to main menu
                 """);

            switch(getInput("")){
                case 1:
             
            }
        return 0;
    }


}
