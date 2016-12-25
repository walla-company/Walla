package genieus.com.walla.v2.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import genieus.com.walla.R;

/**
 * Created by Anesu on 9/3/2016.
 */
public class FilterViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public TextView label;
    public RelativeLayout container1, container2;

    public FilterViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.filter_img);
        label = (TextView) itemView.findViewById(R.id.filter_name);
        container1 = (RelativeLayout) itemView.findViewById(R.id.container);
        container2 = (RelativeLayout) itemView.findViewById(R.id.interest_container);
    }
}
