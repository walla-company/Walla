package genieus.com.walla.v2.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import genieus.com.walla.R;

/**
 * Created by anesu on 8/13/17.
 */

public class ImageUtils {
    public static void changeDrawableColor(final Drawable drawable, final int color) {
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        }
    }

    public static void changeImageViewColor(final ImageView image, final int color) {
        image.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public static void loadImageFromUrl(final Context context,
                                        final ImageView icon,
                                        final Optional<String> urlOptional) {
        if (urlOptional.isPresent() && !urlOptional.get().isEmpty()) {
            if (!urlOptional.get().startsWith("gs://")) {
                Picasso.with(context)
                        .load(urlOptional.get())
                        .into(icon);
            } else {
                FirebaseStorage.getInstance()
                        .getReferenceFromUrl(urlOptional.get())
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Picasso.with(context)
                                            .load(task.getResult().toString())
                                            .into(icon);
                                }
                            }
                        });
            }
        } else {
           icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.default_profile));
        }
    }
}
