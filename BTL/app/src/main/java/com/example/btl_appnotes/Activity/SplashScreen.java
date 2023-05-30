package com.example.btl_appnotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_appnotes.MainActivity;
import com.example.btl_appnotes.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        },2000);
    }
}