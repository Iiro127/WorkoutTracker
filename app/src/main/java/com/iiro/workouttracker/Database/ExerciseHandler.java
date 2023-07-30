package com.iiro.workouttracker.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iiro.workouttracker.Objects.Exercise;

import java.util.ArrayList;

public class ExerciseHandler extends ExerciseDatabaseHelper {

    public ExerciseHandler(Context context) {
        super(context);
    }

    public boolean create(Exercise exercise){
        ContentValues values = new ContentValues();

        values.put("name", exercise.getName());
        values.put("category", exercise.getCategory());
        values.put("favourite", false);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean success = db.insert("Exercises", null, values) > 0;
        db.close();

        return success;
    }

    public ArrayList<Exercise> readAll(){
        ArrayList<Exercise> exercises = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Exercises ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));

                Exercise exercise = new Exercise(name, category);
                exercise.setId(id);
                exercises.add(exercise);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return exercises;
    }

    public ArrayList<Exercise> readForCategory(String searchCategory){
        ArrayList<Exercise> exercises = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Exercises WHERE category = '" + searchCategory + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));

                Exercise exercise = new Exercise(name, category);
                exercise.setId(id);
                exercises.add(exercise);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return exercises;
    }

    public ArrayList<Exercise> readFavourites(){
        ArrayList<Exercise> exercises = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Exercises WHERE favourite = 1";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));

                Exercise exercise = new Exercise(name, category);
                exercise.setId(id);
                exercises.add(exercise);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return exercises;
    }

    public boolean changeFavourite(Exercise exercise){
        ContentValues values = new ContentValues();

        values.put("name", exercise.getName());
        values.put("category", exercise.getCategory());
        if (isFavourite(exercise)){
            values.put("favourite", 0);
        } else {
            values.put("favourite", 1);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.update("Exercises", values, "id='" + exercise.getId() + "'", null) > 0;

        db.close();

        return success;
    }

    public Exercise readSingleExercise(int id){
        Exercise exercise = null;

        String sqlQuery = "SELECT * FROM Exercises WHERE id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));

                exercise = new Exercise(name, category);
                exercise.setId(id);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exercise;
    }

    public boolean isFavourite(Exercise exercise){
        ArrayList<Exercise> favourites = readFavourites();
        String sqlQuery = "SELECT * FROM Exercises WHERE name='" + exercise.getName() + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

            for (Exercise favourite : favourites){
                if (favourite.getName().equals(name)){
                    return true;
                }
            }
        }

        cursor.close();
        db.close();

        return false;
    }

    public boolean update(Exercise exercise){
        ContentValues values = new ContentValues();

        values.put("name", exercise.getName());
        values.put("category", exercise.getCategory());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.update("Exercises", values, "id='" + exercise.getId() + "'", null) > 0;

        db.close();

        return success;
    }

    public boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete("Exercises", "id='" + id + "'", null) > 0;
        db.close();

        return success;
    }
}
