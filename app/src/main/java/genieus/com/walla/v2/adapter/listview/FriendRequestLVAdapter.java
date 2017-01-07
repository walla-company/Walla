package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import genieus.com.walla.v2.info.FriendRequestInfo;

/**
 * Created by anesu on 1/6/17.
 */

public class FriendRequestLVAdapter extends ArrayAdapter<FriendRequestInfo> {
    private List data;
    public FriendRequestLVAdapter(Context context, int resource, List<FriendRequestInfo> data) {
        super(context, resource);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
