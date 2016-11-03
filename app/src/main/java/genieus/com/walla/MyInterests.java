package genieus.com.walla;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MyInterests extends AppCompatActivity {
    public class Setting{
        String name;
        boolean state;
        public Setting(String name, boolean state){
            this.name = name;
            this.state = state;
        }
    }

    FirebaseUser user;
    private DatabaseReference mDatabase;

    ListView lv;
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUi();
    }

    private void initUi(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(user.getEmail().trim().endsWith("@sandiego.edu")){
            mDatabase = mDatabase.child("sandiego-*-edu");
        }

        lv = (ListView) findViewById(R.id.my_interests);
        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        getInterests();
    }

    private void getInterests(){
        mDatabase.child("notification_settings").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> s = (Map<String, Boolean>) dataSnapshot.getValue();
                Setting[] interests = new Setting[]{new Setting("Art", s.get("art")),
                        new Setting("School", s.get("school")),
                        new Setting("Sports", s.get("sports")),
                        new Setting("Rides", s.get("rides")),
                        new Setting("Games", s.get("games")),
                        new Setting("Food", s.get("food")),
                        new Setting("Other", s.get("other"))};

                loading.setVisibility(View.GONE);
                MyInterestsAdapter adapter = new MyInterestsAdapter(MyInterests.this, R.layout.my_interests_template, interests);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
