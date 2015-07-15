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
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.EditText;
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
import com.david.gameofshapes.Highscore;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;
import com.david.gameofshapes.Timer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.david.gameofshapes.ShapeImage.convertDpToPx;


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
    public static int nameId;
    public static View v;

    public static ArrayList<Highscore> highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_run);

        initializeVariables();

        buildSolvableTable(listImages,rows,4);

        for(int i=0; i < listImages[0].length;i++){
            tableLayout.addView(rows[i]);
        }
        for(int i=0; i<listImages.length;i++){
            for(int j=0; j<listImages[0].length;j++){
                listImages[i][j].getImage().setOnClickListener(null);
            }
        }

        copyImage(listImages, resetImages);

        //onAppearanceAnimations(allAnimations);

        resetGameVariables();
        ShapeImage.setNumPenta(numberOfPentagones());

        AsyncTask<Void,Void,Void> writeTask = new NewPuzzleTask();
        writeTask.execute();

        new LoadHighscores().execute(10);
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
        numberPuzzle = 0;
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
        nameId = 1;
    }

    public static void retry(){
        numberPuzzle = 0;
        numPuzzle.setText("" + numberPuzzle);
        isSpeedRun = true;
        timer = new Timer(timeLimit, timerView,onTimerExtinct());

        //buildSolvableTable(listImages, rows, 4);

        //copyImage(listImages, resetImages);

        //onAppearanceAnimations(allAnimations);

        resetGameVariables();
        ShapeImage.setNumPenta(numberOfPentagones());
        switchPuzzle();
        for(int i=0; i<listImages.length;i++){
            for(int j=0; j<listImages[0].length;j++){
                listImages[i][j].getImage().setOnClickListener(null);
            }
        }
        first = true;
        if (first) {
            countDown.setImageResource(R.drawable.three);
            countDown.startAnimation(startAnim(countDown,1,true));
            first = false;
        }
        //timer.start();
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
                    for(int i=0; i<listImages.length;i++){
                        for(int j=0; j<listImages[0].length;j++){
                            listImages[i][j].getImage().setOnClickListener(listImages[i][j].click);
                        }
                    }
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
                for(int i=0; i<listImages.length;i++){
                    for(int j=0; j<listImages[0].length;j++){
                        listImages[i][j].getImage().setOnClickListener(null);
                    }
                }
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
        v = inflater.inflate(R.layout.dialog_score, null);
        builder.setView(v);

        AlertDialog dlg = builder.create();
        final AlertDialog dlg2 = dlg;
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;

        int ret = Collections.binarySearch(highscores, new Highscore("", numberPuzzle));
        int index = Math.abs(ret + 1);
        final int index2 = index;
        LinearLayout topScores = (LinearLayout) v.findViewById(R.id.topScores);
        topScores.setGravity(Gravity.CENTER_HORIZONTAL);
        final LinearLayout score2 = topScores;
        topScores.removeAllViews();
        TextView title = new TextView(contextSpeedRunActivity);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.topMargin = convertDpToPx(10, (Activity) contextSpeedRunActivity);
        layout.bottomMargin = convertDpToPx(10, (Activity) contextSpeedRunActivity);
        title.setLayoutParams(layout);
        title.setText("Top scores");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        topScores.addView(title);
        updateScoreBoard(topScores, index);
        Button retry = (Button) v.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newName = (EditText) score2.findViewById(nameId);
                if(newName == null){
                    dlg2.dismiss();
                    retry();
                }else{
                    String name = newName.getText().toString();
                    if(name == null || name.equals("")){
                        new WriteDataTask().execute("no name", "" + numberPuzzle);
                        highscores.add(index2, new Highscore("no name", numberPuzzle));
                    }else{
                        new WriteDataTask().execute(name, "" + numberPuzzle);
                        highscores.add(index2,new Highscore(name, numberPuzzle));
                    }
                    dlg2.dismiss();
                    retry();
                }
            }
        });

        Button quit = (Button) v.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText newName = (EditText) score2.findViewById(nameId);
                if(newName == null){
                    dlg2.dismiss();
                    resetGameVariables();
                    isSpeedRun = false;
                    ((Activity)contextSpeedRunActivity).finish();
                }else{
                    String name = newName.getText().toString();
                    if(name == null || name.equals("")){
                        new WriteDataTask().execute("no name",""+numberPuzzle);
                        highscores.add(index2,new Highscore("no name", numberPuzzle));
                    }else{
                        new WriteDataTask().execute(name,""+numberPuzzle);
                        highscores.add(index2,new Highscore(name, numberPuzzle));
                    }
                    dlg2.dismiss();
                    resetGameVariables();
                    isSpeedRun = false;
                    ((Activity)contextSpeedRunActivity).finish();
                }
            }
        });

        return dlg;

    }

    public static void updateScoreBoard(LinearLayout topScores, int index){
        int size = highscores.size();
        if(index == size){
            LinearLayout top1 = createScoreLayout(1);
            topScores.addView(top1);
            if(size >= 2){
                Highscore high2 = highscores.get(size-1);
                Highscore high3 = highscores.get(size-2);
                TextView top2 = new TextView(contextSpeedRunActivity);
                top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top2.setText("2. " + high2.getName() + " - " + high2.getScore() + " puzzles");
                top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top2.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top2);
                TextView top3 = new TextView(contextSpeedRunActivity);
                top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top3.setText("3. " + high3.getName() + " - " + high3.getScore() + " puzzles");
                top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top3.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top3);
            }else if(size >= 1){
                Highscore high2 = highscores.get(size-1);
                TextView top2 = new TextView(contextSpeedRunActivity);
                top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top2.setText("2. " + high2.getName() + " - " + high2.getScore() + " puzzles");
                top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top2.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top2);
                TextView top3 = new TextView(contextSpeedRunActivity);
                top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top3.setText("3. No score ");
                top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top3.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top3);
            }else{
                TextView top2 = new TextView(contextSpeedRunActivity);
                top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top2.setText("2. No score ");
                top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top2.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top2);
                TextView top3 = new TextView(contextSpeedRunActivity);
                top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top3.setText("3. No score ");
                top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top3.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top3);
            }

        }else if(index == size-1){
            Highscore high1 = highscores.get(size-1);
            TextView top1 = new TextView(contextSpeedRunActivity);
            top1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top1.setText("1. " + high1.getName() + " - " + high1.getScore() + " puzzles");
            top1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top1.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top1);
            LinearLayout top2 = createScoreLayout(2);
            topScores.addView(top2);
            TextView top3 = new TextView(contextSpeedRunActivity);
            top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(size >= 2){
                Highscore high3 = highscores.get(size-2);
                top3.setText("3. " + high3.getName() + " - " + high3.getScore() + " puzzles");
            }else{
                top3.setText("3. No score");
            }
            top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top3.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top3);

        }else if(index == size-2){
            Highscore high1 = highscores.get(size-1);
            Highscore high2 = highscores.get(size-2);
            TextView top1 = new TextView(contextSpeedRunActivity);
            top1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top1.setText("1. " + high1.getName() + " - " + high1.getScore() + " puzzles");
            top1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top1.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top1);
            TextView top2 = new TextView(contextSpeedRunActivity);
            top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top2.setText("2. " + high2.getName() + " - " + high2.getScore() + " puzzles");
            top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top2.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top2);
            LinearLayout top3 = createScoreLayout(3);
            topScores.addView(top3);
        }else if(size-index+1 > 10) {
            Highscore high1 = highscores.get(size-1);
            Highscore high2 = highscores.get(size-2);
            Highscore high3 = highscores.get(size-3);
            TextView top1 = new TextView(contextSpeedRunActivity);
            top1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top1.setText("1. " + high1.getName() + " - " + high1.getScore() + " puzzles");
            top1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top1.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top1);
            TextView top2 = new TextView(contextSpeedRunActivity);
            top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top2.setText("2. " + high2.getName() + " - " + high2.getScore() + " puzzles");
            top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top2.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top2);
            TextView top3 = new TextView(contextSpeedRunActivity);
            top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            top3.setText("3. " + high3.getName() + " - " + high3.getScore() + " puzzles");
            top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            top3.setGravity(Gravity.CENTER_HORIZONTAL);
            topScores.addView(top3);
            ImageView img = (ImageView) v.findViewById(R.id.scoreImg);
            img.setImageResource(R.drawable.not_fast_enough);
        }else{
                Highscore high1 = highscores.get(size-1);
                Highscore high2 = highscores.get(size-2);
                Highscore high3 = highscores.get(size-3);
                TextView top1 = new TextView(contextSpeedRunActivity);
                top1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top1.setText("1. " + high1.getName() + " - " + high1.getScore() + " puzzles");
                top1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top1.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top1);
                TextView top2 = new TextView(contextSpeedRunActivity);
                top2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top2.setText("2. " + high2.getName() + " - " + high2.getScore() + " puzzles");
                top2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top2.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top2);
                TextView top3 = new TextView(contextSpeedRunActivity);
                top3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                top3.setText("3. " + high3.getName() + " - " + high3.getScore() + " puzzles");
                top3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                top3.setGravity(Gravity.CENTER_HORIZONTAL);
                topScores.addView(top3);
                LinearLayout top4 = createScoreLayout(size-index+1);
                topScores.addView(top4);
        }
    }

    public static LinearLayout createScoreLayout(int rank){
        LinearLayout top = new LinearLayout(contextSpeedRunActivity);
        top.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView txt = new TextView(contextSpeedRunActivity);
        txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txt.setText(rank + ". ");
        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txt.setGravity(Gravity.CENTER_HORIZONTAL);
        top.addView(txt);
        EditText name = new EditText(contextSpeedRunActivity);
        name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        name.setHint("Name");
        name.setId(nameId);
        name.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        name.setMaxLines(1);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        InputFilter[] fltr = new InputFilter[1];
        fltr[0] = new InputFilter.LengthFilter(10);
        name.setFilters(fltr);
        top.addView(name);
        TextView txt2 = new TextView(contextSpeedRunActivity);
        txt2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txt2.setText(" - " + numberPuzzle + " puzzles");
        txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txt2.setGravity(Gravity.CENTER_HORIZONTAL);
        top.addView(txt2);
        return top;
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

    private static class LoadHighscores extends AsyncTask<Integer,Void,ArrayList<Highscore>> {

        @Override
        protected ArrayList<Highscore> doInBackground(Integer... params) {
            ShapesDbHelper myDbHelper = new ShapesDbHelper(contextSpeedRunActivity);
            SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
            return DbContract.HighscoresTable.getTopN(myDb,params[0].intValue());
        }

        @Override
        protected void onPostExecute(ArrayList<Highscore> result) {
            highscores = result;
        }
    }

    private static class WriteDataTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {

            ShapesDbHelper myDbHelper = new ShapesDbHelper(contextSpeedRunActivity);
            SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbContract.HighscoresTable.COLUMN_NAME_NAME,params[0]);
            values.put(DbContract.HighscoresTable.COLUMN_NAME_SCORE,Integer.parseInt(params[1]));
            long count = myDb.insert(DbContract.HighscoresTable.TABNAME, null, values);
            return null;
        }
    }
}
