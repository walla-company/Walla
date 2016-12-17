package genieus.com.walla.v2;

/**
 * Created by Anesu on 12/17/2016.
 */
public class Event {
    enum Type{
        LIT,
        CHILL
    }
    private String category;
    private int hoursDiff;
    private String title;
    private Type type;
    private String startTime;
    private String endTime;
    private int interested;
    private int going;

    public Event(String category, int hoursDiff, String title, Type type, String startTime, String endTime, int interested, int going) {
        this.category = category;
        this.hoursDiff = hoursDiff;
        this.title = title;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interested = interested;
        this.going = going;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getHoursDiff() {
        return hoursDiff;
    }

    public void setHoursDiff(int hoursDiff) {
        this.hoursDiff = hoursDiff;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getInterested() {
        return interested;
    }

    public void setInterested(int interested) {
        this.interested = interested;
    }

    public int getGoing() {
        return going;
    }

    public void setGoing(int going) {
        this.going = going;
    }
}
