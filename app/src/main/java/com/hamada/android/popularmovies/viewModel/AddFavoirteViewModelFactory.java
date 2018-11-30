package com.hamada.android.popularmovies.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.hamada.android.popularmovies.livedata.AppDatabase;

public class AddFavoirteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase appDatabase;
    private final int id;

    public AddFavoirteViewModelFactory(AppDatabase appDatabase, int id) {
        this.appDatabase = appDatabase;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new AddFavoriteViewModel(appDatabase,id);
    }
}
