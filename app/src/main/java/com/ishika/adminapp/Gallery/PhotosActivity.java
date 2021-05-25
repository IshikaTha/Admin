package com.ishika.adminapp.Gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishika.adminapp.R;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView pRecyclerview;
    private GalleryAdapter galleryAdapter;
    private ShimmerFrameLayout shimmerFrameLayout1;
    private LinearLayout shimmerL, t1;
    private String event;
    private LinearLayout noData;
    private TextView eventHead;
    SwipeRefreshLayout photosSwipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        shimmerFrameLayout1 = findViewById(R.id.shimmer_view_container1);
        shimmerL = findViewById(R.id.shimmerL);

        eventHead = findViewById(R.id.eventHead);

        event = getIntent().getStringExtra("events");
        eventHead.setText(event);
        t1 = findViewById(R.id.t1);
        noData = findViewById(R.id.noData1);
        photosSwipe = findViewById(R.id.photosSwipe);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");

        pRecyclerview = findViewById(R.id.pRecyclerview);
        getPhotos();

        photosSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhotos();
                photosSwipe.setRefreshing(false);
            }
        });
    }

    private void getPhotos() {

        databaseReference.child(event).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GalleryData> list = new ArrayList<>();
                if(!snapshot.exists()) {
                    noData.setVisibility(View.VISIBLE);
                    pRecyclerview.setVisibility(View.GONE);
                }else {
                    noData.setVisibility(View.GONE);
                    pRecyclerview.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GalleryData galleryData = dataSnapshot.getValue(GalleryData.class);
                        list.add(0, galleryData);
                    }
                }
                galleryAdapter = new GalleryAdapter(PhotosActivity.this, list);
                galleryAdapter.notifyDataSetChanged();
                GridLayoutManager gridLayoutManager = new GridLayoutManager(PhotosActivity.this, 4);
                pRecyclerview.setLayoutManager(gridLayoutManager);
                pRecyclerview.setHasFixedSize(true);
                pRecyclerview.setAdapter(galleryAdapter);
                shimmerFrameLayout1.stopShimmer();
                shimmerL.setVisibility(View.GONE);
                t1.setVisibility(View.VISIBLE);
                eventHead.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhotosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout1.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout1.startShimmer();
        super.onResume();
    }


//    private void ListFiles(String s, int count, String img){
//        String state = Environment.getExternalStorageState();
//        Bitmap bitmap = StringToBitMap(img);
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (checkPermission()) {
//                    String path = Environment.getExternalStorageDirectory().toString();
//                    File file = new File(path, s+count+".jpg");
//                    if (!file.exists()) {
//                        Log.d("path", file.toString());
//                        try {
//                            FileOutputStream fos = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                            fos.flush();
//                            fos.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    requestPermission(); // Code for permission
//                }
//            } else {
//                String path = Environment.getExternalStorageDirectory().toString();
//                File file = new File(path, s+count+".jpg");
//                if (!file.exists()) {
//                    Log.d("path", file.toString());
//                    FileOutputStream fos = null;
//                    try {
//                        fos = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        fos.flush();
//                        fos.close();
//                    } catch (java.io.IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    public Bitmap StringToBitMap(String image){
//        try{
//            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
//
//            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
//            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//            return bitmap;
//        }catch(Exception e){
//            e.getMessage();
//            return null;
//        }
//    }
//
//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(PhotosActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        } else{
//            return false;
//        }
//    }
//
//    private void requestPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(PhotosActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            Toast.makeText(PhotosActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(PhotosActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        }
//    }

    public boolean onSupportNavigateUp () {
            onBackPressed();
            return super.onSupportNavigateUp();
    }
}