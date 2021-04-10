package com.cswala.cswala.Activities;

public class User {

    private String Name, Email, Phone, Gender, Dob, Image;

    public User(){

    }

    public User(String email){

        this.email =email;

    }
    
     public User(String image, String name, String email, String phone, String gender, String dob) {
        Name = name;
        Email = email;
        Phone = phone;
        Gender = gender;
        Dob = dob;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
