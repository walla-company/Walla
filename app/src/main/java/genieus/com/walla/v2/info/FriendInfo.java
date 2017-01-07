package genieus.com.walla.v2.info;

import android.graphics.Bitmap;

/**
 * Created by anesu on 12/20/16.
 */

public class FriendInfo {
    private String name, year, major, image_url;


    public FriendInfo(String name, String year, String major, String image_url){
        this.name = name;
        this.year = year;
        this.major = major;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
