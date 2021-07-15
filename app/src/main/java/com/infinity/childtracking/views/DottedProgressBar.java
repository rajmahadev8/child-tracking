package com.infinity.childtracking.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.infinity.childtracking.R;

public class DottedProgressBar extends View {

    private int dotRadius = 12;
    private int bounceRadius =16;

    private int dotPosition;
    private int dotAmount =3;


    public DottedProgressBar(Context context) {
        super(context);
    }

    public DottedProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DottedProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DottedProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        paint.setColor(getResources().getColor(R.color.tomato));

        createDot(canvas,paint);
    }

    private void createDot(Canvas canvas, Paint paint) {
        for(int i=0;i<dotAmount;i++){
            if(i== dotPosition){
                canvas.drawCircle(30+(i*40),bounceRadius,bounceRadius,paint);
            }else{
                canvas.drawCircle(30+(i*40),bounceRadius,dotRadius,paint);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        startAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width,height;

        int calculatedWidth = (20*9);
        width = calculatedWidth;
        height = bounceRadius*2;

        setMeasuredDimension(width,height);
    }

    private void startAnimation() {
        BounceAnimation bounceAnim = new BounceAnimation();
        bounceAnim.setDuration(500);
        bounceAnim.setRepeatCount(Animation.INFINITE);
        bounceAnim.setInterpolator(new LinearInterpolator());
        bounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                dotPosition++;
                if (dotPosition == dotAmount){
                    dotPosition =0;
                }
            }
        });
        startAnimation(bounceAnim);
    }
    class BounceAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            invalidate();
        }
    }
}

