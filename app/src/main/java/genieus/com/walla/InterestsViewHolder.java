package genieus.com.walla;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsViewHolder extends RecyclerView.ViewHolder {

    ImageView iv;
    TextView tv;
    RelativeLayout rl, container;

    public InterestsViewHolder(View itemView) {
        super(itemView);
        iv = (ImageView) itemView.findViewById(R.id.filter_img);
        tv = (TextView) itemView.findViewById(R.id.filter_name);
        rl = (RelativeLayout) itemView.findViewById(R.id.container);
        container = (RelativeLayout) itemView.findViewById(R.id.interest_container);
    }
}
