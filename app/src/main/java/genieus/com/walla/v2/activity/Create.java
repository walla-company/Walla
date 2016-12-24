package genieus.com.walla.v2.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

public class Create extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private TextView start_time, end_time, location, visibility_label, title_label, start_time_label,
            end_time_label, location_label, details_label, host_label, group_label, interest_label,
            friends_label, guests_label;
    private RelativeLayout map_container;
    private Button post;
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "Places";
    private String BUTTONBLUE = "#63CAF9";

    private Fonts fonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        fonts = new Fonts(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        start_time = (TextView) findViewById(R.id.start_time_in);
        end_time = (TextView) findViewById(R.id.end_time_in);
        location = (TextView) findViewById(R.id.location_in);
        map_container = (RelativeLayout) findViewById(R.id.location_pin_container);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(location);
            }
        });

        map_container.setVisibility(View.GONE);

        post = (Button) findViewById(R.id.post_btn);
        changeBackgroundColor(post, BUTTONBLUE);

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
        interest_label = (TextView) findViewById(R.id.interests_label);
        interest_label.setTypeface(fonts.AzoSansRegular);
        friends_label = (TextView) findViewById(R.id.friends_label);
        friends_label.setTypeface(fonts.AzoSansRegular);
        guests_label = (TextView) findViewById(R.id.guests_label);
        guests_label.setTypeface(fonts.AzoSansRegular);

    }

    private void changeBackgroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }

    private void showDateDialog(){
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        int yr = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, yr, month, day).show();
    }

    private void showTimeDialog(){
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        int h = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int m = cal.get(java.util.Calendar.MINUTE);

        new TimePickerDialog(this, this, h, m, false).show();
    }

    private void setMarker(LatLng place){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place , 17);
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
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
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
                // The user canceled the operation.
            }
        }
    }

    private void initLocation(Place place) {
        map_container.setVisibility(View.VISIBLE );
        LatLng loc = place.getLatLng();
        setMarker(loc);
        location.setText(place.getName());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        showTimeDialog();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
