package com.iiro.workouttracker.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iiro.workouttracker.Objects.Exercise;
import com.iiro.workouttracker.Objects.Score;
import com.iiro.workouttracker.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter <ExerciseAdapter.ExerciseHolder> {
    ArrayList<Exercise> exercises;
    Context context;
    ViewGroup parent;
    ItemClicked itemCLicked, otherClicked;

    public ExerciseAdapter(ArrayList<Exercise> exercises, Context context, ItemClicked itemCLicked) {
        this.exercises = exercises;
        this.context = context;
        this.itemCLicked = itemCLicked;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_holder, parent, false);
        this.parent = parent;

        return new ExerciseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Score latest = new ScoreHandler(context).readLatestForExercise(exercises.get(position));
        Exercise exercise = new ExerciseHandler(context).readSingleExercise(exercises.get(position).getId());

        boolean favourite = new ExerciseHandler(context).isFavourite(exercise);

        if (favourite){
            holder.btn_favourite.setImageResource(R.drawable.ic_favourite_active);
        } else {
            holder.btn_favourite.setImageResource(R.drawable.ic_favourite_inactive);

        }

        holder.name.setText(exercises.get(position).getName());

        if (latest != null){
            holder.score.setText(latest.getScore());
            holder.date.setText(latest.getDate());
        } else {
            holder.score.setText("No scores logged");
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseHolder extends RecyclerView.ViewHolder{
        ConstraintLayout layout;
        TextView name, score, date;
        ImageView btn_details, btn_favourite;
        ImageButton btn_popup;
        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_exercise);
            score = itemView.findViewById(R.id.txt_latest);
            date = itemView.findViewById(R.id.txt_date);
            btn_details = itemView.findViewById(R.id.btn_details);
            btn_favourite = itemView.findViewById(R.id.btn_favourite);
            layout = itemView.findViewById(R.id.exercise_layout);
            btn_popup = itemView.findViewById(R.id.btn_popup);

            btn_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { itemCLicked.detailsClick(getAdapterPosition(), view); }
            });
            btn_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { itemCLicked.showMenu(getAdapterPosition(), view); }
            });

            btn_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemCLicked.favClick(getAdapterPosition(), view);
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemCLicked.select(getAdapterPosition(), view);
                }
            });
        }
    }

    public interface ItemClicked{
        void detailsClick(int position, View view);
        void favClick(int position, View view);
        void select(int position, View view);
        void showMenu(int position, View view);
    }
}
