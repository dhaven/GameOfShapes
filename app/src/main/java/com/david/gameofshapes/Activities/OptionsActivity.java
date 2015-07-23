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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;
import com.david.gameofshapes.Highscore;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


public class OptionsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

        showScores();

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

    public void showScores(){
        //Get the top 10 scores
        ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
        SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
        ArrayList<Highscore> highscores =  DbContract.HighscoresTable.getTopN(myDb, 10);

        //Show dialog with the scores
        //scoresDialog(highscores).show();
        scoresList(highscores);

    }

    public void scoresList(final ArrayList<Highscore> highscores){
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>(){{
            add(new HashMap<String, String>(){{
                put("ROOT_NAME", "Highscores");
            }});
        }};

        List<List<Map<String, String>>> listOfChildGroups = new ArrayList<List<Map<String, String>>>();

        List<Map<String, String>> childGroupForFirstGroupRow = new ArrayList<Map<String, String>>(){{
            for(int i = 0; i < highscores.size(); i++){
                Highscore score = highscores.get(i);
                final String string = "" + (i+1) + ". " + score.getName() + " - " + score.getScore();
                add(new HashMap<String, String>() {{
                    put("CHILD_NAME", string);
                }});
            }
        }};
        listOfChildGroups.add(childGroupForFirstGroupRow);

        SimpleExpandableListAdapter listAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] {"ROOT_NAME"},
                new int[] { android.R.id.text1 }, listOfChildGroups,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "CHILD_NAME"},
                new int[] { android.R.id.text1});

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.highscores_list);
        listView.setAdapter(listAdapter);
    }


    public void showTutorial(View view){
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    public void chooseMusic(View view){
        //Get list of music (to change)
        String[] listMusic = {"Music 1", "Music 2", "Music 3", "Music 4"};

        //Show dialog
        musicDialog(listMusic).show();
    }

    public AlertDialog musicDialog( String[] listMusic){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_options_music, null);
        builder.setView(view);
        builder.setTitle("Musics");

        //Add a textview for each music
        LinearLayout linearLayout = (LinearLayout) view;
        for(int i = 0; i < listMusic.length; i++){
            TextView textView = new TextView(view.getContext());
            textView.setText(listMusic[i]);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO music = listMusic[1]
                }
            });

           linearLayout.addView(textView);
        }

        return builder.create();
    }

    public void showPuzzlesSolved(View view){
        //Get all puzzles
        ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
        SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
        ArrayList<Puzzle> allPuzzles =  DbContract.PuzzlesTable.getAllPuzzles(myDb);

        //Get the number of puzzles solved by difficulty

        int easyPuzzles = 0;    //total number of easy puzzles
        int easyPuzzlesSolved = 0;  //number of easy puzzles solved
        int mediumPuzzles = 0;
        int mediumPuzzlesSolved = 0;
        int hardPuzzles = 0;
        int hardPuzzlesSolved = 0;

        for(int i = 0; i < allPuzzles.size(); i++){
            Puzzle puzzle = allPuzzles.get(i);

            if(puzzle.getDifficulty() == "easy"){
                easyPuzzles++;
                if (puzzle.getSolved() == 1){
                    easyPuzzlesSolved++;
                }
            }
            else if(puzzle.getDifficulty() == "medium"){
                mediumPuzzles++;
                if (puzzle.getSolved() == 1){
                    mediumPuzzlesSolved++;
                }
            }
            else{
                hardPuzzles++;
                if(puzzle.getSolved() == 1){
                    hardPuzzlesSolved++;
                }
            }
        }

        //Show dialog
        puzzlesSolvedDialog(easyPuzzles, easyPuzzlesSolved, mediumPuzzles, mediumPuzzlesSolved, hardPuzzles, hardPuzzlesSolved).show();
    }

    public AlertDialog puzzlesSolvedDialog(int e, int eS, int m, int mS, int h, int hS){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_options_puzzles_solved, null);
        builder.setView(view);
        builder.setTitle("Puzzles Solved");

        TextView textView1 = (TextView) view.findViewById(R.id.easyPuzzles);
        textView1.setText( eS + " / " + e);

        TextView textView2 = (TextView) view.findViewById(R.id.easyPuzzlesSolved);
        textView2.setText("Easy puzzles solved : ");

        TextView textView3 = (TextView) view.findViewById(R.id.mediumPuzzles);
        textView3.setText( mS + " / " + m);

        TextView textView4 = (TextView) view.findViewById(R.id.mediumPuzzlesSolved);
        textView4.setText("Medium puzzles solved : ");

        TextView textView5 = (TextView) view.findViewById(R.id.hardPuzzles);
        textView5.setText(hS + " / " + h);

        TextView textView6 = (TextView) view.findViewById(R.id.hardPuzzlesSolved);
        textView6.setText("Hard puzzles solved : ");

        return builder.create();
    }

}
