package com.ishika.adminapp.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishika.adminapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FacultyActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment, itDepartment, caDepartment, eeDepartment, ecDepartment, eieDepartment, fDepartment, ashuDepartment;
    private LinearLayout csNoData, itNoData, caNoData, eeNoData, ecNoData, eieNoData, fNoData, ashuNoData;
    private List<TeacherData> list1, list2, list3, list4, list5, list6, list7, list8;
    private DatabaseReference databaseReference, dbRef;
    private TeacherAdapter teacherAdapter;
    private ProgressBar progressBar3;
    SwipeRefreshLayout facultySwipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Faculties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar3 = new ProgressBar(this);
        progressBar3 = findViewById(R.id.progressBar3);

        fab = findViewById(R.id.fab);

        TextView cs = findViewById(R.id.cs);
        TextView it = findViewById(R.id.it);
        TextView ee = findViewById(R.id.ee);
        TextView eie = findViewById(R.id.eie);
        TextView ca = findViewById(R.id.ca);
        TextView ece = findViewById(R.id.ece);
        TextView ft = findViewById(R.id.ft);
        TextView ashu = findViewById(R.id.ashu);

        cs.setSelected(true);
        it.setSelected(true);
        ee.setSelected(true);
        eie.setSelected(true);
        ca.setSelected(true);
        ece.setSelected(true);
        ft.setSelected(true);
        ashu.setSelected(true);

        csDepartment = findViewById(R.id.csDepartment);
        itDepartment = findViewById(R.id.itDepartment);
        caDepartment = findViewById(R.id.caDepartment);
        eeDepartment = findViewById(R.id.eeDepartment);
        ecDepartment = findViewById(R.id.ecDepartment);
        eieDepartment = findViewById(R.id.eieDepartment);
        fDepartment = findViewById(R.id.fDepartment);
        ashuDepartment = findViewById(R.id.ashuDepartment);

        csNoData = findViewById(R.id.csNoData);
        itNoData = findViewById(R.id.itNoData);
        caNoData = findViewById(R.id.caNoData);
        eeNoData = findViewById(R.id.eeNoData);
        ecNoData = findViewById(R.id.ecNoData);
        eieNoData = findViewById(R.id.eieNoData);
        fNoData = findViewById(R.id.fNoData);
        ashuNoData = findViewById(R.id.ashuNoData);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");

        csDepartment();
        itDepartment();
        caDepartment();
        eeDepartment();
        ecDepartment();
        eieDepartment();
        fDepartment();
        ashuDepartment();

        facultySwipe = findViewById(R.id.facultySwipe);
        facultySwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                csDepartment();
                itDepartment();
                caDepartment();
                eeDepartment();
                ecDepartment();
                eieDepartment();
                fDepartment();
                ashuDepartment();
                facultySwipe.setRefreshing(false);
            }
        });
        fab.setOnClickListener(v -> startActivity(new Intent(FacultyActivity.this, AddTeacherActivity.class)));
    }

    private void csDepartment() {
        dbRef = databaseReference.child("Computer Science and Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if(!snapshot.exists()) {
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list1.add(0,teacherData);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list1, FacultyActivity.this, "Computer Science and Engineering");
                    progressBar3.setVisibility(View.GONE);
                    csDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void itDepartment() {
        dbRef = databaseReference.child("Information Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if(!snapshot.exists()) {
                    itNoData.setVisibility(View.VISIBLE);
                    itDepartment.setVisibility(View.GONE);
                }else {
                    itNoData.setVisibility(View.GONE);
                    itDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list2.add(0,teacherData);
                    }
                    itDepartment.setHasFixedSize(true);
                    itDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list2, FacultyActivity.this, "Information Technology");
                    progressBar3.setVisibility(View.GONE);
                    itDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void caDepartment() {
        dbRef = databaseReference.child("Computer Application");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if(!snapshot.exists()) {
                    caNoData.setVisibility(View.VISIBLE);
                    caDepartment.setVisibility(View.GONE);
                }else {
                    caNoData.setVisibility(View.GONE);
                    caDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list3.add(0,teacherData);
                    }
                    caDepartment.setHasFixedSize(true);
                    caDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list3, FacultyActivity.this, "Computer Application");
                    progressBar3.setVisibility(View.GONE);
                    caDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eeDepartment() {
        dbRef = databaseReference.child("Electrical Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if(!snapshot.exists()) {
                    eeNoData.setVisibility(View.VISIBLE);
                    eeDepartment.setVisibility(View.GONE);
                }else {
                    eeNoData.setVisibility(View.GONE);
                    eeDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list4.add(0,teacherData);
                    }
                    eeDepartment.setHasFixedSize(true);
                    eeDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list4, FacultyActivity.this, "Electrical Engineering");
                    progressBar3.setVisibility(View.GONE);
                    eeDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ecDepartment() {
        dbRef = databaseReference.child("Electronic and Communication Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();
                if(!snapshot.exists()) {
                    ecNoData.setVisibility(View.VISIBLE);
                    ecDepartment.setVisibility(View.GONE);
                }else {
                    ecNoData.setVisibility(View.GONE);
                    ecDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list5.add(0,teacherData);
                    }
                    ecDepartment.setHasFixedSize(true);
                    ecDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list5, FacultyActivity.this, "Electronic and Communication Engineering");
                    progressBar3.setVisibility(View.GONE);
                    ecDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eieDepartment() {
        dbRef = databaseReference.child("Electronics and Instrumentation Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6 = new ArrayList<>();
                if(!snapshot.exists()) {
                    eieNoData.setVisibility(View.VISIBLE);
                    eieDepartment.setVisibility(View.GONE);
                }else {
                    eieNoData.setVisibility(View.GONE);
                    eieDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list6.add(0,teacherData);
                    }
                    eieDepartment.setHasFixedSize(true);
                    eieDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list6, FacultyActivity.this, "Electronics and Instrumentation Engineering");
                    progressBar3.setVisibility(View.GONE);
                    eieDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fDepartment() {
        dbRef = databaseReference.child("Food Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list7 = new ArrayList<>();
                if(!snapshot.exists()) {
                    fNoData.setVisibility(View.VISIBLE);
                    fDepartment.setVisibility(View.GONE);
                }else {
                    fNoData.setVisibility(View.GONE);
                    fDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list7.add(0,teacherData);
                    }
                    fDepartment.setHasFixedSize(true);
                    fDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list7, FacultyActivity.this, "Food Technology");
                    progressBar3.setVisibility(View.GONE);
                    fDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ashuDepartment() {
        dbRef = databaseReference.child("Applied Science and Humanities");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list8 = new ArrayList<>();
                if(!snapshot.exists()) {
                    ashuNoData.setVisibility(View.VISIBLE);
                    ashuDepartment.setVisibility(View.GONE);
                }else {
                    ashuNoData.setVisibility(View.GONE);
                    ashuDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData teacherData = dataSnapshot.getValue(TeacherData.class);
                        list8.add(0,teacherData);
                    }
                    ashuDepartment.setHasFixedSize(true);
                    ashuDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list8, FacultyActivity.this, "Applied Science and Humanities");
                    progressBar3.setVisibility(View.GONE);
                    ashuDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar3.setVisibility(View.GONE);
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}