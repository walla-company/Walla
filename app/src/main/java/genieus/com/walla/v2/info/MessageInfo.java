package genieus.com.walla.v2.info;

/**
 * Created by anesu on 1/31/17.
 */

public class MessageInfo {
    private String uid, message;

    public MessageInfo(String uid, String message){
        this.message = message;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String name) {
        this.uid = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
