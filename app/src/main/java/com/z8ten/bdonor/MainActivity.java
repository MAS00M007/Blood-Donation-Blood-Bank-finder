package com.z8ten.bdonor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private Button findDonorsButton, becomeDonorButton, findBloodBanksButton, addbloodbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        logoImageView = findViewById(R.id.logoImageView);
        findDonorsButton = findViewById(R.id.findDonorsButton);
        becomeDonorButton = findViewById(R.id.becomeDonorButton);
        findBloodBanksButton = findViewById(R.id.findBloodBanksButton);
        addbloodbank = findViewById(R.id.addBloodBanksButton);




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

    }

}