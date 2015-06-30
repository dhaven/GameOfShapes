package com.david.gameofshapes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.david.gameofshapes.R;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulayout);
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
        /*
        Todo
        speedrun mode, how many puzzles can you solve in 60 seconds ?
         */
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
