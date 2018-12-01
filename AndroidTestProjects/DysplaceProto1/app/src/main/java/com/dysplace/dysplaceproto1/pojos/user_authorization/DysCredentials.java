package com.dysplace.dysplaceproto1.pojos.user_authorization;

public class DysCredentials {

    private String userID;
    private String password;

    public DysCredentials(String userID, String password){
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
