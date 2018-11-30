package com.hamada.android.popularmovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hamada.android.popularmovies.livedata.AppDatabase;
import com.hamada.android.popularmovies.livedata.FavoriteEntry;

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<FavoriteEntry> listLiveData;

    public AddFavoriteViewModel(AppDatabase database ,int id) {
        listLiveData=database.favoriteDao().loadFavoriteById(id);
    }

    public LiveData<FavoriteEntry> getListLiveData() {
        return listLiveData;
    }
}
