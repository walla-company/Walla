package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;
import genieus.com.walla.v2.info.Fonts;

public class Details extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private Fonts fonts;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private RecyclerView tabs;
    private RelativeLayout host_container;
    private TextView duration, title, location_label, location, show_on_map, interested, going, invitees_label, invitee_in,
            host_name, host_info, details_in, details_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initUi();
        initGoogleClient();
    }

    private void initUi() {
        fonts = new Fonts(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tabs = (RecyclerView) findViewById(R.id.tabs_rv);
        LinearLayoutManager horizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        tabs.setLayoutManager(horizontal);

        TabRVAdapter tabAdapter = new TabRVAdapter(this, new ArrayList<String>(Arrays.asList("Movies", "Academics")));
        tabs.setAdapter(tabAdapter);

        host_container = (RelativeLayout) findViewById(R.id.host_container);
        host_container.setOnClickListener(this);

        duration = (TextView) findViewById(R.id.duration);
        duration.setTypeface(fonts.AzoSansRegular);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(fonts.AzoSansMedium);
        location_label = (TextView) findViewById(R.id.location_label);
        location_label.setTypeface(fonts.AzoSansRegular);
        location = (TextView) findViewById(R.id.location_in);
        location.setTypeface(fonts.AzoSansBold);
        show_on_map = (TextView) findViewById(R.id.show_on_map_label);
        show_on_map.setTypeface(fonts.AzoSansRegular);
        interested = (TextView) findViewById(R.id.interested_in);
        interested.setTypeface(fonts.AzoSansRegular);
        going = (TextView) findViewById(R.id.going_in);
        going.setTypeface(fonts.AzoSansRegular);
        invitee_in = (TextView) findViewById(R.id.invitees_in);
        invitee_in.setTypeface(fonts.AzoSansMedium);
        invitees_label = (TextView) findViewById(R.id.invitees_label);
        invitees_label.setTypeface(fonts.AzoSansRegular);
        host_name = (TextView) findViewById(R.id.host_name);
        host_name.setTypeface(fonts.AzoSansMedium);
        host_info = (TextView) findViewById(R.id.host_info);
        host_info.setTypeface(fonts.AzoSansRegular);
        details_in = (TextView) findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansMedium);
        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansRegular);
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
                startActivity(new Intent(this, ViewProfile.class));
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        setMarker(sydney);
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
