package com.david.gameofshapes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;

import com.david.gameofshapes.Activities.GameActivity;
import com.david.gameofshapes.Activities.PuzzleSelectionActivity;
import com.david.gameofshapes.Activities.SpeedRunActivity;
import com.david.gameofshapes.Activities.TutorialActivity;
import com.david.gameofshapes.Animations.DisplayNextView;
import com.david.gameofshapes.Animations.Flip3dAnimation;
import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by david on 31/01/2015.
 */
public class ShapeImage {

    private ImageButton shape;
    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private static int counterMoves;
    private static int numPenta = 0;
    private static Semaphore counterSem = new Semaphore(1, true);
    public static boolean finish = false;
    public static boolean win = false;

    public View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton shape = (ImageButton) v;
            ArrayList<String> correspondingShapes = new ArrayList<String>();
            int tag = ((Integer)shape.getTag()).intValue();
            if(tag == R.drawable.triangle_wood) {
                shape.startAnimation(getRotation(0,-90,R.drawable.triangle_wood,R.drawable.square_wood,shape));
            }else if(tag == R.drawable.square_wood){
                shape.startAnimation(getRotation(0,-90,R.drawable.square_wood,R.drawable.penta_wood,shape));
                numPenta++;
            }else{
                shape.startAnimation(getRotation(0,-90,R.drawable.penta_wood,R.drawable.triangle_wood,shape));
                numPenta--;
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
            if(TutorialActivity.isTutorial2){
                TutorialActivity.clickCountTuto2++;
            }else if(TutorialActivity.isTutorial3){
                TutorialActivity.clickCountTuto3++;
            }else if(TutorialActivity.isTutorial4){

            }
            else{
                //update the counter moves
                if(SpeedRunActivity.isSpeedRun == false) {
                    updateMoves();
                }
                //verify if the player hasn't lost
                hasWin();
                hasLost();;
            }


        }
    };

    //Update the counter for moves
    public static void updateMoves(){
        try {
            counterSem.acquire();

            //decrease the counter
            counterMoves--;
            GameActivity.viewCounterMoves.setText("" + counterMoves);

            counterSem.release();
        }catch(InterruptedException e){
            System.err.println("Error :" +e.getMessage());
        }
    }

    //Return true if the player has lost (counterMoves inferior or equal to 0)
    public static void hasLost(){
        if(finish == false && SpeedRunActivity.isSpeedRun==false) {
            try {
                counterSem.acquire();
                if (counterMoves <= 0) {//condition for player losing
                    finish = true;
                    GameActivity.looseProcedure();

                }
                counterSem.release();

            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static void hasWin(){
        if (finish == false && SpeedRunActivity.isSpeedRun == false){
            if (numPenta == 16){
                finish = true;
                win = true;
                PuzzleSelectionActivity.adapter.getAllPuzzles().get(GameActivity.puzzleId).setSolved(1);
                AsyncTask<Integer,Void,Void> writeTask = new WriteDataTask();
                writeTask.execute(GameActivity.puzzleId + 1);
                GameActivity.winProcedure();
            }
        }
        else if (SpeedRunActivity.isSpeedRun == true && finish == false){
            if (numPenta == 16){
                finish = true;
                win = true;
                SpeedRunActivity.winProcedure();
            }
        }
    }

    private Flip3dAnimation getRotation(float start, float end, int fromImage, int toImage, ImageButton shape) {
        // Find the center of image
        final float centerX = shape.getWidth() / 2.0f;
        final float centerY = shape.getHeight() / 2.0f;
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
        if(SpeedRunActivity.isSpeedRun){
            rotation.setDuration(50);
        }
        else{
            rotation.setDuration(150);
        }
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(fromImage, toImage, shape));
        return rotation;
    }

    public ShapeImage(Context context, String shape){
        this.shape = new ImageButton(context);
        this.shape.setLayoutParams(new TableRow.LayoutParams(convertDpToPx(85,(Activity)context),convertDpToPx(85,(Activity)context)));
        if(shape != null){
            if(shape.equals("triangle")){
                this.shape.setImageResource(R.drawable.triangle_wood);
                this.shape.setTag(R.drawable.triangle_wood);
            }else if(shape.equals("square")){
                this.shape.setImageResource(R.drawable.square_wood);
                this.shape.setTag(R.drawable.square_wood);
            }else if(shape.equals("penta")){
                this.shape.setImageResource(R.drawable.penta_wood);
                this.shape.setTag(R.drawable.penta_wood);
            }else {
                System.err.print("wrong number generated");
            }
        }else{
            Random random = new Random();
            int num = random.nextInt(3);
            if(num == 0){
                this.shape.setImageResource(R.drawable.triangle_wood);
                this.shape.setTag(R.drawable.triangle_wood);
            }else if(num == 1){
                this.shape.setImageResource(R.drawable.square_wood);
                this.shape.setTag(R.drawable.square_wood);
            }else if(num == 2){
                this.shape.setImageResource(R.drawable.penta_wood);
                this.shape.setTag(R.drawable.penta_wood);
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
        if(((Integer)img.getTag()).intValue() == R.drawable.triangle_wood){
            img.setTag(R.drawable.square_wood);
            return R.drawable.square_wood;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square_wood){
            img.setTag(R.drawable.penta_wood);
            return R.drawable.penta_wood;
        }else{
            img.setTag(R.drawable.triangle_wood);
            return R.drawable.triangle_wood;
        }
    }

    public int getNextTag(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.triangle_wood){
            return R.drawable.square_wood;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square_wood){
            numPenta++;
            return R.drawable.penta_wood;
        }else{
            numPenta--;
            return R.drawable.triangle_wood;
        }
    }

    public int setPreviousTag(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta_wood){
            img.setTag(R.drawable.square_wood);
            return R.drawable.square_wood;
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square_wood){
            img.setTag(R.drawable.triangle_wood);
            return R.drawable.triangle_wood;
        }else{
            img.setTag(R.drawable.penta_wood);
            return R.drawable.penta_wood;
        }
    }

    public void setCorrImage(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta_wood){
            img.setImageResource(R.drawable.penta_wood);
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square_wood){
            img.setImageResource(R.drawable.square_wood);
        }else{
            img.setImageResource(R.drawable.triangle_wood);
        }
    }

    public String imgToString(ImageButton img){
        if(((Integer)img.getTag()).intValue() == R.drawable.penta_wood){
            return "penta";
        }else if(((Integer)img.getTag()).intValue() == R.drawable.square_wood){
            return "square";
        }else{
            return "triangle";
        }
    }

    public static int convertDpToPx(int Dp, Activity act){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Dp, ((Activity) act).getResources().getDisplayMetrics());
    }

    private static class WriteDataTask extends AsyncTask<Integer,Void,Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            ShapesDbHelper myDbHelper = new ShapesDbHelper(GameActivity.contextGameActivity);
            SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbContract.PuzzlesTable.COLUMN_NAME_SOLVED,1);
            int count = myDb.update(DbContract.PuzzlesTable.TABNAME,values,DbContract.PuzzlesTable.COLUMN_NAME_PUZZLEID + " = '" + params[0] + "'",null);

            return null;
        }

        protected void onPostExecute(ArrayList<Puzzle> result) {
        }
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
    public static int getCounter(){return counterMoves;}
    public static void setCounter(int n){counterMoves = n;}
    public static void setNumPenta(int n){numPenta = n;}
    public  int getTag(){return ((Integer)shape.getTag()).intValue();}
}
