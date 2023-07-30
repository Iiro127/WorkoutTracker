package com.iiro.workouttracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExerciseDatabase";
    private static final int DATABASE_VERSION = 1;
    public ExerciseDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE Exercises ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, category TEXT, favourite INTEGER )";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlQuery = "DROP TABLE IF EXISTS Exercises";
        sqLiteDatabase.execSQL(sqlQuery);
        onCreate(sqLiteDatabase);
    }
}
