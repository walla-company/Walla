package genieus.com.walla.v1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import genieus.com.walla.R;

/**
 * Created by Anesu on 9/10/2016.
 */
public class MyInterestsAdapter extends ArrayAdapter<String> {
    private DatabaseReference mDatabase;
    FirebaseUser user;
    MyInterests.Setting[] interests;
    int res;
    public MyInterestsAdapter(Context context, int resource, MyInterests.Setting[] interests) {
        super(context, resource);
        this.interests = interests;
        res = resource;

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(user.getEmail().trim().endsWith("@sandiego.edu")){
            mDatabase = mDatabase.child("sandiego-*-edu");
        }
    }

    @Override
    public int getCount() {
        return interests.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(res, parent, false);
        }

        final MyInterests.Setting setting = interests[position];

        TextView tv = (TextView) convertView.findViewById(R.id.interest_name);
        final Switch sw = (Switch) convertView.findViewById(R.id.interest_toggle);
        sw.setText(setting.name);
        sw.setTextSize(0);
        tv.setText(setting.name);
        sw.setChecked(setting.state);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.child("notification_settings").child(user.getUid()).child(buttonView.getText().toString().toLowerCase()).setValue(isChecked);
                Log.d("db", setting.name + " is now " + isChecked);
            }
        });


        return convertView;
    }
}