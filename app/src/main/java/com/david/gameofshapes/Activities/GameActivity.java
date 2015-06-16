package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.david.gameofshapes.Animations.Flip3dAnimation;
import com.david.gameofshapes.R;
import com.david.gameofshapes.ShapeImage;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends Activity {
    private ImageButton square;
    private ImageButton triangle;
    private ImageButton circle;
    private TableLayout tableLayout;
    private LinearLayout container;
    private TableRow[] rows;
    private ShapeImage[][] listImages;
    private ShapeImage[][] resetImages;
    private Flip3dAnimation[][] allAnimations;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);
        tableLayout = (TableLayout) findViewById(R.id.grid);
        container = (LinearLayout) findViewById(R.id.container);
        level = getIntent().getStringExtra("menulayout");
        listImages = new ShapeImage[4][4];
        rows = new TableRow[4];
        //buildRandomTable(listImages,rows,level);
        buildSolvableTable(listImages,rows,level,4);
        resetImages = new ShapeImage[4][4];
        copyImage(listImages,resetImages);
        allAnimations = new Flip3dAnimation[4][4];
        onAppearanceAnimations(allAnimations);
    }

    public void buildRandomTable(ShapeImage[][] listImages, TableRow[] rows,String level){
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(this);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                listImages[i][j] = new ShapeImage(this,level,null);
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }
        setAdjacency(listImages);
    }

    public void onAppearanceAnimations(Flip3dAnimation[][] allAnimations){
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

    public void buildSolvableTable(ShapeImage[][] listImages, TableRow[] rows,String level, int numMoves){
        for(int i = 0; i < listImages.length;i++) {
            rows[i] = new TableRow(this);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j = 0; j < listImages[0].length; j++) {
                listImages[i][j] = new ShapeImage(this,level,"penta");
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }
        setAdjacency(listImages);
        Random random = new Random();
        ShapeImage currImage = null;
        ArrayList<String> adj = new ArrayList<String>();
        for(int i = 0; i < numMoves; i++){
            if(level.equals("medium")){
                while(adj.size() == 0) {
                    int posX = random.nextInt(listImages.length);
                    int posY = random.nextInt(listImages[0].length);
                    currImage = listImages[posX][posY];
                    if(((Integer) currImage.getImage().getTag()).intValue() != R.drawable.triangle2){
                        adj = currImage.getCorrespondingShapes(((Integer) currImage.getImage().getTag()).intValue());
                    }
                }
                currImage.setPreviousTag(currImage.getImage());
                currImage.setCorrImage(currImage.getImage());
                for(String parse : adj){
                    currImage.setPreviousTag(currImage.getCorrButton(parse));
                    currImage.setCorrImage(currImage.getCorrButton(parse));
                }
                adj = new ArrayList<String>();
            }else{ //level.equals("hard")
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
    }


    public void reset(View view){
        copyImage(resetImages,listImages);
        for(int i = 0; i < rows.length; i++){
            rows[i].removeAllViews();
        }
        tableLayout.removeAllViews();
        for(int i = 0; i < listImages.length; i++){
            rows[i] = new TableRow(this);
            rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for(int j = 0; j < listImages[0].length; j++){
                rows[i].addView(listImages[i][j].getImage());
            }
            tableLayout.addView(rows[i]);
        }
    }

    public void copyImage(ShapeImage[][] in, ShapeImage[][] out){
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                out[i][j] = new ShapeImage(this,level,in[i][j].imgToString(in[i][j].getImage()));
            }
        }
        setAdjacency(out);
    }
    public void setAdjacency(ShapeImage[][] listImages){
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

}
