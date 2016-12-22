package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/21/16.
 */
public class GroupInfo {
    String name, abbr, color;

    public GroupInfo(String name, String abbr, String color){
        this.name = name;
        this.abbr = abbr;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
