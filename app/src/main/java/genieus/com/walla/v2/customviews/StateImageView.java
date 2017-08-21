package genieus.com.walla.v2.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by anesu on 8/13/17.
 */

public class StateImageView extends ImageView {
    public enum State {
        ACTIVE, NOT_ACTIVE, DISABLED;
    }

    private State currentState;

    public StateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentState = State.NOT_ACTIVE;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(final State nextState) {
        currentState = nextState;
    }
}
