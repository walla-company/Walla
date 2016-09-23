package genieus.com.walla;

import android.graphics.Color;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    final private int MINUTES_30 = 1800;
    private String datePosted;
    private String eventTitle;
    private String eventCateogory;
    private String color;
    private String key;
    private String posterUid;

    private String location;
    private long people;
    private boolean expired;
    private double rawTime;

    private boolean posterSet;

    public Event(String title, String category, String date, String time, String postedBy, String location, String key, long people){
        this.postedBy = postedBy;
        timePosted = parseTime(time);
        eventCateogory = category;
        eventTitle = title.trim();
        datePosted = parseDate(date);
        color = getColor(category);
        this.location = location;
        this.people = people;
        this.key = key;
        posterUid = postedBy;
        expired = checkExpired(date);
        rawTime = Double.parseDouble(time);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String getColor(String category){
        switch(category){
            case "All":
                return "#FFA160";
            case "Art":
                return "#E47E30";
            case "School":
                return "#F0C330";
            case "Sports":
                return "#3A99D8";
            case "Rides":
                return "#39CA74";
            case "Games":
                return "#FFBB9C";
            case "Food":
                return "#E54D42";
            case "Other":
                return "#9A5CB4";
            default:
                return null;

        }
    }

    private boolean checkExpired(String strDate){
        double time = Double.parseDouble(strDate);
        Date date = new Date((long) time * 1000);

        long post = date.getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;

        long diff = now - post;
        if(diff > MINUTES_30){
            return true;
        }else{
            return false;
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
        Date date = new Date((long) time * 1000);
        int hr = date.getHours() % 12;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        String timeStr = dateFormat.format(date);
        return hr + timeStr.substring(2);
    }

    public double getRawTime() {
        return rawTime;
    }

    public void setRawTime(double rawTime) {
        this.rawTime = rawTime;
    }

    public long getPeople() {
        return people;
    }

    public void setPeople(long people) {
        this.people = people;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getColor() {
        return color;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public boolean usernamePresent(){
        return posterSet;
    }

    public void setPostedBy(String postedBy) {
        posterSet = true;
        this.postedBy = postedBy;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getPosterUid() {
        return posterUid;
    }

    public void setPosterUid(String posterUid) {
        this.posterUid = posterUid;
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
