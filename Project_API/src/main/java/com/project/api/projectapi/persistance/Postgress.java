package com.project.api.projectapi.persistance;

import com.jcraft.jsch.*;
import com.project.api.projectapi.model.Template;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
public class Postgress{

    int lport = 5432;
    int rport = 5432;
    String user;
    String password;
    String databaseName;
    Statement stat = null;
    Connection conn = null;
    Session session = null;
    String rhost = "starbug.cs.rit.edu";
    String driverName = "org.postgresql.Driver";

    public Postgress(@Value("${CS_USERNAME}") String user, @Value("${CS_PASSWORD}") String password, @Value("${DB_NAME}") String databaseName){
        try{
            this.user = user;
            this.password = password;
            this.databaseName = databaseName;
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            this.session = jsch.getSession(user, rhost, 22);
            this.session.setPassword(password);
            this.session.setConfig(config);
            this.session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            this.session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "127.0.0.1", rport);
            System.out.println("Port Forwarded");

            String url = "jdbc:postgresql://127.0.0.1:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            this.stat = conn.createStatement();
            System.out.println("Database connection established");
        }catch (Exception e) {
            System.out.println("ERROR at Postgress constructor setup --> " + e);
        }
    }
    public Connection getConnection(){
        return this.conn;
    }
    /*
    public boolean setQuery(String command){
        try {
            int i = this.stat.executeUpdate(command);
            if(i<1){return false;}
            System.out.println("Command run : "+command);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR While executing setQuery : --> "+ e);
        }
        return false;
    }
    public ResultSet getQuery(String query){
        try {
            ResultSet load = this.stat.executeQuery(query);
            return load;
        } catch (Exception e) {
            System.out.println("ERROR While getting resultset from database --> \n" + e);
        }
        return null;
    }
    */
}
