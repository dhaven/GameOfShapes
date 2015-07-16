package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;

import java.util.ArrayList;


public class OptionsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

    }

    public void resetGame(View view){
        resetHighScores();
    }

    public void resetHighScores(){
        ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
        SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
        myDbHelper.onUpgrade(myDb,1,2);
        PuzzleSelectionActivity.puzzleList = DbContract.PuzzlesTable.getAllPuzzles(myDb);

        //reset launch tutorial

        SharedPreferences sharedPref = getSharedPreferences("TutoCompleted", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public void showScores(View view){

    }

    public void showTutorial(View view){
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    public void chooseMusic(View view){

    }

}
