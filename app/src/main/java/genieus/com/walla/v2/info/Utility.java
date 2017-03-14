package genieus.com.walla.v2.info;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import genieus.com.walla.v2.api.WallaApi;

/**
 * Created by anesu on 12/24/16.
 */

public class Utility {
    private static WallaApi api;

    public static void initApi(Context context){
        api = WallaApi.getInstance(context);
    }

    public static int calculateNoOfColumns(View view, double cellWidth, double padding) {
        double width = pxToDp(view.getMeasuredWidth());
        Log.d("Span", "" + width);
        int cols = (int) (width / cellWidth);
        if((cols * cellWidth) + (padding * (cols - 1)) < width){
            return cols;
        }else{
            return cols - 1;
        }
    }

    public static double sizeToFit(double parentWidth, int items, double padding){
        double paddingWitdth = padding * (items -1 );
        double widthLeft = parentWidth - paddingWitdth;
        double widthPerItem = widthLeft / items;
        return widthPerItem;
    }


    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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
                api.saveUserProfileImageUrl(uid, downloadUrl.toString());
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
