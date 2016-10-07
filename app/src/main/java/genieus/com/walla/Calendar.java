package genieus.com.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Calendar extends AppCompatActivity implements View.OnClickListener{
    ListView lv;
    private DatabaseReference mDatabase;
    FirebaseUser user;
    CalendarAdapter adapter;
    List<Event> events;

    RelativeLayout profile, activities;
    final double SECONDS_IN_2_WEEKS = 1.21e+6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        lv = (ListView) findViewById(R.id.events);
        profile = (RelativeLayout)findViewById(R.id.profile_btn);
        activities = (RelativeLayout) findViewById(R.id.activities_btn);

        profile.setOnClickListener(this);
        activities.setOnClickListener(this);

        events = new ArrayList<>();
        adapter = new CalendarAdapter(Calendar.this, R.layout.event_template, events);

        lv.setAdapter(adapter);

        long time = System.currentTimeMillis() / 1000;
        mDatabase.child("user_attending/" + user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> att = (Map<String, Object>) dataSnapshot.getValue();
                        for(String key : att.keySet()){
                            getDetails(key);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        /*
        mDatabase.child("user_activities/" + user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> act = (Map<String, Object>) dataSnapshot.getValue();
                if(act == null)
                    return;

                mDatabase.child("users/" + user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                        String name = (String) userInfo.get("name");

                        events.add(new Event(
                                (String) act.get("description"),
                                (String) act.get("interest"),
                                "" + act.get("activityTime"),
                                "" + act.get("activityTime"),
                                (String) act.get("uid"),
                                name,
                                (String) act.get("location"),
                                (String) act.get("key"),
                                1));

                        Collections.sort(events, new Comparator<Event>() {
                            @Override
                            public int compare(Event lhs, Event rhs) {
                                return Double.compare(rhs.getRawTime(), lhs.getRawTime());
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }

    private void getDetails(String key){
        mDatabase.child("activities/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> act = (Map<String, Object>) dataSnapshot.getValue();
                String uid = (String) act.get("uid");

                mDatabase.child("users/" + uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                        String name = (String) userInfo.get("name");

                        events.add(new Event(
                                (String) act.get("description"),
                                (String) act.get("interest"),
                                "" +  act.get("activityTime"),
                                "" + act.get("activityTime"),
                                (String) act.get("uid"),
                                name,
                                (String) act.get("location"),
                                (String) act.get("key"),
                                (long) act.get("numberGoing")));

                        Collections.sort(events, new Comparator<Event>() {
                            @Override
                            public int compare(Event lhs, Event rhs) {
                                return Double.compare(rhs.getRawTime(), lhs.getRawTime());
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.profile_btn:
                startActivity(new Intent(this, Profile.class));
                break;
            case R.id.activities_btn:
                startActivity(new Intent(this, Activities.class));
                break;
        }
    }
}
