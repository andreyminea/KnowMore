package com.example.knlgsharing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<PostViewHolder>
{
    ArrayList<Post> events;
    Context mcontext;
    OnItemListener onItemListener;

    public EventsAdapter(Context context, ArrayList<Post> posts, OnItemListener onItemListener)
    {
        mcontext = context;
        events = posts;

        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_row, parent, false);
        final PostViewHolder viewHolder = new PostViewHolder(view,onItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        postViewHolder.setDate(events.get(i).getDay());
        postViewHolder.setHour(events.get(i).getTime());
        postViewHolder.setImage(mcontext, events.get(i).getImage());
        postViewHolder.setModerator(events.get(i).getModerator());
        postViewHolder.setTitle(events.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface OnItemListener
    {
        void ItemClick(int position);
    }

    public ArrayList<Post> getArray()
    {
        return events;
    }

}
