package com.z8ten.bdonor;

public class Donor {
    private String name;
    private String bloodType;
    private String phone;

    private String city;

    // Empty constructor needed for Firestore
    public Donor() {}

    public Donor(String name, String bloodType, String phone,String city) {
        this.name = name;
        this.bloodType = bloodType;
        this.phone = phone;
        this.city=city;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}