package com.dysplace.awscloudlogic;

public class DysplaceCredentials {

    private String userID;
    private String password;

    public DysplaceCredentials(){}

    public DysplaceCredentials(String userID, String password){
        this.userID = userID;
        this.password = password;
    }

    // Accessors

    public String getUserID(){
        return this.userID;
    }

    public String getPassword(){
        return this.password;
    }

    // Modifiers

    public void setUserID(String userID){
        this.userID = userID;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
