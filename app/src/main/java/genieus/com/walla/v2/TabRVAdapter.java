package genieus.com.walla.v2;

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

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/17/2016.
 */
public class TabRVAdapter extends RecyclerView.Adapter<TabHolder> {
    Context context;
    List<String> list;

    public TabRVAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TabHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.single_tab, parent, false);

        return new TabHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TabHolder holder, int position) {
        holder.tabName.setText(list.get(position));
        /*
        if(isGroup(list.get(position))){
            Drawable background = holder.container.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.colorPrimary));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.colorPrimary));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isGroup(String tag){
        switch (tag){
            case "CS201":
                return true;
            case "Dab Squad":
                return true;
            default:
                return false;
        }
    }
}
