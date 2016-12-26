package genieus.com.walla.v2.info;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by anesu on 12/24/16.
 */

public class Utility {
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
}
