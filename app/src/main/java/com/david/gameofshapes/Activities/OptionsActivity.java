package com.david.gameofshapes.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;
import com.david.gameofshapes.Highscore;
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
        //Get the top 10 scores
        ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
        SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
        ArrayList<Highscore> highscores =  DbContract.HighscoresTable.getTopN(myDb, 10);

        //Show dialog with the scores
        scoresDialog(highscores).show();

    }

    public AlertDialog scoresDialog(ArrayList<Highscore> highscores){
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle("Top scores");

        LayoutInflater inflater = this.getLayoutInflater();

        View view =  inflater.inflate(R.layout.dialog_options_scores, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        addScores(view, highscores);


        return dialog;
    }

    public void addScores(View view, ArrayList<Highscore> highscores){
        TableLayout scoresTable = (TableLayout) view.findViewById(R.id.table_scores);



        for(int i = 0; i < highscores.size(); i++){
            Highscore score = highscores.get(i);
            TextView scoreView = (TextView) scoresTable.getChildAt(i);
            scoreView.setText( (i+1) + ".  " + score.getName() + " - " + score.getScore());
        }
    }

    public void showTutorial(View view){
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    public void chooseMusic(View view){

    }

}
