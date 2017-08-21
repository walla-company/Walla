package genieus.com.walla.v2.datatypes;

/**
 * Created by anesu on 12/20/16.
 */
public class NotificationInfo {
    private String type, message, image_url;
    private int icon;
    private String senderUId, nuid, activityUid;
    private double time_created;
    private boolean read;

    public NotificationInfo(String type, String message){
        this.type = type;
        this.message = message;
    }

    public NotificationInfo(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderUId() {
        return senderUId;
    }

    public void setSenderUId(String senderUId) {
        this.senderUId = senderUId;
    }

    public String getNuid() {
        return nuid;
    }

    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public String getActivityUid() {
        return activityUid;
    }

    public void setActivityUid(String activityUid) {
        this.activityUid = activityUid;
    }

    public double getTime_created() {
        return time_created;
    }

    public void setTime_created(double time_created) {
        this.time_created = time_created;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
