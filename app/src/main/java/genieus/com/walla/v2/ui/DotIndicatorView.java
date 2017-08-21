package genieus.com.walla.v2.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Utility;
import genieus.com.walla.v2.utils.ImageUtils;

/**
 * Created by anesu on 8/20/17.
 */

public class DotIndicatorView extends RelativeLayout {
    private int mDotTotal;
    private View activeDot;
    private List<View> mDotList;

    public DotIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray params = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DotIndicatorView,
                0, 0);

        mDotTotal = params.getInteger(R.styleable.DotIndicatorView_totalDots, 0);

        initDots();
    }

    public DotIndicatorView(Context context, final int dotTotal) {
        super(context);
        mDotTotal = dotTotal;
        initDots();
    }

    private void initDots() {
        final LinearLayout container = new LinearLayout(getContext());
        final RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        containerParams.addRule(CENTER_IN_PARENT);
        container.setLayoutParams(containerParams);
        addView(container);

        mDotList = new ArrayList<>(mDotTotal);

        for (int i = 0; i < mDotTotal; i++) {
            final View dot = new View(getContext());
            dot.setBackground(getResources().getDrawable(R.drawable.circle));
            ImageUtils.changeDrawableColor(dot.getBackground(), Color.WHITE);

            final LinearLayout.LayoutParams dotParams =
                    new LinearLayout.LayoutParams(Utility.dpToPx(12), Utility.dpToPx(12));
            dotParams.setMargins(16, 16, 16, 16);
            dotParams.weight = 1;
            dotParams.gravity = Gravity.CENTER_VERTICAL;
            dot.setLayoutParams(dotParams);

            container.addView(dot);
            mDotList.add(dot);
        }

        setActiveDot(1);
    }

    public void setActiveDot(final int dot) {
        if (dot <= mDotTotal) {
            final View nextActive = mDotList.get(dot - 1);
            ImageUtils.changeDrawableColor(nextActive.getBackground(), Color.BLACK);

            if (activeDot != null) {
                ImageUtils.changeDrawableColor(activeDot.getBackground(), Color.WHITE);
            }

            activeDot = nextActive;
        }
    }
}
