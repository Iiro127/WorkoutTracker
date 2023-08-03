package com.iiro.workouttracker.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.iiro.workouttracker.Objects.Exercise;
import com.iiro.workouttracker.Objects.Score;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScoreHandler extends ScoreDatabaseHelper{

    public ScoreHandler(Context context) {
        super(context);
    }

    public boolean create(Score score){
        ContentValues values = new ContentValues();

        values.put("name", score.getName());
        values.put("score", score.getScore());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            values.put("date", LocalDate.now().toString());
        }

        SQLiteDatabase db = this.getWritableDatabase();

        boolean success = db.insert("Scores", null, values) > 0;

        return success;
    }


    public ArrayList<Score> readAllForExercise(String exerciseName){
        ArrayList<Score> scores = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Scores WHERE name='" + exerciseName + "' ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String score = cursor.getString(cursor.getColumnIndex("score"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));

                Score newScore = new Score(name, score);
                newScore.setDate(date);
                newScore.setId(id);
                scores.add(newScore);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return scores;
    }

    public Score readLatestForExercise(Exercise exercise){
        ArrayList<Score> scores = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Scores WHERE name='" + exercise.getName() + "' ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String score = cursor.getString(cursor.getColumnIndex("score"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));

                Score newScore = new Score(name, score);
                newScore.setDate(date);
                newScore.setId(id);
                scores.add(newScore);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        if (scores.size() == 0){
            return null;
        }

        return scores.get(0);
    }

    public Score readFirstForExercise(Exercise exercise){
        ArrayList<Score> scores = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Scores WHERE name='" + exercise.getName() + "' ORDER BY id ASC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String score = cursor.getString(cursor.getColumnIndex("score"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));

                Score newScore = new Score(name, score);
                newScore.setDate(date);
                newScore.setId(id);
                scores.add(newScore);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        if (scores.size() == 0){
            return null;
        }

        return scores.get(0);
    }

    public boolean deleteAll(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete("Scores", "name='" + name + "'", null) > 0;
        db.close();

        return success;
    }

    public boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete("Scores", "id='" + id + "'", null) > 0;
        db.close();

        return success;
    }
}
