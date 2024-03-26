package com.project.api.projectapi.controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.api.projectapi.model.TestConnection;
import com.project.api.projectapi.persistance.Postgress;


@RestController
@RequestMapping("test")
public class ConnectionTestController {
    private  Postgress posty;
    private  Connection conn = null;
    private static final Logger LOG = Logger.getLogger(ConnectionTestController.class.getName());

    public ConnectionTestController(Postgress posty) {
        this.posty = posty;
        this.conn = this.posty.getConnection();
    }

    @GetMapping("")
    public ResponseEntity<TestConnection[]>getAll( ){
        LOG.info("\nGET /test");
        try {
            String query = "SELECT * FROM testy;";
            Statement stat = conn.createStatement();
            stat = conn.createStatement();
            ResultSet load = stat.executeQuery(query);
            // or ResultSet res = getQuery(String query) can substitue the 3 lines;
            ArrayList<TestConnection> arr = new ArrayList<TestConnection>();
            TestConnection Obj;
            while(load.next()){
                Obj=new TestConnection( load.getInt("testid"),
                                        load.getString( "testname"),
                                        load.getString( "testdate")
                                    );
                arr.add(Obj);
            }
            System.out.println(arr.size());
            TestConnection[] retVal = new TestConnection[arr.size()];
            int index = -1;
            for(TestConnection i: arr){
                index++;
                retVal[index]=i;
            }
            
            return new ResponseEntity<TestConnection[]>(retVal,HttpStatus.OK);
        }catch (Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
