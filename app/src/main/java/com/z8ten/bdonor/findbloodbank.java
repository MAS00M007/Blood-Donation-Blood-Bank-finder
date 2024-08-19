package com.z8ten.bdonor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class findbloodbank extends AppCompatActivity {

    private AutoCompleteTextView bloodTypeAutoComplete2;
    private EditText cityEditText;
    private MaterialButton searchButton;
    private RecyclerView bloodBanksRecyclerView;
    private BloodBankAdapter bloodBankAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findbloodbank);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        bloodTypeAutoComplete2 = findViewById(R.id.bloodTypeAutoComplete2);
        cityEditText = findViewById(R.id.cityEditText);
        searchButton = findViewById(R.id.searchButton);
        bloodBanksRecyclerView = findViewById(R.id.bloodBanksRecyclerView);

        // Set up blood type dropdown
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodTypes);
        bloodTypeAutoComplete2.setAdapter(adapter);

        // Set up RecyclerView
        bloodBankAdapter = new BloodBankAdapter(new ArrayList<>());
        bloodBanksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodBanksRecyclerView.setAdapter(bloodBankAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBloodType = bloodTypeAutoComplete2.getText().toString();
                String selectedCity = cityEditText.getText().toString().trim();
                if (selectedBloodType.isEmpty()) {
                    Toast.makeText(findbloodbank.this, "Please select a blood type", Toast.LENGTH_SHORT).show();
                } else {
                    searchBloodBanks(selectedBloodType, selectedCity);
                }
            }
        });
    }

    private void searchBloodBanks(String bloodType, String city) {
        // Start building the query
        com.google.firebase.firestore.Query query = db.collection("bloodBanks")
                .whereArrayContains("availableBloodTypes", bloodType);

        // Add city filter only if city is not empty
        if (!city.isEmpty()) {
            query = query.whereEqualTo("city", city);
        }

        // Execute the query
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<BloodBank> bloodBanks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BloodBank bloodBank = document.toObject(BloodBank.class);
                            bloodBanks.add(bloodBank);
                        }
                        bloodBankAdapter.updateBloodBanks(bloodBanks);
                        if (bloodBanks.isEmpty()) {
                            Toast.makeText(findbloodbank.this, "No blood banks found for the given criteria", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(findbloodbank.this, "Error searching blood banks: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
