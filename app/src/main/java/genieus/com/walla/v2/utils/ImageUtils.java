package genieus.com.walla.v2.utils;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;

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

    public static void saveProfilePic(Bitmap picture, final String uid){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://walla-launch.appspot.com");

        UploadTask uploadTask = storageRef.child("profile_images")
                .child(uid + ".jpg")
                .putBytes(compressToByteArray(picture));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //better make sure you have called the initApi method before calling this
                WallaApi.saveUserProfileImageUrl(uid, downloadUrl.toString());
            }
        });
    }

    public static  byte[] compressToByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byte_data = stream.toByteArray();
        return byte_data;
    }
}
