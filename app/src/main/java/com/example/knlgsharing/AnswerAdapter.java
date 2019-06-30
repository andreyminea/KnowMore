package com.example.knlgsharing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder>
{
    ArrayList<Answer> Answers;


    public AnswerAdapter(ArrayList<Answer> ans) {
        Answers = ans;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_row, parent, false);
        AnswerViewHolder viewHolder = new AnswerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        holder.setName(Answers.get(position).getName());
        holder.setText(Answers.get(position).getResponse());

    }

    @Override
    public int getItemCount() {
        return Answers.size();
    }
}
