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
import org.w3c.dom.Text;

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

    private ImageButton interested_btn, going_btn;
    private ProgressBar progress;
    private Button delete_btn;
    private CircleImageView host_image1, host_image2;
    private LinearLayout discussion_area;
    private CardView details_cv;
    private EditText comment_in;
    private RelativeLayout host_container, main_container;
    private TextView title, host_name1, host_name2, details,
            get_directions, interested_count, going_count, interested_in, going_in;

    private MenuItem delete;

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

        if(event.getHost().equals(auth.getCurrentUser().getUid())){
            delete.setVisible(true);
        }

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

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        details_cv = (CardView) findViewById(R.id.details_card);

        interested_btn = (ImageButton) findViewById(R.id.interested_btn1);
        interested_btn.setOnClickListener(this);

        going_btn = (ImageButton) findViewById(R.id.going_btn1);
        going_btn.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);
        title.setTypeface(fonts.AzoSansRegular);
        String msg = String.format("%s %s", event.getTitle(), isFreeFoodActivity(event) ? "Free food!" : "");
        title.setText(msg);

        get_directions = (TextView) findViewById(R.id.get_directions);
        get_directions.setTypeface(fonts.AzoSansRegular);
        get_directions.setOnClickListener(this);

        interested_count = (TextView) findViewById(R.id.interested_count1);
        interested_count.setTypeface(fonts.AzoSansRegular);
        interested_count.setText("" + event.getInterested_list().size());

        going_count = (TextView) findViewById(R.id.going_count1);
        going_count.setTypeface(fonts.AzoSansRegular);
        going_count.setText("" + event.getGoing_list().size());

        interested_in = (TextView) findViewById(R.id.interested_in);
        interested_in.setTypeface(fonts.AzoSansRegular);
        interested_in.setText(getInterestedString());

        going_in = (TextView) findViewById(R.id.going_in);
        going_in.setTypeface(fonts.AzoSansRegular);
        going_in.setText(getGoingString());

        details = (TextView) findViewById(R.id.details);
        details.setTypeface(fonts.AzoSansRegular);
        String info = String.format("%s at %s at %s", event.getStringDate(event.getStart_time()), event.getStringTime(event.getStart_time(), true), event.getLocation_name());
        details.setText(info);

        discussion_area = (LinearLayout) findViewById(R.id.comment_section);

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

                                    }
                                }, auth.getCurrentUser().getUid(), message, event.getAuid());

                                Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
                                MessageInfo info = new MessageInfo(auth.getCurrentUser().getUid(), message);
                                discussion_area.addView(getComment(info));

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


        if(event.getInterested_list().contains(auth.getCurrentUser().getUid())){
            interested_btn.setBackgroundResource(R.mipmap.interested_btn_active);
            isInterested = true;
            isGoing = false;
        }else if(event.getGoing_list().contains(auth.getCurrentUser().getUid())){
            going_btn.setBackgroundResource(R.mipmap.going_btn_active);
            isInterested = false;
            isGoing = true;
        }

        host_image1 = (CircleImageView) findViewById(R.id.host_image1);
        host_image2 = (CircleImageView) findViewById(R.id.host_image2);

        host_name1 = (TextView) findViewById(R.id.host_name1);
        host_name1.setTypeface(fonts.AzoSansRegular);
        host_name2 = (TextView) findViewById(R.id.host_name2);
        host_name2.setTypeface(fonts.AzoSansRegular);

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                UserInfo user = (UserInfo) data;

                host_name1.setText(user.getFirst_name());
                host_name2.setText(user.getFirst_name());

                setImage(host_image1, user.getProfile_url());
                setImage(host_image2, user.getProfile_url());
            }
        }, event.getHost());

        loadComments();

    }

    private boolean isFreeFoodActivity(EventInfo event){
        boolean freeFood = false;
        for(String interest : event.getInterests()){
            if(interest.toLowerCase().contains("food")){
                freeFood = true;
            }
        }

        return freeFood;
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
        name.setTypeface(fonts.AzoSansRegular);
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
                name.setText(user.getFirst_name());
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
        mMap.getUiSettings().setScrollGesturesEnabled(false);
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

            interested_btn.setBackgroundResource(R.mipmap.interested_btn_active);
            going_btn.setBackgroundResource(R.mipmap.going_btn1);

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

            going_btn.setBackgroundResource(R.mipmap.going_btn_active);
            interested_btn.setBackgroundResource(R.mipmap.interested_btn1);

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
            case R.id.interested_btn1:
                interested();
                break;
            case R.id.going_btn1:
                going();
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

        if(event.getLocation_lat() == 0 && event.getLocation_long() == 0){
            map_container.setVisibility(View.GONE);
            get_directions.setVisibility(View.GONE);
        }else {
            setMarker(new LatLng(event.getLocation_lat(), event.getLocation_long()));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        delete = menu.findItem(R.id.action_delete).setVisible(false);

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
            case R.id.action_delete:
                deleteActivity();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }
}
