package com.ishika.adminapp.Gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView convoCard, sihCard, icmCard, visCard, indeCard, repCard, othCard;
    private DatabaseReference databaseReference;
    private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7;
    SwipeRefreshLayout swipeGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        convoCard = findViewById(R.id.convoCard);
        sihCard = findViewById(R.id.sihCard);
        icmCard = findViewById(R.id.icmCard);
        visCard = findViewById(R.id.visCard);
        indeCard = findViewById(R.id.indeCard);
        repCard = findViewById(R.id.repCard);
        othCard = findViewById(R.id.othCard);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);

        FloatingActionButton fb1 = findViewById(R.id.fb1);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        getConvoPhotos();
        getSIHPhotos();
        getIcmPhotos();
        getVisPhotos();
        getIndePhotos();
        getOthPhotos();
        getRepPhotos();

        convoCard.setOnClickListener(this);
        sihCard.setOnClickListener(this);
        icmCard.setOnClickListener(this);
        visCard.setOnClickListener(this);
        indeCard.setOnClickListener(this);
        repCard.setOnClickListener(this);
        othCard.setOnClickListener(this);

        swipeGallery = findViewById(R.id.swipeGallery);

        swipeGallery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConvoPhotos();
                getSIHPhotos();
                getIcmPhotos();
                getVisPhotos();
                getIndePhotos();
                getOthPhotos();
                getRepPhotos();
                swipeGallery.setRefreshing(false);
            }
        });

        fb1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UploadPhotoActivity.class);
            startActivity(intent);
        });

    }
    private void getConvoPhotos() {
        databaseReference.child("Convocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll1.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(convoCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSIHPhotos() {
        databaseReference.child("SIH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll2.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(sihCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIcmPhotos() {
        databaseReference.child("ICM ICPC").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll3.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(icmCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVisPhotos() {
        databaseReference.child("Industrial Visit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll4.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(visCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIndePhotos() {
        databaseReference.child("Independence Day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll5.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(indeCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRepPhotos() {
        databaseReference.child("Republic Day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll6.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(repCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOthPhotos() {
        databaseReference.child("Others").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                ArrayList<GalleryData> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                    list.add(0, galleryData);
                    count++;
                }
                if(count==0) {
                    ll7.setVisibility(View.GONE);
                }else{
                    String i1 = list.get(0).getImage();
                    try {
                        Picasso.get().load(i1).into(othCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.convoCard) {
            Intent i1 = new Intent(getApplicationContext(), PhotosActivity.class);
            i1.putExtra("events", "Convocation");
            startActivity(i1);
        }
        if(v.getId() ==  R.id.sihCard) {
            Intent i2 = new Intent(getApplicationContext(), PhotosActivity.class);
            i2.putExtra("events", "SIH");
            startActivity(i2);
        }
        if(v.getId() ==  R.id.icmCard) {
            Intent i3 = new Intent(getApplicationContext(), PhotosActivity.class);
            i3.putExtra("events", "ICM ICPC");
            startActivity(i3);
        }
        if(v.getId() ==  R.id.visCard) {
            Intent i4 = new Intent(getApplicationContext(), PhotosActivity.class);
            i4.putExtra("events", "Industrial Visit");
            startActivity(i4);
        }
        if(v.getId() ==  R.id.indeCard) {
            Intent i5 = new Intent(getApplicationContext(), PhotosActivity.class);
            i5.putExtra("events", "Independence Day");
            startActivity(i5);
        }
        if(v.getId() ==   R.id.repCard) {
            Intent i6 = new Intent(getApplicationContext(), PhotosActivity.class);
            i6.putExtra("events", "Republic Day");
            startActivity(i6);
        }
        if(v.getId() ==   R.id.othCard) {
            Intent i7 = new Intent(getApplicationContext(), PhotosActivity.class);
            i7.putExtra("events", "Others");
            startActivity(i7);
        }
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}