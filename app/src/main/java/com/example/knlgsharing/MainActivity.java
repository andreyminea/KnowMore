package com.example.knlgsharing;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EventsAdapter.OnItemListener {

    private RecyclerView mPost;
    private DatabaseReference mDatabase;
    String current_user_id;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Post> events;
    ArrayList<Post> AllEvents;
    EventsAdapter adapter;

    SearchView search = null;
    Toolbar toolbar;
    Boolean isSearch = false;
    Boolean isPrevEvents = false;

    String selector = "Normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        search = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton prev = findViewById(R.id.action_prev);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display prev events
                if (!isPrevEvents) {
                    isPrevEvents = true;
                    showPrevEvents(AllEvents);
                } else {
                    isPrevEvents = false;
                    onStart();
                }

            }
        });

        FloatingActionButton search = findViewById(R.id.action_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display search activity
                setUpSearch(isSearch);
            }
        });

        FloatingActionButton signout = findViewById(R.id.action_signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //get the ref for my posts
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Global").child("Posts");
        message(mDatabase.toString());

        mDatabase.keepSynced(true);

        mPost = findViewById(R.id.sessions_knsh);

        //setting for displaying the cards

        linearLayoutManager = new LinearLayoutManager(this);

        // the last is displayed first
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mPost.setHasFixedSize(true);

        mPost.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    events = new ArrayList<>();
                    AllEvents = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        events.add(ds.getValue(Post.class));
                        AllEvents.add(ds.getValue(Post.class));
                    }
                }

                events = sortAfterDate(events);

                if (selector.equals("Normal"))
                    events = removePrevEvents(events);

                setRecyclerView(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void setUpSearch(Boolean ok) {
        if (ok == false) {
            search.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            isSearch = true;

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
        } else {
            isSearch = false;
            search.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }


    }

    private void showPrevEvents(ArrayList<Post> ev) {
        message("There are " + ev.size() + " events");
        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        ArrayList<Post> results = new ArrayList<>();

        for (Post object : ev) {
            String[] emails;
            emails = object.getParticipants().split(",");
            message("//////////////////////////");
            for (String s : emails) {
                message(s);
                if (currentEmail.equals(s)) {
                    results.add(object);
                }
            }

        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date currentDate = calendar.getTime();
        Date eventDate;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < results.size(); i++) {
            Post object = results.get(i);
            try {
                eventDate = dateFormat.parse(object.getDay());
                if (!currentDate.after(eventDate)) {
                    results.remove(object);
                    i = -1;
                }

            } catch (ParseException e) {
            }
        }
        AllEvents = results;
        setRecyclerView(results);

    }

    private ArrayList<Post> removePrevEvents(ArrayList<Post> events) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date currentDate = calendar.getTime();
        Date eventDate;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < events.size(); i++) {
            Post object = events.get(i);
            try {
                eventDate = dateFormat.parse(object.getDay());
                if (currentDate.after(eventDate)) {
                    events.remove(object);
                    i = -1;
                }

            } catch (ParseException e) {
            }
        }

        return events;
    }

    private void message(String s) {
        Log.d("DEBUGGG", "here " + s);
    }

    private void lookfor(String s) {
        ArrayList<Post> results = new ArrayList<>();

        if (!isPrevEvents) {
            for (Post object : events) {
                if (object.getTitle().toLowerCase().contains(s.toLowerCase())) {
                    results.add(object);
                }
            }
        } else {
            for (Post object : AllEvents) {
                if (object.getTitle().toLowerCase().contains(s.toLowerCase())) {
                    results.add(object);
                }
            }
        }
        setRecyclerView(results);
    }

    private void setRecyclerView(ArrayList<Post> arrayList) {
        adapter = new EventsAdapter(getApplicationContext(), arrayList, this);
        mPost.setAdapter(adapter);
    }


    class SortDate implements Comparator<Post> {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        public SortDate() {
        }

        @Override
        public int compare(Post i, Post j) {
            try {
                Date di = dateFormat.parse(i.getDay());
                Date dj = dateFormat.parse(j.getDay());
                if (di.before(dj))
                    return -1;
                else
                    return 1;

            } catch (ParseException e) {
            }
            return 0;
        }
    }


    private ArrayList<Post> sortAfterDate(ArrayList<Post> events) {
        Collections.sort(events, new SortDate());
        return events;
    }

    @Override
    public void ItemClick(final int position) {
        Log.d("DEBUGG CLICK", "" + position);

        Intent intent = new Intent(MainActivity.this, EventActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("position", adapter.getArray().get(position));

        intent.putExtras(bundle);

        Log.d("DEBUGGG", adapter.getArray().get(position).getEmailModerator());

        startActivity(intent);

    }



}
