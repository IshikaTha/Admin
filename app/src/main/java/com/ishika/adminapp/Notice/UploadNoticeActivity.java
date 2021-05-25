package com.ishika.adminapp.Notice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
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

public class UploadNoticeActivity extends AppCompatActivity {
    private  EditText nTitle;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    ProgressDialog progressDialog;
    private final int req = 1;
    private Bitmap bitmap;
    ImageView IVPreviewImage;

    String downUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Notice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        IVPreviewImage = findViewById(R.id.NoticePre);

        MaterialCardView addNotice = findViewById(R.id.addNotice);
        nTitle = findViewById(R.id.noticeT);
        Button uploadN = findViewById(R.id.upNotice);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait");
        progressDialog.setMessage("Uploading...");

        addNotice.setOnClickListener(v -> chooseFromGallery());


        uploadN.setOnClickListener(v -> {
            if(nTitle.getText().toString().isEmpty()){
                nTitle.setError("Empty");
                nTitle.requestFocus();
            }else if(bitmap == null){
                uploadNotice();
            }
            else {
                uploadImage();
            }
        });

    }

    private void uploadNotice() {
        final String uniqueKey = databaseReference.child("Notice").push().getKey();
        String title = nTitle.getText().toString();

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calTime.getTime());


        NoticeData noticeData = new NoticeData(title, date, downUrl, time, uniqueKey);

        assert uniqueKey != null;
        databaseReference.child("Notice").child(uniqueKey).setValue(noticeData).addOnSuccessListener(aVoid -> {
            progressDialog.dismiss();
            Intent intent = new Intent(UploadNoticeActivity.this, NoticesActivity.class);
            startActivity(intent);
            Toast.makeText(UploadNoticeActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(UploadNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });


    }

    private void uploadImage(){
        progressDialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child("Notice").child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(UploadNoticeActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    uploadNotice();
                }));
            }else {
                progressDialog.dismiss();
                Toast.makeText(UploadNoticeActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // compare the resultCode with the SELECT_PICTURE constant
        if(requestCode == req && resultCode == RESULT_OK){

            // Get the url of the image from data
            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            IVPreviewImage.setImageBitmap(bitmap);
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}