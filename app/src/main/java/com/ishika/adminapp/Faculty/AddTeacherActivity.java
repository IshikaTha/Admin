package com.ishika.adminapp.Faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
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
import java.util.Objects;

public class AddTeacherActivity extends AppCompatActivity {
    private ImageView teacherImg;
    private EditText fName, fEmail, fPost, fPhone;
    private Spinner addTeacherCategory;
    private final int req = 26;
    private Bitmap bitmap = null;
    private String name, category, email, phone, post, downUrl = "";

    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Faculty");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        teacherImg = findViewById(R.id.teacherImg);
        fName = findViewById(R.id.fName);
        fEmail = findViewById(R.id.fEmail);
        fPost = findViewById(R.id.fPost);
        fPhone = findViewById(R.id.fPhone);
        addTeacherCategory = findViewById(R.id.teacherCategory);
        Button addFac = findViewById(R.id.addFac);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);


        String[] items = new String[]{"Select Category", "Computer Science and Engineering", "Information Technology",
                "Electrical Engineering", "Electronics and Instrumentation Engineering",
                "Electronic and Communication Engineering", "Food Technology", "Applied Science and Humanities",
                "Computer Application"};

        addTeacherCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items));
        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        teacherImg.setOnClickListener(v -> chooseFromGallery());


        addFac.setOnClickListener(v -> validate());
    }

    private void validate() {
        name = fName.getText().toString();
        email = fEmail.getText().toString();
        post = fPost.getText().toString();
        phone = fPhone.getText().toString();

        if(name.isEmpty()){
            fName.setError("Empty");
            fName.requestFocus();
        }
        else if(email.isEmpty()){
            fEmail.setError("Empty");
            fEmail.requestFocus();
        }

        else if(phone.isEmpty()){
            fPhone.setError("Empty");
            fPhone.requestFocus();
        }
        else if(post.isEmpty()){
            fPost.setError("Empty");
            fPost.requestFocus();
        }
        else if(category.equals("Select Category")){
            Toast.makeText(this, "Please provide departmental category", Toast.LENGTH_SHORT).show();
        }else if(bitmap == null){
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            insertData();
        }else {
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            uploadImage();
        }
    }

    private void insertData() {
        final String uniqueKey = databaseReference.child(category).push().getKey();

        TeacherData teacherData = new TeacherData(name, email, phone, post, downUrl, uniqueKey);

        assert uniqueKey != null;
        databaseReference.child(category).child(uniqueKey).setValue(teacherData).addOnSuccessListener(aVoid -> {
            fName.setText("");
            fEmail.setText("");
            fPhone.setText("");
            fPost.setText("");
            progressDialog.dismiss();
            Toast.makeText(AddTeacherActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(AddTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImg = byteArrayOutputStream.toByteArray();
        final StorageReference storagePath1;
        storagePath1 = storageReference.child("Faculty").child(finalImg + "jpg");
        final UploadTask uploadTask = storagePath1.putBytes(finalImg);

        uploadTask.addOnCompleteListener(AddTeacherActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePath1.getDownloadUrl().addOnSuccessListener(uri -> {
                    downUrl = String.valueOf(uri);
                    insertData();
                }));
            }else {
                progressDialog.dismiss();
                Toast.makeText(AddTeacherActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
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
            teacherImg.setImageBitmap(bitmap);
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}