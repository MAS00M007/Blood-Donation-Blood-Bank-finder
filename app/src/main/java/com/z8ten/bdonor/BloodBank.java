package com.z8ten.bdonor;

import java.util.List;

public class BloodBank {
    private String name;
    private String city;
    private List<String> availableBloodTypes;
    private String phone;

    public BloodBank() {}

    public BloodBank(String name, String city, List<String> availableBloodTypes, String phone) {
        this.name = name;
        this.city = city;
        this.availableBloodTypes = availableBloodTypes;
        this.phone = phone;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public List<String> getAvailableBloodTypes() { return availableBloodTypes; }
    public void setAvailableBloodTypes(List<String> availableBloodTypes) { this.availableBloodTypes = availableBloodTypes; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
