package com.ishika.adminapp.Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OpenPhotoActivity extends AppCompatActivity {

    private String uniqueKey;
    private String event;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_photo);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PhotoView photo = findViewById(R.id.photo);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        uniqueKey = getIntent().getStringExtra("key");
        String gImage = getIntent().getStringExtra("photo");
        event = getIntent().getStringExtra("event");

        Picasso.get().load(gImage).into(photo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.up_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OpenPhotoActivity.this);
        builder.setMessage("Are you sure, you want to delete this photo?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> databaseReference.child(event).child(uniqueKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OpenPhotoActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(OpenPhotoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show()));

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
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}