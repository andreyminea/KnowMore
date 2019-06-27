package com.example.knlgsharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InterfaceAddress;
import java.util.ArrayList;

public class FirebaseAdapter extends RecyclerView.Adapter<PostViewHolder>
{
    ArrayList<Post> events;
    String selector;
    Context mcontext;
    OnItemListener onItemListener;

    public FirebaseAdapter(String selector, Context context, ArrayList<Post> posts, OnItemListener onItemListener)
    {
        mcontext = context;
        events = posts;
        this.selector = selector;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_row, parent, false);
        PostViewHolder viewHolder = new PostViewHolder(view,selector,onItemListener);
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
        void ItemLongClick(int position);
    }

}
