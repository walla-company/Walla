package genieus.com.walla.v2.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.InterestsViewRVAdapter;
import genieus.com.walla.v2.adapter.recyclerview.MiniGroupRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.InterestInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Create extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, DialogInterface.OnClickListener {
    private static final int INVITEFRIENDS = 2;
    private static final int INVITEGROUPS = 3;
    private static final int INTERESTS = 4;
    private static final int HOSTGROUP = 5;
    private GoogleMap mMap;
    private TextView start_time, end_time, location, visibility_label, title_label, start_time_label,
            end_time_label, location_label, details_label, host_label, group_label, interest_label,
            friends_label, guests_label, friends_in, guests_in, location2_label,
            interest_in, title_in, free_food_in;
    private RelativeLayout map_container, group_in, host_in, host_container, location2;
    private Button post;
    private ImageButton chill, lit;
    private RecyclerView groups_rv, host_group_rv;
    private FrameLayout host_click;
    private MiniGroupRVAdapter adapter, hostAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "Places";
    private String BUTTONBLUE = "#63CAF9";
    private AlertDialog.Builder guestInviteBuilder, confirmCreate;
    private AlertDialog guestInviteAlert;
    private CharSequence[] guestsInviteOptions = {"Yes", "No"};
    private CharSequence[] createOptions = {"Yes", "No"};
    private FirebaseAuth auth;
    private UserInfo user;

    private JSONObject postObj;
    private EditText details_in, location2_in;

    private boolean choosingStartTime;
    private Calendar time;

    private Fonts fonts;
    private WallaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initGoogleClient();
        initUi();
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void initUi() {
        auth = FirebaseAuth.getInstance();
        api = WallaApi.getInstance(this);
        fonts = new Fonts(this);
        postObj = new JSONObject();

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                user = (UserInfo) data;
                if(!user.isVerified()){
                    AlertDialog.Builder unverifiedBuilder = new AlertDialog.Builder(Create.this);
                    unverifiedBuilder.setTitle("Verify email");
                    unverifiedBuilder.setMessage("You must verify you email adress before you can post activities on Walla");
                    unverifiedBuilder.setCancelable(false)
                            .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    api.requestVerification(auth.getCurrentUser().getEmail(), auth.getCurrentUser().getUid());
                                    Toast.makeText(Create.this, "Verification email sent to " + auth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });

                    unverifiedBuilder.show();
                }
            }
        }, auth.getCurrentUser().getUid());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        location2 = (RelativeLayout) findViewById(R.id.location2_container);
        location2.setVisibility(View.GONE);

        location2_in = (EditText) findViewById(R.id.location2_in);
        location2_in.setTypeface(fonts.AzoSansRegular);

        host_click = (FrameLayout) findViewById(R.id.host_click);
        host_click.setOnClickListener(this);

        lit = (ImageButton) findViewById(R.id.fire_btn);
        lit.setOnClickListener(this);
        chill = (ImageButton) findViewById(R.id.chill_btn);
        chill.setOnClickListener(this);
        try {
            postObj.put("activity_public", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        start_time = (TextView) findViewById(R.id.start_time_in);
        end_time = (TextView) findViewById(R.id.end_time_in);
        location = (TextView) findViewById(R.id.location_in);
        map_container = (RelativeLayout) findViewById(R.id.location_pin_container);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingStartTime = true;
                start_time.setError(null);
                showDateDialog();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingStartTime = false;
                end_time.setError(null);
                showDateDialog();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(location);
                location.setError(null);
            }
        });

        map_container.setVisibility(View.GONE);

        post = (Button) findViewById(R.id.post_btn);
        changeBackgroundColor(post, BUTTONBLUE);
        post.setTypeface(fonts.AzoSansMedium);
        post.setOnClickListener(this);

        guestInviteBuilder = new AlertDialog.Builder(this);
        guestInviteBuilder.setTitle("Select an option");
        guestInviteBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        guestInviteBuilder.setSingleChoiceItems(guestsInviteOptions, -1, this);
        guestInviteAlert = guestInviteBuilder.create();

        location2_label = (TextView) findViewById(R.id.location2_label);
        location2_label.setTypeface(fonts.AzoSansRegular);

        visibility_label = (TextView) findViewById(R.id.visibility_label);
        visibility_label.setTypeface(fonts.AzoSansRegular);
        title_label = (TextView) findViewById(R.id.title_label);
        title_label.setTypeface(fonts.AzoSansRegular);
        start_time_label = (TextView) findViewById(R.id.start_time_label);
        start_time_label.setTypeface(fonts.AzoSansRegular);
        end_time_label = (TextView) findViewById(R.id.end_time_label);
        end_time_label.setTypeface(fonts.AzoSansRegular);
        location_label = (TextView) findViewById(R.id.location_label);
        location_label.setTypeface(fonts.AzoSansRegular);
        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansRegular);
        group_label = (TextView) findViewById(R.id.groups_label);
        group_label.setTypeface(fonts.AzoSansRegular);
        group_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteGroups();
            }
        });
        interest_label = (TextView) findViewById(R.id.interests_label);
        interest_label.setTypeface(fonts.AzoSansRegular);
        friends_label = (TextView) findViewById(R.id.friends_label);
        friends_label.setTypeface(fonts.AzoSansRegular);
        guests_label = (TextView) findViewById(R.id.guests_label);
        guests_label.setTypeface(fonts.AzoSansRegular);
        host_label = (TextView) findViewById(R.id.host_label);
        host_label.setTypeface(fonts.AzoSansRegular);

        free_food_in = (TextView) findViewById(R.id.free_food_in);
        free_food_in.setTypeface(fonts.AzoSansRegular);
        free_food_in.setOnClickListener(this);
        friends_in = (TextView) findViewById(R.id.friends_in);
        friends_in.setTypeface(fonts.AzoSansRegular);
        friends_in.setOnClickListener(this);
        title_in = (TextView) findViewById(R.id.title_in);
        title_in.setTypeface(fonts.AzoSansRegular);
        title_in.setTextColor(start_time.getTextColors());
        guests_in = (TextView) findViewById(R.id.guests_in);
        guests_in.setTypeface(fonts.AzoSansRegular);
        guests_in.setOnClickListener(this);
        guests_in.setText("Yes"); //default
        try {
            postObj.put("can_others_invite", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        interest_in = (TextView) findViewById(R.id.interests_in);
        interest_in.setTypeface(fonts.AzoSansRegular);
        interest_in.setOnClickListener(this);
        title_in = (EditText) findViewById(R.id.title_in);
        title_in.setTypeface(fonts.AzoSansRegular);
        details_in = (EditText) findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansRegular);
        details_in.setTextColor(start_time.getTextColors());
        group_in = (RelativeLayout) findViewById(R.id.group_in);
        group_in.setOnClickListener(this);
        host_in = (RelativeLayout) findViewById(R.id.host_in);
        host_in.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        groups_rv = (RecyclerView) findViewById(R.id.groups_rv);
        host_group_rv = (RecyclerView) findViewById(R.id.group_host_rv);
        groups_rv.setLayoutManager(manager);
        host_group_rv.setLayoutManager(manager2);

        api.isUserSuspended(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                boolean isSuspended = (boolean) data;
                if(isSuspended){
                    AlertDialog.Builder suspendedBuilder = new AlertDialog.Builder(Create.this);
                    suspendedBuilder.setTitle("Cannot post");
                    suspendedBuilder.setMessage("Your account has been suspended, you cannot post activities");
                    suspendedBuilder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   finish();
                                }
                            });

                    suspendedBuilder.show();
                }
            }
        }, auth.getInstance().getCurrentUser().getUid());

        JSONArray ar = new JSONArray();

        try {
            postObj.put("interests", ar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeBackgroundColor(View view, String color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(Color.parseColor(color));
        }
    }

    private void showDateDialog() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        int yr = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, yr, month, day).show();
    }

    private void showTimeDialog() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        int h = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int m = cal.get(java.util.Calendar.MINUTE);

        new TimePickerDialog(this, this, h, m, false).show();
    }

    private void setMarker(LatLng place) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, 17);
        mMap.addMarker(new MarkerOptions().position(place).title("EventInfo location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        setMarker(sydney);
    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                initLocation(place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                showLocationOptional();
            }
        } else if (requestCode == INVITEFRIENDS) {
            if (resultCode == RESULT_OK) {
                initFriends(data);
            }
        } else if (requestCode == INVITEGROUPS) {
            if (resultCode == RESULT_OK) {
                initGroups(data);
            }
        } else if (requestCode == INTERESTS) {
            if (resultCode == RESULT_OK) {
                initInterests(data);
            }
        } else if (requestCode == HOSTGROUP) {
            if(data != null)
                initGroupHost(data);
        }
    }

    private void showLocationOptional(){
        postObj.remove("location_name");

        location2.setVisibility(View.VISIBLE);
        location_label.append(" (optional)");
        location2_in.requestFocus();
    }

    private void initGroupHost(Intent data) {
        String info = data.getStringExtra("result");
        JSONArray array = null;
        try {
            array = new JSONArray(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<GroupInfo> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject group = null;
            try {
                group = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list.add(new GroupInfo(group.getString("name"), group.getString("abbr"), group.getString("color")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                postObj.put("host_group", group.getString("guid"));
                postObj.put("host_group_name", group.getString("name"));
                postObj.put("host_group_short_name", group.getString("abbr"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Log.d("hostdata", list.size() + " = size");
        hostAdapter = new MiniGroupRVAdapter(this, list);
        host_group_rv.setAdapter(hostAdapter);
    }

    private void initInterests(Intent data) {
        String info = data.getStringExtra("result");
        interest_in.setText(info);
        String[] interests = info.replace(" ", "").split(",");
        JSONArray array = new JSONArray(Arrays.asList(interests));
        try {
            postObj.put("interests", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initGroups(Intent data) {
        String info = data.getStringExtra("result");
        JSONArray array = null;
        try {
            array = new JSONArray(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<GroupInfo> list = new ArrayList<>();
        List<String> identifiers = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject group = null;
            try {
                group = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list.add(new GroupInfo(group.getString("name"), group.getString("abbr"), group.getString("color")));
                identifiers.add(group.getString("guid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray arr = new JSONArray(identifiers);
        try {
            postObj.put("invited_groups", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new MiniGroupRVAdapter(this, list);
        groups_rv.setAdapter(adapter);
    }

    private void initFriends(Intent data) {
        String info = data.getStringExtra("result");
        try {
            JSONArray array = new JSONArray(info);
            JSONArray strArray = new JSONArray();
            String[] friends = new String[array.length()];
            for(int i = 0; i < array.length(); i++){
                strArray.put(array.getJSONObject(i).get("uid"));
                friends[i] = array.getJSONObject(i).getString("name");
            }

            JSONArray params = new JSONArray(Arrays.asList(friends));
            postObj.put("invited_users", strArray);
            friends_in.setText(Arrays.asList(friends).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initLocation(Place place) {
        location2.setVisibility(View.GONE);
        map_container.setVisibility(View.VISIBLE);
        LatLng loc = place.getLatLng();
        setMarker(loc);
        location.setText(place.getName());

        try {
            postObj.put("location_name", place.getName());
            postObj.put("location_lat", place.getLatLng().latitude);
            postObj.put("location_long", place.getLatLng().longitude);
            postObj.put("location_address", place.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initStartTime(Calendar time) {
        String day = "";
        SimpleDateFormat format1 = new SimpleDateFormat("MMM d, h:mm aaa");
        SimpleDateFormat format2 = new SimpleDateFormat("h:mm aaa");
        Calendar now = Calendar.getInstance();
        if (time.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && time.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            int diff = time.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
            if (diff == 0) {
                day = "Today, " + format2.format(time.getTime());
            } else if (diff == 1) {
                day = "Tomorrow, " + format2.format(time.getTime());
            } else {

                day = format1.format(time.getTime());
            }
        } else {

        }

        try {
            postObj.put("start_time", (long) time.getTimeInMillis() / 1000);

            Calendar midnight = Calendar.getInstance();
            midnight.setTimeInMillis(time.getTimeInMillis());
            midnight.set(Calendar.HOUR_OF_DAY, 0);
            midnight.set(Calendar.MINUTE, 0);
            midnight.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);

            postObj.put("end_time", (long) midnight.getTimeInMillis() / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        start_time.setText(day);
    }

    private void initEndTime(Calendar time) {
        String day = "";
        SimpleDateFormat format1 = new SimpleDateFormat("MMM d, h:mm aaa");
        SimpleDateFormat format2 = new SimpleDateFormat("h:mm aaa");
        Calendar now = Calendar.getInstance();
        if (time.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && time.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            int diff = time.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
            if (diff == 0) {
                day = "Today, " + format2.format(time.getTime());
            } else if (diff == 1) {
                day = "Tomorrow, " + format2.format(time.getTime());
            } else {

                day = format1.format(time.getTime());
            }
        } else {

        }

        try {
            postObj.put("end_time", (long)time.getTimeInMillis() / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        end_time.setText(day);
    }

    private void initGuestInvitations(int which) {
        guests_in.setText(guestsInviteOptions[which]);
        try {
            postObj.put("can_others_invite", which == 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGroupHost(){
        Intent intent = new Intent(this, MyGroups.class);
        intent.putExtra("max", 1);
        startActivityForResult(intent, HOSTGROUP);
    }

    private void inviteFriends() {
        Intent intent = new Intent(this, Friends.class);
        startActivityForResult(intent, INVITEFRIENDS);
    }

    private void inviteGroups() {
        Intent intent = new Intent(this, MyGroups.class);
        intent.putExtra("max", -1);
        startActivityForResult(intent, INVITEGROUPS);
    }

    private void showGuestInviteOptions() {
        guestInviteAlert.show();
    }

    private void postActivity() {
        try {
            postObj.put("title", title_in.getText().toString());
            postObj.put("details", details_in.getText().toString());
            postObj.put("host", auth.getCurrentUser().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isValidActivity()) {
            confirmCreate = new AlertDialog.Builder(this);
            confirmCreate.setTitle("Confirm");

            confirmCreate.setTitle("Are you sure you want to create this event");
            confirmCreate.setItems(createOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            //yes
                            if(user.isVerified()){
                                Log.d("postdata", postObj.toString());
                                api.postActivity(new WallaApi.OnDataReceived() {
                                    @Override
                                    public void onDataReceived(Object data, int call) {
                                        Home.refreshPage(false);
                                    }
                                }, postObj);
                                finish();
                            }else{
                                Snackbar.make(map_container, "Email not verified", Snackbar.LENGTH_INDEFINITE).setAction("VERIFY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        api.requestVerification(auth.getCurrentUser().getEmail(), auth.getCurrentUser().getUid());
                                        Toast.makeText(Create.this,  "Email sent to " + auth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                    }
                                }).show();
                            }

                            dialog.cancel();
                            break;
                        case 1:
                            //no
                            dialog.cancel();
                            break;
                        default:
                            dialog.cancel();
                            break;
                    }
                }
            });

            confirmCreate.setCancelable(false);
            confirmCreate.show();
        } else {
            Toast.makeText(this, "Required data is missing", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isValidActivity() {
        boolean valid = true;

        if (title_in.getText().toString().equals("")) {
            title_in.setError("required");
            valid = false;
        }

        if (!postObj.has("can_others_invite")) {
            guests_in.setError("required");
            valid = false;
        }

        if (!postObj.has("activity_public")) {
            //visibility_in.setError("required");
            valid = false;
        }

        if (!postObj.has("start_time")) {
            start_time.setError("required");
            valid = false;
        }

        if (!postObj.has("end_time")) {
            end_time.setError("required");
        }


        if (!postObj.has("location_name")) {
            if(location2_in.getText().toString().isEmpty()){
                location2_in.setError("required");
                valid = false;
            }else {
                try {
                    postObj.put("location_name", location2_in.getText().toString());
                    postObj.put("location_lat", 0);
                    postObj.put("location_long", 0);
                    postObj.put("location_address", location2_in.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!postObj.has("start_time")) {
            start_time.setError("required");
            valid = false;
        }

        if (!postObj.has("interests")) {
            interest_in.setError("required");
            valid = false;
        }

        return valid;

    }

    private void changeBackgroundColor(View view, int color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(color);
        }
    }

    private void showFreeFoodOptions(){
        AlertDialog.Builder foodAlert = new AlertDialog.Builder(this);
        foodAlert.setTitle("Confirm");

        foodAlert.setTitle("Will there be free food at the activity?");
        foodAlert.setItems(new String[]{"Yes!", "No"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //yes
                        foodYesClicked();
                        dialog.cancel();
                        break;
                    case 1:
                        //no
                        foodNoClicked();
                        dialog.cancel();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });

        foodAlert.setCancelable(false);
        foodAlert.show();
    }

    private void foodYesClicked(){
        JSONArray ar = new JSONArray();
        free_food_in.setText("Yes!");

        try {
            ar.put("Free Food");
            postObj.put("interests", ar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void foodNoClicked(){
        JSONArray ar = new JSONArray();
        free_food_in.setText("No");

        try {
            ar.put("-");
            postObj.put("interests", ar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        time = Calendar.getInstance();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, monthOfYear);
        time.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        showTimeDialog();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
        time.set(Calendar.MINUTE, minute);

        if (choosingStartTime) {
            initStartTime(time);
        } else {
            initEndTime(time);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.friends_in:
                inviteFriends();
                break;
            case R.id.group_in:
                inviteGroups();
                break;
            case R.id.host_click:
                Log.d("host_in", "click");
                setGroupHost();
                break;
            case R.id.guests_in:
                showGuestInviteOptions();
                break;
            case R.id.interests_in:
                startActivityForResult(new Intent(this, InterestsView.class), INTERESTS);
                interest_in.setError(null);
                break;
            case R.id.post_btn:
                postActivity();
                break;
            case R.id.free_food_in:
                showFreeFoodOptions();
                break;
            case R.id.chill_btn:
                chill.setImageResource(R.mipmap.chillbtn);
                lit.setImageResource(R.mipmap.public_pressed);
                try {
                    postObj.put("activity_public", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fire_btn:
                lit.setImageResource(R.mipmap.firebtn);
                chill.setImageResource(R.mipmap.private_pressed);
                try {
                    postObj.put("activity_public", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
       if (dialog.equals(guestInviteAlert))
            initGuestInvitations(which);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
