package genieus.com.walla.v2.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.shaded.fasterxml.jackson.databind.util.JSONPObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import genieus.com.walla.v2.info.DomainInfo;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.InterestInfo;
import genieus.com.walla.v2.info.MessageInfo;
import genieus.com.walla.v2.info.NotificationInfo;
import genieus.com.walla.v2.info.UserInfo;

/**
 * Created by anesu on 12/25/16.
 */

public class WallaApi {
    //api call identifiers
    public static final int MIN_VERSION = 0;
    public static final int DOMAINS = 1;
    public static final int ACTIVITIES = 2;
    public static final int ATTENDEES = 3;
    public static final int USER_INFO = 4;
    public static final int IS_ATTENDING = 5;
    public static final int IS_VERIFIED = 6;
    public static final int GET_ACTIVITY = 7;
    public static final int GET_ACTIVITIES = 8;
    public static final int GET_GROUP = 9;
    public static final int GET_NOTIFICATIONS = 10;
    public static final int ADD_USER = 11;
    public static final int GET_SUGGESTED_USERS = 12;
    public static final int GET_SUGGESTED_GROUPS = 13;
    public static final int GET_COMMENTS = 14;
    public static final int POST_COMMENT = 15;
    public static final int GET_USERS = 16;
    public static final int GET_GROUPS = 17;
    public static final int IS_SUSPENDED = 17;


    public interface OnDataReceived {
        public void onDataReceived(Object data, int call);
    }

    private static Context context;

    private static String token = "3eaf7dFmNF447d";
    private static String platform = "android";
    private static String site = "https://walla-server.herokuapp.com";
    private static String min_version = "/api/min_version?";
    private static String domains = "/api/domains?";
    private static String activities = "/api/activities?";
    private static String attendees = "/api/attendees?";
    private static String user_info = "/api/get_user?";
    private static String is_attending = "/api/is_attending?";
    private static String report_post = "/api/report_post?";
    private static String add_activity = "/api/add_activity?";
    private static String get_activity = "/api/get_activity?";
    private static String get_activities = "/api/get_activities?";
    private static String update_fname = "/api/update_user_first_name?";
    private static String update_lname = "/api/update_user_last_name?";
    private static String update_email = "/api/update_user_email?";
    private static String update_major = "/api/update_user_major?";
    private static String update_hometown = "/api/update_user_hometown?";
    private static String update_description = "/api/update_user_description?";
    private static String update_academic_level = "/api/update_user_academic_level?";
    private static String update_profile_image_url = "/api/update_user_profile_image_url?";
    private static String update_interests = "/api/update_user_interests?";
    private static String get_group = "/api/get_group?";
    private static String get_notifications = "/api/get_notifications?";
    private static String approve_friend = "/api/approve_friend?";
    private static String ignore_friend = "/api/ignore_friend_request?";
    private static String add_user = "/api/add_user?";
    private static String request_friend = "/api/request_friend?";
    private static String going = "/api/going?";
    private static String interested = "/api/interested?";
    private static String suggested_groups = "/api/get_suggested_groups?";
    private static String suggested_users = "/api/get_suggested_users?";
    private static String join_group = "/api/join_group?";
    private static String leave_group = "/api/leave_group?";
    private static String verify_email = "/api/request_verification?";
    private static String add_token = "/api/add_notification_token?";
    private static String invite_user = "/api/invite_user?";
    private static String post_comment = "/api/post_discussion?";
    private static String get_comments = "/api/get_discussions?";
    private static String get_users = "/api/get_users?";
    private static String get_groups = "/api/get_groups?";
    private static String is_suspended = "/api/is_user_suspended?";
    private static String delete_activity = "/api/delete_activity?";



    public static String domain = "";
    private static RequestQueue queue;


    public WallaApi(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        if(domain == null || domain.isEmpty()){
           if(FirebaseAuth.getInstance().getCurrentUser() != null){
               String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
               String domainExt = getDomailFromEmail(email);
               int dot = domainExt.indexOf('.');
               if(dot >= 0)
                   domain = domainExt.substring(0, dot);
               else
                   domain = "";
           }
        }
    }

    private String getDomailFromEmail(String emailStr) {
        String[] splitEmail = emailStr.split("(\\.|@)");

        String domain = "";
        if(splitEmail.length >= 2){
            int len = splitEmail.length;
            domain = String.format("%s.%s", splitEmail[len - 2], splitEmail[len - 1]);
        }

        return domain;
    }

    public static void getMinVersion(final OnDataReceived listener) {
        final String url = site + min_version + "token=" + token + "&platform=" + platform;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String data;

                try {
                    data = response.getString("min_version");
                } catch (JSONException e) {
                    data = "";
                }

                listener.onDataReceived(data, MIN_VERSION);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getAllowedDomains(final OnDataReceived listener) {
        final String url = site + domains + "token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<DomainInfo> domains = new ArrayList<>();
                Iterator<String> keys = response.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        JSONObject data = response.getJSONObject(key);
                        domains.add(new DomainInfo(key, data.getString("domain"), data.getString("full_name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onDataReceived(domains, DOMAINS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getUserInfo(final OnDataReceived listener, String uid) {
        final String url = site + user_info + "token=" + token + "&uid=" + uid + "&school_identifier=" + domain;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserInfo info = new UserInfo();

                try {
                    List<String> list = new ArrayList<>();
                    if (response.has("interests")) {
                        JSONArray interests = response.getJSONArray("interests");

                        for (int i = 0; i < interests.length(); i++) {
                            list.add(interests.getString(i));
                        }
                    }

                    info.setInterests(list);

                    List<String> list2 = new ArrayList<>();
                    if (response.has("groups")) {
                        JSONObject groups = response.getJSONObject("groups");
                        Iterator<String> keys = groups.keys();

                        while(keys.hasNext()){
                            list2.add(keys.next());
                        }
                    }

                    info.setGroups(list2);

                    List<String> friends = new ArrayList<>();
                    if(response.has("friends")){
                        Iterator<String> keys = response.getJSONObject("friends").keys();
                        while(keys.hasNext()){
                            friends.add(keys.next());
                        }
                    }
                    info.setFriends(friends);

                    List<String> events = new ArrayList<>();
                    if(response.has("activities")){
                        JSONObject eventsArr= response.getJSONObject("activities");
                        Iterator<String> ekeys = eventsArr.keys();
                        while(ekeys.hasNext()){
                            events.add(ekeys.next());
                        }

                        info.setActivities(events);

                    }

                    List<String> sent = new ArrayList<>();
                    if(response.has("sent_friend_requests")){
                        JSONObject sent_request = response.getJSONObject("sent_friend_requests");
                        Iterator<String> keys = sent_request.keys();
                        while(keys.hasNext()){
                            sent.add(keys.next());
                        }
                    }
                    info.setSent_requests(sent);

                    List<String> recieved = new ArrayList<>();
                    if(response.has("received_friend_requests")){
                        JSONObject recieved_request = response.getJSONObject("received_friend_requests");
                        Iterator<String> keys = recieved_request.keys();
                        while(keys.hasNext()){
                            sent.add(keys.next());
                        }
                    }
                    info.setReceived_requests(recieved);

                    List<String> calendar = new ArrayList<>();
                    if(response.has("calendar")){
                        JSONObject cal = response.getJSONObject("calendar");
                        Iterator<String> keys = cal.keys();
                        while(keys.hasNext()){
                            calendar.add(keys.next());
                        }
                    }
                    info.setCalendar(calendar);
                    info.setFirst_name(response.getString("first_name"));
                    info.setLast_name(response.getString("last_name"));
                    info.setProfile_url(response.getString("profile_image_url"));
                    info.setMajor(response.getString("major"));
                    info.setYear(response.getString("academic_level"));
                    info.setHometown(response.getString("hometown"));
                    info.setUid(response.getString("user_id"));
                    info.setDescription(response.getString("description"));
                    info.setEmail(response.getString("email"));
                    info.setVerified(response.getBoolean("verified"));
                } catch (JSONException e) {
                    Log.d("readerror", e.toString());
                    e.printStackTrace();
                }

                listener.onDataReceived(info, USER_INFO);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getActivities(final OnDataReceived listener, int hours) {
        final String url = site + activities + "token=" + token + "&domain=" + domain + "&filter=" + hours;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<EventInfo> events = new ArrayList<>();

                int len = response.length();
                for (int i = 0; i < len; i++) {
                    try {
                        JSONObject event = response.getJSONObject(i);
                        EventInfo info = new EventInfo();

                        info.setTitle(event.getString("description"));
                        info.setLocation_name(event.getString("location"));
                        //TODO finish adding fields

                        events.add(info);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onDataReceived(events, ACTIVITIES);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getGroup(final OnDataReceived listener, final String guid) {
        final String url = site + get_group + "token=" + token + "&school_identifier=" + domain + "&guid=" + guid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GroupInfo group = new GroupInfo();

                try {
                    group.setName(response.getString("name"));
                    group.setAbbr(response.getString("short_name"));
                    group.setColor(response.getString("color"));
                    group.setDescription(response.getString("details"));
                    group.setGuid(response.getString("group_id"));

                    List<String> members = new ArrayList<>();
                    if(response.has("members")){
                        Iterator<String> keys = response.getJSONObject("members").keys();
                        while(keys.hasNext())
                            members.add(keys.next());
                    }

                    group.setMembers(members);

                    List<String> activities = new ArrayList<>();
                    if(response.has("activities")){
                        Iterator<String> keys = response.getJSONObject("activities").keys();
                        while(keys.hasNext()){
                            activities.add(keys.next());
                        }
                    }

                    group.setActivities(activities);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataReceived(group, GET_GROUP);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void isAttendingEvent(final OnDataReceived listener, String uid, String eid) {
        final String url = site + is_attending + "token=" + token + "&uid=" + uid + "&event=" + eid + "&domain=" + domain;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean attending = false;
                try {
                    attending = response.getBoolean("attending");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataReceived(attending, IS_ATTENDING);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getAttendees(final OnDataReceived listener, String eventId) {
        final String url = site + attendees + "token=" + token + "&event=" + eventId + "&domain=" + domain;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<UserInfo> attendees = new ArrayList<>();

                int len = response.length();
                for (int i = 0; i < len; i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        UserInfo user = new UserInfo();
                        user.setName(data.getString("name"));
                        //TODO add more fields

                        attendees.add(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                listener.onDataReceived(attendees, ATTENDEES);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);

    }

    public static void verifyEmail(final String uid, final String email) {
        isVerified(new OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                if ((boolean) data) Log.d("apidata", "already verified");
                else {
                    final String url = site + verify_email + "token=" + token + "&uid=" + uid + "&email=" + email + "&domain=" + domain;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("apidata", "sent");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("jsonerror", url + " " + error.toString());
                        }
                    });

                    queue.add(request);
                }
            }
        }, uid);
    }

    public static void reportPost(String eid, String reporter) {
        final String url = site + report_post + "token=" + token + "&uid=" + reporter + "&event=" + eid + "&domain=" + domain;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("apidata", "reported");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void isVerified(final OnDataReceived listener, String uid) {

        getUserInfo(new OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                listener.onDataReceived(((UserInfo) data).isVerified(), IS_VERIFIED);
            }
        }, uid);

    }

    public static void postActivity(JSONObject params) {
        final String url = site + add_activity + "token=" + token;

        try {
            params.put("school_identifier", domain);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Log.d("apidata", params.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);


    }

    public static void getActivity(final OnDataReceived listener, String auid) {
        final String url = site + get_activity + "token=" + token + "&school_identifier=" + domain + "&auid=" + auid;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EventInfo event = new EventInfo();

                try {
                    List<String> going, interested;
                    going = new ArrayList<>();
                    interested = new ArrayList<>();

                    if(response.has("replies")){
                        JSONObject replies = response.getJSONObject("replies");
                        Iterator<String> keys = replies.keys();

                        String type, key;
                        while(keys.hasNext()){
                            key = keys.next();
                            type = replies.getString(key);

                            if(type.equals("going"))
                                going.add(key);
                            else
                                interested.add(key);
                        }
                    }

                    event.setGoing_list(going);
                    event.setInterested_list(interested);

                    if(response.has("host_group"))
                        event.setHost_group(response.getString("host_group"));
                    else
                        event.setHost_group("");


                    event.setGroup_name(response.getString("host_group_name"));
                    event.setGroup_abbr(response.getString("host_group_short_name"));
                    event.setTitle(response.getString("title"));
                    event.setAuid(response.getString("activity_id"));
                    event.setCan_guests_invite(response.getBoolean("can_others_invite"));
                    event.setIs_public(response.getBoolean("public"));
                    event.setStart_time(response.getLong("start_time"));
                    event.setEnd_time(response.getLong("end_time"));
                    event.setLocation_name(response.getJSONObject("location").getString("name"));
                    event.setLocation_long(response.getJSONObject("location").getDouble("long"));
                    event.setLocation_lat(response.getJSONObject("location").getDouble("lat"));
                    event.setInterests(interestsJsontoList(response.getJSONArray("interests")));
                    event.setDetails(response.getString("details"));
                    event.setHost(response.getString("host"));

                    boolean isDeleted = false;
                    if(response.has("deleted")){
                        isDeleted = response.getBoolean("deleted");
                    }

                    event.setDeleted(isDeleted);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataReceived(event, GET_ACTIVITY);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getActivities(String uid, final OnDataReceived listener) {
        final String url = site + get_activities + "token=" + token + "&school_identifier=" + domain + "&uid=" + uid;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {

                List<EventInfo> events = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject response = array.getJSONObject(i);
                        EventInfo event = new EventInfo();

                        List<String> going, interested;
                        going = new ArrayList<>();
                        interested = new ArrayList<>();

                        if(response.has("replies")){
                            JSONObject replies = response.getJSONObject("replies");
                            Iterator<String> keys = replies.keys();

                            String type, key;
                            while(keys.hasNext()){
                                key = keys.next();
                                type = replies.getString(key);

                                if(type.equals("going"))
                                    going.add(key);
                                else
                                    interested.add(key);
                            }
                        }

                        event.setGoing_list(going);
                        event.setInterested_list(interested);

                        if(response.has("host_group"))
                            event.setHost_group(response.getString("host_group"));
                        else
                            event.setHost_group("");

                        event.setGroup_name(response.getString("host_group_name"));
                        event.setGroup_abbr(response.getString("host_group_short_name"));
                        event.setTitle(response.getString("title"));
                        event.setAuid(response.getString("activity_id"));
                        event.setCan_guests_invite(response.getBoolean("can_others_invite"));
                        event.setIs_public(response.getBoolean("public"));
                        event.setStart_time(response.getLong("start_time"));
                        event.setEnd_time(response.getLong("end_time"));
                        Log.d("apidata", response.getJSONObject("location").toString());
                        event.setLocation_name(response.getJSONObject("location").getString("name"));
                        event.setLocation_long(response.getJSONObject("location").getDouble("long"));
                        event.setLocation_lat(response.getJSONObject("location").getDouble("lat"));
                        event.setInterests(interestsJsontoList(response.getJSONArray("interests")));
                        event.setDetails(response.getString("details"));
                        event.setHost(response.getString("host"));

                        events.add(event);
                    } catch (JSONException e) {
                        Log.d("apidata", e.toString());
                    }
                }

                listener.onDataReceived(events, GET_ACTIVITIES);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void saveUserFirstName(String uid, String name) {
        final String url = site + update_fname + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("first_name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserLastName(String uid, String name) {
        final String url = site + update_lname + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("last_name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserEmail(String uid, String email) {
        final String url = site + update_email + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserAcademicLevel(String uid, String level) {
        final String url = site + update_academic_level + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("academic_level", level);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserMajor(String uid, String major) {
        final String url = site + update_major + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("major", major);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserHometown(String uid, String town) {
        final String url = site + update_hometown + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("hometown", town);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserDescription(String uid, String description) {
        final String url = site + update_description + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserProfileImageUrl(String uid, String imageUrl) {
        final String url = site + update_profile_image_url + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("profile_image_url", imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void saveUserInterests(String uid, JSONArray interests) {
        final String url = site + update_interests + "token=" + token + "&school_identifier=" + domain;
        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("interests", interests);
            params.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("apidata", url + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void getNotifications(final OnDataReceived listener, String uid){
        final String url = site + get_notifications + "token=" + token + "&school_identifier=" + domain + "&uid=" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<NotificationInfo> list = new ArrayList<>();
                        Iterator<String> keys = response.keys();
                        while(keys.hasNext()){
                            String key = keys.next();
                            JSONObject notifObj;
                            try {
                                notifObj = response.getJSONObject(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }


                            NotificationInfo notif = new NotificationInfo();
                            try {
                                notif.setTime_created(notifObj.getDouble("time_created"));
                                notif.setNuid(notifObj.getString("notification_id"));
                                notif.setSenderUId(notifObj.getString("sender"));
                                notif.setType(notifObj.getString("type"));
                                if(notifObj.has("text")){
                                    notif.setMessage(notifObj.getString("text"));
                                }

                                if(notifObj.has("activity_id")){
                                    notif.setActivityUid(notifObj.getString("activity_id"));
                                }

                                list.add(notif);

                            } catch (JSONException e) {
                                Log.d("notiferror", e.toString());
                                e.printStackTrace();
                            }

                        }

                        //Log.d("notifdata", list.toString());
                        listener.onDataReceived(list, GET_NOTIFICATIONS);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d("jsonerror", url + error.toString());
                        }
                    }
                });

        queue.add(request);
    }

    public static void approveFriendRequest(String uid, String friend){
        final String url = site + approve_friend + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("friend", friend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void ignoreFriendRequest(String uid, String friend){
        final String url = site + ignore_friend + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("friend", friend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void addUser(final OnDataReceived listener, JSONObject params){
        final String url = site + add_user + "token=" + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("userdata", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("jsonerror", error.toString());
            }
        });

        queue.add(request);
    }

    public static void requestFriend(String uid, String fuid){
        final String url = site + request_friend + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("friend", fuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);

    }

    public static void going(String uid, String auid){
        final String url = site + going + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("auid", auid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);

    }

    public static void interested(String uid, String auid){
        final String url = site + interested + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("auid", auid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getSuggestedGroups(final OnDataReceived listener){
        final String url = site + suggested_groups + "token=" + token + "&school_identifier=" + domain;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<GroupInfo> groups = new ArrayList<>();
                Log.d("groupdata", response.toString());
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject groupObj = response.getJSONObject(i);

                        GroupInfo group = new GroupInfo();
                        group.setName(groupObj.getString("name"));
                        group.setAbbr((groupObj.getString("short_name")));
                        group.setColor(groupObj.getString("color"));
                        group.setGuid(groupObj.getString("group_id"));
                        group.setDescription(groupObj.getString("details"));

                        groups.add(group);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onDataReceived(groups, GET_SUGGESTED_GROUPS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getSuggestedUsers(final OnDataReceived listener){
        final String url = site + suggested_users + "token=" + token + "&school_identifier=" + domain;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<UserInfo> users = new ArrayList<>();
                for(int k = 0; k < response.length(); k++){
                    try {
                        JSONObject userObj = response.getJSONObject(k);

                        UserInfo info = new UserInfo();

                        try {
                            List<String> list = new ArrayList<>();
                            if (userObj.has("interests")) {
                                JSONArray interests = userObj.getJSONArray("interests");

                                for (int i = 0; i < interests.length(); i++) {
                                    list.add(interests.getString(i));
                                }
                            }

                            info.setInterests(list);

                            List<String> list2 = new ArrayList<>();
                            if (userObj.has("groups")) {
                                JSONObject groups = userObj.getJSONObject("groups");
                                Iterator<String> keys = groups.keys();

                                while(keys.hasNext()){
                                    list2.add(keys.next());
                                }
                            }

                            info.setGroups(list2);

                            List<String> friends = new ArrayList<>();
                            if(userObj.has("friends")){
                                Iterator<String> keys = userObj.getJSONObject("friends").keys();
                                while(keys.hasNext()){
                                    friends.add(keys.next());
                                }
                            }
                            info.setFriends(friends);

                            List<String> events = new ArrayList<>();
                            if(userObj.has("activities")){
                                JSONObject eventsArr= userObj.getJSONObject("activities");
                                Iterator<String> ekeys = eventsArr.keys();
                                while(ekeys.hasNext()){
                                    events.add(ekeys.next());
                                }

                                info.setActivities(events);

                            }

                            info.setFirst_name(userObj.getString("first_name"));
                            info.setLast_name(userObj.getString("last_name"));
                            info.setProfile_url(userObj.getString("profile_image_url"));
                            info.setMajor(userObj.getString("major"));
                            info.setYear(userObj.getString("academic_level"));
                            info.setHometown(userObj.getString("hometown"));
                            info.setUid(userObj.getString("user_id"));
                            info.setDescription(userObj.getString("description"));
                            info.setEmail(userObj.getString("email"));
                            info.setVerified(userObj.getBoolean("verified"));
                        } catch (JSONException e) {
                            Log.d("readerror", e.toString());
                            e.printStackTrace();
                        }

                        users.add(info);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onDataReceived(users, GET_SUGGESTED_USERS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void joinGroup(String uid, String guid){
        final String url = site + join_group + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void leaveGroup(String uid, String guid){
        final String url = site + leave_group + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void requestVerification(String email, String uid){
        final String url = site + verify_email + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void registerToken(String uid, String notifToken){
        final String url = site + add_token + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("notification_token", notifToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("register-3", "works");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void inviteUser(String senderUid, String uid, String auid){
        final String url = site + invite_user + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("sender", senderUid);
            params.put("auid", auid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void postComment(final OnDataReceived listener, String uid, String msg, String auid){
        final String url = site + post_comment + "token=" + token;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("auid", auid);
            params.put("text", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onDataReceived(true, POST_COMMENT);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getComments(final OnDataReceived listener, String auid){
        final String url = site + get_comments + "token=" + token + "&school_identifier=" + domain + "&auid=" + auid;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("auid", auid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("commentdata", response.toString());
                List<MessageInfo> list = new ArrayList<>();

                JSONObject message;
                Iterator<String> keys = response.keys();
                while(keys.hasNext()){
                    try {
                        message = response.getJSONObject(keys.next());
                        MessageInfo info = new MessageInfo(message.getString("user_id"), message.getString("text"));
                        list.add(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onDataReceived(list, GET_COMMENTS);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getUsers(final OnDataReceived listener){
        final String url = site + get_users + "token=" + token + "&school_identifier=" + domain;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<UserInfo> list = new ArrayList<>();

                JSONObject message;
                Iterator<String> keys = response.keys();
                while(keys.hasNext()){
                    try {
                        String key = keys.next();
                        message = response.getJSONObject(key);
                        UserInfo info = new UserInfo();
                        info.setUid(key);
                        info.setFirst_name(message.getString("first_name"));
                        info.setLast_name(message.getString("last_name"));
                        info.setProfile_url(message.getString("profile_image_url"));
                        list.add(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onDataReceived(list, GET_USERS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getAllGroups(final OnDataReceived listener){
        final String url = site + get_groups + "token=" + token + "&school_identifier=" + domain;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<GroupInfo> list = new ArrayList<>();

                JSONObject grp;
                Iterator<String> k = response.keys();
                while(k.hasNext()){
                    try {
                        String key = k.next();
                        grp = response.getJSONObject(key);
                        GroupInfo group = new GroupInfo();

                        try {
                            group.setName(grp.getString("name"));
                            group.setAbbr(grp.getString("short_name"));
                            group.setColor(grp.getString("color"));
                            group.setDescription(grp.getString("details"));
                            group.setGuid(grp.getString("group_id"));

                            List<String> members = new ArrayList<>();
                            if(grp.has("members")){
                                Iterator<String> keys = grp.getJSONObject("members").keys();
                                while(keys.hasNext())
                                    members.add(keys.next());
                            }

                            group.setMembers(members);

                            List<String> activities = new ArrayList<>();
                            if(grp.has("activities")){
                                Iterator<String> keys = grp.getJSONObject("activities").keys();
                                while(keys.hasNext()){
                                    activities.add(keys.next());
                                }
                            }

                            group.setActivities(activities);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        list.add(group);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                listener.onDataReceived(list, GET_GROUPS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void isUserSuspended(final OnDataReceived listener, String uid){
        final String url = site + is_suspended + "token=" + token + "&school_identifier=" + domain + "&uid=" + uid;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean isSuspended = response.getBoolean("suspended");
                    listener.onDataReceived(isSuspended, IS_SUSPENDED);
                } catch (JSONException e) {
                    listener.onDataReceived(false, IS_SUSPENDED);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void deleteActivity(String uid, String auid){
        final String url = site + delete_activity + "token=" + token + "&school_identifier=" + domain + "&uid=" + uid;

        JSONObject params = new JSONObject();
        try {
            params.put("school_identifier", domain);
            params.put("uid", uid);
            params.put("auid", auid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               //empty
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url + " " + error.toString());
            }
        });

        queue.add(request);
    }


    private static List<String> interestsJsontoList(JSONArray array) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                data.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

}
