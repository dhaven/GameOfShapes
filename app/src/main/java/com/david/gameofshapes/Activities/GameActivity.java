package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.david.gameofshapes.Animations.Flip3dAnimation;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends Activity {
    private static TableLayout tableLayout;
    private LinearLayout container;
    private static TableRow[] rows;
    private static ShapeImage[][] listImages;
    private static ShapeImage[][] resetImages;
    private static Flip3dAnimation[][] allAnimations;
    public static int puzzleId;
    private static TextView numPuzzle;
    public static Context contextGameActivity;
    //private String level;

    public static TextView viewCounterMoves;
    public static int numMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);

        initializeVariables();

        buildSpecificTable(listImages, rows, PuzzleSelectionActivity.puzzleList.get(puzzleId));
        //buildSolvableTable(listImages,rows,4);

        copyImage(listImages, resetImages);

        onAppearanceAnimations(allAnimations);

        ShapeImage.setNumPenta(numberOfPentagones());
    }

    //Initialize the variables used by the activity
    public void initializeVariables(){
        contextGameActivity = this;
        tableLayout = (TableLayout) findViewById(R.id.grid);
        container = (LinearLayout) findViewById(R.id.container);
        numPuzzle = (TextView) findViewById(R.id.puzzleid);
        viewCounterMoves = (TextView) findViewById(R.id.counter_moves);
        puzzleId = getIntent().getIntExtra("puzzleId",-1);
        numPuzzle.setText(""+(puzzleId+1));
        numMoves = PuzzleSelectionActivity.puzzleList.get(puzzleId).getNum_moves();
        viewCounterMoves.setText("" + numMoves);
        ShapeImage.setCounter(numMoves);
        listImages = new ShapeImage[4][4];
        rows = new TableRow[4];
        resetImages = new ShapeImage[4][4];
        allAnimations = new Flip3dAnimation[4][4];
    }

                                    //********* CREATION OF THE PUZZLE ******

    public static void buildSpecificTable(ShapeImage[][] listImages, TableRow[] rows, Puzzle puzzle){
        String[] labels = puzzle.getConfig().split(",");
        //Toast.makeText(this,labels.toString(), Toast.LENGTH_LONG).show();
        int level = 0;
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(contextGameActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                if(labels[j + level].equals("T")){
                    listImages[i][j] = new ShapeImage(contextGameActivity,"triangle");
                    rows[i].addView(listImages[i][j].getImage());
                }else if(labels[j + level].equals("C")){
                    listImages[i][j] = new ShapeImage(contextGameActivity,"square");
                    rows[i].addView(listImages[i][j].getImage());
                }else{
                    listImages[i][j] = new ShapeImage(contextGameActivity,"penta");
                    rows[i].addView(listImages[i][j].getImage());
                }
            }
            tableLayout.addView(rows[i]);
            level+=4;
        }
        setAdjacency(listImages);
    }

    //to keep for speedrun
    public static void buildSolvableTable(ShapeImage[][] listImages, TableRow[] rows, int numMoves){
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(contextGameActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                listImages[i][j] = new ShapeImage(contextGameActivity,"penta");
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
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


    //old
    public void buildRandomTable(ShapeImage[][] listImages, TableRow[] rows,String level){
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(this);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                listImages[i][j] = new ShapeImage(this,null);
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }
        setAdjacency(listImages);
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
        //Show the failure image with it's animation
        final ImageView successImage = new ImageView(contextGameActivity);
        successImage.setImageResource(R.drawable.success_message);
        successImage.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        tableLayout.addView(successImage);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    System.err.println("Error: " + e.getMessage());
                }
                //Launch the next puzzle
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tableLayout.removeView(successImage);
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

        for(int i = 0; i < listImages.length; i++){
            for(int j = 0; j < listImages[i].length; j++){
                Animation disappearAnimation = AnimationUtils.loadAnimation(contextGameActivity, R.anim.disappearance_animation);
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
                                    buildSolvableTable(listImages, rows, 4);
                                    onAppearanceAnimations(allAnimations);
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


    //Procedure when the player loose
    public static void looseProcedure(){
        //Show the failure image with it's animation
        ImageView failureImage = new ImageView(contextGameActivity);
        failureImage.setImageResource(R.drawable.failure_message);
        failureImage.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        tableLayout.addView(failureImage);
        Animation failure_animation = AnimationUtils.loadAnimation(contextGameActivity, R.anim.loose_animation);
        failureImage.startAnimation(failure_animation);

        final long animationTime = failure_animation.computeDurationHint();
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Wait for the animation to end
                try {
                    Thread.sleep(animationTime + 500);
                }catch(InterruptedException e){
                    System.err.println("Error: " + e.getMessage());
                }
                //Reset the game
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetProc();
                    }
                });

            }
        }).start();

    }

                             //  ***** ADDITIONNAL METHODS ******


    public void reset(View view){
        resetProc();
    }

    //Procedure when reseting the game
    public static void resetProc(){
        copyImage(resetImages,listImages);
        for(int i = 0; i < rows.length; i++){
            rows[i].removeAllViews();
        }
        tableLayout.removeAllViews();
        for(int i = 0; i < listImages.length; i++){
            rows[i] = new TableRow(contextGameActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for(int j = 0; j < listImages[0].length; j++){
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }

        //reset the counter
        ShapeImage.setCounter(numMoves);
        ShapeImage.finish = false;
        ShapeImage.win = false;
        ShapeImage.setNumPenta(numberOfPentagones());
        viewCounterMoves.setText("" + ShapeImage.getCounter());
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
                out[i][j] = new ShapeImage(contextGameActivity,in[i][j].imgToString(in[i][j].getImage()));
            }
        }
        setAdjacency(out);
    }
}
