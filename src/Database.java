import com.jcraft.jsch.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    Connection conn;
    public Database() {
         Properties secrets = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("secrets.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            secrets.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int lport = 54362;
        String rhost = "starbug.cs.rit.edu";
        int rport = 5432;
        String user = secrets.getProperty("username"); //change to your username
        String password = secrets.getProperty("password"); //change to your password
        String databaseName = "p320_32"; //change to your database name

        String driverName = "org.postgresql.Driver";
        this.conn = null;
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "127.0.0.1", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://127.0.0.1:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            this.conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");
            // Do something with the database....

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public static void main(String[] args) throws SQLException, IOException {

    }

}