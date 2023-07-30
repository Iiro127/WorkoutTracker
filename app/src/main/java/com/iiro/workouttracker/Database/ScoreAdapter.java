package com.iiro.workouttracker.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iiro.workouttracker.Objects.Score;
import com.iiro.workouttracker.R;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter <ScoreAdapter.ScoreHolder>{
    private ArrayList<Score> scores;
    Context context;
    ViewGroup parent;

    public ScoreAdapter(ArrayList<Score> scores, Context context) {
        this.scores = scores;
        this.context = context;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_holder, parent, false);
        this.parent = parent;

        return new ScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        holder.score.setText(scores.get(position).getScore());
        holder.date.setText(scores.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    class ScoreHolder extends RecyclerView.ViewHolder{
        TextView score, date;

        public ScoreHolder(@NonNull View itemView) {
            super(itemView);

            score = itemView.findViewById(R.id.txt_reps);
            date = itemView.findViewById(R.id.txt_date);
        }
    }
}
