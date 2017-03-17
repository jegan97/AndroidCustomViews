package com.example.jegansbeast.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by JEGAN'S BEAST on 3/17/2017.
 */

public class BorderLinearLayout extends LinearLayout implements View.OnClickListener{

    Paint paint;
    BorderAnimation animation;
    int borderColor,borderWidth;
    int animationSpeed;
    boolean animated,top,right,bottom,left;
    float h,w;

    public BorderLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        bottom = left = right = top = false;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animation = new BorderAnimation();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderLinearLayout, 0, 0);
        try {

            borderColor = ta.getColor(R.styleable.BorderLinearLayout_border_color, Color.BLACK);
            borderWidth = ta.getInt(R.styleable.BorderLinearLayout_border_width,10);
            animated = ta.getBoolean(R.styleable.BorderLinearLayout_animate_onclick,true);
            animationSpeed = ta.getInt(R.styleable.BorderLinearLayout_animation_speed,2000);

        } finally {
            ta.recycle();
        }

        animation.setDuration(animationSpeed/4);

        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);

        if(!animated){
            bottom = left = right = top = true;
        }

        setWillNotDraw(false);
        setPadding(borderWidth,borderWidth,borderWidth,borderWidth);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);

        if(!top && !right && !bottom && !left)
            return;

        if(top){
            canvas.drawLine(0,0,getWidth(),0,paint);
        }
        else{
            canvas.drawLine(0,0,w,0,paint);
            return;
        }

        if(right){
            canvas.drawLine(getWidth(),0,getWidth(),getHeight(),paint);
        }
        else{
            canvas.drawLine(getWidth(),0,getWidth(),h,paint);
            return;
        }

        if(bottom){
            canvas.drawLine(getWidth(),getHeight(),0,getHeight(),paint);
        }
        else{
            canvas.drawLine(getWidth(),getHeight(),getWidth()-w,getHeight(),paint);
            return;
        }

        if(left){
            canvas.drawLine(0,getHeight(),0,0,paint);
        }
        else{
            canvas.drawLine(0,getHeight(),0,getHeight()-h,paint);
        }



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {

        if(animated) {

            if(left){
                bottom = left = right = top = false;
                invalidate();
                return;
            }

            bottom = left = right = top = false;

            startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    if(!top){
                        top = true;
                        startAnimation(animation);
                        return;
                    }

                    if(!right){
                        right = true;
                        startAnimation(animation);
                        return;
                    }
                    if(!bottom){
                        bottom = true;
                        startAnimation(animation);
                        return;
                    }
                    if(!left){
                        left = true;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private class BorderAnimation extends Animation{

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Log.d("xyz",interpolatedTime+"");
            h = getWidth()*interpolatedTime;
            w = getHeight()*interpolatedTime;
            requestLayout();
        }

    }
}
