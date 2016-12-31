package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/28/16.
 */

public class UserInfo {
    private String name;
    private boolean verified;

    public UserInfo(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString(){
        return name;
    }
}
