package com.z8ten.bdonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT =2000 ;
    private TextView spText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        spText=findViewById(R.id.sp_text);

        String text = "<font color='#000000'>Give</font> <font color='#FF0000'>Blood</font><br/><font color='#000000'>Save</font> <font color='#0000FF'>Life</font>";
        spText.setText(Html.fromHtml(text));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkUserLoginStatus();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkUserLoginStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(splash_screen.this, MainActivity.class));
        } else {
            startActivity(new Intent(splash_screen.this, login.class));
        }

        finish();
    }
}
