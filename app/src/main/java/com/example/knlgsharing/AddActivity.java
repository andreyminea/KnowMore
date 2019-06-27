package com.example.knlgsharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    MaterialButton cancel;
    MaterialButton add;
    TextInputEditText title;
    TextInputEditText moderator;
    TextInputEditText emailModerator;
    TextInputEditText description;
    TextInputEditText image;
    TextInputEditText seats;
    TextInputEditText date;
    TextInputEditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.field_title);
        moderator = findViewById(R.id.field_moderator_name);
        emailModerator = findViewById(R.id.field_moderator_email);
        description = findViewById(R.id.field_description);
        image = findViewById(R.id.field_image);
        seats = findViewById(R.id.field_seats);
        date = findViewById(R.id.field_date);
        time = findViewById(R.id.field_time);

        cancel = findViewById(R.id.cancel_btn);
        add = findViewById(R.id.save_btn);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendData();
                Intent intent = new Intent(AddActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendData()
    {
        String img = "";
        if(!image.getText().toString().isEmpty())
            img=image.getText().toString();

        Post post = new Post(
                title.getText().toString(),
                moderator.getText().toString(),
                date.getText().toString(),
                time.getText().toString(),
                img,
                description.getText().toString(),
                new Long(Integer.parseInt(seats.getText().toString())),
                emailModerator.getText().toString(),
                "");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Global").child("Posts");

        ref.child(post.getTitle()+post.getModerator()).setValue(post);
    }
}
