package genieus.com.walla.v2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/13/2016.
 */
public class FilterVH extends RecyclerView.ViewHolder{
    ImageView close;
    TextView text;
    LinearLayout container;

    public FilterVH(View itemView) {
        super(itemView);

        close = (ImageView) itemView.findViewById(R.id.close);
        text = (TextView) itemView.findViewById(R.id.title);
        container = (LinearLayout) itemView.findViewById(R.id.container);
    }
}
