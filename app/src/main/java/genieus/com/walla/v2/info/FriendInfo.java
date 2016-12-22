package genieus.com.walla.v2.info;

import android.graphics.Bitmap;

/**
 * Created by anesu on 12/20/16.
 */

public class FriendInfo {
    private String name, year, major;
    private Bitmap image;

    public FriendInfo(String name, String year, String major){
        this(name, year, major, null);
    }

    public FriendInfo(String name, String year, String major, Bitmap image){
        this.name = name;
        this.year = year;
        this.major = major;
        this.image = image;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
