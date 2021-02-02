package com.lotpick.lotpick.Models;


import com.google.firebase.firestore.DocumentId;

public class Modelclass  {

    @DocumentId
    private String Id;
    private String Title;
    private String Rating;
    private String Location;
    private String Price;
    private String Image;
    private String Description;
    private String Uid;
    private String UserName;

    private String Listing_part,No_of_MeetingRooms,No_of_Seats
            ,Shared_Office,openoffice,wifi,pantry,vendingmachine,printer ,smthgelse;


    private Modelclass(){

    }
    public Modelclass(String Title, String Rating, String Price,
                      String Location, String Image, String Description
                      , String Id, String Uid, String userName, String Listing_part,
                      String No_of_MeetingRooms, String No_of_Seats,
                      String Shared_Office, String openoffice, String wifi,
                      String pantry, String vendingmachine, String printer,
                      String smthgelse){
        this.Title= Title;
        this.Rating = Rating;
        this.Location = Location;
        this.Price = Price;
        this.Image =Image;
        this.Description =Description;
        this.Id = Id;
        this.Uid = Uid;
        this.UserName = userName;
        this.Listing_part = Listing_part;
        this.No_of_MeetingRooms = No_of_MeetingRooms;
        this.No_of_Seats = No_of_Seats;
        this.Shared_Office = Shared_Office;
        this.openoffice = openoffice;
        this.wifi = wifi;
        this.pantry = pantry;
        this.vendingmachine = vendingmachine;
        this.printer = printer;
        this.smthgelse = smthgelse;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }



    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }


    public String getDescription() {
        return Description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getListing_part() {
        return Listing_part;
    }

    public void setListing_part(String Listing_part) {
        this.Listing_part = Listing_part;
    }

    public String getNo_of_MeetingRooms() {
        return No_of_MeetingRooms;
    }

    public void setNo_of_MeetingRooms(String No_of_MeetingRooms) {
        this.No_of_MeetingRooms = No_of_MeetingRooms;
    }

    public String getNo_of_Seats() {
        return No_of_Seats;
    }

    public void setNo_of_Seats(String No_of_Seats) {
        this.No_of_Seats = No_of_Seats;
    }

    public String getShared_Office() {
        return Shared_Office;
    }

    public void setShared_Office(String Shared_Office) {
       this.Shared_Office = Shared_Office;
    }

    public String getOpenoffice() {
        return openoffice;
    }

    public void setOpenoffice(String openoffice) {
        this.openoffice = openoffice;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getPantry() {
        return pantry;
    }

    public void setPantry(String pantry) {
        this.pantry = pantry;
    }

    public String getVendingmachine() {
        return vendingmachine;
    }

    public void setVendingmachine(String vendingmachine) {
        this.vendingmachine = vendingmachine;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getSmthgelse() {
        return smthgelse;
    }

    public void setSmthgelse(String smthgelse) {
        this.smthgelse = smthgelse;
    }
}
