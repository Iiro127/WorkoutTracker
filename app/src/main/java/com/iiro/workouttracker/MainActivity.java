package com.iiro.workouttracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iiro.workouttracker.Activity.view_all_exercises;
import com.iiro.workouttracker.Activity.view_scores;
import com.iiro.workouttracker.Database.ExerciseAdapter;
import com.iiro.workouttracker.Database.ExerciseHandler;
import com.iiro.workouttracker.Objects.Exercise;

import java.util.ArrayList;
// intent homma jäi kesken
public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_latest, recycler_favourites;
    private TextView txt_emptyLatest, txt_emptyFavourites, txt_latestLogs, txt_confirm;
    private EditText edt_name;
    private ImageButton btn_new;
    private RadioGroup radioGroup;
    private String currentCategory;
    private ExerciseAdapter adapterAll, adapterFavourites;
    private ArrayList<Exercise> all, favourites;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_latest = findViewById(R.id.recycler_latest);
        recycler_favourites = findViewById(R.id.recycler_favourites);
        btn_new = findViewById(R.id.btn_new);
        txt_emptyLatest = findViewById(R.id.txt_emptyLatest);
        txt_emptyFavourites = findViewById(R.id.txt_emptyFavourites);
        txt_latestLogs = findViewById(R.id.txt_latestLogs);

        recycler_latest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        txt_latestLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, view_all_exercises.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newExercise();
            }
        });

        loadExercises();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadExercises();
        }
    }


    @SuppressLint("MissingInflatedId")
    private void newExercise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInput = inflater.inflate(R.layout.new_exercise, null, false);
        builder.setView(viewInput);
        builder.setTitle("Set category");

        edt_name = viewInput.findViewById(R.id.edt_name);
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
                if (currentCategory != null && !edt_name.getText().toString().equals("")){
                    Exercise newExercise = new Exercise(edt_name.getText().toString(), currentCategory);
                    new ExerciseHandler(MainActivity.this).create(newExercise);
                    Toast.makeText(MainActivity.this, currentCategory, Toast.LENGTH_SHORT).show();

                    loadExercises();
                } else {
                    Toast.makeText(MainActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
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

    private void deleteExercise(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInput = inflater.inflate(R.layout.delete_exercise, null, false);
        builder.setView(viewInput);
        builder.setTitle("Confirm action");

        txt_confirm = viewInput.findViewById(R.id.txt_confirm);

        Exercise exercise = new ExerciseHandler(this).readSingleExercise(all.get(position).getId());

        txt_confirm.setText("Are you sure you want to delete " + exercise.getName() + " along with all of its scores? This cannot be undone.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new ExerciseHandler(MainActivity.this).delete(exercise.getId());
                Toast.makeText(MainActivity.this, exercise.getName() + " deleted", Toast.LENGTH_SHORT).show();

                loadExercises();
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

    public ArrayList<Exercise> readAll(){
        ArrayList<Exercise> exercises1 = new ExerciseHandler(MainActivity.this).readAll();
        return exercises1;
    }

    public ArrayList<Exercise> readFavourites(){
        ArrayList<Exercise> exercises1 = new ExerciseHandler(MainActivity.this).readFavourites();
        return exercises1;
    }

    public void showPopup(View v, int position) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the click actions for each menu item
                if (item.getItemId() == R.id.menu_delete) {
                    deleteExercise(position);
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    public void loadExercises(){
        all = readAll();
        favourites = readFavourites();
        adapterAll = new ExerciseAdapter(all, this, new ExerciseAdapter.ItemClicked() {
            @Override
            public void detailsClick(int position, View view) {
                Intent intent = new Intent(MainActivity.this, view_scores.class);
                intent.putExtra("name", new ExerciseHandler(MainActivity.this).readSingleExercise(all.get(position).getId()).getName());
                startActivityForResult(intent, 1);
            }

            @Override
            public void favClick(int position, View view) {
                Exercise exercise = new ExerciseHandler(MainActivity.this).readSingleExercise(all.get(position).getId());
                new ExerciseHandler(MainActivity.this).changeFavourite(exercise);

                loadExercises();
            }

            @Override
            public void select(int position, View view) {

            }

            @Override
            public void showMenu(int position, View view) {
                showPopup(view, position);
            }
        });

        adapterFavourites = new ExerciseAdapter(favourites, this, new ExerciseAdapter.ItemClicked() {
            @Override
            public void detailsClick(int position, View view) {
                Intent intent = new Intent(MainActivity.this, view_scores.class);
                intent.putExtra("name", new ExerciseHandler(MainActivity.this).readSingleExercise(favourites.get(position).getId()).getName());
                startActivityForResult(intent, 1);
            }

            @Override
            public void favClick(int position, View view) {
                Exercise exercise = new ExerciseHandler(MainActivity.this).readSingleExercise(favourites.get(position).getId());
                new ExerciseHandler(MainActivity.this).changeFavourite(exercise);

                loadExercises();
            }

            @Override
            public void select(int position, View view) {

            }

            @Override
            public void showMenu(int position, View view) {

            }
        });

        if (all.size() == 0){
            txt_emptyLatest.setText("You have no logs");
        } else {
            txt_emptyLatest.setText("");

        }

        if (favourites.size() == 0){
            txt_emptyFavourites.setText("You have no favourites");
        } else {
            txt_emptyFavourites.setText("");
        }

        recycler_latest.setAdapter(adapterAll);
        recycler_favourites.setAdapter(adapterFavourites);
    }
}