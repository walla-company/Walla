package genieus.com.walla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;

import java.util.Calendar;

public class Create extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener{
    final String[] categories = new String[]{"Art", "School", "Sports", "Rides", "Games", "Food", "Other"};
    int year, month, day;
    TextView enter_time;
    TextView select_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();


    }

    private void initUi(){
        enter_time = (TextView) findViewById(R.id.enter_time);
        enter_time.setOnClickListener(this);

        select_category = (TextView) findViewById(R.id.select_category);
        select_category.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.enter_time:
                showDateDialog();
                break;
            case R.id.select_category:
                showCategories();
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
