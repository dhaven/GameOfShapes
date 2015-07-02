package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.david.gameofshapes.Database.DbContract;
import com.david.gameofshapes.Database.ShapesDbHelper;
import com.david.gameofshapes.ImageAdapter;
import com.david.gameofshapes.Puzzle;
import com.david.gameofshapes.R;

import java.util.ArrayList;


public class PuzzleSelectionActivity extends Activity {

    public static ArrayList<Puzzle> puzzleList; //list of all puzzles
    public static ImageAdapter adapter;
    public static GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        grid = (GridView) findViewById(R.id.grid);
        adapter = new ImageAdapter(this,puzzleList);
        grid.setAdapter(adapter);
        //Toast.makeText(this, puzzleList.get(0).getConfig().toString(),Toast.LENGTH_LONG).show();
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(),""+position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                //intent.putExtra("menulayout","hard");
                intent.putExtra("puzzleId", position);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_puzzle_selection, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter = new ImageAdapter(this,puzzleList);
        grid.setAdapter(adapter);
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
