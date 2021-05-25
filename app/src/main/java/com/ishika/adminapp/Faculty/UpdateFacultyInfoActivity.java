package com.ishika.adminapp.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class UpdateFacultyInfoActivity extends AppCompatActivity {

    private ImageView updateFacImage;
    private EditText updateFName, updateFEmail, updateFPost, updateFPhone;
    private ProgressDialog progressDialog;

    private String uniqueKey;
    private final int req = 20;
    Bitmap bitmap = null;

    private String fImage, fName, fEmail, fPost, fPhone;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String downUrl = "";
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty_info);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Faculties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        fPhone = getIntent().getStringExtra("phone");

        fImage = getIntent().getStringExtra("image");
        fName = getIntent().getStringExtra("name");
        fEmail = getIntent().getStringExtra("email");
        fPost = getIntent().getStringExtra("post");

        updateFacImage = findViewById(R.id.updateFacImage);
        updateFName = findViewById(R.id.updateFName);
        updateFEmail = findViewById(R.id.updateFEmail);
        updateFPhone = findViewById(R.id.updateFPhone);
        updateFPost = findViewById(R.id.updateFPost);
        Button updateF = findViewById(R.id.updateF);

        try {
            Picasso.get().load(fImage).into(updateFacImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateFName.setText(fName);
        updateFEmail.setText(fEmail);
        updateFPhone.setText(fPhone);
        updateFPost.setText(fPost);
        updateFacImage.setOnClickListener(v -> chooseFromGallery());

        updateF.setOnClickListener(v -> {
            fName = updateFName.getText().toString();
            fEmail = updateFEmail.getText().toString();
            fPhone = updateFPhone.getText().toString();
            fPost = updateFPost.getText().toString();
            validate();
        });
    }

    private void validate() {
        if (fName.isEmpty()) {
            updateFName.setError("Empty");
            updateFName.requestFocus();
        } else if (fEmail.isEmpty()) {
            updateFEmail.setError("Empty");
            updateFEmail.requestFocus();
        } else if (fPhone.isEmpty()) {
            updateFPhone.setError("Empty");
            updateFPhone.requestFocus();
        }
        else if (fPost.isEmpty()) {
            updateFPost.setError("Empty");
            updateFPost.requestFocus();
        } else if (bitmap == null) {
            updateData(fImage);
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child("Faculty").child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(UpdateFacultyInfoActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    updateData(downUrl);
                }));
            } else {
                progressDialog.dismiss();
                Toast.makeText(UpdateFacultyInfoActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("image", s);
        hashMap.put("name", fName);
        hashMap.put("email", fEmail);
        hashMap.put("phone", fPhone);
        hashMap.put("post", fPost);

        databaseReference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(o -> {
            Toast.makeText(UpdateFacultyInfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(UpdateFacultyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req && resultCode == RESULT_OK) {

            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateFacImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.up_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateFacultyInfoActivity.this);
        builder.setMessage("Are you sure, you want to delete this faculty?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> databaseReference.child(category).child(uniqueKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateFacultyInfoActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(UpdateFacultyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show()));

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = null;
        try {
            alertDialog = builder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (alertDialog != null)
            alertDialog.show();
    }
}