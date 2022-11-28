package model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Provider {

    private String phoneNumber;
    private String name;
    private String email;
    private String postalCode;
    private String address;
    @Nullable
    private ArrayList<Item> ItemsPublished;
    private String userType;
    private String itemLabel;

    public Provider(String phoneNumber, String name, String email,
                    String postalCode, String address, ArrayList<Item> itemsPublished) {

        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.postalCode = postalCode;
        this.address = address;
        this.ItemsPublished = itemsPublished;
        this.userType = "provider";
        this.itemLabel = "item";
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public ArrayList<Item> getItemsPublished() {
        return ItemsPublished;
    }

    public void setItemsPublished(ArrayList<Item> itemsPublished) {
        ItemsPublished = itemsPublished;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }
}
