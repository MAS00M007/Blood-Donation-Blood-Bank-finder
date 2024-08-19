package com.z8ten.bdonor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FindDonors extends AppCompatActivity {

    private AutoCompleteTextView bloodTypeAutoComplete;
    private MaterialButton searchButton;
    private RecyclerView donorsRecyclerView;
    private DonorAdapter donorAdapter;
    private FirebaseFirestore db;
    private EditText cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donors);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        bloodTypeAutoComplete = findViewById(R.id.bloodTypeAutoComplete);
        searchButton = findViewById(R.id.searchButton);
        donorsRecyclerView = findViewById(R.id.donorsRecyclerView);
        cityName = findViewById(R.id.cityEditText);



        // Set up blood type dropdown
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodTypes);
        bloodTypeAutoComplete.setAdapter(adapter);

        // Set up RecyclerView
        donorAdapter = new DonorAdapter(new ArrayList<>());
        donorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorsRecyclerView.setAdapter(donorAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBloodType = bloodTypeAutoComplete.getText().toString();
                String selectedCity = cityName.getText().toString().trim();
                if (selectedBloodType.isEmpty()) {
                    Toast.makeText(FindDonors.this, "Please select a blood type", Toast.LENGTH_SHORT).show();
                } else {
                    searchDonors(selectedBloodType, selectedCity);
                }
            }
        });
    }

    private void searchDonors(String bloodType, String city) {

        Query query = db.collection("donors")
                .whereEqualTo("bloodType", bloodType);
        if (!city.isEmpty()) {
            query = query.whereEqualTo("city", city);
        }

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Donor> donors = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donor donor = document.toObject(Donor.class);
                            donors.add(donor);
                        }
                        donorAdapter.updateDonors(donors);
                        if (donors.isEmpty()) {
                            Toast.makeText(FindDonors.this, "No donors found for this blood type and city", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FindDonors.this, "Error searching donors: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}