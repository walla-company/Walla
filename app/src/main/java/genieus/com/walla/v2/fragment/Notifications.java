package genieus.com.walla.v2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.NotificationInfoLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.NotificationInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notifications.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notifications extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String FRIEND_REQUEST = "friend_request";
    public static final String USER_INVITED = "user_invited";

    private ListView info_lv;
    private NotificationInfoLVAdapter infoAdapter;
    private List<NotificationInfo> list;
    private WallaApi api;
    private FirebaseAuth auth;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Notifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static Notifications newInstance(String param1, String param2) {
        Notifications fragment = new Notifications();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        info_lv = (ListView) view.findViewById(R.id.info_lv);
        api = new WallaApi(getContext());
        auth = FirebaseAuth.getInstance();

        api.getNotifications(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                list = (List<NotificationInfo>) data;
                initUi();
            }
        }, auth.getCurrentUser().getUid());

        return view;
    }

    private void initUi() {
        /*
        List<NotificationInfo> data = new ArrayList<>();
        data.add(new NotificationInfo("new freind", "Jimbo McBoosie sent you a friend request!"));
        data.add(new NotificationInfo("new freind", "Jimbo McBoosie sent you a friend request"));
        data.add(new NotificationInfo("new freind", "Jimbo McBoosie sent you a friend request"));
        data.add(new NotificationInfo("new freind", "Jimbo McBoosie sent you a friend request"));
        data.add(new NotificationInfo("new freind", "Jimbo McBoosie sent you a friend request"));
        */


        Collections.sort(list, new Comparator<NotificationInfo>() {
            @Override
            public int compare(NotificationInfo o1, NotificationInfo o2) {
                return Double.compare(o2.getTime_created(), o1.getTime_created());
            }
        });
        infoAdapter = new NotificationInfoLVAdapter(getContext(), R.layout.single_notification, list);
        info_lv.setAdapter(infoAdapter);
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
