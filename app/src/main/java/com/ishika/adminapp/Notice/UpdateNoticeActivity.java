package com.ishika.adminapp.Notice;

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

public class UpdateNoticeActivity extends AppCompatActivity {
    private ImageView updateNotImage;
    private EditText updateNTitle;

    private ProgressDialog progressDialog;

    private String uniqueKey;
    private final int req = 20;
    Bitmap bitmap = null;

    private String nImage, nTitle;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String downUrl ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);

        updateNotImage = findViewById(R.id.updateNotImage);
        updateNTitle = findViewById(R.id.updateNTitle);
        Button updateN = findViewById(R.id.updateN);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Notice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        databaseReference.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference();

        uniqueKey = getIntent().getStringExtra("key");
        nImage = getIntent().getStringExtra("image");
        nTitle = getIntent().getStringExtra("title");
        try {
            Picasso.get().load(nImage).into(updateNotImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateNTitle.setText(nTitle);

        updateNotImage.setOnClickListener(v -> chooseFromGallery());

        updateN.setOnClickListener(v -> {
            nTitle = updateNTitle.getText().toString();
            validate();
        });
    }

    private void validate() {
        if(nTitle.isEmpty()) {
            updateNTitle.setError("Empty");
            updateNTitle.requestFocus();
        }
        else if(bitmap == null){
            //Toast.makeText(this, "Profile Photo not set", Toast.LENGTH_SHORT).show();
            updateData(nImage);
        }else {
            uploadImage();
        }
    }

    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child("Gallery").child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(UpdateNoticeActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    updateData(downUrl);
                }));
            }else {
                progressDialog.dismiss();
                Toast.makeText(UpdateNoticeActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateData(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("image", s);
        hashMap.put("title", nTitle);

        databaseReference.child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(o -> {
            Toast.makeText(UpdateNoticeActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), NoticesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(UpdateNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == req && resultCode == RESULT_OK){

            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            updateNotImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.up_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete){
                delete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateNoticeActivity.this);
        builder.setMessage("Are you sure, you want to delete this notice?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> databaseReference.child(uniqueKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateNoticeActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NoticesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(UpdateNoticeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show()));

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