package com.david.gameofshapes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by david on 15/06/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Puzzle> allPuzzles;

    public ImageAdapter(Context c,ArrayList<Puzzle> allPuzzles){
        mContext = c;
        this.allPuzzles = allPuzzles;

    }
    @Override
    public int getCount() {
        return allPuzzles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout individual;
        if (convertView == null) {
            Puzzle current = allPuzzles.get(position);
            individual = new RelativeLayout(mContext);
            individual.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ShapeImage.convertDpToPx(85,(Activity)parent.getContext())));
            ImageView level = new ImageView(mContext);
            level.setScaleType(ImageView.ScaleType.FIT_XY);
            if(current.getDifficulty().equals("easy")){
                if(current.getSolved() == 1){
                    level.setImageResource(R.drawable.easy_checked);
                }else{
                    level.setImageResource(R.drawable.easy_unchecked);
                }
            }else if(current.getDifficulty().equals("medium")){
                if(current.getSolved() == 1){
                    level.setImageResource(R.drawable.medium_checked);
                }else{
                    level.setImageResource(R.drawable.medium_unchecked);
                }
            }else{
                if(current.getSolved() == 1){
                    level.setImageResource(R.drawable.hard_checked);
                }else{
                    level.setImageResource(R.drawable.hard_unchecked);
                }
            }
            level.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView num = new TextView(mContext);
            num.setText("" + current.getPuzzle_id());
            num.setTextColor(Color.BLACK);
            num.setTextSize(40);
            RelativeLayout.LayoutParams numLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            numLayout.leftMargin = ShapeImage.convertDpToPx(15,(Activity)parent.getContext());
            numLayout.topMargin = ShapeImage.convertDpToPx(25,(Activity)parent.getContext());
            num.setLayoutParams(numLayout);
            individual.addView(level);
            individual.addView(num);
        } else {
            individual = (RelativeLayout) convertView;
        }
        return individual;
    }

    public ArrayList<Puzzle> getAllPuzzles() {
        return allPuzzles;
    }

    public void setAllPuzzles(ArrayList<Puzzle> allPuzzles) {
        this.allPuzzles = allPuzzles;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
