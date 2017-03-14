package genieus.com.walla.v2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.GroupTabRVAdapter;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.MessageInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Details extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private Fonts fonts;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private WallaApi api;
    private FirebaseAuth auth;

    private static final int INVITEFRIENDS = 2;

    private ImageButton interested_btn, going_btn, share_btn, invite_btn;
    private ProgressBar progress;
    private Button delete_btn;
    private RecyclerView tabs, groups;
    private CircleImageView host_image;
    private LinearLayout discussion_area;
    private CardView details_cv;
    private EditText comment_in;
    private RelativeLayout host_container, main_container;
    private TextView duration, title, location_label, location, show_on_map, interested, going, invitees_label, invitee_in,
            host_name, details_in, details_label, host_info, get_directions, interested_count, going_count, interested_in, going_in;

    private EventInfo event;
    private RelativeLayout map_container;

    private boolean isGoing, isInterested;
    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initGoogleClient();
        main_container = (RelativeLayout) findViewById(R.id.main_container);
        main_container.setVisibility(View.GONE);
        api = WallaApi.getInstance(this);
        auth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        String auid = extras.getString("auid");

        api.getActivity(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                event = (EventInfo) data;
                try {
                    api.getUserInfo(new WallaApi.OnDataReceived() {
                        @Override
                        public void onDataReceived(Object data, int call) {
                            user = (UserInfo) data;
                            initUi();
                        }
                    }, auth.getCurrentUser().getUid());
                }catch (Exception e){
                    Toast.makeText(Details.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, auid);
    }

    private void initUi() {
        fonts = new Fonts(this);

        main_container.setVisibility(View.VISIBLE);

        if(event.isDeleted()){
            main_container.setVisibility(View.GONE);

            AlertDialog.Builder alertOfDelete = new AlertDialog.Builder(Details.this);
            alertOfDelete.setTitle("Activity cannot be viewed");
            alertOfDelete.setMessage("This activity no longer exists");
            alertOfDelete.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            alertOfDelete.show();
        }

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map_container = (RelativeLayout) findViewById(R.id.map_container);

        tabs = (RecyclerView) findViewById(R.id.tabs_rv);
        groups = (RecyclerView) findViewById(R.id.groups_rv);
        LinearLayoutManager horizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager horizontal2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        tabs.setLayoutManager(horizontal);
        groups.setLayoutManager(horizontal2);

        if(event.getHost_group() != null && event.getHost_group().equals("")) {
            api.getGroup(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    GroupTabRVAdapter groupTabAdapter = new GroupTabRVAdapter(Details.this, new ArrayList<GroupInfo>(Arrays.asList((GroupInfo) data)));
                    groups.setAdapter(groupTabAdapter);
                }
            }, event.getHost_group());
        }

        TabRVAdapter tabAdapter = new TabRVAdapter(this, event.getInterests());
        tabs.setAdapter(tabAdapter);

        host_container = (RelativeLayout) findViewById(R.id.host_container);
        host_container.setOnClickListener(this);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        details_cv = (CardView) findViewById(R.id.details_card);

        interested_btn = (ImageButton) findViewById(R.id.interested_btn);
        interested_btn.setOnClickListener(this);
        going_btn = (ImageButton) findViewById(R.id.going_btn);
        going_btn.setOnClickListener(this);
        share_btn = (ImageButton) findViewById(R.id.share_btn);
        share_btn.setOnClickListener(this);
        invite_btn = (ImageButton) findViewById(R.id.invite_btn);
        invite_btn.setOnClickListener(this);

        duration = (TextView) findViewById(R.id.duration);
        duration.setTypeface(fonts.AzoSansRegular);
        duration.setText(String.format("%s\nto %s", event.getStringTime(event.getStart_time(), true), event.getStringTime(event.getEnd_time(), false)));
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(fonts.AzoSansRegular);
        title.setText(event.getTitle());
        location_label = (TextView) findViewById(R.id.location_label);
        location_label.setTypeface(fonts.AzoSansRegular);
        location = (TextView) findViewById(R.id.location_in);
        location.setTypeface(fonts.AzoSansMedium);
        location.setText(event.getLocation_name());
        show_on_map = (TextView) findViewById(R.id.show_on_map_label);
        show_on_map.setTypeface(fonts.AzoSansRegular);
        show_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(map_container.getVisibility() == View.GONE){
                    map_container.setVisibility(View.VISIBLE);
                    get_directions.setVisibility(View.VISIBLE);
                    show_on_map.setText("hide map");
                }else{
                    map_container.setVisibility(View.GONE);
                    get_directions.setVisibility(View.GONE);
                    show_on_map.setText("show on map");
                }
            }
        });
        interested = (TextView) findViewById(R.id.interested_in);
        interested.setTypeface(fonts.AzoSansRegular);
        going = (TextView) findViewById(R.id.going_in);
        going.setTypeface(fonts.AzoSansRegular);
        invitee_in = (TextView) findViewById(R.id.invitees_in);
        invitee_in.setTypeface(fonts.AzoSansRegular);
        invitees_label = (TextView) findViewById(R.id.invitees_label);
        invitees_label.setTypeface(fonts.AzoSansRegular);
        host_name = (TextView) findViewById(R.id.host_name);
        host_name.setTypeface(fonts.AzoSansRegular);
        host_info = (TextView) findViewById(R.id.host_info);
        host_info.setTypeface(fonts.AzoSansRegular);
        details_in = (TextView) findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansRegular);
        details_in.setText(event.getDetails());
        host_info = (TextView) findViewById(R.id.host_info);
        host_info.setTypeface(fonts.AzoSansRegular);
        get_directions = (TextView) findViewById(R.id.get_directions);
        get_directions.setTypeface(fonts.AzoSansRegular);
        get_directions.setOnClickListener(this);

        interested_count = (TextView) findViewById(R.id.interested_count);
        interested_count.setTypeface(fonts.AzoSansRegular);
        interested_count.setText("" + event.getInterested_list().size());
        going_count = (TextView) findViewById(R.id.going_count);
        going_count.setTypeface(fonts.AzoSansRegular);
        going_count.setText("" + event.getGoing_list().size());
        interested_in = (TextView) findViewById(R.id.interested_in);
        interested_in.setTypeface(fonts.AzoSansRegular);
        interested_in.setText(getInterestedString());
        going_in = (TextView) findViewById(R.id.going_in);
        going_in.setTypeface(fonts.AzoSansRegular);
        going_in.setText(getGoingString());
        comment_in = (EditText) findViewById(R.id.comment_input);
        comment_in.setTypeface(fonts.AzoSansRegular);
        comment_in.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent ev) {
                boolean handled = false;
                final String message = comment_in.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_in.getWindowToken(), 0);
                    api.isUserSuspended(new WallaApi.OnDataReceived() {
                        @Override
                        public void onDataReceived(Object data, int call) {
                            boolean isSuspended = (boolean) data;
                            if(!isSuspended){
                                api.postComment(new WallaApi.OnDataReceived() {
                                    @Override
                                    public void onDataReceived(Object data, int call) {
                                        Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
                                        loadComments();
                                    }
                                }, auth.getCurrentUser().getUid(), message, event.getAuid());

                            }else{
                                Toast.makeText(getApplicationContext(), "You cannot post comment because your account is suspended", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, auth.getCurrentUser().getUid());
                    //Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
                    //loadComments();
                    comment_in.setText("");

                    handled = true;
                }
                return handled;
            }
        });

        delete_btn = (Button) findViewById(R.id.delete_btn);
        delete_btn.setTypeface(fonts.AzoSansRegular);
        delete_btn.setOnClickListener(this);

        if(event.getHost().equals(auth.getCurrentUser().getUid())){
            delete_btn.setVisibility(View.VISIBLE);
            changeBackGroundColor(delete_btn, getResources().getColor(R.color.lightred));
        }


        if(event.getDetails() == null || event.getDetails().equals(""))
            details_cv.setVisibility(View.GONE);

        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansRegular);

        if(event.getInterested_list().contains(auth.getCurrentUser().getUid())){
            interested_btn.setImageResource(R.mipmap.interestedbuttonpressed);
            isInterested = true;
            isGoing = false;
        }else if(event.getGoing_list().contains(auth.getCurrentUser().getUid())){
            going_btn.setImageResource(R.mipmap.goingbuttonpressed);
            isInterested = false;
            isGoing = true;
        }

        host_image = (CircleImageView) findViewById(R.id.host_image);
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                UserInfo user = (UserInfo) data;
                host_name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
                host_info.setText(String.format("%s · %s", user.getYear(), user.getMajor()));

                if(user.getProfile_url() != null && !user.getProfile_url().equals("")) {
                    if(!user.getProfile_url().startsWith("gs://walla-launch.appspot.com")) {
                        Picasso.with(Details.this) //Context
                                .load(user.getProfile_url()) //URL/FILE
                                .into(host_image);//an ImageView Object to show the loaded image;
                    }else{
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        storage.getReferenceFromUrl(user.getProfile_url()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Picasso.with(Details.this) //Context
                                            .load(task.getResult().toString()) //URL/FILE
                                            .into(host_image);//an ImageView Object to show the loaded image;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }

            }
        }, event.getHost());

        discussion_area = (LinearLayout) findViewById(R.id.comment_section);
        loadComments();

    }

    private void changeBackGroundColor(View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }
    }

    private void loadComments(){
        discussion_area.removeAllViews();

        api.getComments(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                List<MessageInfo> list = (List<MessageInfo>) data;
                for(MessageInfo info : list){
                    discussion_area.addView(getComment(info));
                }
            }
        }, event.getAuid());
    }

    private View getComment(final MessageInfo info){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.single_message, null);

        final CircleImageView image = (CircleImageView) view.findViewById(R.id.profile_image);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView message = (TextView) view.findViewById(R.id.message);
        name.setTypeface(fonts.AzoSansMedium);
        message.setTypeface(fonts.AzoSansRegular);
        message.setText(info.getMessage());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, ViewProfile.class);
                intent.putExtra("uid", info.getUid());
                startActivity(intent);
            }
        };

        name.setOnClickListener(listener);
        image.setOnClickListener(listener);

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                UserInfo user = (UserInfo) data;
                name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
                setImage(image, user.getProfile_url());
            }
        }, info.getUid());

        return view;
    }

    private void setImage(final ImageView image, String url){
        if(url != null && !url.equals("")) {
            if(!url.startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(this) //Context
                        .load(url) //URL/FILE
                        .into(image);//an ImageView Object to show the loaded image;
            }else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(url).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.with(Details.this) //Context
                                    .load(task.getResult().toString()) //URL/FILE
                                    .into(image);//an ImageView Object to show the loaded image;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }

    private String getInterestedString(){
        int people = event.getInterested_list().size();
        switch (people){
            case 0:
                return "Are you interested?";
            default:
                return people + ((people >= 2) ? " people are " : " person is ") + " interested";

        }

    }

    private String getGoingString(){
        int people = event.getGoing_list().size();
        switch (people){
            case 0:
                return "Be the first to RSVP";
            default:
                return people + ((people >= 2) ? " people are " : " person is ") + " going";

        }
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void setMarker(LatLng place){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place , 17);
        mMap.addMarker(new MarkerOptions().position(place).title("EventInfo location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(cameraUpdate);
    }

    private void hostAction(){
        if(event.getHost().equals(auth.getCurrentUser().getUid())){
            Intent intent = new Intent(this, EditProfile.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, ViewProfile.class);
            intent.putExtra("uid", event.getHost());
            startActivity(intent);
        }
    }

    private void interested(){
        if(isInterested){
            Toast.makeText(this, "You are already interested in this event", Toast.LENGTH_LONG).show();
        }else{
            event.getGoing_list().remove(auth.getCurrentUser().getUid());
            event.getInterested_list().add(auth.getCurrentUser().getUid());

            api.interested(auth.getCurrentUser().getUid(), event.getAuid());

            interested_btn.setImageResource(R.mipmap.interestedbuttonpressed);
            going_btn.setImageResource(R.mipmap.goingbtn);

            going_in.setText(getGoingString());
            interested_in.setText(getInterestedString());

            going_count.setText("" + event.getGoing_list().size());
            interested_count.setText("" + event.getInterested_list().size());

            isInterested = true;
            isGoing = false;
        }
    }

    private void going(){
        if(isGoing){
            Toast.makeText(this, "You are already going to this event", Toast.LENGTH_LONG).show();
        }else{
            event.getGoing_list().add(auth.getCurrentUser().getUid());
            event.getInterested_list().remove(auth.getCurrentUser().getUid());

            api.going(auth.getCurrentUser().getUid(), event.getAuid());

            going_btn.setImageResource(R.mipmap.goingbuttonpressed);
            interested_btn.setImageResource(R.mipmap.interestedbtn);

            going_in.setText(getGoingString());
            interested_in.setText(getInterestedString());

            going_count.setText("" + event.getGoing_list().size());
            interested_count.setText("" + event.getInterested_list().size());

            isInterested = false;
            isGoing = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INVITEFRIENDS) {
            if (resultCode == RESULT_OK) {
                initFriends(data);
            }
        }
    }

    private void initFriends(Intent data) {
        String info = data.getStringExtra("result");
        try {
            JSONArray array = new JSONArray(info);
            for(int i = 0; i < array.length(); i++){
                JSONObject person = array.getJSONObject(i);
                api.inviteUser(auth.getCurrentUser().getUid(), person.getString("uid"), event.getAuid());
            }

            String msg = String.format("invitation sent to %d %s", array.length(), (array.length()) == 1 ? "person" : "people");
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void shareEvent(){
        String msg = String.format("You are invited to '%s' from %s to %s at %s",
                event.getTitle(), event.getStringTime(event.getStart_time(), true),
                event.getStringTime(event.getEnd_time(), true), event.getLocation_name());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void deleteActivity(){
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(Details.this);
        confirmDelete.setTitle("Are you sure?");
        confirmDelete.setMessage("You cannot undo this action");
        confirmDelete.setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        api.deleteActivity(auth.getCurrentUser().getUid(), event.getAuid());
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        confirmDelete.show();
    }

    private void flag(){

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.host_container:
                hostAction();
                break;
            case R.id.get_directions:
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", event.getLocation_lat(), event.getLocation_long());
                Intent directions = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(directions);
                break;
            case R.id.interested_btn:
                interested();
                break;
            case R.id.going_btn:
                going();
                break;
            case R.id.invite_btn:
                Intent intent = new Intent(this, Friends.class);
                startActivityForResult(intent, INVITEFRIENDS);
                break;
            case R.id.share_btn:
                shareEvent();
                break;
            case R.id.delete_btn:
                deleteActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMarker(new LatLng(event.getLocation_lat(), event.getLocation_long()));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        switch (id) {
            case R.id.action_interested:
                interested();
                break;
            case R.id.action_going:
                going();
                break;
            case R.id.action_flag:
                flag();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }
}
