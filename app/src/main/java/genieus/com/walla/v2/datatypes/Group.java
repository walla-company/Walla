package genieus.com.walla.v2.datatypes;

import java.util.List;

/**
 * Created by anesu on 12/21/16.
 */
public class Group {
    private String name, abbr, color, description, guid;
    private List<String> members, activities;

    public Group(String name, String abbr, String color){
        this.name = name;
        this.abbr = abbr;
        this.color = color;
    }

    public Group(){}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }

        if(!(o instanceof Group)){
            return false;
        }

        Group other =(Group) o;
        return (other.getGuid().equals(this.getGuid()));
    }

    @Override
    public int hashCode() {
        return getGuid().hashCode();
    }
}
