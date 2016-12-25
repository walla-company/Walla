package genieus.com.walla.v2.info;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by anesu on 12/24/16.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / dp);
        return noOfColumns;
    }
}
