package com.z8ten.bdonor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class doner_user extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient ;

    private TextInputEditText nameEditText, ageEditText, phoneEditText, emailEditText, cityEditText;
    private AutoCompleteTextView bloodTypeAutoComplete;
    private MaterialCheckBox termsCheckBox;
    private MaterialButton submitButton;
    private final int LOCATION_REQUEST_CODE = 100;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_user);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        bloodTypeAutoComplete = findViewById(R.id.bloodTypeAutoComplete);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        cityEditText = findViewById(R.id.cityEditText);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        submitButton = findViewById(R.id.submitButton);

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        // Set up blood type dropdown
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodTypes);
        bloodTypeAutoComplete.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    saveDonorInfo(mAuth.getUid());
                }
            }
        });

        cityEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (cityEditText.getRight() - cityEditText.getCompoundDrawables()[2].getBounds().width())) {
                        getLocation();

                        //Toast.makeText(view.getContext(), "Drawable icon clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private boolean validateForm() {

        return !nameEditText.getText().toString().isEmpty() &&
                !ageEditText.getText().toString().isEmpty() &&
                !bloodTypeAutoComplete.getText().toString().isEmpty() &&
                !phoneEditText.getText().toString().isEmpty() &&
                !emailEditText.getText().toString().isEmpty() &&
                !cityEditText.getText().toString().isEmpty() &&
                termsCheckBox.isChecked();
    }


   private void getLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        else {
            fetchLocation();
        }
   }

    private void fetchLocation() {
fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
    @Override
    public void onComplete(@NonNull Task<Location> task) {
Location location=task.getResult();
if(location!=null){
    Geocoder geocoder=new Geocoder(doner_user.this, Locale.getDefault());
    try {
        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        if(addresses !=null && !addresses.isEmpty()){
            Address address=addresses.get(0);
            cityEditText.setText(address.getAddressLine(0));

        }
        else {
            Toast.makeText(doner_user.this, "Address not found", Toast.LENGTH_SHORT).show();
        }
    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(doner_user.this, "Unable to get address", Toast.LENGTH_SHORT).show();
    }
} else {
    Toast.makeText(doner_user.this, "Location not found", Toast.LENGTH_SHORT).show();
}

}

});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                fetchLocation();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDonorInfo(String userId) {
        Map<String, Object> donor = new HashMap<>();
        donor.put("name", nameEditText.getText().toString());
        donor.put("age", Integer.parseInt(ageEditText.getText().toString()));
        donor.put("bloodType", bloodTypeAutoComplete.getText().toString());
        donor.put("phone", phoneEditText.getText().toString());
        donor.put("email", emailEditText.getText().toString());
        donor.put("city", cityEditText.getText().toString());

        db.collection("donors").document(userId)
                .set(donor)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(doner_user.this, "Donor registered successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(doner_user.this, "Error registering donor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}