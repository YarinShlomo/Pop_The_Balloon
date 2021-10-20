package com.example.pop_the_balloon;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import java.util.Objects;

//set balloons properties and the way they act
public class Balloon extends androidx.appcompat.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;
    private BalloonListener mListener;
    private boolean mPopped;

    public Balloon(Context context) {
        super(context);
    }

    /**
     *
     * @param context who called you
     * @param color rgb color
     * @param rawHeight the height of the balloon (length changes according)
     */
    public Balloon(Context context, int color, int rawHeight) {
        super(context);

        mListener = (BalloonListener) context;


        Drawable balloon = ContextCompat.getDrawable(context, R.drawable.balloon);

        Objects.requireNonNull(balloon).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        // each balloon represented by 2 pictures, balloon and "!" sign on him
        Drawable[] layers = new Drawable[2];
        layers[0] = balloon;
        layers[1] = ContextCompat.getDrawable(context, R.drawable.effectballon);
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        this.setImageDrawable(layerDrawable);

        int rawWidth = rawHeight / 2;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }


    /**
     *
     * @param screenHeight height of the screen
     * @param duration how long will the balloon appear on the screen
     */
    public void releaseBalloon(int screenHeight, int duration) {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        // start "releasing" balloons
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!mPopped) {
            mListener.popBalloon(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY((float) valueAnimator.getAnimatedValue());
    }

    //whenever user popes a balloon the balloon animation will stop and mPopped = true
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mPopped && event.getAction() == MotionEvent.ACTION_DOWN) {
            mListener.popBalloon(this, true);
            mPopped = true;
            mAnimator.cancel();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            performClick();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setPopped(boolean popped) {
        mPopped = popped;
        if (popped) {
            mAnimator.cancel();
        }

    }

    // implemented in MainActivity
    public interface BalloonListener {
        void popBalloon(Balloon balloon, boolean userTouch);
    }

}
