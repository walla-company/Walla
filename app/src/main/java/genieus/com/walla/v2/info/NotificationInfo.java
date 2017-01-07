package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/20/16.
 */
public class NotificationInfo {
    private String type, message;
    private int icon;
    private String senderUId, nuid;

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
}
