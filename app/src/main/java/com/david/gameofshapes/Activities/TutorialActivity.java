package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;
import com.david.gameofshapes.SimpleGestureFilter;

public class TutorialActivity extends Activity implements SimpleGestureFilter.SimpleGestureListener {

    private static ViewAnimator tutoView;
    public static boolean isTutorial2;
    public static int clickCountTuto2;
    public static boolean isTutorial3;
    public static int clickCountTuto3;
    public static boolean isTutorial4;
    public static Context contextTutoActivity;
    private static TableRow[] rows = new TableRow[3];
    private static ShapeImage[][] listImages = new ShapeImage[3][3];
    private static ShapeImage[][] resetImages = new ShapeImage[3][3];
    private static TableLayout table;
    private static AlertDialog intro_dialog;
    private static AlertDialog outro_dialog;
    private static boolean isRunning;
    private static SimpleGestureFilter detector;
    private static Thread t2;
    private static Thread t3;
    private static Thread t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initializeVariables();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        intro_dialog = startTutoDialog();
        intro_dialog.show();
        tutoView.addView(buildTuto2(), 1, params);
        tutoView.addView(buildTuto3(), 2, params);
        tutoView.addView(buildTuto4(), 3, params);
        startThreadControl();
    }

    public void initializeVariables(){
        rows = new TableRow[3];
        listImages = new ShapeImage[3][3];
        resetImages = new ShapeImage[3][3];
        clickCountTuto2 = 0;
        clickCountTuto3 = 0;
        contextTutoActivity = this;
        isRunning = true;
        tutoView = (ViewAnimator) findViewById(R.id.viewanimator);
        tutoView.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        tutoView.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
        detector = new SimpleGestureFilter(this,this);
    }

    public static void startThreadControl(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while(isRunning){
                    try {
                        Thread.sleep(100);
                        int index = tutoView.getDisplayedChild();
                        if(index == 1 && ((t2 != null && !t2.isAlive()) || (t2 == null)) ){
                            t2 = newThreadTuto2(handler);
                            t2.start();

                        }else if(index == 2 && ((t3 != null && !t3.isAlive()) || (t3 == null)) ){
                            t3 = newThreadTuto3(handler);
                            t3.start();
                        }else if(index == 3 && ((t4 != null && !t4.isAlive()) || (t4 == null))){
                            t4 = newThreadTuto4(handler);
                            t4.start();
                        }
                    } catch (InterruptedException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    public static AlertDialog startTutoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contextTutoActivity,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_intro_tuto, null);
        builder.setView(v);
        AlertDialog dlg = builder.create();
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        ((AlertDialog) dialog).getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
                        dialog.dismiss();
                        ((Activity) contextTutoActivity).onBackPressed();
                        return true;
                    default:
                        return false;
                }
            }
        });
        final AlertDialog dlg2 = dlg;
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_slide;
        Button startTuto = (Button) v.findViewById(R.id.startTuto);
        startTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg2.dismiss();
                tutoView.showNext();
                isTutorial2 = true;
            }
        });
        return dlg;
    }

    public static AlertDialog endTutoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contextTutoActivity,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_outro_tuto, null);
        builder.setView(v);
        AlertDialog dlg = builder.create();
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        dialog.dismiss();
                        ((Activity) contextTutoActivity).onBackPressed();
                        return true;
                    default:
                        return false;
                }
            }
        });
        final AlertDialog dlg2 = dlg;
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_slide;
        Button endTuto = (Button) v.findViewById(R.id.endTuto);
        endTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg2.dismiss();
                isTutorial4 = false;
                isRunning = false;
                SharedPreferences sharedPref = contextTutoActivity.getSharedPreferences("TutoCompleted", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("isCompleted","true");
                editor.commit();
                Intent intent = new Intent(contextTutoActivity, MenuActivity.class);
                contextTutoActivity.startActivity(intent);
                ((Activity) contextTutoActivity).finish();
            }
        });
        return dlg;
    }

    public static View buildTuto2(){
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View tuto2 = inflater.inflate(R.layout.tuto_2, null);
        LinearLayout container = (LinearLayout) tuto2.findViewById(R.id.tuto2);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        ImageButton img = new ShapeImage(contextTutoActivity, "triangle").getImage();
        container.addView(img);
        return tuto2;
    }

    public static Thread newThreadTuto2(final Handler handler){
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while( clickCountTuto2 < 3 && isRunning){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isRunning){
                            clickCountTuto2 = 0;
                            isTutorial2 = false;
                            isTutorial3 = true;
                            tutoView.setInAnimation(AnimationUtils.loadAnimation(contextTutoActivity, R.anim.slide_in_right));
                            tutoView.setOutAnimation(AnimationUtils.loadAnimation(contextTutoActivity, R.anim.slide_left_out));
                            tutoView.showNext();
                        }
                    }
                });
            }
        });
        return t2;
    }

    public static View buildTuto3(){
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View tuto3 = inflater.inflate(R.layout.tuto_3, null);
        GridLayout grid = (GridLayout) tuto3.findViewById(R.id.grid_tuto);
        grid.setRowCount(3);
        grid.setColumnCount(3);
        ShapeImage top = new ShapeImage(contextTutoActivity,"triangle");
        GridLayout.LayoutParams topLt = new GridLayout.LayoutParams(GridLayout.spec(0,1),GridLayout.spec(1,1));
        topLt.width = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        topLt.height = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        grid.addView(top.getImage(),topLt);
        ShapeImage right = new ShapeImage(contextTutoActivity,"triangle");
        GridLayout.LayoutParams rightLt = new GridLayout.LayoutParams(GridLayout.spec(1,1),GridLayout.spec(2,1));
        rightLt.width = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        rightLt.height = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        grid.addView(right.getImage(),rightLt);
        ShapeImage bottom = new ShapeImage(contextTutoActivity,"triangle");
        GridLayout.LayoutParams bottomLt = new GridLayout.LayoutParams(GridLayout.spec(2,1),GridLayout.spec(1,1));
        bottomLt.width = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        bottomLt.height = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        grid.addView(bottom.getImage(),bottomLt);
        ShapeImage left = new ShapeImage(contextTutoActivity,"triangle");
        GridLayout.LayoutParams leftLt = new GridLayout.LayoutParams(GridLayout.spec(1,1),GridLayout.spec(0,1));
        leftLt.width = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        leftLt.height = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        grid.addView(left.getImage(),leftLt);
        ShapeImage center = new ShapeImage(contextTutoActivity,"triangle");
        GridLayout.LayoutParams centerLt = new GridLayout.LayoutParams(GridLayout.spec(1,1),GridLayout.spec(1,1));
        centerLt.width = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        centerLt.height = ShapeImage.convertDpToPx(85, (Activity) contextTutoActivity);
        grid.addView(center.getImage(), centerLt);
        top.setDown(center.getImage());
        left.setRight(center.getImage());
        right.setLeft(center.getImage());
        bottom.setUp(center.getImage());
        center.setUp(top.getImage());
        center.setLeft(left.getImage());
        center.setRight(right.getImage());
        center.setDown(bottom.getImage());
        return tuto3;
    }

    public static Thread newThreadTuto3(final Handler handler){
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while( clickCountTuto3 < 4 && isRunning){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isRunning){
                            clickCountTuto3 = 0;
                            isTutorial3 = false;
                            isTutorial4 = true;
                            tutoView.setInAnimation(AnimationUtils.loadAnimation(contextTutoActivity, R.anim.slide_in_right));
                            tutoView.setOutAnimation(AnimationUtils.loadAnimation(contextTutoActivity, R.anim.slide_left_out));
                            tutoView.showNext();
                        }
                    }
                });
            }
        });
        return t3;
    }

    public static View buildTuto4(){
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View tuto4 = inflater.inflate(R.layout.tuto_4, null);
        table = (TableLayout) tuto4.findViewById(R.id.grid_tuto2);
        Puzzle puzzle = new Puzzle("C,T,P,T,C,T,P,T,C");
        GameActivity.buildSpecificTable(contextTutoActivity, listImages, rows, puzzle, table);
        GameActivity.copyImage(contextTutoActivity, listImages, resetImages);
        return tuto4;
    }
    public void reset(View view){
            GameActivity.copyImage(contextTutoActivity, resetImages, listImages);
        for(int i = 0; i < rows.length; i++){
            rows[i].removeAllViews();
        }
        table.removeAllViews();
        for(int i = 0; i < listImages.length; i++){
            rows[i] = new TableRow(contextTutoActivity);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for(int j = 0; j < listImages[0].length; j++){
                rows[i].addView(listImages[i][j].getImage());
            }
            table.addView(rows[i]);
        }
    }

    public static Thread newThreadTuto4(final Handler handler){
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(numberOfPentagones() != 9 && isRunning){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isRunning){
                            tutoView.showNext();
                            outro_dialog = endTutoDialog();
                            outro_dialog.show();
                        }
                    }
                });
            }
        });
        return t4;
    }

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

    @Override
    public void onBackPressed(){
        ((Activity)contextTutoActivity).finish();
        isRunning = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :
                tutoView.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
                tutoView.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_out));
                if(tutoView.getDisplayedChild() > 1){
                    if(tutoView.getDisplayedChild() == 3){
                        isTutorial4 = false;
                        isTutorial3 = true;
                        tutoView.showPrevious();
                    }else{
                        isTutorial3 = false;
                        isTutorial2 = true;
                        tutoView.showPrevious();
                    }
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT :
                tutoView.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
                tutoView.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
                if(tutoView.getDisplayedChild() < 3){
                    if(tutoView.getDisplayedChild() == 1){
                        isTutorial2 = false;
                        isTutorial3 = true;
                        tutoView.showNext();
                    }else{
                        isTutorial3 = false;
                        isTutorial4 = true;
                        tutoView.showNext();
                    }
                }
                break;
        }
    }
}
