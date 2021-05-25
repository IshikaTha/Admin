package com.ishika.adminapp.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishika.adminapp.R;

import java.util.ArrayList;
import java.util.Objects;

public class NoticesActivity extends AppCompatActivity {
    private RecyclerView upNotRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter noticeAdapter;
    private DatabaseReference databaseReference;
    SwipeRefreshLayout noticeSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        progressBar = new ProgressBar(this);

        FloatingActionButton fb = findViewById(R.id.fb);
        upNotRecyclerview = findViewById(R.id.upNotRecyclerview);
        progressBar = findViewById(R.id.progressBar);

        fb.setOnClickListener(v -> {
            Intent intent = new Intent(NoticesActivity.this, UploadNoticeActivity.class);
            startActivity(intent);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        upNotRecyclerview.setLayoutManager(linearLayoutManager);
        upNotRecyclerview.setHasFixedSize(true);
        getNotice();
        noticeSwipe = findViewById(R.id.noticeSwipe);

        noticeSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotice();
                noticeSwipe.setRefreshing(false);
            }
        });
    }

    private void getNotice() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NoticeData noticeData = dataSnapshot.getValue(NoticeData.class);
                    list.add(0, noticeData);
                }
                noticeAdapter = new NoticeAdapter(NoticesActivity.this, list);
                noticeAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                upNotRecyclerview.setAdapter(noticeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NoticesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}