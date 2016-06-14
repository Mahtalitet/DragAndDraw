package com.learning.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
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
    private static final int FIRST_POINTER_ID = 0;
    private static final int SECOND_POINTER_ID = 1;

    private Box mCurrentBox;
    private Angle mCurrentAngle;
    private ArrayList<Box> mBoxes;
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private Path mCurrentPath;
    private RectF mCurrentRectangle;
    private int mCurrentFingerId;

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
        mCurrentPath = new Path();
        mCurrentRectangle = new RectF();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() == 1) {
            mCurrentFingerId = event.getPointerId(FIRST_POINTER_ID);
        } else {
            mCurrentFingerId = event.getPointerId(SECOND_POINTER_ID);
        }

        int pointerIndex = event.findPointerIndex(mCurrentFingerId);
        PointF curr;

        if (mCurrentFingerId == event.getPointerId(FIRST_POINTER_ID)) {
            Log.e(TAG, "This is first finger!");
            curr = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
            Log.d(TAG, "(FIRST) Received event at x="+curr.x+", y="+curr.y+":");

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "(FIRST) ACTION_DOWN: User pressed at the screen.");
                    mCurrentBox = new Box(curr);
                    mBoxes.add(mCurrentBox);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "(FIRST) ACTION_MOVE: User is moving by the screen.");
                    if (mCurrentBox != null) {
                        mCurrentBox.setCurrent(curr);
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "(FIRST) ACTION_UP: User put away a finger from the screen.");
                    mCurrentBox = null;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d(TAG, "(FIRST) ACTION_CANCEL: Parent view intercept an action event.");
                    mCurrentBox = null;
                    break;
            }
        } else if (mCurrentFingerId == event.getPointerId(SECOND_POINTER_ID)) {
            Log.e(TAG, "This is second finger!");
            curr = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
            Log.d(TAG, "(SECOND) Received event at x="+curr.x+", y="+curr.y+":");

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e(TAG, "(SECOND) ACTION_DOWN: User pressed at the screen.");
                    mCurrentAngle = new Angle(curr);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e(TAG, "(SECOND) ACTION_MOVE: User is moving by the screen.");
                    if (mCurrentAngle != null) {
                        mCurrentBox.rotate(mCurrentAngle.getDegree(curr));
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.e(TAG, "(SECOND) ACTION_UP: User put away a finger from the screen.");
                    mCurrentAngle = null;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e(TAG, "(SECOND) ACTION_CANCEL: Parent view intercept an action event.");
                    mCurrentAngle = null;
                    break;
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (int i = 0; i < mBoxes.size(); i++) {
            Box box = mBoxes.get(i);
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            mCurrentRectangle.set(left, top, right, bottom);
            mCurrentPath.reset();
            mCurrentPath.addRect(mCurrentRectangle, Path.Direction.CW);
            mCurrentPath.transform(box.getStyle());
            canvas.drawPath(mCurrentPath, mBoxPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_PARCEL, super.onSaveInstanceState());
        bundle.putParcelableArrayList(KEY_BOXES, mBoxes);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mBoxes = bundle.getParcelableArrayList(KEY_BOXES);
            invalidate();
            state = bundle.getParcelable(KEY_SUPER_PARCEL);
        }

        super.onRestoreInstanceState(state);
    }
}
