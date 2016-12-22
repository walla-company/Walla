package genieus.com.walla.v2.info;

import android.graphics.Bitmap;

/**
 * Created by anesu on 12/21/16.
 */
public class MutualFriendInfo {
    Bitmap icon;
    String name;
    int mutualFriends;

    public MutualFriendInfo(Bitmap icon, String name, int mutualFriends) {
        this.icon = icon;
        this.name = name;
        this.mutualFriends = mutualFriends;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }
}
