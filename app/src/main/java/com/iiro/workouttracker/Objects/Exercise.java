package com.iiro.workouttracker.Objects;

import android.os.Build;

import java.time.LocalDate;
import java.util.Date;

public class Exercise {
    private String name, score, category;
    private LocalDate date;
    private int id;
    private boolean favourite;

    public Exercise(String name, String category){
        this.name = name;
        this.category = category;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }*/
}
