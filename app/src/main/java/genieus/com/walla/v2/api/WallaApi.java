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
import org.json.JSONObject;

/**
 * Created by anesu on 12/25/16.
 */

public class WallaApi {
    //api call identifiers
    public static final int MIN_VERSION = 0;
    public static final int DOMAINS = 1;
    public static final int ACTIVITIES = 2;



    public interface OnDataReceived{
        public void onDataReceivedObject(JSONObject data, int call);
        public void onDataReceivedArray(JSONArray data, int call);
    }

    private static String token = "8ac6-dFN2m2d2d";
    private static String platform = "android";
    private static String site = "https://wallaserver.herokuapp.com";
    private static String min_version = "/api/min_version?";
    private static String domains = "/api/domains?";
    private static String activities = "/api/activities?";

    private static String domain = "duke-*-edu";
    private static RequestQueue queue;


    public WallaApi(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public static void getMinVersion(final OnDataReceived listener){
        final String url = site + min_version + "token=" + token + "&platform=" + platform;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onDataReceivedObject(response, MIN_VERSION);
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
                listener.onDataReceivedObject(response, DOMAINS);
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
                listener.onDataReceivedArray(response, DOMAINS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonerror", url +  " " + error.toString());
            }
        });

        queue.add(request);
    }

    public static void getActivities(final OnDataReceived listener){
        getActivities(listener, 24);
    }

}
