package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Details;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.MessageInfo;

/**
 * Created by anesu on 1/31/17.
 */

public class DiscussionLVAdapter extends ArrayAdapter<MessageInfo>{
    private List<MessageInfo> list;
    private int resource;
    private Fonts fonts;

    public DiscussionLVAdapter(Context context, int resource, List<MessageInfo> list) {
        super(context, resource);
        this.resource = resource;
        this.list = list;

        fonts = new Fonts(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(resource, null);

        MessageInfo info = list.get(position);

        final CircleImageView image = (CircleImageView) view.findViewById(R.id.profile_image);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView message = (TextView) view.findViewById(R.id.message);

        name.setTypeface(fonts.AzoSansMedium);
        message.setTypeface(fonts.AzoSansRegular);

        name.setText(info.getName());
        message.setText(info.getMessage());

        if(info.getUrl() != null && !info.getUrl().equals("")) {
            if(!info.getUrl().startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(getContext()) //Context
                        .load(info.getUrl()) //URL/FILE
                        .into(image);//an ImageView Object to show the loaded image;
            }else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(info.getUrl()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.with(getContext()) //Context
                                    .load(task.getResult().toString()) //URL/FILE
                                    .into(image);//an ImageView Object to show the loaded image;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }

        return view;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
