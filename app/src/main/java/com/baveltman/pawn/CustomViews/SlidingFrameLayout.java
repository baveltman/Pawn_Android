package com.baveltman.pawn.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class SlidingFrameLayout extends FrameLayout
{

    private static final String TAG = "SlidingFrameLayout";

    private float xFraction = 0;
    private ViewTreeObserver.OnPreDrawListener preDrawListener = null;

    public SlidingFrameLayout(Context context) {
        super(context);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public float getXFraction()
    {
        return this.xFraction;
    }

    public void setXFraction(float fraction) {
        this.xFraction = fraction;

        if (getWidth() == 0) {
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setXFraction(xFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }

        float translationX = getWidth() * fraction;
        setTranslationX(translationX);
    }
} 