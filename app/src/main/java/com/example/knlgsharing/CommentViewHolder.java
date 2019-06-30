package com.example.knlgsharing;

import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CommentViewHolder extends RecyclerView.ViewHolder{

    View view;
    Boolean showAnswers = false;
    Button answerBtn;
    ArrayList<Answer> list;

    public CommentViewHolder(@NonNull View itemView, Boolean ok) {
        super(itemView);
        this.view = itemView;
        this.showAnswers = ok;
        answerBtn = view.findViewById(R.id.answer_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswers = !showAnswers;
                setAnswers(list);
            }
        });
    }

    public void setName(String Name)
    {
        TextView name = view.findViewById(R.id.comment_poster);
        name.setText(Name);
    }
    public void setQuestion(String Question)
    {
        TextView name = view.findViewById(R.id.comment_question);
        name.setText(Question);
    }

    public void setAnswers(ArrayList<Answer> comments)
    {
        RecyclerView commentSection = view.findViewById(R.id.answerArrayList);
        list=comments;
        if(!showAnswers)
            commentSection.setVisibility(View.GONE);
        else
        {
            commentSection.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());

            // the last is displayed first
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);

            commentSection.setHasFixedSize(true);
            commentSection.setLayoutManager(linearLayoutManager);

            AnswerAdapter answerAdapter = new AnswerAdapter(comments);
            commentSection.setAdapter(answerAdapter);
        }
    }

}
