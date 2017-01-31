package genieus.com.walla.v2.info;

/**
 * Created by anesu on 1/31/17.
 */

public class MessageInfo {
    private String name, message, url;

    public MessageInfo(String name, String message, String url){
        this.name = name;
        this.message = message;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
