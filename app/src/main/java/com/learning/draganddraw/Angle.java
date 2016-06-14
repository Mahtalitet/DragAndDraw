package com.learning.draganddraw;

import android.graphics.PointF;
import android.util.Log;

public class Angle {
    private static final String TAG = Angle.class.toString();

    private PointF mStartPoint;

    public Angle(PointF startPoint) {
        mStartPoint = startPoint;
    }

    public float getDegree(PointF current) {
        return getAngle(current);
    }

    private float getAngle(PointF current) {
        float angle =  (float) Math.toDegrees(Math.atan2(mStartPoint.y - current.y, mStartPoint.x - current.x)) / 12;
        Log.d(TAG, "Find angle is: "+angle);
        return angle;
    }
}
