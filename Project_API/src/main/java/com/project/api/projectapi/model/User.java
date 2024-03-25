///////////////////////////////////////////////////////////////////////////////////////////////////////
package com.project.api.projectapi.model;
//  FILE : User.java
//  AUTHOR : Pranav Sehgal, Cristian Malone
//  DESCRIPTION: User model, implemented to represent user 
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.sql.Date;


public class User {

    public static int nextUID;
    int uid;
    String userName;
    String firstName;
    String lastName;
    Date creation;
    Date lastAccessDate;

    public User(String userName, String firstName, String lastName, Date creation, Date lastAccessDate) {
        this.uid = nextUID++;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creation = creation;
        this.lastAccessDate = lastAccessDate;
    }



}
