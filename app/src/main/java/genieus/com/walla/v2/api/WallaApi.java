package genieus.com.walla.v2.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    public interface OnDataReceived{
        public void onDataReceived(Object data, int call);
    }

    private static int defaultActivityFilter = 24;

    private static String token = "8ac6-dFN2m2d2d";
    private static String platform = "android";
    private static String site = "https://wallaserver.herokuapp.com";
    private static String min_version = "/api/min_version?";
    private static String domains = "/api/domains?";
    private static String activities = "/api/activities?";
    private static String attendees = "/api/attendees?";
    private static String user_info = "/api/user_info?";
    private static String is_attending = "/api/is_attending?";


    private static String domain = "sandiego-*-edu";
    private static RequestQueue queue;


    public WallaApi(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public static void getMinVersion(final OnDataReceived listener){
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
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getAllowedDomains(final OnDataReceived listener){
        final String url = site + domains + "token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<DomainInfo> domains = new ArrayList<>();
                Iterator<String> keys = response.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    try {
                        domains.add(new DomainInfo(key, response.getString(key)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onDataReceived(domains, DOMAINS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getActivities(final OnDataReceived listener, int hours){
        final String url = site + activities + "token=" + token + "&domain=" + domain + "&filter=" + hours;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<EventInfo> events = new ArrayList<>();

                int len = response.length();
                for(int i = 0; i < len; i++){
                    try {
                        JSONObject event = response.getJSONObject(i);
                        EventInfo info = new EventInfo();

                        info.setTitle(event.getString("description"));
                        info.setLocation(event.getString("location"));
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
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getUserInfo(final OnDataReceived listener, String uid){
        final String url = site + user_info + "token=" + token + "&uid=" + uid + "&domain=" + domain;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserInfo info = new UserInfo();

                try {
                    info.setName(response.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onDataReceived(info, USER_INFO);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void isAttendingEvent(final OnDataReceived listener, String uid, String eid){
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
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getAttendees(final OnDataReceived listener, String eventId){
        final String url = site + attendees + "token=" + token + "&event=" + eventId + "&domain=" + domain;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onDataReceived(response, ATTENDEES);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);

    }

}
