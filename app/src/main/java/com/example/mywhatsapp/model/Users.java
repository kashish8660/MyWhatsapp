package com.example.mywhatsapp.model;

public class Users {
    public Users(String profilePic, String userName, String mail, String password, String userId, String lastMessage) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }
    public Users(){} //Having an empty constructor makes thing more flexible, as we can just create an object of this class through this. And the later on can set only those variables which are required

    public Users( String userName, String mail, String password) { //signup constructor
        this.userName = userName;
        this.mail = mail;
        this.password = password;
    }



    String lastMessage;
    String profilePic;
    String userName;
    String mail;
    String password;
    String userId; //each user will have it's own unique id, like aadhar card

    public String getProfilePic() {
        return profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public String getMail() {
        return mail;
    }
    public String getPassword() {
        return password;
    }
    public String getUserId() {
        return userId;
    }
    public String getLastMessage() { return lastMessage;    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
