package com.z8ten.bdonor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.Timestamp;

public class MainActivity extends AppCompatActivity {

    //private ImageView logoImageView;
    private Button findDonorsButton, becomeDonorButton, findBloodBanksButton, addbloodbank;
    private MaterialButton addrequestbtn;
    private RecyclerView recentRequestsRecyclerView;
    private BloodRequestAdapter bloodRequestAdapter; // Your adapter class
    private List<BloodRequest> bloodRequestList; // List to hold fetched data
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        //logoImageView = findViewById(R.id.logoImageView);
        findDonorsButton = findViewById(R.id.findDonorsButton);
        becomeDonorButton = findViewById(R.id.becomeDonorButton);
        findBloodBanksButton = findViewById(R.id.findBloodBanksButton);
        addbloodbank = findViewById(R.id.addBloodBanksButton);
        addrequestbtn = findViewById(R.id.add_request_fab);


        db = FirebaseFirestore.getInstance();
        bloodRequestList = new ArrayList<>();
        recentRequestsRecyclerView = findViewById(R.id.recentRequestsRecyclerView); // Your RecyclerView ID
        bloodRequestAdapter = new BloodRequestAdapter(this, bloodRequestList); // Pass the list to your adapter
        recentRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentRequestsRecyclerView.setAdapter(bloodRequestAdapter);

        // Fetch blood requests from Firestore
        fetchBloodRequests();
        createNotificationChannel();


        addbloodbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addbloodbankActivity.class));
            }
        });

        findDonorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FindDonors.class));
            }
        });

        becomeDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, doner_user.class));
            }
        });

        findBloodBanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, findbloodbank.class));
            }
        });

        addrequestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialogView = inflater.inflate(R.layout.addrequestdialoglayout, null);

                // Find views from the dialog layout
                TextInputEditText donorNameEdit = dialogView.findViewById(R.id.donorNameEdit);
                Spinner donorBloodTypeSpinner = dialogView.findViewById(R.id.donorBloodTypeSpinner);
                TextInputEditText donorHospitalEdit = dialogView.findViewById(R.id.donorHospitalEdit);
                TextInputEditText donorContactNumberEdit = dialogView.findViewById(R.id.donorContactNumberEdit);

                // Populate the blood type spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.blood_groups, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                donorBloodTypeSpinner.setAdapter(adapter);

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView)
                        .setTitle("Add Blood Request")
                        .setPositiveButton("Submit", (dialog, which) -> {

                            // Handle submission logic
                            String donorName = donorNameEdit.getText().toString();
                            String bloodType = donorBloodTypeSpinner.getSelectedItem().toString();
                            String hospital = donorHospitalEdit.getText().toString();
                            String contactNumber = donorContactNumberEdit.getText().toString();
                            if (donorName.isEmpty() || bloodType.isEmpty() || hospital.isEmpty() || contactNumber.isEmpty()) {
                                Toast.makeText(MainActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                // Creating a blood request object
                                BloodRequest bloodRequest = new BloodRequest(donorName, bloodType, hospital, contactNumber,Timestamp.now());


                                db.collection("bloodRequests")
                                        .add(bloodRequest)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(MainActivity.this, "Request added successfully", Toast.LENGTH_SHORT).show();
                                            sendNotification(bloodType);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MainActivity.this, "Error adding request", Toast.LENGTH_SHORT).show();
                                        });
                            }
                            // Save request or do other logic here
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                // Show the dialog
                builder.create().show();
            }
        });

    }

    private void fetchBloodRequests() {
        db.collection("bloodRequests")
                .orderBy("timestamp", Query.Direction.DESCENDING)// Replace with your collection name
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(MainActivity.this, "Error fetching data.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        bloodRequestList.clear(); // Clear the list to avoid duplicates
                        for (QueryDocumentSnapshot doc : value) {
                            BloodRequest bloodRequest = doc.toObject(BloodRequest.class);
                            bloodRequestList.add(bloodRequest);
                        }
                        bloodRequestAdapter.notifyDataSetChanged(); // Notify adapter about data change
                    }
                });
    }

    private void sendNotification(String bloodType) {
        // Create custom notification layout
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        notificationLayout.setTextViewText(R.id.notification_title, "Urgent: Blood Request");
        notificationLayout.setTextViewText(R.id.notification_body, "Blood needed: " + bloodType);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "bloodRequestChannel")
                .setSmallIcon(R.drawable.blood_donor_logo)
                .setCustomContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return; // Return to avoid crashing without permission
        }

        notificationManager.notify(0, builder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BloodRequestChannel";
            String description = "Channel for Blood Requests";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("bloodRequestChannel", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

}


