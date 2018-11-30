package com.hamada.android.popularmovies.data;


import android.provider.BaseColumns;

public class FavoriteContract {

    public FavoriteContract(){

    }

    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIEID = "movie_id";
        public static final String COLUMN_MOVVIE_TITLE = "title";
        public static final String COLUMN_USER_RATED = "user_rated";
        public static final String COLUMN_PASTER_PATH = "poster_path";
        public static final String COLUMN_PLOT_SYNP0SIS = "plot_synopsis";
    }

}
