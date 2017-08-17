package genieus.com.walla.v2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import genieus.com.walla.R;

public class Login extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = getLayoutInflater(savedInstanceState)
                .inflate(R.layout.activity_login_screen, null, false);
        return layout;
    }
}
