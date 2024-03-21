import com.jcraft.jsch.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
public class Postgress{

    int lport = 5432;
    String rhost = "starbug.cs.rit.edu";
    int rport = 5432;
    String user;
    String password;
    String databaseName;
            Connection conn = null;
        Session session = null;
    public Postgress(@Value("${CS_USERNAME}") String user, @Value("${CS_PASSWORD}") String password, @Value("${DB_NAME}") String databaseName){
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
    }
    public static void main(String[] args) throws SQLException {

    }
}
