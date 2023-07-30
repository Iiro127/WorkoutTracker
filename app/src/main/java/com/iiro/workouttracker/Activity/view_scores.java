package com.iiro.workouttracker.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iiro.workouttracker.Database.ExerciseHandler;
import com.iiro.workouttracker.Database.ScoreAdapter;
import com.iiro.workouttracker.Database.ScoreHandler;
import com.iiro.workouttracker.MainActivity;
import com.iiro.workouttracker.Objects.Exercise;
import com.iiro.workouttracker.Objects.Score;
import com.iiro.workouttracker.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class view_scores extends AppCompatActivity {
    private TextView txt_name, txt_empty;
    private EditText edt_reps, edt_weight;
    private Button btn_add;
    private RecyclerView recycler;
    private ArrayList<Score> scores;
    private ScoreAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);
        Intent intent = getIntent();

        txt_name = findViewById(R.id.txt_name);
        txt_empty = findViewById(R.id.txt_empty);
        recycler = findViewById(R.id.recycler);
        btn_add = findViewById(R.id.btn_add);

        recycler.setLayoutManager(new LinearLayoutManager(view_scores.this));

        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new ScoreHandler(view_scores.this).delete(scores.get(viewHolder.getAdapterPosition()).getId());
                scores.remove(viewHolder.getAdapterPosition());
                Toast.makeText(view_scores.this, "Score deleted", Toast.LENGTH_SHORT).show();
                loadScores();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recycler);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newExercise();
            }
        });

        txt_name.setText(intent.getStringExtra("name"));


        loadScores();
    }

    @SuppressLint("MissingInflatedId")
    private void newExercise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view_scores.this);
        LayoutInflater inflater = (LayoutInflater) view_scores.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInput = inflater.inflate(R.layout.new_score, null, false);
        builder.setView(viewInput);
        builder.setTitle("New score");

        edt_reps = viewInput.findViewById(R.id.edt_reps);
        edt_weight = viewInput.findViewById(R.id.edt_weight);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!edt_reps.getText().toString().equals("") || !edt_weight.getText().toString().equals("")){
                    String score = edt_weight.getText().toString() + "kg x " + edt_reps.getText().toString();

                    Score newScore = new Score(getIntent().getStringExtra("name"), score);
                    new ScoreHandler(view_scores.this).create(newScore);
                    Toast.makeText(view_scores.this, "Score logged", Toast.LENGTH_SHORT).show();

                    loadScores();
                } else {
                    Toast.makeText(view_scores.this, "Empty fields", Toast.LENGTH_SHORT).show();
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

    public ArrayList<Score> readScores(){
        ArrayList<Score> all = new ScoreHandler(this).readAllForExercise(getIntent().getStringExtra("name"));
        return all;
    }

    public void loadScores(){
        scores = readScores();
        adapter = new ScoreAdapter(scores, this);
        recycler.setAdapter(adapter);

        if (scores.size() == 0){
            txt_empty.setText("No scores logged");
        } else {
            txt_empty.setText("");
        }
    }
}