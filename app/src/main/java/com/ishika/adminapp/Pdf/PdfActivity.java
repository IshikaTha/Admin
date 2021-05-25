package com.ishika.adminapp.Pdf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.ishika.adminapp.R;

import java.util.Objects;

public class PdfActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton pFab;
    MaterialTextView cse4, cse3, cse2, cse1, it4, it3, it2, it1, ee4, ee3, ee2, ee1, eie4, eie3, eie2, eie1, ece4, ece3, ece2, ece1, ft4, ft3, ft2, ft1, b4, b3, b2, b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Objects.requireNonNull(getSupportActionBar()).setTitle("e-books");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pFab = findViewById(R.id.pFab);

        cse4 = findViewById(R.id.cse4);
        cse3 = findViewById(R.id.cse3);
        cse2 = findViewById(R.id.cse2);
        cse1 = findViewById(R.id.cse1);

        it4 = findViewById(R.id.it4);
        it3 = findViewById(R.id.it3);
        it2 = findViewById(R.id.it2);
        it1 = findViewById(R.id.it1);

        ee4 = findViewById(R.id.ee4);
        ee3 = findViewById(R.id.ee3);
        ee2 = findViewById(R.id.ee2);
        ee1 = findViewById(R.id.ee1);

        eie4 = findViewById(R.id.eie4);
        eie3 = findViewById(R.id.eie3);
        eie2 = findViewById(R.id.eie2);
        eie1 = findViewById(R.id.eie1);


        ece4 = findViewById(R.id.ece4);
        ece3 = findViewById(R.id.ece3);
        ece2 = findViewById(R.id.ece2);
        ece1 = findViewById(R.id.ece1);

        ft4 = findViewById(R.id.ft4);
        ft3 = findViewById(R.id.ft3);
        ft2 = findViewById(R.id.ft2);
        ft1 = findViewById(R.id.ft1);

        b4 = findViewById(R.id.b4);
        b3 = findViewById(R.id.b3);
        b2 = findViewById(R.id.b2);
        b1 = findViewById(R.id.b1);

        pFab.setOnClickListener(this);

        cse4.setOnClickListener(this);
        cse3.setOnClickListener(this);
        cse2.setOnClickListener(this);
        cse1.setOnClickListener(this);

        it4.setOnClickListener(this);
        it3.setOnClickListener(this);
        it2.setOnClickListener(this);
        it1.setOnClickListener(this);

        ee4.setOnClickListener(this);
        ee3.setOnClickListener(this);
        ee2.setOnClickListener(this);
        ee1.setOnClickListener(this);

        eie4.setOnClickListener(this);
        eie3.setOnClickListener(this);
        eie2.setOnClickListener(this);
        eie1.setOnClickListener(this);

        ece4.setOnClickListener(this);
        ece3.setOnClickListener(this);
        ece2.setOnClickListener(this);
        ece1.setOnClickListener(this);

        ft4.setOnClickListener(this);
        ft3.setOnClickListener(this);
        ft2.setOnClickListener(this);
        ft1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.pFab) {
            Intent intent = new Intent(getApplicationContext(), UploadPdfActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.cse4) {
            Intent intent1 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent1.putExtra("batch", "BTech 4th year");
            intent1.putExtra("stream", "CSE");
            startActivity(intent1);
        }
        if(v.getId() == R.id.cse3) {
            Intent intent2 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent2.putExtra("batch", "BTech 3rd year");
            intent2.putExtra("stream", "CSE");
            startActivity(intent2);
        }
        if(v.getId() == R.id.cse2) {
            Intent intent3 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent3.putExtra("batch", "BTech 2nd year");
            intent3.putExtra("stream", "CSE");
            startActivity(intent3);
        }
        if(v.getId() == R.id.cse1) {
            Intent intent4 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent4.putExtra("batch", "BTech 1st year");
            intent4.putExtra("stream", "CSE");
            startActivity(intent4);
        }

        if(v.getId() == R.id.it4) {
            Intent intent5 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent5.putExtra("batch", "BTech 4th year");
            intent5.putExtra("stream", "IT");
            startActivity(intent5);
        }
        if(v.getId() == R.id.it3) {
            Intent intent6 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent6.putExtra("batch", "BTech 3rd year");
            intent6.putExtra("stream", "IT");
            startActivity(intent6);
        }
        if(v.getId() == R.id.it2) {
            Intent intent7 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent7.putExtra("batch", "BTech 2nd year");
            intent7.putExtra("stream", "IT");
            startActivity(intent7);
        }
        if(v.getId() == R.id.it1) {
            Intent intent8 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent8.putExtra("batch", "BTech 1st year");
            intent8.putExtra("stream", "IT");
            startActivity(intent8);
        }
        if(v.getId() == R.id.ee4) {
            Intent intent9 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent9.putExtra("batch", "BTech 4th year");
            intent9.putExtra("stream", "EE");
            startActivity(intent9);
        }
        if(v.getId() == R.id.ee3) {
            Intent intent10 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent10.putExtra("batch", "BTech 3rd year");
            intent10.putExtra("stream", "EE");
            startActivity(intent10);
        }
        if(v.getId() == R.id.ee2) {
            Intent intent11 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent11.putExtra("batch", "BTech 2nd year");
            intent11.putExtra("stream", "EE");
            startActivity(intent11);
        }
        if(v.getId() == R.id.ee1) {
            Intent intent12 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent12.putExtra("batch", "BTech 1st year");
            intent12.putExtra("stream", "EE");
            startActivity(intent12);
        }
        if(v.getId() == R.id.eie4) {
            Intent intent13 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent13.putExtra("batch", "BTech 4th year");
            intent13.putExtra("stream", "EIE");
            startActivity(intent13);
        }
        if(v.getId() == R.id.eie3) {
            Intent intent14 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent14.putExtra("batch", "BTech 3rd year");
            intent14.putExtra("stream", "EIE");
            startActivity(intent14);
        }
        if(v.getId() == R.id.eie2) {
            Intent intent15 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent15.putExtra("batch", "BTech 2nd year");
            intent15.putExtra("stream", "EIE");
            startActivity(intent15);
        }
        if(v.getId() == R.id.eie1) {
            Intent intent16 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent16.putExtra("batch", "BTech 1st year");
            intent16.putExtra("stream", "EIE");
            startActivity(intent16);
        }

        if(v.getId() == R.id.ece4) {
            Intent intent21 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent21.putExtra("batch", "BTech 4th year");
            intent21.putExtra("stream", "ECE");
            startActivity(intent21);
        }
        if(v.getId() == R.id.ece3) {
            Intent intent22 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent22.putExtra("batch", "BTech 3rd year");
            intent22.putExtra("stream", "ECE");
            startActivity(intent22);
        }
        if(v.getId() == R.id.ece2) {
            Intent intent23 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent23.putExtra("batch", "BTech 2nd year");
            intent23.putExtra("stream", "ECE");
            startActivity(intent23);
        }
        if(v.getId() == R.id.ece1) {
            Intent intent24 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent24.putExtra("batch", "BTech 1st year");
            intent24.putExtra("stream", "ECE");
            startActivity(intent24);
        }
        if(v.getId() == R.id.ft4) {
            Intent intent25 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent25.putExtra("batch", "BTech 4th year");
            intent25.putExtra("stream", "FT");
            startActivity(intent25);
        }
        if(v.getId() == R.id.ft3) {
            Intent intent26 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent26.putExtra("batch", "BTech 3rd year");
            intent26.putExtra("stream", "FT");
            startActivity(intent26);
        }
        if(v.getId() == R.id.ft2) {
            Intent intent27 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent27.putExtra("batch", "BTech 2nd year");
            intent27.putExtra("stream", "FT");
            startActivity(intent27);
        }
        if(v.getId() == R.id.ft1) {
            Intent intent28 = new Intent(getApplicationContext(), PdfListActivity.class);
            intent28.putExtra("batch", "BTech 1st year");
            intent28.putExtra("stream", "FT");
            startActivity(intent28);
        }
    }
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}