package com.example.knlgsharing;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
{
    View view;
    Context mContext;
    FirebaseAdapter.OnItemListener onItemListener;

    public PostViewHolder(View itemView, String selection, FirebaseAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        view = itemView;
        mContext = itemView.getContext();
        this.onItemListener = onItemListener;
        if(selection.equals("Normal"))
            itemView.setOnClickListener(PostViewHolder.this);
        else
            itemView.setOnLongClickListener(PostViewHolder.this);

    }


    public void setTitle(String title)
    {
        TextView post_moderator = view.findViewById(R.id.post_title);
        post_moderator.setText(title);
    }

    public void setModerator(String moderator)
    {
        TextView post_moderator = view.findViewById(R.id.post_moderator);
        post_moderator.setText(moderator);
    }

    public void setDate(String date)
    {
        TextView post_date = view.findViewById(R.id.post_date);
        post_date.setText(date);
    }

    public void setHour(String hour)
    {
        TextView post_hour = view.findViewById(R.id.post_hour);
        post_hour.setText(hour);
    }

    public void setImage(Context mcontext, String image)
    {
        ImageView post_image = view.findViewById(R.id.post_image);
        if(!image.isEmpty()) {
            Picasso.with(mcontext).load(image).into(post_image);
            post_image.setVisibility(View.VISIBLE);
        }
        else
            post_image.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        Log.d("DEBUGG", "Onclick an item");
        onItemListener.ItemClick(getAdapterPosition());

    }

    @Override
    public boolean onLongClick(View v)
    {
        onItemListener.ItemLongClick(getAdapterPosition());
        return true;
    }
}

