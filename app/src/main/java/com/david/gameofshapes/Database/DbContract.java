package com.david.gameofshapes.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.david.gameofshapes.Puzzle;

import java.util.ArrayList;

/**
 * Created by david on 16/06/2015.
 */
public class DbContract {

    public static final int[] PUZZLE_ID = {1,2,3,4,5};
    public static final String[] DIFFICULTY = {"easy","easy","medium","medium","hard"};
    public static final int[] NUM_MOVES = {3,4,4,5,6}; //number of moves to solve the puzzle
    public static final int[] SIZE ={3,3,4,4,4}; //size of the puzzle (i.e. 3x3,4x4,...
    public static final int[] SOLVED={0,0,1,0,1}; //0 if puzzle is not solved, 1 otherwise

    public static abstract class PuzzlesTable implements BaseColumns {
        public static final String TABNAME = "Puzzles";
        public static final String COLUMN_NAME_PUZZLEID = "PuzzleId";
        public static final String COLUMN_NAME_DIFFICULTY = "Difficulty";
        public static final String COLUMN_NAME_NUMMOVES = "NumMoves";
        public static final String COLUMN_NAME_SIZE = "Size";
        public static final String COLUMN_NAME_SOLVED = "Solved";
        public static final String[] COLUMN_NAMES = {"PuzzleId", "Difficulty", "NumMoves", "Size", "Solved"};

        public static final String PUZZLESTABLE_CREATE = "CREATE TABLE " + TABNAME + " (PuzzleId integer(3) NOT NULL, Difficulty varchar(6) NOT NULL, NumMoves integer(2) NOT NULL , Size integer(1) NOT NULL, Solved integer(1) NOT NULL, primary key(PuzzleId));";

        public static final void insertPuzzles(SQLiteDatabase db){
            for(int i = 0; i < PUZZLE_ID.length; i++){
                db.execSQL("INSERT INTO " + TABNAME + "(" + COLUMN_NAME_PUZZLEID + "," + COLUMN_NAME_DIFFICULTY + "," + COLUMN_NAME_NUMMOVES + "," + COLUMN_NAME_SIZE + "," + COLUMN_NAME_SOLVED + ") VALUES('" + PUZZLE_ID[i] + "','" + DIFFICULTY[i] + "','" + NUM_MOVES[i] + "','" + SIZE[i] + "','" + SOLVED[i] + "');");
            }
        }

        public static ArrayList<Puzzle> getAllPuzzles(SQLiteDatabase db){
            int puzzle_id;
            String difficulty;
            int num_moves;
            int size;
            int solved;
            ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
            Cursor cursor = db.query(TABNAME,COLUMN_NAMES,null, null, null, null,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                puzzle_id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PUZZLEID));
                difficulty = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DIFFICULTY));
                num_moves = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_NUMMOVES));
                size = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SIZE));
                solved = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SOLVED));
                puzzles.add(new Puzzle(puzzle_id,difficulty,num_moves,size,solved));
                cursor.moveToNext();
            }
            db.close();
            return puzzles;
        }


    }
}
