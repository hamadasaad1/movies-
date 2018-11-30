package com.hamada.android.popularmovies.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface FavoriteDao {
    @Query("select *from favoritetabel order by id ASC")
    LiveData<List<FavoriteEntry>> loadAllFavoirte();


    @Query("select *from favoritetabel where title=:title ")
    List<FavoriteEntry>loadAll(String title);
    @Insert
    void insetFavorite(FavoriteEntry favoriteEntry);

    @Delete
    void deleteFavorite(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntry favoriteEntry);

    @Query("select *from favoritetabel where id=:id")
    LiveData<FavoriteEntry>loadFavoriteById(int id);

    @Query("delete from favoritetabel where movie_id=:id")
    void deleteWithId(int id);


}
