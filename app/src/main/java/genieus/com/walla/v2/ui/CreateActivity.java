package genieus.com.walla.v2.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
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

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragments.Home;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.utils.ImageUtils;
import genieus.com.walla.v2.utils.Time;

public class CreateActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;
    private static final int TITLE_CHAR_LIMIT = 200;
    private static final int SECS_IN_HOUR = 3600;

    private static boolean isTitleFilled, isTimeFilled, isLocationFilled;

    private GoogleMap mMap;
    private JSONObject post;

    @BindView(R.id.title_action)
    EditText editTitle;

    @BindView(R.id.date_action)
    TextView editDate;

    @BindView(R.id.location_action)
    EditText editLocation;

    @BindView(R.id.location_pin_action)
    TextView editPin;

    @BindView(R.id.title_prompt)
    TextView promptTitle;

    @BindView(R.id.char_counter)
    TextView titleCharCount;

    @BindView(R.id.date_prompt)
    TextView promptDate;

    @BindView(R.id.location_prompt)
    TextView promptLocation;

    @BindView(R.id.location_pin_prompt)
    TextView promptPin;

    @BindView(R.id.food_prompt)
    TextView promptFood;

    @BindView(R.id.map_container)
    RelativeLayout mapContainer;

    @BindView(R.id.free_food)
    Switch freeFoodSwitch;

    @BindView(R.id.post_btn)
    Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        initUi();
    }

    private void initUi() {
        Fonts.applyFont(Fonts.AzoSansRegular,
                promptTitle, promptDate, promptLocation, promptPin, promptFood, titleCharCount);
        Fonts.applyFont(Fonts.AzoSansRegular, editTitle, editDate, editLocation, editPin);

        post = new JSONObject();
        mapContainer.setVisibility(View.GONE);
        initTitleCharLimit();
        initMapView();

        freeFoodSwitch.setText("No");
        freeFoodSwitch.setTypeface(Fonts.AzoSansRegular);

        postBtn.setTypeface(Fonts.AzoSansRegular);
        postBtn.setVisibility(View.GONE);
        postBtn.setEnabled(false);
        ImageUtils.changeDrawableColor(postBtn.getBackground(),
                getResources().getColor(R.color.lightBlue));
    }

    private void initMapView() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initTitleCharLimit() {
        titleCharCount.setText(String.format("0/%d", TITLE_CHAR_LIMIT));
        editTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TITLE_CHAR_LIMIT)});
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int charCount = s.length();
                titleCharCount.setText(String.format("%d/%d", charCount, TITLE_CHAR_LIMIT));

                if (charCount == 0) {
                    isTitleFilled = false;
                } else {
                    isTitleFilled = true;
                }

                maybeEnablePostButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int charCount = s.length();

                if (charCount == 0) {
                    isLocationFilled = false;
                } else {
                    isLocationFilled = true;
                }

                maybeEnablePostButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void maybeEnablePostButton() {
        if (isTimeFilled && isTitleFilled && isLocationFilled) {
            postBtn.setEnabled(true);
            postBtn.setVisibility(View.VISIBLE);
        } else {
            postBtn.setEnabled(false);
            postBtn.setVisibility(View.GONE);
        }
    }

    private void setMarker(LatLng place) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, 17);
        mMap.addMarker(new MarkerOptions().position(place).title("Event location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(cameraUpdate);
    }

    private void fillPostObject() {
        try {
            post.put("title", editTitle.getText().toString());
            post.put("details", "");
            post.put("host", FirebaseAuth.getInstance().getCurrentUser().getUid());
            post.put("can_others_invite", false);
            post.put("activity_public", true);
            post.put("can_others_invite", false);

            if (editPin.getText().toString().isEmpty()) {
                post.put("location_name", editLocation.getText().toString());
                post.put("location_lat", 0);
                post.put("location_long", 0);
                post.put("location_address", editLocation.getText().toString());
            }

            post.put("interests", new JSONArray(Arrays.asList("Free Food")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Connection Failure", connectionResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE
                && resultCode == RESULT_OK) {
            final Place place = PlaceAutocomplete.getPlace(this, data);
            editPin.setText(place.getAddress());
            mapContainer.setVisibility(View.VISIBLE);

            setMarker(place.getLatLng());

            try {
                post.put("location_name", place.getName());
                post.put("location_lat", place.getLatLng().latitude);
                post.put("location_long", place.getLatLng().longitude);
                post.put("location_address", place.getAddress());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            mapContainer.setVisibility(View.GONE);
            editPin.setText("");
        }
    }

    @OnClick(R.id.post_btn)
    void tryPostActivity() {
        fillPostObject();
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setTitle("Are you sure you want to create this event")
                .setCancelable(false)
                .setItems(new CharSequence[]{"Yes", "No"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Log.d("_post", post.toString());
                                WallaApi.postActivity(new WallaApi.OnDataReceived() {
                                    @Override
                                    public void onDataReceived(Object data, int call) {
                                        Home.refreshPage(false);
                                    }
                                }, post);

                                dialog.cancel();
                                finish();
                                break;
                            default:
                                dialog.cancel();
                                break;
                        }
                    }
                }).show();
    }

    @OnClick({R.id.date_prompt, R.id.date_action})
    void showDateTimePicker() {
        final Calendar now = Calendar.getInstance();
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int day = now.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,
                                  final int year, final int month, final int dayOfMonth) {
                final int hour = now.get(Calendar.HOUR_OF_DAY);
                final int minute = now.get(Calendar.MINUTE);

                new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final Calendar eventTime = Calendar.getInstance();
                        eventTime.set(year, month, dayOfMonth, hour, minute);
                        editDate.setText(Time.getDateTimeString(eventTime));

                        isTimeFilled = true;
                        maybeEnablePostButton();

                        try {
                            post.put("start_time", (long) eventTime.getTimeInMillis() / 1000);
                            post.put("end_time",
                                    SECS_IN_HOUR + (long) eventTime.getTimeInMillis() / 1000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, false).show();
            }
        }, year, month, day).show();
    }

    @OnClick({R.id.location_pin_prompt, R.id.location_pin_action})
    void openGooglePlacesIntent() {
        try {
            final Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnCheckedChanged(R.id.free_food)
    void onFreeFoodToggleChanged(CompoundButton buttonView, boolean isChecked) {
        freeFoodSwitch.setText(isChecked ? "Yes" : "No");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }
}
