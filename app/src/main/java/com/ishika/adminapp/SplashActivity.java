package com.ishika.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ishika.adminapp.Email.LoginActivity;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Objects;


public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            thread.start();
        }else {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            thread.start();
        }

    }
}