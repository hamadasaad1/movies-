package com.hamada.android.popularmovies.livedata;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FavoriteEntry.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private final static String LAGTAG=AppDatabase.class.getSimpleName();
    private final static Object lock=new Object();
    private static  AppDatabase mInstance;
    public static AppDatabase getData(Context context){

        if (mInstance==null){
            synchronized (lock){
                Log.d(LAGTAG,"create new database instance ");
                mInstance=Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,"favorite").build();

            }
        }
        Log.d(LAGTAG,"return new database instance ");
        return mInstance;

    }

    public abstract FavoriteDao favoriteDao();
}
