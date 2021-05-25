package com.ishika.adminapp.Email;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishika.adminapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    String name, email, photo;
    private TextView greetings, Email, pName;
    private ImageView pImage;
    private ProgressBar profileProgressbar;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String userId;

    {
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        assert firebaseUser != null;
        greetings = findViewById(R.id.greetings);
        Email = findViewById(R.id.editEmail);
        pName = findViewById(R.id.yourName);
        pImage = findViewById(R.id.profileImg);
        Button edit = findViewById(R.id.edit);

        profileProgressbar = new ProgressBar(this);
        profileProgressbar = findViewById(R.id.profileProgress);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins");
        DatabaseReference dbRef = databaseReference.child(userId);
        dbRef.keepSynced(true);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin adminProfile = snapshot.getValue(Admin.class);
                if (adminProfile != null) {
                    name = adminProfile.getName();
                    email = adminProfile.getEmail();
                    photo = adminProfile.getPhoto();
                    String welcome = "Welcome " + name + "!";
                    greetings.setText(welcome);
                    pName.setText(name);
                    Email.setText(email);
                    try {
                        Picasso.get().load(adminProfile.getPhoto()).into(pImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                profileProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });



        edit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("image", photo);
            intent.putExtra("email", email);
            startActivity(intent);
        });

    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}