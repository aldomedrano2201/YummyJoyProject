package model;

import java.util.ArrayList;

public class Client {

    private String phoneNumber;
    private String name;
    private String email;
    private String userType;

    public Client(String phoneNumber, String name, String email) {

        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.userType = "client";
    }




    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
