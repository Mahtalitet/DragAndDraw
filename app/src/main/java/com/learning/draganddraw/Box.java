package com.learning.draganddraw;


import android.annotation.TargetApi;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable{

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    private PointF mOrigin;
    private PointF mCurrent;
    private Matrix mStyle;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;
        mStyle = new Matrix();
    }

    protected Box(Parcel in) {
        mOrigin = in.readParcelable(PointF.class.getClassLoader());
        mCurrent = in.readParcelable(PointF.class.getClassLoader());
        mStyle = new Matrix();
        float[] getValues = new float[9];
        in.readFloatArray(getValues);
        mStyle.setValues(getValues);
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public Matrix getStyle() {
        return mStyle;
    }

    public void rotate(float angle, float pointX, float pointY) {
        if (mStyle != null) {
            mStyle.preRotate(angle, pointX, pointY);
        }
    }

    public void rotate(float angle) {
        if (mStyle != null) {
            mStyle.preRotate(angle, mCurrent.x, mCurrent.y);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mOrigin, flags);
        dest.writeParcelable(mCurrent, flags);
        float[] values = new float[9];
        mStyle.getValues(values);
        dest.writeFloatArray(values);
    }


}
