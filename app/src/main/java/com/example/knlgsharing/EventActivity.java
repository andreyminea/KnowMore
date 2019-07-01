package com.example.knlgsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    ImageButton sendComment;
    EditText textComment;

    DatabaseReference commentRef;
    DatabaseReference ref;
    DatabaseReference eventRef;

    String userEmail;
    String adminEmail;
    String name;

    private Post post;

    private Long people;

    RecyclerView recyclerView;


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
        sendComment = findViewById(R.id.sendComment);
        textComment = findViewById(R.id.newComment);

        recyclerView = findViewById(R.id.comment_section);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        post = (Post) bundle.getSerializable("position");

        title.setText(post.getTitle());
        moderator.setText(post.getModerator());
        description.setText(post.getDescription());
        day.setText(post.getDay());
        time.setText(post.getTime());
        people = post.getSeatsLeft();

        if (post.getSeatsLeft() == 0)
            Toast.makeText(getApplicationContext(), "No more seats left", Toast.LENGTH_SHORT).show();

        if (!post.getImage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(post.getImage()).into(image);
            image.setVisibility(View.VISIBLE);
        } else
            image.setVisibility(View.GONE);


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = commentRef.child("Coms");
                String send = textComment.getText().toString();
                String key = ref.push().getKey();
                ref.child(key).child("question").setValue(send);
                ref.child(key).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("Global/Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Post.class).equalsPost(post)) {
                            eventRef = ds.getRef();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("Global/Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                        if (ds.getKey().equals(eventRef.getKey())) {
                            commentRef = ds.getRef();
                            getComments(commentRef);
                            break;
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        name = auth.getCurrentUser().getDisplayName();
        userEmail = auth.getCurrentUser().getEmail();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Global").child("Admin").child("Email");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminEmail = dataSnapshot.getValue(String.class);
                setButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adminEmail.equals(userEmail)) {
                    Intent intent = new Intent(EventActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EventActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void setButton()
    {
        Boolean ok = false;
        String[] emails;
        emails = post.getParticipants().split(",");
        for (String s : emails) {
            if (userEmail.equals(s)) {
                ok = true;
                break;
            }
        }

        if (userEmail.equals(post.getEmailModerator()) || userEmail.equals(adminEmail)) {
            plus.setText("Edit");
            plus.setVisibility(View.VISIBLE);

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventActivity.this, AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dates", post);
                    bundle.putString("ref", eventRef.toString());
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        } else {
            if (people > 0 && !ok)
            {
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post.setSeatsLeft((people - 1));
                        String participants = post.getParticipants().concat("," + userEmail);
                        post.setParticipants(participants);
                        eventRef.setValue(post);
                        plus.setVisibility(View.GONE);
                    }
                });
            } else
                plus.setVisibility(View.GONE);
        }
    }


    public void setEverything(ArrayList<Comment> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        // the last is displayed first
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        CommentAdapter adapter = new CommentAdapter(list, name, getApplicationContext(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), commentRef);
        recyclerView.setAdapter(adapter);
    }


    private void getComments(final DatabaseReference ref) {
        final ArrayList<Comment> results = new ArrayList<>();

        ref.child("Coms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        String q = ds.child("question").getValue(String.class);
                        ArrayList<Answer> answers = new ArrayList<>();
                        for (DataSnapshot ids : ds.child("Answer").getChildren()) {
                            Answer a = ids.getValue(Answer.class);
                            answers.add(a);
                        }
                        Comment c = new Comment(name, q, answers, ref.child("Coms"));
                        results.add(c);
                    }

                }
                setEverything(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
