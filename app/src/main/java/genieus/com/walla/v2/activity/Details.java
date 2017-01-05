package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.GroupTabRVAdapter;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Details extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private Fonts fonts;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private WallaApi api;

    private RecyclerView tabs, groups;
    private CircleImageView host_image;
    private CardView details_cv;
    private RelativeLayout host_container;
    private TextView duration, title, location_label, location, show_on_map, interested, going, invitees_label, invitee_in,
            host_name, details_in, details_label, host_info, get_directions;

    private EventInfo event;
    private RelativeLayout map_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initGoogleClient();

        api = new WallaApi(this);

        Bundle extras = getIntent().getExtras();
        String auid = extras.getString("auid");

        api.getActivity(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                event = (EventInfo) data;
                initUi();
            }
        }, auid);
    }

    private void initUi() {
        fonts = new Fonts(this);

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

        api.getGroup(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("groupdata", ((GroupInfo) data).getName());
                GroupTabRVAdapter groupTabAdapter = new GroupTabRVAdapter(Details.this, new ArrayList<GroupInfo>(Arrays.asList((GroupInfo) data)));
                groups.setAdapter(groupTabAdapter);
            }
        }, event.getHost_group());

        TabRVAdapter tabAdapter = new TabRVAdapter(this, event.getInterests());
        tabs.setAdapter(tabAdapter);

        host_container = (RelativeLayout) findViewById(R.id.host_container);
        host_container.setOnClickListener(this);

        details_cv = (CardView) findViewById(R.id.details_card);

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

        if(event.getDetails() == null || event.getDetails().equals(""))
            details_cv.setVisibility(View.GONE);

        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansRegular);

        host_image = (CircleImageView) findViewById(R.id.host_image);
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                UserInfo user = (UserInfo) data;
                host_name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
                host_info.setText(String.format("%s · %s", user.getYear(), user.getMajor()));
                Picasso.with(Details.this) //Context
                        .load(user.getProfile_url()) //URL/FILE
                        .into(host_image);//an ImageView Object to show the loaded image
            }
        }, event.getHost());

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.host_container:
                Intent intent = new Intent(this, ViewProfile.class);
                intent.putExtra("uid", "user");
                startActivity(intent);
                break;
            case R.id.get_directions:
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", event.getLocation_lat(), event.getLocation_long());
                Intent directions = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(directions);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
