package genieus.com.walla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Create extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener{
    final String[] categories = new String[]{"Art", "School", "Sports", "Rides", "Games", "Food", "Other"};
    TextView enter_time;
    TextView char_count;
    EditText title;
    TextView select_category;
    EditText location;
    Date date;
    Button post;

    private boolean creating;
    private String postKey;

    private DatabaseReference mDatabase;
    FirebaseUser user;

    List<String> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{
            Bundle ex = getIntent().getExtras();
            if(ex.containsKey("description")){
                creating = false;
                postKey = ex.getString("key");
                initAsUpdate(ex.getString("description"),
                        ex.getString("interest"),
                        ex.getString("location"),
                        ex.getDouble("rawTime"));
            }else{
                creating = true;
                initUi();
            }
        }catch (Exception e){
            e.printStackTrace();
            creating = true;
            initUi();
        }

    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        date = new Date();

        enter_time = (TextView) findViewById(R.id.enter_time);
        char_count = (TextView) findViewById(R.id.char_count);
        post = (Button) findViewById(R.id.post_btn);
        location = (EditText) findViewById(R.id.enter_location);
        title = (EditText) findViewById(R.id.enter_title);
        enter_time.setOnClickListener(this);
        post.setOnClickListener(this);

        select_category = (TextView) findViewById(R.id.select_category);
        select_category.setOnClickListener(this);
        select_category.setTextColor(getResources().getColor(R.color.colorPrimary));

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                char_count.setText("Character count: " + title.getText().toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initAsUpdate(String desc, String interest, String loc, double time){
        getSupportActionBar().setTitle("Edit");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        date = new Date();

        enter_time = (TextView) findViewById(R.id.enter_time);
        char_count = (TextView) findViewById(R.id.char_count);
        post = (Button) findViewById(R.id.post_btn);
        location = (EditText) findViewById(R.id.enter_location);
        title = (EditText) findViewById(R.id.enter_title);

        post.setText("Update Post");

        enter_time.setOnClickListener(this);
        post.setOnClickListener(this);

        select_category = (TextView) findViewById(R.id.select_category);
        select_category.setTextColor(getResources().getColor(R.color.colorPrimary));
        select_category.setOnClickListener(this);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                char_count.setText("Character count: " + title.getText().toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String timeStr = getTimeString(time);

        enter_time.setText(timeStr);
        title.setText(desc);
        select_category.setText(interest);
        location.setText(loc);
    }

    private String getTimeString(double time){
        String timeStr = "";
        Date activityDate = new Date((long) time * 1000);
        int mon = activityDate.getMonth();
        int day = activityDate.getDate();
        timeStr += getMonthName(mon) + " "  + day;

        int hr = activityDate.getHours() % 12;
        if(hr == 0)
            hr = 12;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        String timeFormated = dateFormat.format(activityDate);

        timeStr += ", " + hr + timeFormated.substring(2);
        return timeStr;
    }


    private void showCategories(){
         new AlertDialog.Builder(this)
                .setTitle("Choose an Interest")
                .setItems(categories, this)
                .setCancelable(true)
                .create()
                .show();
    }

    private void showDateDialog(){
        final Calendar cal = Calendar.getInstance();
        int yr = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, yr, month, day).show();
    }

    private void showTimeDialog(){
        final Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);

        new TimePickerDialog(this, this, h, m, true).show();
    }

    private void update(){
        String doing = title.getText().toString();
        String where = location.getText().toString();
        String poster = user.getUid();
        String category = select_category.getText().toString();

        mDatabase.child("activities/" + postKey + "/description").setValue(doing);
        mDatabase.child("user_activities/" + poster + "/" + postKey + "/description").setValue(doing);

        mDatabase.child("activities/" + postKey + "/interest").setValue(category);
        mDatabase.child("user_activities/" + poster + "/" + postKey + "/interest").setValue(category);

        mDatabase.child("activities/" + postKey + "/location").setValue(where);
        mDatabase.child("user_activities/" + poster + "/" + postKey + "/location").setValue(where);

        mDatabase.child("activities/" + postKey + "/activityTime").setValue(3);
        mDatabase.child("user_activities/" + poster + "/" + postKey + "/activityTime").setValue(3);
    }

    private void deletePost(){
        new android.support.v7.app.AlertDialog.Builder(Create.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child("activities/" + postKey).removeValue();
                        mDatabase.child("user_activities/" + user.getUid() + "/" + postKey).removeValue();

                        Toast.makeText(Create.this, "Post has been deleted", Toast.LENGTH_LONG).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void post(){
        if(date == null){
            Toast.makeText(this, "Please select a date", Toast.LENGTH_LONG).show();
            return;
        }

        date.setYear(new Date().getYear());
        String doing = title.getText().toString();
        String where = location.getText().toString();
        String poster = user.getUid();
        String category = select_category.getText().toString();
        String key = mDatabase.child("activities").push().getKey();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        double time = cal.getTimeInMillis();

        final double MILLI_SECONDS_IN_DAY = 8.64e+7;
        if((time - MILLI_SECONDS_IN_DAY) > System.currentTimeMillis()){
            Toast.makeText(this, "Events must be within 24 hours", Toast.LENGTH_LONG).show();
            return;
        }

        if(time - System.currentTimeMillis() < 0){
            Toast.makeText(this, "You cannot create an event in the past", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("activityTime", time / 1000);
        event.put("description", doing);
        event.put("interest", category);
        event.put("location", where);
        event.put("timePosted", System.currentTimeMillis() / 1000);
        event.put("key", key);
        event.put("locationSaved", false);
        event.put("sent", false);
        event.put("numberGoing", 1);
        event.put("uid", poster);

        boolean invalid;
        invalid = doing == null || doing.isEmpty() ||
                category == null ||
                category.isEmpty() || where== null ||
                where.isEmpty() || key == null ||
                key.isEmpty() || poster == null ||
                poster.isEmpty();

        Map<String, Object> simple = new HashMap<>();
        simple.put("activityTime", time / 1000);
        simple.put("description", doing);
        simple.put("interest", category);
        simple.put("location", where);
        simple.put("timePosted", System.currentTimeMillis() / 1000);
        simple.put("key", key);
        simple.put("uid", poster);


        if(!invalid) {
            if(!posts.contains(doing)) {
                posts.add(doing);
                mDatabase.child("activities/" + key).setValue(event);
                mDatabase.child("user_activities/" + poster + "/" + key).setValue(simple);


                Snackbar.make(post, "Post created successfully!", Snackbar.LENGTH_LONG).show();

                Snackbar snack = Snackbar.make(post, "Post created!", Snackbar.LENGTH_LONG);
                /*snack.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //erase post
                    }
                });

                */

                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                snack.show();
            }
        }
        else{
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.enter_time:
                showDateDialog();
                break;
            case R.id.select_category:
                showCategories();
                break;
            case R.id.post_btn:
                if(creating)
                    post();
                else
                    update();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        showTimeDialog();

        enter_time.setText(getMonthName(monthOfYear) + " " + dayOfMonth);
        date.setYear(year);
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        date.setMinutes(minute);
        date.setHours(hourOfDay);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        int hr = hourOfDay  % 12;
        enter_time.append(", " + hr + dateFormat.format(date).substring(2));
    }

    public static String getMonthName(int month){
        switch(month+1){
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        select_category.setText(categories[which]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        if(creating){
            MenuItem delete = menu.findItem(R.id.action_delete);
            delete.setVisible(false);
            invalidateOptionsMenu();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete:
                if(!creating)
                    deletePost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
