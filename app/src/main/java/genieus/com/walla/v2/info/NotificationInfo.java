package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/20/16.
 */
public class NotificationInfo {
    private String type, message;
    private int icon;

    public NotificationInfo(String type, String message){
        this.type = type;
        this.message = message;
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
}
