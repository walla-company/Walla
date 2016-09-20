package genieus.com.walla;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

public class Activities extends AppCompatActivity implements View.OnClickListener, InterestsRVAdapter.ItemClickListener, ChildEventListener {

    private DatabaseReference mDatabase;
    FirebaseUser user;
    RecyclerView rv;
    ListView lv;
    RelativeLayout profile;
    List<Event> events;
    EventAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        events = new ArrayList<>();
        lv = (ListView) findViewById(R.id.events);
        adp = new EventAdapter(Activities.this, R.layout.event_template, events);
        lv.setAdapter(adp);

        rv = (RecyclerView) findViewById(R.id.interests);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        profile = (RelativeLayout) findViewById(R.id.profile_btn);
        profile.setOnClickListener(this);

        /*
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event Title Goes Here", "Other","Jan. 1", "12:00 AM", "Arya Stark"));
        events.add(new Event("Event Title Goes Here", "Other","May. 1", "12:00 AM", "Jon Snow"));
        events.add(new Event("Event Title Goes Here", "Other","Feb. 1", "12:00 AM", "Dat Boi"));
        events.add(new Event("Event Title Goes Here", "Other","Jan. 1", "12:00 AM", "Chicken Sandwich"));
        events.add(new Event("Event Title Goes Here", "Other","Jan. 1", "12:00 AM", "Dank Memes"));
        events.add(new Event("Event Title Goes Here", "Other","Jan. 1", "12:00 AM", "Timmy Boy"));
        events.add(new Event("Event Title Goes Here", "Other","Sept. 1", "12:00 AM", "Harry Something"));
        events.add(new Event("Event Title Goes Here", "Other","Jan. 1", "12:00 AM", "Ayy Lmfao"));
        events.add(new Event("Event Title Goes Here", "Other","Dec. 1", "12:00 AM", "No Name"));

        EventAdapter adp = new EventAdapter(this, R.layout.event_template, events);
        lv.setAdapter(adp);
        */



        List<Interests> interests = new ArrayList<>();
        interests.add(new Interests("All", R.mipmap.all));
        interests.add(new Interests("Art", R.mipmap.art));
        interests.add(new Interests("School", R.mipmap.school));
        interests.add(new Interests("Sports", R.mipmap.sports));
        interests.add(new Interests("Rides", R.mipmap.rides));
        interests.add(new Interests("Games", R.mipmap.games));
        interests.add(new Interests("Food", R.mipmap.food));
        interests.add(new Interests("Other", R.mipmap.other));

        InterestsRVAdapter adapter = new InterestsRVAdapter(interests, this);
        rv.setAdapter(adapter);


        getFeed();
    }

    private void getFeed(){
        mDatabase.child("activities").orderByChild("activityTime")
                .startAt((System.currentTimeMillis() / 1000) + 1800)
                .addChildEventListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_write){
            Intent intent = new Intent(this, Create.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.profile_btn){
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
    }

    private String getColor(String category){
        switch(category){
            case "All":
                return "#FFA160";
            case "Art":
                return "#E47E30";
            case "School":
                return "#F0C330";
            case "Sports":
                return "#3A99D8";
            case "Rides":
                return "#39CA74";
            case "Games":
                return "#FFBB9C";
            case "Food":
                return "#E54D42";
            case "Other":
                return "#9A5CB4";
            default:
                return null;

        }
    }

    @Override
    public void onItemClicked(Interests event, View view, List<View> all) {
        view.setBackgroundResource(R.drawable.circle_orange);

        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(getColor(event.getName())));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(getColor(event.getName())));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(getColor(event.getName())));
        }

        for(View v : all){
            if(!v.equals(view)){
                v.setBackgroundResource(R.drawable.circle_grey);
            }
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Map<Object, Object> act = (Map<Object, Object>) dataSnapshot.getValue();
        events.add(new Event(
                (String) act.get("description"),
                (String) act.get("interest"),
                "" +  act.get("activityTime"),
                "" + act.get("activityTime"),
                (String) act.get("uid")));

        adp.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
