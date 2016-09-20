package genieus.com.walla;

import android.graphics.Color;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Anesu on 9/3/2016.
 */
public class Event {
    private String postedBy;
    private String timePosted;

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    private String datePosted;
    private String eventTitle;
    private String eventCateogory;
    private String color;

    public Event(String title, String category, String date, String time, String postedBy){
        this.postedBy = postedBy;
        timePosted = parseTime(time);
        eventCateogory = category;
        eventTitle = title;
        datePosted = parseDate(date);
        color = getColor(category);
    }

    private String getColor(String category){
        switch(category){
            case "All":
                return "FFA160";
            case "Art":
                return "E47E30";
            case "School":
                return "F0C330";
            case "Sports":
                return "3A99D8";
            case "Rides":
                return "39CA74";
            case "Games":
                return "FFBB9C";
            case "Food":
                return "E54D42";
            case "Other":
                return "9A5CB4";
            default:
                return null;

        }
    }

    private String parseDate(String d){
        double time = Double.parseDouble(d);
        Date date = new Date((long) time * 1000);
        int mon = date.getMonth();
        int day = date.getDate();
        return getMonth(mon) + " "  + day;

    }

    private String getMonth(int i){
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        return months[i];
    }

    private String parseTime(String t){
        double time = Double.parseDouble(t);
        int time24 = (int) time % 24;
        String timeStr = time24 > 12 ? (time24 % 12 + "PM") : (time24 + "AM");
        return timeStr;


    }
    public String getColor() {
        return color;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventCateogory() {
        return eventCateogory;
    }

    public void setEventCateogory(String eventCateogory) {
        this.eventCateogory = eventCateogory;
    }
}
