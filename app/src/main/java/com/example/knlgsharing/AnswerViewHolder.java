package com.example.knlgsharing;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


class AnswerViewHolder extends RecyclerView.ViewHolder {

    View view;

    public AnswerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

    }

    public void setName(String Name)
    {
        TextView name = view.findViewById(R.id.answer_name);
        name.setText(Name);
    }
    public void setText(String Question)
    {
        TextView name = view.findViewById(R.id.answer_text);
        name.setText(Question);
    }

}
