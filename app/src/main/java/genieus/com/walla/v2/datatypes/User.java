package genieus.com.walla.v2.datatypes;

import android.util.Log;

import java.util.List;

/**
 * Created by anesu on 12/28/16.
 */

public class User {
    private String name;
    private String year;
    private String profile_url;
    private String major;
    private String hometown;
    private String description;
    private String uid;
    private String first_name;
    private String last_name;
    private String email;
    private String goal1;
    private String goal2;
    private String goal3;
    private String wannaMeet;

    public String getReasonSchool() {
        return reasonSchool;
    }

    public void setReasonSchool(String reasonSchool) {
        this.reasonSchool = reasonSchool;
    }

    private String reasonSchool;

    public String getWannaMeet() {
        return wannaMeet;
    }

    public void setWannaMeet(String wannaMeet) {
        this.wannaMeet = wannaMeet;
    }

    private List<String> interests, friends, groups, sent_requests, received_requests, calendar;
    private List<String> activities;
    private boolean verified, intro_complete;

    public User(){};

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProfileUrl() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
        Log.d("added12", interests.toString());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getSent_requests() {
        return sent_requests;
    }

    public void setSent_requests(List<String> sent_requests) {
        this.sent_requests = sent_requests;
    }

    public List<String> getReceived_requests() {
        return received_requests;
    }

    public void setReceived_requests(List<String> received_requests) {
        this.received_requests = received_requests;
    }

    public List<String> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<String> calendar) {
        this.calendar = calendar;
    }

    @Override
    public String toString(){
        return name;
    }

    public boolean isIntro_complete() {
        return intro_complete;
    }

    public void setIntro_complete(boolean intro_complete) {
        this.intro_complete = intro_complete;
    }

    public String getGoal1() {
        return goal1;
    }

    public void setGoal1(String goal1) {
        this.goal1 = goal1;
    }

    public String getGoal2() {
        return goal2;
    }

    public void setGoal2(String goal2) {
        this.goal2 = goal2;
    }

    public String getGoal3() {
        return goal3;
    }

    public void setGoal3(String goal3) {
        this.goal3 = goal3;
    }
}
