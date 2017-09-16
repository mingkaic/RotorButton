package com.mingkaic.rotorbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

import com.nineoldandroids.view.ViewHelper;

public class RotorButton extends Button {

    public interface OnOrientationChangedListener {
        void onOrientationChanged(int action, double orientation);
    }

    // rotation_speed attributes
    private double rotation_speed;

    // button distance attribute
    private int offsetCenter;

    // action attributes
    int vibrationDuration;
    int eventVolume;

    private double currentOrientation = 0; // in degrees

    private float pivotX;
    private float pivotY;

    OnOrientationChangedListener onOrientationChangedListener;

    public RotorButton(Context context) {
        this(context, null);
    }

    public RotorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.RotatingButton, 0, 0);

        rotation_speed = attr.getInt(R.styleable.RotatingButton_mb_rotation_speed, 1);
        offsetCenter = attr.getDimensionPixelSize(R.styleable.RotatingButton_mb_offset_center, getResources().getDimensionPixelSize(R.dimen.default_offset_center));

        vibrationDuration = attr.getInt(R.styleable.RotatingButton_mb_vibration_duration, 0);
        eventVolume = attr.getInt(R.styleable.RotatingButton_mb_event_volume, 0);

        recalculateOrientation(this);

        attr.recycle();
    }

    /** Touch Event */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestTouchEvent();
                pivotX = this.getWidth() / 2.0f;
                pivotY = this.getHeight() / 2.0f;
                orientationChanged(MotionEvent.ACTION_DOWN, currentOrientation);
                break;
            case MotionEvent.ACTION_MOVE:
                float diffX = event.getX() - pivotX;
                float diffY = pivotY - event.getY();
                double tapAngle = getAngle(diffX, diffY);
                boolean ccw = isAClockwise(tapAngle);
                rotorView(this, ccw);
                orientationChanged(MotionEvent.ACTION_MOVE, currentOrientation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                orientationChanged(MotionEvent.ACTION_UP, currentOrientation);
        }
        return super.onTouchEvent(event);
    }

    private void rotorView(View v, boolean isAClockwise) {
        // re-calculate orientation
        double dOrientation = rotation_speed;
        if (!isAClockwise)
            dOrientation = -dOrientation;
        currentOrientation += (dOrientation + 360);
        currentOrientation %= 360;
        recalculateOrientation(v);
    }

    private void recalculateOrientation(View v) {
        Pair<Double, Double> coords = polar2Cartesian(offsetCenter, currentOrientation);

        ViewHelper.setTranslationX(v, coords.first.floatValue());
        ViewHelper.setTranslationY(v, -coords.second.floatValue());
    }

    private boolean isAClockwise(double tapOrientation) {
        double upperOri = currentOrientation + 450;
        double lowerOri = currentOrientation + 270;
        tapOrientation += 360;
        return upperOri >= tapOrientation && tapOrientation > lowerOri;
    }

    private Pair<Double, Double> polar2Cartesian(int radius, double theta_deg) {
        double theta = theta_deg * Math.PI / 180;
        double x = radius * Math.cos(theta);
        double y = radius * Math.sin(theta);
        return new Pair<>(x, y);
    }

    private double getAngle(double dX, double dY) {
        double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        double rad = 0;
        if (length != 0) {
            rad = Math.asin(dY / length);
        }
        if (dX < 0d) {
            if (dY >= 0d) {
                rad = Math.PI - rad;
            }
            else {
                rad = -rad + Math.PI;
            }
        }
        else if (dY < 0) {
            rad = rad + 2d * Math.PI;
        }
        return rad * 180 / Math.PI;
    }

    private void orientationChanged(int action, double orientation) {
        if (onOrientationChangedListener != null)
            onOrientationChangedListener.onOrientationChanged(action, orientation);
    }

    private void requestTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
    }


    // Getter and Setter
    public double getRotation_speed() {
        return rotation_speed;
    }

    public void setRotation_speed(double rotation_speed) {
        this.rotation_speed = rotation_speed % 50;
    }

    public OnOrientationChangedListener getOnOrientationChangedListener() {
        return onOrientationChangedListener;
    }

    public void setOnOrientationChangedListener(OnOrientationChangedListener onOrientationChangedListener) {
        this.onOrientationChangedListener = onOrientationChangedListener;
    }

    public int getOffsetCenter() {
        return offsetCenter;
    }

    public void setOffsetCenter(int offsetCenter) {
        this.offsetCenter = offsetCenter;
        recalculateOrientation(this);
    }

}
