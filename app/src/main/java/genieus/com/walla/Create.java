package genieus.com.walla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Create extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener{
    final String[] categories = new String[]{"Art", "School", "Sports", "Rides", "Games", "Food", "Other"};
    int year, month, day;
    TextView enter_time;
    TextView char_count;
    EditText title;
    TextView select_category;
    EditText location;
    Date date;
    Button post;

    private DatabaseReference mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();


    }

    private void initUi(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        enter_time = (TextView) findViewById(R.id.enter_time);
        char_count = (TextView) findViewById(R.id.char_count);
        post = (Button) findViewById(R.id.post_btn);
        location = (EditText) findViewById(R.id.enter_location);
        title = (EditText) findViewById(R.id.enter_title);
        enter_time.setOnClickListener(this);

        select_category = (TextView) findViewById(R.id.select_category);
        select_category.setOnClickListener(this);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                char_count.setText("Character count: " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void post(){
        String doing = title.getText().toString();
        String where = location.getText().toString();
        String poster = user.getUid();
        String category = select_category.getText().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String time = "" + cal.getTimeInMillis();

        Map<String, Object> event = new HashMap<>();
        event.put("activityTime", time);
        event.put("description", doing);
        event.put("interests", category);
        event.put("location", where);
        event.put("timePosted", System.currentTimeMillis());
        event.put("key", user.getUid());
        event.put("numberGoing", 1);
        event.put("uid", user.getUid());
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
                post();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        showTimeDialog();

        enter_time.setText(getMonthName(monthOfYear) + " " + dayOfMonth);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        enter_time.append(", " + hourOfDay + ":" + minute);
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
        select_category.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
