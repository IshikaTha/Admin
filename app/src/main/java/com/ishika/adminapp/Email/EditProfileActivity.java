package com.ishika.adminapp.Email;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName;
    private TextView editEmail;
    private ImageView editImage;
    private String name, email, image, userId;
    private final int req = 20;
    Bitmap bitmap = null;
    private String downUrl ="";

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private ProgressBar editProfileProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Objects.requireNonNull(getSupportActionBar()).hide();

        editImage = findViewById(R.id.editImg);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        Button update = findViewById(R.id.update);
        TextView resetPass = findViewById(R.id.resetPass);

        progressDialog = new ProgressDialog(this);
        editProfileProgress = findViewById(R.id.editProfileProgress);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Admins");

        FirebaseUser firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admins");

        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        editName.setText(name);
        editEmail.setText(email);
        try {
            Picasso.get().load(image).into(editImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editProfileProgress.setVisibility(View.GONE);

        editImage.setOnClickListener(v -> chooseFromGallery());

        editEmail.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangeEmailActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
        update.setOnClickListener(v -> {
            editProfileProgress.setVisibility(View.VISIBLE);
            name = editName.getText().toString();
            email = editEmail.getText().toString();
            if(name.isEmpty()){
                editName.setError("Please provide username");
                editName.requestFocus();
            }
            else if(bitmap == null){
                updateData(image);
            }else {
                uploadImage();
            }
        });
        resetPass.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ResetPassword.class)));
    }

    private void updateData(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("photo", s);
        hashMap.put("name", name);
        hashMap.put("email", email);

        databaseReference.child(userId).updateChildren(hashMap).addOnSuccessListener(o -> {
            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    updateData(downUrl);
                    editProfileProgress.setVisibility(View.GONE);
                    Toast.makeText(this, "Updated sucessfullly", Toast.LENGTH_SHORT).show();
                }));
            }else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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
        if(requestCode == req && resultCode == RESULT_OK){

            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            editImage.setImageBitmap(bitmap);
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}