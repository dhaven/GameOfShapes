package com.david.gameofshapes.Animations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;

public class DisplayNextView implements Animation.AnimationListener {

    int fromImage;
    int toImage;
    ImageButton shape;

    public DisplayNextView(int image1, int image2, ImageButton shape) {
        this.shape = shape;
        this.fromImage = image1;
        this.toImage = image2;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        shape.setImageResource(toImage);
        shape.setTag(toImage);
        final float centerX = shape.getWidth() / 2.0f;
        final float centerY = shape.getHeight() / 2.0f;
        final Flip3dAnimation rotation = new Flip3dAnimation(90,0, centerX, centerY);
        rotation.setDuration(150);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        shape.startAnimation(rotation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
