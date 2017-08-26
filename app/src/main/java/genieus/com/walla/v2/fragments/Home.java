package genieus.com.walla.v2.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapters.EventsAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.datatypes.Event;
import genieus.com.walla.v2.utils.Fonts;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView interest_rv;
    private static ListView events_lv;
    private static EventsAdapter adapterEvents;
    private List<Event> events;
    private static TextView filter_tv;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private static AlertDialog alert;
    private Fonts fonts;
    private String currentFilter = "";

    private static Context context;

    private static WallaApi api;
    private static FirebaseAuth auth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initUi() {
        context = getContext();
        api = WallaApi.getInstance(getContext());
        fonts = new Fonts(getContext());
        events = new ArrayList<>();
        initEvents();
    }

    private void initEvents() {
        final Context context = getContext();

        api.getActivities(auth.getCurrentUser().getUid(), new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                List<Event> temp = (List<Event>) data;
                if(!temp.equals(events)) {
                    events = temp;
                    adapterEvents = new EventsAdapter(context, R.layout.single_activity, events);
                    events_lv.setAdapter(adapterEvents);
                    adapterEvents.getFilter().filter("");
                }
            }
        });

    }

    public static void refreshPage(final boolean force) {
        swipeRefreshLayout.setRefreshing(false);
        api.getActivities(auth.getCurrentUser().getUid(), new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                if(force || ((List<Event>) data).size() != adapterEvents.getCount()) {
                    adapterEvents = new EventsAdapter(context, R.layout.single_activity, (List<Event>) data);
                    events_lv.setAdapter(adapterEvents);
                    adapterEvents.getFilter().filter("");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        events_lv = (ListView) view.findViewById(R.id.events);
        filter_tv = (TextView) view.findViewById(R.id.filter_label);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(true);
            }
        });

        auth = FirebaseAuth.getInstance();

        initUi();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
