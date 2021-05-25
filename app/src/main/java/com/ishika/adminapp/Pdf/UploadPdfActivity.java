package com.ishika.adminapp.Pdf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ishika.adminapp.R;

import java.io.File;
import java.util.Objects;

public class UploadPdfActivity extends AppCompatActivity {
    private EditText pTitle;
    private TextView pdfText;
    private Spinner yearCategory, streamCategory;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri pdfData;

    ProgressDialog progressDialog;
    private final int req = 1;

    private String ycategory, scategory;

    private String pdfName, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload e-Books");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pdf");
        databaseReference.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference();

        MaterialCardView addPdf = findViewById(R.id.addPdf);
        pTitle = findViewById(R.id.pTitle);
        yearCategory = findViewById(R.id.yearCategory);
        streamCategory = findViewById(R.id.streamCategory);
        Button uploadP = findViewById(R.id.upPdf);

        pdfText = findViewById(R.id.pdfText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Uploading...");

        String[] years = new String[]{"Select Course", "BTech 1st year", "BTech 2nd year", "BTech 3rd year", "BTech 4th year"};

        yearCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years));
        yearCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ycategory = yearCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] streams = new String[]{"Select Category", "CSE", "IT", "EE", "EIE", "CA", "ECE", "FT"};

        streamCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, streams));
        streamCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scategory = streamCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addPdf.setOnClickListener(v -> choseFromPdf());

        uploadP.setOnClickListener(v -> {
            title = pTitle.getText().toString();

            if(title.isEmpty()){
                pTitle.setError("Title missing");
                pTitle.requestFocus();
            }else if(pdfData == null){
                Toast.makeText(UploadPdfActivity.this, "Please upload pdf", Toast.LENGTH_SHORT).show();
            }else if(ycategory.equals("Select Category")){
                Toast.makeText(UploadPdfActivity.this, "Please select course you are opting for", Toast.LENGTH_SHORT).show();
            }else if(scategory.equals("Select Category")){
                Toast.makeText(UploadPdfActivity.this, "Please provide departmental category", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadPdf();
            }
        });
    }

    private void uploadPdf() {
        progressDialog.show();
         StorageReference filePath = storageReference.child("PDF").child(ycategory).child(scategory + "-" +  pdfName + "-" + System.currentTimeMillis() + ".pdf");

        filePath.putFile(pdfData).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri uri = uriTask.getResult();
            uploadData(String.valueOf(uri));
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(UploadPdfActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadData(String downUrl) {
        final String uniqueKey = databaseReference.child(ycategory).child(scategory).push().getKey();
        PdfData data = new PdfData(pdfName, title, uniqueKey, downUrl);
        assert uniqueKey != null;
        databaseReference.child(ycategory).child(scategory).child(uniqueKey).setValue(data).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            Toast.makeText(UploadPdfActivity.this, "Pdf uploaded successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UploadPdfActivity.this, PdfActivity.class);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(UploadPdfActivity.this, "Failed to upload the pdf", Toast.LENGTH_SHORT).show();
        });
    }

    private void choseFromPdf() {
        Intent intent = new Intent();
        intent.setType("pdf/docs/ppt");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf file"), req);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // compare the resultCode with the SELECT_PICTURE constant
        if(requestCode == req && resultCode == RESULT_OK){
            assert data != null;
            pdfData = data.getData();
            if(pdfData.toString().startsWith("content://")){
                Cursor cursor;
                try {
                    cursor = UploadPdfActivity.this.getContentResolver().query(pdfData, null, null, null, null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            pdfText.setText(pdfName);
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}