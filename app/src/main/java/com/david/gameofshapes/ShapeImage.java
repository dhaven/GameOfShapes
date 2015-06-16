package com.david.gameofshapes;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;

import com.david.gameofshapes.Animations.DisplayNextView;
import com.david.gameofshapes.Animations.Flip3dAnimation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by david on 31/01/2015.
 */
public class ShapeImage {

    private ImageButton shape;
    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private String level;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton shape = (ImageButton) v;
            ArrayList<String> correspondingShapes = new ArrayList<String>();
            int tag = ((Integer)shape.getTag()).intValue();
            if(level.equals("easy")){
                if(tag == R.drawable.triangle2) {
                    correspondingShapes = getCorrespondingShapes(R.drawable.triangle2);
                    if (correspondingShapes.size() != 0) {
                        shape.startAnimation(getRotation(0,-90,R.drawable.triangle2,R.drawable.square2, shape));
                    }
                }else if(tag == R.drawable.square2){
                    correspondingShapes = getCorrespondingShapes(R.drawable.square2);
                    if(correspondingShapes.size() != 0) {
                        shape.startAnimation(getRotation(0,-90,R.drawable.square2,R.drawable.penta2, shape));
                    }
                }else {
                    correspondingShapes = getCorrespondingShapes(R.drawable.penta2);
                    if (correspondingShapes.size() != 0) {
                        shape.startAnimation(getRotation(0,-90,R.drawable.penta2,R.drawable.triangle2, shape));
                    }
                }
                for(String parse : correspondingShapes){
                    ImageButton butt = getCorrButton(parse);
                    butt.startAnimation(getRotation(0,-90,(Integer)butt.getTag(), getNextTag(butt),butt));
                }
            }else if(level.equals("medium")){
                if(tag == R.drawable.triangle2) {
                    correspondingShapes = getCorrespondingShapes(R.drawable.triangle2);
                    if (correspondingShapes.size() != 0) {
                        shape.startAnimation(getRotation(0,-90,R.drawable.triangle2,R.drawable.square2,shape));
                    }
                }else if(tag == R.drawable.square2){
                    correspondingShapes = getCorrespondingShapes(R.drawable.square2);
                    if(correspondingShapes.size() != 0) {
                        shape.startAnimation(getRotation(0,-90,R.drawable.square2,R.drawable.penta2,shape));
                    }
                }
                if(correspondingShapes.size() != 0) {
                    for (String parse : correspondingShapes) {
                        ImageButton butt = getCorrButton(parse);
                        butt.startAnimation(getRotation(0,-90,(Integer)butt.getTag(), getNextTag(butt),butt));
                    }
                }
            }else{
                if(tag == R.drawable.triangle2) {
                    shape.startAnimation(getRotation(0,-90,R.drawable.triangle2,R.drawable.square2,shape));
                }else if(tag == R.drawable.square2){
                    shape.startAnimation(getRotation(0,-90,R.drawable.square2,R.drawable.penta2,shape));
                }else{
                    shape.startAnimation(getRotation(0,-90,R.drawable.penta2,R.drawable.triangle2,shape));
                }
                if(up != null){
                    up.startAnimation(getRotation(0,-90,(Integer)up.getTag(), getNextTag(up),up));
                }
                if(right != null){
                    right.startAnimation(getRotation(0,-90,(Integer)right.getTag(), getNextTag(right),right));
                }
                if(down != null){
                    down.startAnimation(getRotation(0,-90,(Integer)down.getTag(), getNextTag(down),down));
                }
                if(left != null){
                    left.startAnimation(getRotation(0,-90,(Integer)left.getTag(), getNextTag(left),left));
                }

            }
        }
    };

    private Flip3dAnimation getRotation(float start, float end, int fromImage, int toImage, ImageButton shape) {
        // Find the center of image
        final float centerX = shape.getWidth() / 2.0f;
        final float centerY = shape.getHeight() / 2.0f;
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
        rotation.setDuration(150);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(fromImage, toImage, shape));
        return rotation;
    }

    public ShapeImage(Context context, String level, String shape){
        this.level = level;
        this.shape = new ImageButton(context);
        this.shape.setLayoutParams(new TableRow.LayoutParams(convertDpToPx(85,(Activity)context),convertDpToPx(85,(Activity)context)));
        if(shape != null){
            if(shape.equals("triangle")){
                this.shape.setImageResource(R.drawable.triangle2);
                this.shape.setTag(R.drawable.triangle2);
            }else if(shape.equals("square")){
                this.shape.setImageResource(R.drawable.square2);
                this.shape.setTag(R.drawable.square2);
            }else if(shape.equals("penta")){
                this.shape.setImageResource(R.drawable.penta2);
                this.shape.setTag(R.drawable.penta2);
            }else {
                System.err.print("wrong number generated");
            }
        }else{
            Random random = new Random();
            int num = random.nextInt(3);
            if(num == 0){
                this.shape.setImageResource(R.drawable.triangle2);
                this.shape.setTag(R.drawable.triangle2);
            }else if(num == 1){
                this.shape.setImageResource(R.drawable.square2);
                this.shape.setTag(R.drawable.square2);
            }else if(num == 2){
                this.shape.setImageResource(R.drawable.penta2);
                this.shape.setTag(R.drawable.penta2);
            }else {
                System.err.print("wrong number generated");
            }
        }
        this.shape.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.shape.setOnClickListener(click);
    }

    public ArrayList<String> getCorrespondingShapes(int tag){
        ArrayList<String> list = new ArrayList<String>();
        if(up != null && ((Integer)up.getTag()).intValue() == tag){
            list.add("up");
        }
        if(right != null && ((Integer)right.getTag()).intValue() == tag){
            list.add("right");
        }
        if(down != null && ((Integer)down.getTag()).intValue() == tag){
            list.add("down");
        }
        if(left != null && ((Integer)left.getTag()).intValue() == tag){
            list.add("left");
        }
        return list;
    }

    public ImageButton getCorrButton(String name){
        if(name.equals("up")){
            return this.up;
        }else if(name.equals("right")){
            return this.right;
        }else if(name.equals("down")){
            return this.down;
        }else{
            return this.left;
        }
    }

    public int setNextTag(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.triangle2){
            img.setTag(R.drawable.square2);
            return R.drawable.square2;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square2){
            img.setTag(R.drawable.penta2);
            return R.drawable.penta2;
        }else{
            img.setTag(R.drawable.triangle2);
            return R.drawable.triangle2;
        }
    }

    public int getNextTag(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.triangle2){
            return R.drawable.square2;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square2){
            return R.drawable.penta2;
        }else{
            return R.drawable.triangle2;
        }
    }

    public int setPreviousTag(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta2){
            img.setTag(R.drawable.square2);
            return R.drawable.square2;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square2){
            img.setTag(R.drawable.triangle2);
            return R.drawable.triangle2;
        }else{
            img.setTag(R.drawable.penta2);
            return R.drawable.penta2;
        }
    }

    public void setCorrImage(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta2){
            img.setImageResource(R.drawable.penta2);
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square2){
            img.setImageResource(R.drawable.square2);
        }else{
            img.setImageResource(R.drawable.triangle2);
        }
    }

    public String imgToString(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta2){
            return "penta";
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square2){
            return "square";
        }else{
            return "triangle";
        }
    }

    public static int convertDpToPx(int Dp, Activity act){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Dp, ((Activity) act).getResources().getDisplayMetrics());
    }

    public ImageButton getImage(){
        return this.shape;
    }
    public void setRight(ImageButton right){
        this.right = right;
    }
    public void setLeft(ImageButton left){
        this.left = left;
    }
    public void setUp(ImageButton up){
        this.up = up;
    }
    public void setDown(ImageButton down){
        this.down = down;
    }
    public ImageButton getUp(){
        return this.up;
    }
    public ImageButton getRight(){
        return this.right;
    }
    public ImageButton getDown(){
        return this.down;
    }
    public ImageButton getLeft(){
        return this.left;
    }
}
