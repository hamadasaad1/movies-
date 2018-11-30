package com.hamada.android.popularmovies.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hamada.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favorite.db";
    public static final String LOGTAG="FAVORITE";
    SQLiteOpenHelper helper;
    SQLiteDatabase database;
    public FavoriteDBHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteContract.FavoriteEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIEID+"INTEGER, "+
                        FavoriteContract.FavoriteEntry.COLUMN_MOVVIE_TITLE+"TEXT NOT NULL, "+
                        FavoriteContract.FavoriteEntry.COLUMN_USER_RATED+"REAL NOT NULL, "+
                        FavoriteContract.FavoriteEntry.COLUMN_PASTER_PATH+"TEXT NOT NULL, "+
                        FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNP0SIS+"TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_ENTRIES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);

    }
    public void open(){

        Log.i(LOGTAG,"database opened");
        database=helper.getWritableDatabase();
    }
    public void closed(){
        Log.i(LOGTAG,"database closed");
        helper.close();
    }
    public void appFavorite(Movie movie){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVVIE_TITLE,movie.getOriginalTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USER_RATED,movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PASTER_PATH,movie.getPosterPath());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNP0SIS,movie.getOverview());

        database.insert(FavoriteContract.FavoriteEntry.TABLE_NAME,null,values);
        database.close();
    }
    public void deleteFavorite(int id){
        SQLiteDatabase database=this.getWritableDatabase();
        database.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID+" = " + id,null);
    }
    public List<Movie> getFavorite(){
        String []Columu={FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVVIE_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USER_RATED,
                FavoriteContract.FavoriteEntry.COLUMN_PASTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNP0SIS};
        String sortOrder=FavoriteContract.FavoriteEntry._ID+"ASC";
        List<Movie> favoriteList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                Columu,
                null,
                null,
                null,
                null,
                sortOrder);
        if (cursor.moveToFirst()){
            do {


                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString
                        (cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.
                        FavoriteEntry.COLUMN_MOVVIE_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.
                        FavoriteEntry.COLUMN_PLOT_SYNP0SIS)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex
                        (FavoriteContract.FavoriteEntry.COLUMN_USER_RATED))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract
                        .FavoriteEntry.COLUMN_PASTER_PATH)));
                favoriteList.add(movie);
            }while (cursor.moveToNext());


        }
        cursor.close();
        database.close();
        return favoriteList;
    }
}
