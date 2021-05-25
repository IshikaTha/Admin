package com.ishika.adminapp.Gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ishika.adminapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class UploadPhotoActivity extends AppCompatActivity {

    private ImageView imgPre;
    private final int req = 1;
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    String downUrl = "";
    String icategory;

    private Spinner imageCategory;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Photos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("Gallery");

        CardView image = findViewById(R.id.addImage);
        Button upImage = findViewById(R.id.upPhoto);
        imgPre = findViewById(R.id.photoPre);
        imageCategory = findViewById(R.id.imageCategory);

        String[] events = new String[]{"Select Event", "Convocation", "SIH", "ICM ICPC", "Industrial Visit", "Independence Day", "Republic Day", "Others"};

        imageCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, events));
        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                icategory = imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        progressDialog = new ProgressDialog(this);

        image.setOnClickListener(v -> chooseFromGallery());

        upImage.setOnClickListener(v -> {
            if(bitmap == null){
                Toast.makeText(UploadPhotoActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
            }else if(icategory.equals("Select Event")){
                Toast.makeText(UploadPhotoActivity.this, "Please select image category", Toast.LENGTH_SHORT).show();
            } else{
                progressDialog.setMessage("Uploading...");
                progressDialog.show();
                uploadPhoto();
            }
        });
    }

    private void uploadPhoto(){
        progressDialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();

        final StorageReference storagePath;
        storagePath = storageReference.child(finalImg + "jpg");

        final UploadTask uploadTask = storagePath.putBytes(finalImg);

        uploadTask.addOnCompleteListener(UploadPhotoActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    uploadPhotoTitle();
                    progressDialog.dismiss();
                    Toast.makeText(UploadPhotoActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();

                })).addOnFailureListener(e -> Toast.makeText(UploadPhotoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
            }else {
                progressDialog.dismiss();
                Toast.makeText(UploadPhotoActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPhotoTitle() {
        final String uniqueKey = databaseReference.push().getKey();
        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MM-yy");
        String date = currentdate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh-mm a");
        String time = currentTime.format(calTime.getTime());

        GalleryData galleryData = new GalleryData(icategory, date, downUrl, time, uniqueKey);

        assert uniqueKey != null;
        databaseReference.child(icategory).child(uniqueKey).setValue(galleryData).addOnSuccessListener(aVoid -> {
            progressDialog.dismiss();
            Intent intent = new Intent(UploadPhotoActivity.this, PhotosActivity.class);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(UploadPhotoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        });
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == req && resultCode == RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgPre.setImageBitmap(bitmap);
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}