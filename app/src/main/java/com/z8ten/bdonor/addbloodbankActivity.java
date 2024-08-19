package com.z8ten.bdonor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addbloodbankActivity extends AppCompatActivity {

    private EditText bloodBankNameEditText;
    private EditText cityEditText;
    private EditText phoneEditText;
    private EditText bloodTypesEditText;
    private MaterialButton addBloodBankButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbloodbank);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        bloodBankNameEditText = findViewById(R.id.bloodBankNameEditText);
        cityEditText = findViewById(R.id.cityEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        bloodTypesEditText = findViewById(R.id.bloodTypesEditText);
        addBloodBankButton = findViewById(R.id.addBloodBankButton);

        addBloodBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBloodBank();
            }
        });
    }

    private void addBloodBank() {
        String name = bloodBankNameEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String bloodTypesInput = bloodTypesEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(city) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(bloodTypesInput)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert blood types from comma-separated string to a list
        List<String> bloodTypes = Arrays.asList(bloodTypesInput.split("\\s*,\\s*"));

        // Create a new blood bank object
        Map<String, Object> bloodBank = new HashMap<>();
        bloodBank.put("name", name);
        bloodBank.put("city", city);
        bloodBank.put("phone", phone);
        bloodBank.put("availableBloodTypes", bloodTypes);

        // Add the blood bank to Firestore
        db.collection("bloodBanks")
                .add(bloodBank)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(addbloodbankActivity.this, "Blood bank added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful addition
                })
                .addOnFailureListener(e -> Toast.makeText(addbloodbankActivity.this, "Error adding blood bank: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
