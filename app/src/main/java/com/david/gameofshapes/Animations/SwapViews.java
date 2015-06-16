package com.david.gameofshapes.Animations;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.david.gameofshapes.Animations.Flip3dAnimation;

/**
 * Created by david on 08/02/2015.
 */
public class SwapViews implements Runnable {

    private boolean mIsFirstView;
    ImageView image1;
    ImageView image2;

    public SwapViews(boolean isFirstView, ImageView image1, ImageView image2) {
        mIsFirstView = isFirstView;
        this.image1 = image1;
        this.image2 = image2;
    }

    @Override
    public void run() {
        final float centerX = image1.getWidth() / 2.0f;
        final float centerY = image1.getHeight() / 2.0f;
        Flip3dAnimation rotation;
        if (mIsFirstView) {
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.VISIBLE);
            image2.requestFocus();
            //ScaleAnimation anim = new ScaleAnimation(0f,1.0f,0f,1.0f,centerX,centerY);
            AlphaAnimation anim = new AlphaAnimation(0f,1f);
            anim.setDuration(500);
            anim.setFillAfter(true);
            anim.setInterpolator(new DecelerateInterpolator());
            image2.startAnimation(anim);
            //rotation = new Flip3dAnimation(90, 0, centerX, centerY);
        } else {
            image2.setVisibility(View.GONE);
            image1.setVisibility(View.VISIBLE);
            image1.requestFocus();
            //ScaleAnimation anim = new ScaleAnimation(0f,1.0f,0f,1.0f,centerX,centerY);
            AlphaAnimation anim = new AlphaAnimation(0f,1f);
            anim.setDuration(500);
            anim.setFillAfter(true);
            anim.setInterpolator(new DecelerateInterpolator());
            image1.startAnimation(anim);
            //rotation = new Flip3dAnimation(90, 0, centerX, centerY);
        }
        /**rotation.setDuration(250);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        if (mIsFirstView) {
            image2.startAnimation(rotation);
        } else {
            image1.startAnimation(rotation);
        }**/
    }
}
