package genieus.com.walla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDetails extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String description, time, location, category, color, key, poster, uid;
    long people;

    TextView desc_tv, time_tv, location_tv, category_tv, people_tv, peopleList_tv;
    Button interested;
    CircleImageView hostPic;
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            description = extras.getString("description");
            time = extras.getString("time");
            location = extras.getString("location");
            category = extras.getString("category");
            people = extras.getLong("people");
            color = extras.getString("color");
            key = extras.getString("key");
            poster = extras.getString("poster");
            uid = extras.getString("uid");
        }

        initUi();

    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        desc_tv = (TextView) findViewById(R.id.description);
        time_tv = (TextView) findViewById(R.id.time);
        location_tv = (TextView) findViewById(R.id.location);
        category_tv = (TextView) findViewById(R.id.category);
        people_tv = (TextView) findViewById(R.id.going);
        peopleList_tv = (TextView) findViewById(R.id.list_going);
        interested = (Button) findViewById(R.id.interested_btn);
        container = (RelativeLayout) findViewById(R.id.cat_container);
        hostPic = (CircleImageView) findViewById(R.id.host_img);

        desc_tv.setText(description);
        time_tv.setText(time);
        location_tv.setText("Location: " + location);
        category_tv.setText(category);
        people_tv.setText("People going: " + people);
        container.setBackgroundColor(Color.parseColor(color));
        category_tv.setTextColor(Color.parseColor(color));

        getHostPic();
        getAttendees();

    }

    private void getHostPic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://walla-launch.appspot.com");

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        storageRef.child("profile_images").child(uid + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("ppic","pic found");
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                hostPic.setImageBitmap(bmp);
            }
        });
    }

    private void getAttendees(){
        peopleList_tv.setText("");
        peopleList_tv.append(poster + " (host)");

        /*
        final DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("attendees").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> attendee = dataSnapshot.getValue(Map.class);
                for(String id : attendee.keySet()){
                    mDatabase.child("users").child(id).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                                    peopleList_tv.append(", " + (String) userInfo.get("name"));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("get name", "getUser:onCancelled", databaseError.toException());
                                    // ...
                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
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
