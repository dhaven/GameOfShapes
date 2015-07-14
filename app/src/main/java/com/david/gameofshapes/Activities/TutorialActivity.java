package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ViewAnimator;

import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;

public class TutorialActivity extends Activity {

    private static ViewAnimator tutoView;
    public static boolean isTutorial;
    public static int clickCountTuto2;
    public static int clickCountTuto3;
    public static Context contextTutoActivity;
    private static TableRow[] rows = new TableRow[3];
    private static ShapeImage[][] listImages = new ShapeImage[3][3];
    private static ShapeImage[][] resetImages = new ShapeImage[3][3];
    private static TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initializeVariables();
        Button next = (Button) findViewById(R.id.next_tuto);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutoView.showNext();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tutoView.addView(buildTuto2(), 1, params);
        tutoView.addView(buildTuto3(), 2, params);
        tutoView.addView(buildTuto4(),3,params);

    }

    public void initializeVariables(){
        rows = new TableRow[3];
        listImages = new ShapeImage[3][3];
        resetImages = new ShapeImage[3][3];
        isTutorial = true;
        clickCountTuto2 = 0;
        clickCountTuto3 = 0;
        contextTutoActivity = this;
        tutoView = (ViewAnimator) findViewById(R.id.viewanimator);
        tutoView.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        tutoView.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
    }

    public static View buildTuto2(){
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View tuto2 = inflater.inflate(R.layout.tuto_2, null);
        LinearLayout container = (LinearLayout) tuto2.findViewById(R.id.tuto2);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        ImageButton img = new ShapeImage(contextTutoActivity, "triangle").getImage();
        container.addView(img);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while( clickCountTuto2 < 3){
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
                        clickCountTuto2 = 0;
                        tutoView.showNext();
                    }
                });
            }
        }).start();
        return tuto2;
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

    public static View buildTuto4(){
        LayoutInflater inflater = ((Activity)contextTutoActivity).getLayoutInflater();
        View tuto4 = inflater.inflate(R.layout.tuto_4, null);
        table = (TableLayout) tuto4.findViewById(R.id.grid_tuto2);
        Puzzle puzzle = new Puzzle("C,T,P,T,C,T,P,T,C");
        GameActivity.buildSpecificTable(contextTutoActivity, listImages, rows, puzzle, table);
        GameActivity.copyImage(contextTutoActivity,listImages,resetImages);
        return tuto4;
    }
    public void reset(View view){
            GameActivity.copyImage(contextTutoActivity,resetImages,listImages);
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
}
