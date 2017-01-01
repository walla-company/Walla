package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.viewholder.InterestsViewHolder;
import genieus.com.walla.v2.viewholder.MiniGroupViewHolder;

/**
 * Created by anesu on 12/31/16.
 */

public class MiniGroupRVAdapter extends RecyclerView.Adapter<MiniGroupViewHolder>{

    private Context context;
    private List<GroupInfo> data;

    public MiniGroupRVAdapter(Context context, List<GroupInfo> data){
        this.data = data;
        this.context = context;
    }
    @Override
    public MiniGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_group_mini, parent, false);

        return new MiniGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MiniGroupViewHolder holder, int position) {
        GroupInfo info = data.get(position);

        changeBackGroundColor(holder.container, info.getColor());
        holder.label.setText(info.getAbbr());
    }

    private void changeBackGroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
