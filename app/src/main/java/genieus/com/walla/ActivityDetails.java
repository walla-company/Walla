package genieus.com.walla;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDetails extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseUser user;
    String description, time, location, category, color, key, poster, uid;
    long people;
    boolean expired;
    boolean userSignedUp;

    final private int EXPIRED = 2;
    final private int PRESSED = 1;
    final private int NOT_PRESSED = 0;

    private Map<String, Object> attendees;

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
            expired = extras.getBoolean("expired");
        }

        initUi();
    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        userSignedUp = false;

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
        container.setBackgroundColor(Color.parseColor(color));
        category_tv.setTextColor(Color.parseColor(color));

        attendees = new HashMap<>();
        if(expired) {
            changeBtnColor(EXPIRED);
        }
        else{
            interested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userSignedUp)
                        unrsvp();
                    else
                        rsvp();
                }
            });
        }

        changeBtnColor(NOT_PRESSED);


        getHostPic();
        getAttendees();
    }

    private void getHostPic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://walla-launch.appspot.com");

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        storageRef.child("profile_images").child(uid + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("ppic","pic found");
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                hostPic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                storageRef.child("profile_images").child(uid + ".pnh").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d("ppic", "pic found");
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        hostPic.setImageBitmap(bmp);
                    }
                });
            }
        });
    }

    private void getAttendees(){
        peopleList_tv.setText(poster + " (host)");


        mDatabase.child("attendees").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> p = (Map<String, Object>) dataSnapshot.getValue();
                if(p != null){
                    people = p.keySet().size();
                    for(String id : p.keySet()) {
                        if(id.equals(user.getUid())){
                            userSignedUp = true;
                            changeBtnColor(PRESSED);
                        }

                        if(attendees.containsKey(id)){
                            continue;
                        }

                        final String userid = id;

                        mDatabase.child("users").child(id).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                                        peopleList_tv.append(", " + (String) userInfo.get("name"));
                                        attendees.put(userid, System.currentTimeMillis() / 1000);
                                        Log.d("attendees", "done");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w("get name", "getUser:onCancelled", databaseError.toException());
                                        // ...
                                    }
                                });
                    }

                }else{
                    Log.d("attendees", key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void changeBtnColor(int state){
        if(state == PRESSED) {
            Drawable background = interested.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(getResources().getColor(R.color.DodgerBlue));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(getResources().getColor(R.color.DodgerBlue));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(getResources().getColor(R.color.DodgerBlue));
            }

            interested.setTextColor(Color.WHITE);
        }else if(state == NOT_PRESSED){
            Drawable background = interested.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(getResources().getColor(R.color.white));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(getResources().getColor(R.color.white));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(getResources().getColor(R.color.white));
            }

            interested.setTextColor(Color.BLACK);
        }
        else if(state == EXPIRED){
            Drawable background = interested.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(getResources().getColor(R.color.LightGrey));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(getResources().getColor(R.color.LightGrey));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(getResources().getColor(R.color.LightGrey));
            }

            interested.setTextColor(Color.WHITE);
        }
    }

    private void unrsvp(){
        changeBtnColor(NOT_PRESSED);
        mDatabase.child("attendees/" + key + "/" + user.getUid()).removeValue();
        mDatabase.child("user_attending/" + user.getUid() + "/" + key).removeValue();
        userSignedUp = false;
        int i = peopleList_tv.getText().toString().lastIndexOf(",");
        if(i >= 0)
            peopleList_tv.setText(peopleList_tv.getText().toString().substring(0,i));
    }

    private void rsvp(){
        mDatabase.child("attendees/" + key + "/" + user.getUid()).setValue(System.currentTimeMillis() / 1000);
        mDatabase.child("user_attending/" + user.getUid() + "/" + key).setValue(System.currentTimeMillis() / 1000);
    }

    private void showFlagConfirm(){
        new AlertDialog.Builder(ActivityDetails.this)
                .setIcon(R.drawable.ic_flag)
                .setTitle("Flag")
                .setMessage("Are you sure you want to flag this post?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ActivityDetails.this, "The issue has been sent to the administration for review", Toast.LENGTH_LONG).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }else if(id == R.id.action_flag){
            showFlagConfirm();
        }else if(id == R.id.action_star){
            if(userSignedUp)
                unrsvp();
            else
                rsvp();
        }
        return super.onOptionsItemSelected(item);
    }
}

