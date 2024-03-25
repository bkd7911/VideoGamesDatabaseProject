///////////////////////////////////////////////////////////////////////////////////////////////////////    
package com.project.api.projectapi.model;

//  FILE : VideoGame.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is a template Model with a public model to encapsulate data
//               USE this as a template to create your own model 

///////////////////////////////////////////////////////////////////////////////////////////////////////
public class VideoGame {

    public static int nextVGID;

    int vgid;

    String title;

    String esrbRating;

    public VideoGame(String title, String esrbRating){
        this.vgid = nextVGID++;
        this.title = title;
        this.esrbRating = esrbRating;
    }

}
///////////////////////////////////////////////////////////////////////////////////////////////////////