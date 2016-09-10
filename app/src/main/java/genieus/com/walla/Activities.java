package genieus.com.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Activities extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rv;
    ListView lv;
    RelativeLayout profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi(){
        rv = (RecyclerView) findViewById(R.id.interests);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        profile = (RelativeLayout) findViewById(R.id.profile_btn);
        profile.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.events);

        List<Event> events = new ArrayList<>();
        events.add(new Event("Event Title Goes Here", "Other", "12:00 AM", "Arya Stark"));

        EventAdapter adp = new EventAdapter(this, R.layout.event_template, events);
        lv.setAdapter(adp);


        List<Interests> interests = new ArrayList<>();
        interests.add(new Interests("All", R.drawable.ic_select_all));
        interests.add(new Interests("Art", R.drawable.ic_art));
        interests.add(new Interests("School", R.drawable.ic_school));
        interests.add(new Interests("Sports", R.drawable.ic_sports));
        interests.add(new Interests("Rides", R.drawable.ic_rides));
        interests.add(new Interests("Games", R.drawable.ic_games));
        interests.add(new Interests("Food", R.drawable.ic_food));
        interests.add(new Interests("Other", R.drawable.ic_other));

        InterestsRVAdapter adapter = new InterestsRVAdapter(interests);
        rv.setAdapter(adapter);
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
}
