package com.example.knlgsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    TextView title;
    TextView moderator;
    TextView description;
    TextView day;
    TextView time;
    ImageView image;
    MaterialButton back;
    MaterialButton plus;
    DatabaseReference ref;
    DatabaseReference eventRef;
    String userEmail;
    String adminEmail;

    private int people;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        title = findViewById(R.id.eventTitle);
        moderator = findViewById(R.id.eventModerator);
        description = findViewById(R.id.eventDescription);
        day = findViewById(R.id.eventDay);
        time = findViewById(R.id.eventTime);
        image = findViewById(R.id.eventImage);
        back = findViewById(R.id.back_btn);
        plus = findViewById(R.id.plus_btn);

        final Post post;
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("position");

        title.setText(post.getTitle());
        moderator.setText(post.getModerator());
        description.setText(post.getDescription());
        day.setText(post.getDay());
        time.setText(post.getTime());
        people = Integer.getInteger(post.getSeatsLeft().toString());

        ref = FirebaseDatabase.getInstance().getReference().child("Global/Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        if(ds.getValue(Post.class).equals(post))
                            eventRef = ds.getRef();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();



        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Global").child("Admin").child("Email");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminEmail = dataSnapshot.getValue(String.class);

            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adminEmail.equals(userEmail)) {
                    Intent intent = new Intent(EventActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(EventActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

        if(people>0) {
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.setParticipants(Integer.toString(people - 1));
                    String participants = post.getParticipants().concat(","+userEmail);
                    post.setParticipants(participants);
                    eventRef.child(eventRef.getKey()).setValue(post);
                    plus.setVisibility(View.GONE);
                }
            });
        }
        else
            plus.setVisibility(View.GONE);

    }
}
