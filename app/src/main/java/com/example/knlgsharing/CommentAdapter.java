package com.example.knlgsharing;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>
{
    ArrayList<Comment> Comments;
    String name;
    Context context;


    public CommentAdapter(ArrayList<Comment> comments, String name, Context context) {
        Comments = comments;
        this.name = name;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view, false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.setName(Comments.get(position).getName());
        holder.setQuestion(Comments.get(position).getQuestion());
        holder.setAnswers(Comments.get(position).getAnswerArrayList());
        holder.answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup, null, false),100,100, true);

                //pw.showAtLocation(context.findViewById(R.id.main), Gravity.CENTER, 0, 0);
                Log.d("DEBUGG","^^");
            }
        });
    }

    @Override
    public int getItemCount() {
        return Comments.size();
    }


}
