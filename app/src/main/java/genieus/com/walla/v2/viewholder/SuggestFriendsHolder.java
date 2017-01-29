package genieus.com.walla.v2.viewholder;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;

/**
 * Created by anesu on 12/21/16.
 */
public class SuggestFriendsHolder extends RecyclerView.ViewHolder {
    public CircleImageView icon;
    public TextView name, mutualFriends;
    public LinearLayout container;
    public Button addBtn;
    public SuggestFriendsHolder(View itemView) {
        super(itemView);
        icon = (CircleImageView) itemView.findViewById(R.id.profile_image);
        name = (TextView) itemView.findViewById(R.id.name);
        mutualFriends = (TextView) itemView.findViewById(R.id.mutual_friends);
        addBtn = (Button) itemView.findViewById(R.id.add_friend);
        container = (LinearLayout) itemView.findViewById(R.id.container);
    }
}
