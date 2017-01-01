package genieus.com.walla.v2.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import genieus.com.walla.R;

/**
 * Created by anesu on 12/31/16.
 */
public class MiniGroupViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout container;
    public TextView label;

    public MiniGroupViewHolder(View itemView) {
        super(itemView);
        container = (RelativeLayout) itemView.findViewById(R.id.group_icon_container);
        label =(TextView) itemView.findViewById(R.id.abbr);
    }
}
