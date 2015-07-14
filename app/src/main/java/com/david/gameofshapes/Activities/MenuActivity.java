package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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


public class MenuActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menulayout);
        if(PuzzleSelectionActivity.puzzleList == null){
            new LoadDataTask().execute(null,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_difficulty, menu);
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

    public void goToPuzzles(View view){
        Intent intent = new Intent(this,PuzzleSelectionActivity.class);
        startActivity(intent);
    }
    public void goToStats(View view){
        /*
        Todo
        send players to an ativity that presents various stats
         */
    }
    public void goToSpeedrun(View view){
        Intent intent = new Intent(this, SpeedRunActivity.class);
        startActivity(intent);
    }

    public void resetHighscores(View view){
        ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
        SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
        myDbHelper.onUpgrade(myDb, 1, 2);
    }

    public void goToTuto(View view){
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
    }

    private class LoadDataTask extends AsyncTask<String,Void,ArrayList<Puzzle>> {


        @Override
        protected ArrayList<Puzzle> doInBackground(String... params) {
            ShapesDbHelper myDbHelper = new ShapesDbHelper(getApplicationContext());
            SQLiteDatabase myDb = myDbHelper.getReadableDatabase();
            //myDbHelper.onUpgrade(myDb,1,2); //uncomment to reset database
            return DbContract.PuzzlesTable.getAllPuzzles(myDb);
        }
        protected void onPostExecute(ArrayList<Puzzle> result) {
            PuzzleSelectionActivity.puzzleList = result;
        }
    }

    public void onClick(View view){ //old
        Button thisButton = (Button) view;
        Intent intent = new Intent(this,GameActivity.class);
        String difficulty = thisButton.getText().toString();
        //Toast.makeText(this,menulayout, Toast.LENGTH_SHORT).show();
        //intent.putExtra("menulayout",menulayout);
        startActivity(intent);
    }
}
