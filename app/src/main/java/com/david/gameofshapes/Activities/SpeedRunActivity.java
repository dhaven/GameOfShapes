package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.david.gameofshapes.Animations.Flip3dAnimation;
import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;
import com.david.gameofshapes.Timer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;


public class SpeedRunActivity extends Activity{
    private static TableLayout tableLayout;
    private static LinearLayout container;
    private static TableRow[] rows;
    private static TableRow[] rowsAsync;
    private static ShapeImage[][] listImages;
    private static ShapeImage[][] listImagesAsync;
    private static ShapeImage[][] resetImages;
    private static ShapeImage[][] resetImagesAsync;
    //private static Flip3dAnimation[][] allAnimations;
    public static int numberPuzzle;
    private static TextView numPuzzle;
    public static Context contextSpeedRunActivity;
    public static boolean isSpeedRun = false;
    public static TextView timerView;
    public static Timer timer;
    public static long timeLimit = 60000; //60 seconds
    public static ImageView countDown;
    public static boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_run);

        initializeVariables();

        buildSolvableTable(listImages,rows,4);

        for(int i=0; i < listImages[0].length;i++){
            tableLayout.addView(rows[i]);
        }

        copyImage(listImages, resetImages);

        //onAppearanceAnimations(allAnimations);

        resetGameVariables();
        ShapeImage.setNumPenta(numberOfPentagones());

        AsyncTask<Void,Void,Void> writeTask = new NewPuzzleTask();
        writeTask.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (first) {
            countDown.startAnimation(startAnim(countDown,1,true));
            first = false;
        }
    }

    //Initialize the variables used by the activity
    public void initializeVariables(){
        contextSpeedRunActivity = this;
        tableLayout = (TableLayout) findViewById(R.id.grid_s);
        container = (LinearLayout) findViewById(R.id.container_s);
        numPuzzle = (TextView) findViewById(R.id.numSolved);
        timerView = (TextView) findViewById(R.id.timer_view);
        numberPuzzle = 1;
        numPuzzle.setText("" + numberPuzzle);
        listImages = new ShapeImage[4][4];
        listImagesAsync = new ShapeImage[4][4];
        rows = new TableRow[4];
        rowsAsync = new TableRow[4];
        resetImages = new ShapeImage[4][4];
        resetImagesAsync = new ShapeImage[4][4];
        //allAnimations = new Flip3dAnimation[4][4];
        isSpeedRun = true;
        first = true;
        countDown = (ImageView) findViewById(R.id.countDown);
        countDown.setVisibility(View.INVISIBLE);
        timer = new Timer(timeLimit, timerView,onTimerExtinct());
    }

    public static void retry(){
        numberPuzzle = 1;
        numPuzzle.setText(""+ numberPuzzle);
        isSpeedRun = true;
        timer = new Timer(timeLimit, timerView,onTimerExtinct());

        buildSolvableTable(listImages, rows, 4);

        copyImage(listImages, resetImages);

        //onAppearanceAnimations(allAnimations);

        resetGameVariables();
        ShapeImage.setNumPenta(numberOfPentagones());
        timer.start();
    }

    public static AnimationSet startAnim(ImageView v, int num,boolean startOffset){
        final int numAnim = num;
        final ImageView view = v;
        ScaleAnimation scaleUp = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,v.getPivotX(),v.getPivotY());
        scaleUp.setDuration(800);
        scaleUp.setFillAfter(true);
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f,0.0f);
        fadeOut.setDuration(400);
        fadeOut.setFillAfter(false);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(400);
        AnimationSet scaleFade = new AnimationSet(contextSpeedRunActivity,null);
        scaleFade.setInterpolator(new OvershootInterpolator(2f));
        scaleFade.addAnimation(scaleUp);
        scaleFade.addAnimation(fadeOut);
        if(startOffset){
            scaleFade.setStartOffset(300);
        }

        scaleFade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //view.setVisibility(View.INVISIBLE);
                if (numAnim == 1) {
                    view.setImageResource(R.drawable.two);
                    view.startAnimation(startAnim(view, numAnim + 1,false));
                } else if (numAnim == 2) {
                    view.setImageResource(R.drawable.one);
                    view.startAnimation(startAnim(view, numAnim + 1,false));
                } else {
                    timer.start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return scaleFade;
    }
    


    //Methode executed when the timer is finished
    public static Timer.TimerListener onTimerExtinct(){
        ShapeImage.finish=true;
        return new Timer.TimerListener() {
            long delay = 0;
            @Override
            public void run() {
                for(int i = 0; i < listImages.length; i++){
                    for(int j = 0; j < listImages[i].length; j++){
                        Animation disappearAnimation = AnimationUtils.loadAnimation(contextSpeedRunActivity, R.anim.disappearance_animation);
                        final int a = i;
                        final int b = j;
                        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listImages[a][b].getImage().setAlpha(0f);
                                        if (a == listImages.length - 1 && b == listImages[a].length - 1) {
                                            for (int i = 0; i < listImages.length; i++) {
                                                for (int j = 0; j < listImages[i].length; j++) {
                                                    rows[i].removeView(listImages[i][j].getImage());
                                                }
                                            }
                                            resetGameVariables();
                                            AlertDialog dlg = scoreDialog();
                                            dlg.show();
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        disappearAnimation.setStartOffset(delay);
                        listImages[i][j].getImage().startAnimation(disappearAnimation);
                        delay += 150;
                    }
                }
            }
        };
    }

    public static AlertDialog scoreDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contextSpeedRunActivity,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = ((Activity)contextSpeedRunActivity).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_score, null);
        builder.setView(v);
        AlertDialog dlg = builder.create();
        final AlertDialog dlg2 = dlg;
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        TextView text = (TextView) v.findViewById(R.id.scoreText);
        text.setText("Puzzles resolved:" + (numberPuzzle-1));

        Button retry = (Button) v.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg2.dismiss();
                retry();
            }
        });

        Button quit = (Button) v.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dlg2.dismiss();
                resetGameVariables();
                ((Activity)contextSpeedRunActivity).finish();
            }
        });

        return dlg;

    }

    //********* CREATION OF THE PUZZLE ******

    //to keep for speedrun
    public static void buildSolvableTable(ShapeImage[][] listImages, TableRow[] rows, int numMoves){
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(contextSpeedRunActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                listImages[i][j] = new ShapeImage(contextSpeedRunActivity,"penta");
                rows[i].addView(listImages[i][j].getImage());
            }
        }
        setAdjacency(listImages);
        Random random = new Random();
        ShapeImage currImage = null;
        ArrayList<String> adj = new ArrayList<String>();
        for(int i = 0; i < numMoves; i++){
            int posX = random.nextInt(listImages.length);
            int posY = random.nextInt(listImages[0].length);
            currImage = listImages[posX][posY];
            currImage.setPreviousTag(currImage.getImage());
            currImage.setCorrImage(currImage.getImage());
            if(currImage.getUp()!=null){
                currImage.setPreviousTag(currImage.getUp());
                currImage.setCorrImage(currImage.getUp());
            }
            if(currImage.getRight() != null){
                currImage.setPreviousTag(currImage.getRight());
                currImage.setCorrImage(currImage.getRight());
            }
            if(currImage.getDown() != null){
                currImage.setPreviousTag(currImage.getDown());
                currImage.setCorrImage(currImage.getDown());
            }
            if(currImage.getLeft() !=  null){
                currImage.setPreviousTag(currImage.getLeft());
                currImage.setCorrImage(currImage.getLeft());
            }

        }
    }

    public static void setAdjacency(ShapeImage[][] listImages){
        for(int i = 0; i < listImages.length;i++) {
            for (int j = 0; j < listImages[0].length; j++) {
                if(i == 0 && j == 0){
                    listImages[i][j].setUp(null);
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(null);
                }else if(i == 0 && j < listImages[0].length-1){
                    listImages[i][j].setUp(null);
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }else if(i == 0 && j == listImages[0].length-1){
                    listImages[i][j].setUp(null);
                    listImages[i][j].setRight(null);
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }else if(i < listImages.length-1 && j == 0){
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(null);
                }else if(i < listImages.length-1 && j < listImages[0].length-1){
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }else if(i < listImages.length-1 && j == listImages[0].length-1){
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(null);
                    listImages[i][j].setDown(listImages[i+1][j].getImage());
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }else if(i == listImages.length-1 && j == 0){
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(null);
                    listImages[i][j].setLeft(null);
                }else if(i == listImages.length-1 && j < listImages[0].length-1){
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(listImages[i][j+1].getImage());
                    listImages[i][j].setDown(null);
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }else{
                    listImages[i][j].setUp(listImages[i-1][j].getImage());
                    listImages[i][j].setRight(null);
                    listImages[i][j].setDown(null);
                    listImages[i][j].setLeft(listImages[i][j-1].getImage());
                }
            }
        }
    }


    public static void onAppearanceAnimations(Flip3dAnimation[][] allAnimations){
        long delay = 300;
        final float centerX = listImages[0][0].getImage().getWidth()/2.0f;
        final float centerY = listImages[0][0].getImage().getHeight()/2.0f;
        for(int i = 0; i < listImages.length; i++){
            for(int j = 0; j < listImages[0].length; j++){
                allAnimations[i][j] = new Flip3dAnimation(-90,0,centerX,centerY);
                allAnimations[i][j].setInterpolator(new AccelerateInterpolator());
                allAnimations[i][j].setFillAfter(true);
                allAnimations[i][j].setDuration(300);
                allAnimations[i][j].setStartOffset(delay);
                listImages[i][j].getImage().startAnimation(allAnimations[i][j]);
                delay += 150;
            }
        }
    }


    // ***** WIN OR LOSE PROCEDURES *******

    //Procedure when the player win
    public static void winProcedure(){

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                }catch(InterruptedException e){
                    System.err.println("Error: " + e.getMessage());
                }
                //Launch the next puzzle
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        numberPuzzle++;
                        switchPuzzle();

                    }
                });

            }
        }).start();
    }


    //Switch a puzzle
    public static void switchPuzzle(){
        long delay = 0;
        int h = tableLayout.getHeight();
        int w = tableLayout.getWidth();
        tableLayout.setMinimumHeight(h);
        tableLayout.setMinimumWidth(w);
        numPuzzle.setText(""+numberPuzzle);

        listImages = listImagesAsync;
        resetImages = resetImagesAsync;
        tableLayout.removeAllViews();
        for(int i=0; i < listImages[0].length; i++){
            tableLayout.addView(rowsAsync[i]);
        }
        /**for(int i = 0; i < listImages.length; i++){
            for(int j = 0; j < listImages[i].length; j++){
                listImages[i][j].getImage().setAlpha(0f);
                 rows[i].removeView(listImages[i][j].getImage());
            }
        }**/
        listImagesAsync = new ShapeImage[4][4];
        resetImagesAsync = new ShapeImage[4][4];
        rowsAsync = new TableRow[4];

        //buildSolvableTable(listImages,rows,4);
        //copyImage(listImages, resetImages);

        AsyncTask<Void,Void,Void> writeTask = new NewPuzzleTask();
        writeTask.execute();

        resetGameVariables();
    }

    //  ***** ADDITIONNAL METHODS ******


    public void reset(View view){
        resetProc();
    }

    //Procedure when resetting the game
    public static void resetProc(){
        copyImage(resetImages,listImages);
        for(int i = 0; i < rows.length; i++){
            rows[i].removeAllViews();
        }
        tableLayout.removeAllViews();
        for(int i = 0; i < listImages.length; i++){
            rows[i] = new TableRow(contextSpeedRunActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for(int j = 0; j < listImages[0].length; j++){
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }

        //reset the counter
        resetGameVariables();
    }

    public static void resetGameVariables(){
        ShapeImage.finish = false;
        ShapeImage.win = false;
        ShapeImage.setNumPenta(numberOfPentagones());
    }


    //Return the actual number of pentagones.
    public static int numberOfPentagones(){
        int n = 0;
        for(int i = 0; i < listImages.length; i++){
            for(int j = 0; j < listImages[i].length; j++){
                if(listImages[i][j].getTag() == R.drawable.penta_wood){
                    n++;
                }
            }
        }
        return n;
    }

    public static void copyImage(ShapeImage[][] in, ShapeImage[][] out){
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                out[i][j] = new ShapeImage(contextSpeedRunActivity,in[i][j].imgToString(in[i][j].getImage()));
            }
        }
        setAdjacency(out);
    }

    public void onBackPressed(){
        resetGameVariables();
        isSpeedRun = false;
        timer.cancel();

        super.onBackPressed();
    }

    private static class NewPuzzleTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            buildSolvableTable(listImagesAsync,rowsAsync,4);
            copyImage(listImagesAsync, resetImagesAsync);
            return null;
        }
    }
}
