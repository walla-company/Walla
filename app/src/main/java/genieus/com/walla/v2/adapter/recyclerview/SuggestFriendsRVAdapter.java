package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.MutualFriendInfo;
import genieus.com.walla.v2.viewholder.SuggestFriendsHolder;

/**
 * Created by anesu on 12/21/16.
 */

public class SuggestFriendsRVAdapter extends RecyclerView.Adapter<SuggestFriendsHolder>{
    private Context context;
    List<MutualFriendInfo> data;

    public SuggestFriendsRVAdapter(Context context, List<MutualFriendInfo> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public SuggestFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.single_friend_suggest, parent, false);
        return new SuggestFriendsHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestFriendsHolder holder, int position) {
        MutualFriendInfo info = data.get(position);

        holder.name.setText(info.getName());
        holder.mutualFriends.setText(info.getMutualFriends() + " mutual friends");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
