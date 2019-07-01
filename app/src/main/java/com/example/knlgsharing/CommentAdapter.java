package com.example.knlgsharing;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>
{
    ArrayList<Comment> Comments;
    String name;
    Context context;
    String userName;
    DatabaseReference postRef;
    DatabaseReference temp;


    public CommentAdapter(final ArrayList<Comment> comments, String name, Context context, String userName, DatabaseReference reference) {
        Comments = comments;
        this.name = name;
        this.context = context;
        this.userName = userName;
        postRef = reference;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view, false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        holder.setName(Comments.get(position).getName());
        holder.setQuestion(Comments.get(position).getQuestion());
        holder.setAnswers(Comments.get(position).getAnswerArrayList());
        final View view = holder.view;

        holder.answerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("DEBUGG","^^" + postRef.toString());

                postRef.child("Coms").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String q = ds.child("question").getValue(String.class);
                            if (name.equals(Comments.get(position).getName()) && q.equals(Comments.get(position).getQuestion()))
                            {
                                temp = ds.getRef();
                                EditText message = view.findViewById(R.id.answerText);

                                String text = message.getText().toString();
                                Answer answer = new Answer();
                                answer.setResponse(text);
                                answer.setName(userName);

                                temp.child("Answer").child(temp.push().getKey()).setValue(answer);

                                Log.d("DEBUGG","^^" + userName);

                                break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return Comments.size();
    }


}
