package genieus.com.walla;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Activities extends AppCompatActivity implements View.OnClickListener, InterestsRVAdapter.ItemClickListener, ChildEventListener {

    private DatabaseReference mDatabase;
    InterestsRVAdapter adapter;
    FirebaseUser user;
    RecyclerView rv;
    ListView lv;
    RelativeLayout profile;
    List<Event> events;
    EventAdapter adp;
    ProgressBar loading;
    TextView notice;
    ImageView write;
    final double SECONDS_IN_DAY = 86400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

        if(!hasActiveInternetConnection()){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    public static boolean hasActiveInternetConnection() {
        //TODO implement
        return true;
    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        startLoadingCounter();

        events = new ArrayList<>();
        lv = (ListView) findViewById(R.id.events);
        adp = new EventAdapter(Activities.this, R.layout.event_template, events);
        adp.getFilter().filter("");
        lv.setAdapter(adp);

        write = (ImageView) findViewById(R.id.icon);
        notice = (TextView) findViewById(R.id.notice);
        rv = (RecyclerView) findViewById(R.id.interests);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        profile = (RelativeLayout) findViewById(R.id.profile_btn);
        profile.setOnClickListener(this);
        write.setOnClickListener(this);

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

        adapter = new InterestsRVAdapter(interests, this, this);
        rv.setAdapter(adapter);


        getFeed();
        try{
            Bundle ex = getIntent().getExtras();
            if(ex.getBoolean("first")){
                showWelcome();
            }
        }catch (Exception e){

        }

        try{
            Bundle ex = getIntent().getExtras();
            if(ex.getBoolean("first")){
                showFirstTimeWelcome();
            }
        }catch (Exception e){

        }

    }

    private void showFirstTimeWelcome(){
        new AlertDialog.Builder(this)
                .setTitle("Hello new wallaby!")
                .setMessage("Welcome to Walla! You're now part of the Duke community that loves to spontaneously hangout. Too many emails about events? We agree, and that's why we made this app where you can simply openly invite everyone on campus to the cool activity you're hosting or attending. Just remember, spontaneous = within 24 hours only!\n")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void startLoadingCounter(){

    }

    private void getFeed(){
        mDatabase.child("activities").orderByChild("activityTime")
                .startAt((System.currentTimeMillis() / 1000) - SECONDS_IN_DAY)
                .addChildEventListener(this);

    }

    private void showWelcome(){
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();

                        Snackbar snack = Snackbar.make(profile, "Welcome back, " + userInfo.get("name") + "!", Snackbar.LENGTH_LONG);
                        View view = snack.getView();
                        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        snack.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    private void createPost(){
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
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
            createPost();
        }else if(id == R.id.actions_notifs){
            Intent intent = new Intent(this, MyInterests.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(v.getId() == R.id.profile_btn){
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }else if(id == R.id.icon){
            createPost();
        }else if(id == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            }
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
    public void onItemClicked(final Interests event, View view, List<View> all, int position) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(getColor(event.getName())));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(getColor(event.getName())));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(getColor(event.getName())));
        }

        String search = event.getName().equals("All") ? "" : event.getName();
        adp.getFilter().filter(search);

        for(int i= 0; i < all.size(); i++){
            if(i != position){
                Drawable bg = all.get(i).getBackground();
                if (bg instanceof ShapeDrawable) {
                    ((ShapeDrawable)bg).getPaint().setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof GradientDrawable) {
                    ((GradientDrawable)bg).setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof ColorDrawable) {
                    ((ColorDrawable)bg).setColor(getResources().getColor(R.color.LightGrey));
                }
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
                (String) act.get("uid"),
                (String) act.get("location"),
                (String) act.get("key"),
                (long) act.get("numberGoing")));

        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                return Double.compare(rhs.getRawTime(), lhs.getRawTime());
            }
        });

        adp.notifyDataSetChanged();
        adp.getFilter().filter("");

        if(loading.getVisibility() == View.VISIBLE){
            loading.setVisibility(View.GONE);
        }
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
