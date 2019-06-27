package com.example.knlgsharing;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AdminActivity extends AppCompatActivity implements FirebaseAdapter.OnItemListener {

    LinearLayoutManager linearLayoutManager;
    DatabaseReference mDatabase;
    String selector = "Normal";
    Boolean isAdminMode = false;
    RecyclerView mPosts;
    ArrayList<Post> events;
    Boolean isSearch = false;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mPosts = findViewById(R.id.admin_events);
        search = findViewById(R.id.searchViewonAdmin);

        FloatingActionButton add = findViewById(R.id.action_add);

        add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminActivity.this,AddActivity.class);
                    startActivity(intent);
                }
            });

        FloatingActionButton search = findViewById(R.id.action_search_all);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSearch(isSearch);
            }
        });

        FloatingActionButton signout = findViewById(R.id.action_log);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminActivity.this, SignActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton delete = findViewById(R.id.action_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAdminMode)
                {
                    selector = "AdminMode";
                    isAdminMode = true;
                    onStart();
                    Toast.makeText(getApplicationContext(),"Long Press on an event to delete", Toast.LENGTH_LONG).show();
                }
                else
                {
                    selector = "Normal";
                    isAdminMode = false;
                    onStart();
                    Toast.makeText(getApplicationContext(),"Normal Mode", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Global").child("Posts");

        mDatabase.keepSynced(true);

        //setting for displaying the cards

        linearLayoutManager = new LinearLayoutManager(this);

        // the last is displayed first
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mPosts.setHasFixedSize(true);
        mPosts.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    events = new ArrayList<>();
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        events.add(ds.getValue(Post.class));
                    }
                }

                events = sortAfterDate(events);

                FirebaseAdapter adapter;
                if (selector.equals("Normal"))
                    setRecyclerView(events);
                else
                    setRecyclerView(events);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUpSearch(Boolean ok)
    {
        if(ok==false)
        {
            search.setVisibility(View.VISIBLE);
            isSearch=true;

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    lookfor(s);
                    return true;
                }
            });
        }
        else
        {
            isSearch=false;
            search.setVisibility(View.GONE);
        }


    }


    private void lookfor(String s)
    {
        ArrayList<Post> results = new ArrayList<>();

            for(Post object : events)
            {
                if(object.getTitle().toLowerCase().contains(s.toLowerCase()))
                {
                    results.add(object);
                }
            }

        setRecyclerView(results);
    }

    @Override
    public void ItemClick(int position) {

        Log.d("DEBUGG CLICK" , ""+position);

        Intent intent = new Intent(AdminActivity.this, EventActivity.class);
        intent.putExtra("position", events.get(position) + "");

        Log.d("DEBUGGG", events.get(position).getEmailModerator());

        startActivity(intent);
    }

    @Override
    public void ItemLongClick(final int position) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Global/Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(position==j) {
                        snapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    class SortDate implements Comparator<Post>
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        public SortDate() {
    }

        @Override
        public int compare(Post i, Post j)
        {
            try {
                Date di = dateFormat.parse(i.getDay());
                Date dj = dateFormat.parse(j.getDay());
                if(di.before(dj))
                    return -1;
                else
                    return 1;

            }
            catch (ParseException e){}
            return 0;
        }
    }

    private ArrayList<Post> sortAfterDate(ArrayList<Post> events)
    {
        Collections.sort(events, new SortDate());
        return events;
    }

    private void setRecyclerView(ArrayList<Post> arrayList)
    {
        FirebaseAdapter adapter = new FirebaseAdapter(selector, getApplicationContext(), arrayList, this);
        mPosts.setAdapter(adapter);
    }

}
