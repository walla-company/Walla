package genieus.com.walla.v2.info;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by anesu on 12/23/16.
 */

public class Fonts {
    private Context context;
    static public Typeface AzoSansMedium, AzoSansRegular, AzoSansBold;

    public Fonts(Context context){
        this.context = context;

        AzoSansMedium = Typeface.createFromAsset(context.getAssets(), "fonts/AzoSans-Medium.otf");
        AzoSansRegular = Typeface.createFromAsset(context.getAssets(), "fonts/AzoSans-Regular.otf");
        AzoSansBold = Typeface.createFromAsset(context.getAssets(), "fonts/AzoSans-Bold.otf");
    }



}
