package com.iiro.workouttracker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iiro.workouttracker.Database.ExerciseAdapter;
import com.iiro.workouttracker.Database.ExerciseHandler;
import com.iiro.workouttracker.Database.ScoreHandler;
import com.iiro.workouttracker.MainActivity;
import com.iiro.workouttracker.Objects.Exercise;
import com.iiro.workouttracker.Objects.Score;
import com.iiro.workouttracker.R;

import java.util.ArrayList;

public class view_all_exercises extends AppCompatActivity {
    private RecyclerView recycler;
    private ArrayList<Exercise> all, favourites;
    private ExerciseAdapter adapter;
    private TextView txt_latest, txt_first, txt_empty;
    private Button btn_filters;
    private RadioGroup radioGroup;
    private String currentCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_exercises);
        txt_first = findViewById(R.id.txt_first);
        txt_latest = findViewById(R.id.txt_latest);
        btn_filters = findViewById(R.id.btn_filter);
        txt_empty = findViewById(R.id.txt_empty);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(view_all_exercises.this));

        btn_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilters();
            }
        });

        loadExercises();
    }

    public void setFilters(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view_all_exercises.this);
        LayoutInflater inflater = (LayoutInflater) view_all_exercises.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInput = inflater.inflate(R.layout.set_filters, null, false);
        builder.setView(viewInput);
        builder.setTitle("Set filter");

        radioGroup = viewInput.findViewById(R.id.category_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = viewInput.findViewById(checkedId);
                currentCategory = radioButton.getText().toString();
            }
        });

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (currentCategory != null){
                    exerciseAdapter(readWithFilters(currentCategory));
                } else {
                    Toast.makeText(view_all_exercises.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ArrayList<Exercise> readWithFilters(String category){
        ArrayList<Exercise> exercises = new ExerciseHandler(view_all_exercises.this).readForCategory(category);
        return exercises;
    }
    public ArrayList<Exercise> readAll(){
        ArrayList<Exercise> exercises = new ExerciseHandler(view_all_exercises.this).readAll();
        return exercises;
    }

    public void loadExercises(){
        all = readAll();

        exerciseAdapter(all);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the click actions for each menu item
                if (item.getItemId() == R.id.menu_delete) {
                    Toast.makeText(view_all_exercises.this, "Clicked delete", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }
    public void exerciseAdapter(ArrayList<Exercise> all){
        adapter = new ExerciseAdapter(all, this, new ExerciseAdapter.ItemClicked() {
            @Override
            public void detailsClick(int position, View view) {
                Intent intent = new Intent(view_all_exercises.this, view_scores.class);
                intent.putExtra("name", new ExerciseHandler(view_all_exercises.this).readSingleExercise(all.get(position).getId()).getName());
                startActivityForResult(intent, 1);
            }

            @Override
            public void favClick(int position, View view) {
                Exercise exercise = new ExerciseHandler(view_all_exercises.this).readSingleExercise(all.get(position).getId());
                new ExerciseHandler(view_all_exercises.this).changeFavourite(exercise);

                loadExercises();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void select(int position, View view) {
                Exercise exercise = new ExerciseHandler(view_all_exercises.this).readSingleExercise(all.get(position).getId());

                try{
                    Score latest = new ScoreHandler(view_all_exercises.this).readLatestForExercise(exercise);
                    Score first = new ScoreHandler(view_all_exercises.this).readFirstForExercise(exercise);

                    txt_first.setText(first.getScore() + "\n" + first.getDate());
                    txt_latest.setText(latest.getScore() + "\n" + latest.getDate());
                } catch (NullPointerException e){
                    txt_first.setText("No scores logged");
                    txt_latest.setText("No scores logged");
                }
            }

            @Override
            public void showMenu(int position, View view) {
                showPopup(view);
            }
        });

        if (all.size() == 0){
            txt_empty.setText("No exercises logged");
        } else {
            txt_empty.setText("");
        }

        recycler.setAdapter(adapter);
    }
}