package com.ishika.adminapp.Email;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ishika.adminapp.MainActivity;
import com.ishika.adminapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView name, email, pass;
    Button btnRegister;
    ProgressBar progressBar;
    private ImageView pImg;
    private final int req = 26;
    private Bitmap bitmap = null;
    StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String downUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.pass);
        pImg = findViewById(R.id.pImg);
        btnRegister = findViewById(R.id.registerbtn);
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        pImg.setOnClickListener(v -> chooseFromGallery());

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String Email = email.getText().toString().trim();
        String Password = pass.getText().toString().trim();
        String FullName = name.getText().toString().trim();

        if(FullName.isEmpty()){
            name.setError("Full name is required");
            name.requestFocus();
            return;
        }

        if(Email.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            pass.setError("Please set password");
            pass.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter valid email");
            email.requestFocus();
        }

        if(Password.length()<6){
            pass.setError("Min password length is 6");
            pass.requestFocus();
        }else if(bitmap != null){
            progressDialog.show();
            uploadImage();
        }else {
            progressDialog.show();
            register(FullName, Email, Password);
        }
    }
    private void register(String FullName, String Email, String Password){
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Admin admin = new Admin(FullName, Email, downUrl);
                FirebaseDatabase.getInstance().getReference("Admins")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .setValue(admin).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to register, account already exists with this email id", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
            } else {
                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child("Admins").child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(RegisterActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    String Email = email.getText().toString().trim();
                    String Password = pass.getText().toString().trim();
                    String FullName = name.getText().toString().trim();
                    register(FullName, Email, Password);
                }));
            }else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
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
            pImg.setImageBitmap(bitmap);
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}