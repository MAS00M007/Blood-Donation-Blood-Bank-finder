package com.z8ten.bdonor;

import com.google.firebase.Timestamp;

public class BloodRequest {
    private String donorName;
    private String bloodType;
    private String hospital;
    private String contactNumber;
    private Timestamp timestamp;  // Add this field


    public BloodRequest() {
        // Required empty constructor for Firestore
    }

    public BloodRequest(String donorName, String bloodType, String hospital, String contactNumber,Timestamp timestamp) {
        this.donorName = donorName;
        this.bloodType = bloodType;
        this.hospital = hospital;
        this.contactNumber = contactNumber;
        this.timestamp = timestamp;
    }

    // Getters and Setters for each field
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
