package com.example.prapri.firstproject;

/**
 * Created by prapri on 19-01-2018.
 */

public class User {
    private int UID;
    private String name;
    private String DOB;
    private String Email;
    private byte[] image;

    public User(){}

    public User(int UID, String name, String DOB, String Email, byte[] image) {
        super();
        this.name = name;
        this.DOB = DOB;
        this.image = image;
        this.UID = UID;
        this.Email = Email;
    }
    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

//    @Override
//    public String toString() {
//        return "User [id=" + UID + ", name=" + name + ", email=" + Email
//                + "]";
//    }

}