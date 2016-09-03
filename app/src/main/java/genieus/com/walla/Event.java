package genieus.com.walla;

/**
 * Created by Anesu on 9/3/2016.
 */
public class Event {
    private String postedBy;
    private String timePosted;
    private String eventTitle;
    private String eventCateogory;

    public Event(String title, String category, String time, String postedBy){
        this.postedBy = postedBy;
        timePosted = time;
        eventCateogory = category;
        eventTitle = title;
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
