package com.example.knlgsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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

    private Post post;

    private Long people;


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


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        post = (Post) bundle.getSerializable("position");

        title.setText(post.getTitle());
        moderator.setText(post.getModerator());
        description.setText(post.getDescription());
        day.setText(post.getDay());
        time.setText(post.getTime());
        people = post.getSeatsLeft();

        if(post.getSeatsLeft()==0)
            Toast.makeText(getApplicationContext(),"No more seats left",Toast.LENGTH_SHORT).show();

        if(!post.getImage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(post.getImage()).into(image);
            image.setVisibility(View.VISIBLE);
        }
        else
            image.setVisibility(View.GONE);

        Log.d("DEBUGG",""+post.getDay());

        ref = FirebaseDatabase.getInstance().getReference().child("Global/Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        if(ds.getValue(Post.class).equalsPost(post))
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

        Boolean ok = false;
        String[] emails;
        emails = post.getParticipants().split(",");
        for (String s : emails) {
            if (userEmail.equals(s)) {
                ok = true;
                break;
            }
        }
        if(people>0 && !ok ) {
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.setSeatsLeft((people - 1));
                    String participants = post.getParticipants().concat(","+userEmail);
                    post.setParticipants(participants);
//                    Map<String, Post> map = new HashMap<>();
//                    map.put(eventRef.getKey().toString(), post);
                    Log.d("DEBUGG", eventRef.getKey().toString());
                    eventRef.setValue(post);
                    plus.setVisibility(View.GONE);
                }
            });
        }
        else
            plus.setVisibility(View.GONE);

    }


}
