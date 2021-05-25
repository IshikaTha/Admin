package com.ishika.adminapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ishika.adminapp.Email.LoginActivity;
import com.ishika.adminapp.Email.ProfileActivity;
import com.ishika.adminapp.Faculty.FacultyActivity;
import com.ishika.adminapp.Gallery.GalleryActivity;
import com.ishika.adminapp.Notice.NoticesActivity;
import com.ishika.adminapp.Pdf.PdfActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    MaterialCardView notice, faculty, pdf, photo;
    FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int checkedItem;
    private String selected;
    private final String CHECKEDITEM ="checked_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("themes", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        switch (getCheckedItem()){
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();

        notice = findViewById(R.id.notice);
        faculty = findViewById(R.id.faculty);
        pdf = findViewById(R.id.addPdf);
        photo = findViewById(R.id.photo);

        if(!isConnected(MainActivity.this)){
            buildDialog(MainActivity.this).show();
        }

        notice.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoticesActivity.class);
            startActivity(intent);
        });

        photo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });


        faculty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
            startActivity(intent);
        });


        pdf.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PdfActivity.class);
            startActivity(intent);
        });
    }

    public boolean isConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mobile != null && mobile.isConnectedOrConnecting() || wifi != null && wifi.isConnectedOrConnecting();
        }else
            return false;
    }


    public AlertDialog.Builder buildDialog(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You are not connected to internet");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> finish());
        builder.setNegativeButton("Cancel", (dialog, which) -> builder.setCancelable(true));
        return builder;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        actionBarDrawerToggle.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navigation_settings)
                startActivity(new Intent(this, ProfileActivity.class));
        if(item.getItemId() ==  R.id.navigation_log_out)
            LogOut();
        if(item.getItemId() ==  R.id.navigation_color)
                showDialog();
        return true;
    }

    private void LogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, you want to log out?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = null;
        try {
            alertDialog = builder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(alertDialog != null)
            alertDialog.show();
    }

    private void showDialog() {
        String[] themes = this.getResources().getStringArray(R.array.theme);
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Select Theme");
        materialAlertDialogBuilder.setSingleChoiceItems(R.array.theme, getCheckedItem(), (dialog, which) -> {
            selected = themes[which];
            checkedItem = which;
        });
        materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            if(selected == null){
                selected = themes[which];
                checkedItem = which;
            }
            switch (checkedItem){
                case 0:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case 1:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case 2:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
            setCheckedItem(checkedItem);
        });
        materialAlertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = materialAlertDialogBuilder.create();
        alertDialog.show();
    }

    private int getCheckedItem(){
        return sharedPreferences.getInt(CHECKEDITEM,0);
    }

    private void setCheckedItem(int i){
        editor.putInt(CHECKEDITEM, i);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }
}