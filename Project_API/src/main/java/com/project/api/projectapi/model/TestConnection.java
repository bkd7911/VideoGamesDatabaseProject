package com.project.api.projectapi.model;


public class TestConnection {
    private int testId;
    private String testName;
    private String testDate;

    public TestConnection( int testId, String testName, String testDate){
        this.testId = testId;
        this.testName= testName;
        this.testDate = testDate;
    }
    
    public int getTestId(){
        return this.testId;
    }
    public String getTestName(){
        return this.testName;
    }
    public String getTestDate(){
        return this.testDate;
    }

}
