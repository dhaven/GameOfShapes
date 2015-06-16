package com.david.gameofshapes.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.david.gameofshapes.Database.DbContract;

/**
 * Created by david on 16/06/2015.
 */
public class ShapesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Shapes.db";
    public static final int DATABASE_VERSION = 1;

    public ShapesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.PuzzlesTable.PUZZLESTABLE_CREATE);
        DbContract.PuzzlesTable.insertPuzzles(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Puzzles;");
        onCreate(db);
    }
}
