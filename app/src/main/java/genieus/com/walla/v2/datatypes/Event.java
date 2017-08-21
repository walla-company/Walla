package genieus.com.walla.v2.datatypes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Anesu on 12/17/2016.
 */
public class Event {

    private String auid, title, location_name, location_address, host, host_group, details, group_name, group_abbr;
    private double location_lat, location_long;
    private long start_time, end_time;
    private boolean is_public, can_guests_invite, deleted;
    private List<String> interests, going_list, interested_list;
    private int going, interested;

    public Event(){};

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost_group() {
        return host_group;
    }

    public void setHost_group(String host_group) {
        this.host_group = host_group;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(double location_long) {
        this.location_long = location_long;
    }

    public boolean is_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public boolean isCan_guests_invite() {
        return can_guests_invite;
    }

    public void setCan_guests_invite(boolean can_guests_invite) {
        this.can_guests_invite = can_guests_invite;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getGoing_list() {
        return going_list;
    }

    public void setGoing_list(List<String> going_list) {
        this.going_list = going_list;
    }

    public List<String> getInterested_list() {
        return interested_list;
    }

    public void setInterested_list(List<String> interested_list) {
        this.interested_list = interested_list;
    }

    public int getGoing() {
        return going;
    }

    public void setGoing(int going) {
        this.going = going;
    }

    public int getInterested() {
        return interested;
    }

    public void setInterested(int interested) {
        this.interested = interested;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_abbr() {
        return group_abbr;
    }

    public void setGroup_abbr(String group_abbr) {
        this.group_abbr = group_abbr;
    }

    @Override
    public String toString(){
        return String.format("%s at %s", title, location_name);
    }

    //helper methods
    public String getStringTime(long time, boolean tag){ //tag used to determine if AM/PM is included
        SimpleDateFormat format1 = new SimpleDateFormat("h:mm aaa");
        SimpleDateFormat format2 = new SimpleDateFormat("h:mm");
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time * 1000);

        String date = "";
        if(tag){
            date = format1.format(now.getTime());
        }else{
            date = format2.format(now.getTime());
        }

        return date;

    }

    public String getStringDate(long time){
        String date = "";
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(time * 1000);

        String day = String.format("%d/%d", start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");

        if(start.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && start.get(Calendar.MONTH) == now.get(Calendar.MONTH)){
            int diff = start.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
            if(diff == 0){
                date = "Today";
            }else if(diff == 1){
                date = "Tomorrow";
            }else {
                date = format2.format(start.getTime());
            }
        }else{
            date = format2.format(start.getTime());
        }

        return String.format("%s (%s)", date, day);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
