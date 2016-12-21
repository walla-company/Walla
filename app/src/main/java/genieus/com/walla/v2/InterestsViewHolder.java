package genieus.com.walla.v2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import genieus.com.walla.R;

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsViewHolder extends RecyclerView.ViewHolder {

    ImageView icon;
    TextView label;
    RelativeLayout container;

    public InterestsViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.interests_icon);
        label = (TextView) itemView.findViewById(R.id.interests_label);
        container = (RelativeLayout) itemView.findViewById(R.id.interests_container);
    }
}
