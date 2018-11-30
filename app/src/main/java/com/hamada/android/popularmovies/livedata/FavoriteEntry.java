package com.hamada.android.popularmovies.livedata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "favoritetabel")
public class FavoriteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movie_id;
    private String title;
    private double rate;
    private String synopsis;
    private String posterPath;

    //constructor used when read from database
    public FavoriteEntry(int id, int movie_id, String title,
                         double rate, String synopsis, String posterPath) {
        this.id = id;
        this.movie_id = movie_id;
        this.title = title;
        this.rate = rate;
        this.synopsis = synopsis;
        this.posterPath = posterPath;
    }
    //constructor used when inset in database
    @Ignore
    public FavoriteEntry(int movie_id, String title,
                         double rate, String synopsis, String posterPath) {
        this.movie_id = movie_id;
        this.title = title;
        this.rate = rate;
        this.synopsis = synopsis;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
