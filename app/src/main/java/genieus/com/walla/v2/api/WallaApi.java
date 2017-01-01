package genieus.com.walla.v2.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import genieus.com.walla.v2.info.DomainInfo;
import genieus.com.walla.v2.info.EventInfo;
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
    private static String verify_email = "/api/request_verification?";
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


    private static String domain = "duke";
    private static RequestQueue queue;


    public WallaApi(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
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

    public static void getUserInfo(final OnDataReceived listener, String uid) {
        final String url = site + user_info + "token=" + token + "&uid=" + uid + "&school_identifier=" + domain;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserInfo info = new UserInfo();

                try {
                    info.setFirst_name(response.getString("first_name"));
                    info.setLast_name(response.getString("last_name"));
                    info.setProfile_url("profile_image_url");
                    info.setMajor(response.getString("major"));
                    info.setYear(response.getString("academic_level"));
                    info.setHometown(response.getString("hometown"));
                    info.setUid(response.getString("user_id"));
                    info.setDescription(response.getString("description"));
                    info.setEmail(response.getString("email"));
                    info.setVerified(response.getBoolean("verified"));

                } catch (JSONException e) {
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

    public static void getActivity(final OnDataReceived listener, String auid){
        final String url = site + get_activity + "token=" + token + "&school_identifier=" + domain + "&auid=" + auid;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EventInfo event = new EventInfo();

                try {
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

                    Log.d("apidata", response.toString());

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

    public static void getActivities(final OnDataReceived listener){
        final String url = site + get_activities + "token=" + token + "&school_identifier=" + domain;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {

                Log.d("apiinfo", array.toString());

                List<EventInfo> events = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    try {
                        JSONObject response = array.getJSONObject(i);
                        EventInfo event = new EventInfo();

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

    public static void saveUserFirstName(String uid, String name){
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

    public static void saveUserLastName(String uid, String name){
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

    public static void saveUserEmail(String uid, String email){
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

    public static void saveUserAcademicLevel(String uid, String level){
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

    public static void saveUserMajor(String uid, String major){
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

    public static void saveUserHometown(String uid, String town){
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

    public static void saveUserDescription(String uid, String description){
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

    public static void saveUserProfileImageUrl(String uid, String imageUrl){
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



    private static List<String> interestsJsontoList(JSONArray array){
        List<String> data = new ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            try {
                data.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

}
