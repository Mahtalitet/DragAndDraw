package com.learning.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BoxDrawingView extends View {
    private static final String TAG = BoxDrawingView.class.toString();
    private static final String KEY_BOXES = "com.learning.draganddraw.boxes";
    private static final String KEY_SUPER_PARCEL = "com.learning.draganddraw.superParcel";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes;
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

        if (mBoxes == null) {
            mBoxes = new ArrayList<>();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF curr = new PointF(event.getX(), event.getY());
        Log.d(TAG, "Received event at x="+curr.x+", y="+curr.y+":");

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN: User pressed at the screen.");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: User is moving by the screen.");
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP: User put away a finger from the screen.");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL: Parent view intercept an action event.");
                mCurrentBox = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            Log.e(TAG, "Left: "+left);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            Log.e(TAG, "Right: "+right);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            Log.e(TAG, "Top: "+top);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            Log.e(TAG, "Bottom: "+bottom);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_PARCEL, super.onSaveInstanceState());
        bundle.putParcelableArrayList(KEY_BOXES, mBoxes);
        Log.d(TAG, "Initial size is: "+mBoxes.size());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mBoxes = bundle.getParcelableArrayList(KEY_BOXES);
            Log.d(TAG, "Restored size is: "+mBoxes.size());
            invalidate();
            state = bundle.getParcelable(KEY_SUPER_PARCEL);
        }

        super.onRestoreInstanceState(state);
    }
}
