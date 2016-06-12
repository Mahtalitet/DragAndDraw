package com.learning.draganddraw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoxDrawingView extends View {
    private static final String TAG = BoxDrawingView.class.toString();

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN: User pressed at the screen.");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: User is moving by the screen.");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP: User put away a finger from the screen.");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL: Parent view intercept an action event.");
                break;
        }

        return true;
    }
}
