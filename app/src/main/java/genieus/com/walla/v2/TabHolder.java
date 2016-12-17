package genieus.com.walla.v2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/17/2016.
 */
public class TabHolder  extends RecyclerView.ViewHolder  {
    TextView tabName;
    RelativeLayout container;
    public TabHolder(View itemView) {
        super(itemView);
        tabName = (TextView) itemView.findViewById(R.id.tab_name);
        container = (RelativeLayout) itemView.findViewById(R.id.tab_container);
    }

}
