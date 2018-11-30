package com.hamada.android.popularmovies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import com.hamada.android.popularmovies.adapter.MoviesAdapter;
import com.hamada.android.popularmovies.api.Client;
import com.hamada.android.popularmovies.api.Service;
import com.hamada.android.popularmovies.data.FavoriteDBHelper;
import com.hamada.android.popularmovies.livedata.AppDatabase;
import com.hamada.android.popularmovies.livedata.FavoriteEntry;
import com.hamada.android.popularmovies.model.Movie;
import com.hamada.android.popularmovies.model.MoviesResponse;
import com.hamada.android.popularmovies.viewModel.MainViewModel;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //my api key
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;


    @BindView(R.id.recycler_view)RecyclerView recyclerView;
    private MoviesAdapter adapter;
//    private List<Movie>movieList;
    ProgressDialog progressDialog;
    @BindView(R.id.main_content)SwipeRefreshLayout swipeRefreshLayout;
    public static final String LoG_TAG=MoviesAdapter.class.getName();
    private AppDatabase database;
    ArrayList< Movie> movieData=new ArrayList<>();
    private final String DATA_STATE = "saved_state";
    private final String LIST_STATE = "recycler_state";
    private Parcelable savedRecyclerViewState;
    GridLayoutManager mGridLayoutManager;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            checkSortOrder2(sharedPreferences);
            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

            initViews();
            //set color for swipeRefresh
            swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_orange_dark);
            //listener
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //when refresh load data by this is method
                    //initViews();
                    checkSortOrder2(sharedPreferences);
                    Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

//    public Activity getActivity(){
//        Context context =this;
//        while (context instanceof ContextWrapper)
//        {
//            if (context instanceof Activity){
//                return (Activity) context;
//            }
//            context=((ContextWrapper)((ContextWrapper) context).getBaseContext());
//
//        }
//
//        return null;
//    }
    //this method to load data
    private void initViews(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching Movies ..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //object from adapter and send this and movie list
        adapter=new MoviesAdapter(this,movieData);
        //check for orientation of activity if portrait draw two item from gridlayout
        //else draw 4 item
        if (this.getResources().getConfiguration()
                .orientation== Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager=new GridLayoutManager(this,calculateNoOfColumns(this));
            recyclerView.setLayoutManager(mGridLayoutManager);

        }else {
            mGridLayoutManager=new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(mGridLayoutManager);

        }
        //set animator for recycler view
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //set adapter for recycler view
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //call method to check sorted of movies
        //checkSortOrder();

       // checkSortOrder2();


    }
    //loadjson to popular movies
    private void loadJson(String type){
        try {
            //check from api key
            if (API_KEY.isEmpty()){
                Toast.makeText(this, "Please obtain API key from the website",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            //object from client class
            Client client=new Client();
            //object from service interface class and sent client object
            Service apiService=client.getClient().create(Service.class);
            //retrofit call back and sent method from class service
            Call<MoviesResponse> call=apiService.getMovies(type,API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movies = response.body().getResults();
                    movieData.clear();
                    movieData = (ArrayList<Movie>) movies;
                    adapter.setMovies(movieData);
                    restorePosition();
                    //to display movie by aphaptical
                    Collections.sort(movies, Movie.BY_NAME_ALPHAPTICAL);
                    progressDialog.cancel();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    Log.d("error",t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data,Check The Internet ",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                }
            });
        }catch (Exception e){
            Log.d("error",e.getMessage());
            Toast.makeText(this, e.getMessage().toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void displayFavorites(){
//        movieList=new ArrayList<>();
        adapter=new MoviesAdapter(this,movieData);
        if (this.getResources().getConfiguration()
                .orientation== Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager=new GridLayoutManager(this,calculateNoOfColumns(this));
            recyclerView.setLayoutManager(mGridLayoutManager);

        }else {
            mGridLayoutManager=new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(mGridLayoutManager);

        }

        progressDialog.cancel();
        //helper=new FavoriteDBHelper(this);
        //set animator for recycler view
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //set adapter for recycler view
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        database=AppDatabase.getData(getApplicationContext());

        setupView();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSortMost_popular:

                startActivity(new Intent(this,SettingActivity2.class));
                return true;

                //to open setting activity 2



//            case R.id.menuSortMost_popular:
//                //startActivity(new Intent(this,SettingActivity2.class));
//                String popular=this.getString(R.string.pref_most_popular);
//                checkSortOrder(popular);
//                return true;
//            case R.id.menuSortRating:
//                String highRating=this.getString(R.string.pref_highest_rated);
//                checkSortOrder(highRating);
//                return true;
//
//            case R.id.menuSortFavorite:
//                String favorite=this.getString(R.string.favorite_movie);
//                checkSortOrder(favorite);
//                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LoG_TAG,"shared preference updated");
        //checkSortOrder();

        checkSortOrder2(sharedPreferences);

    }

    private void checkSortOrder2(SharedPreferences preferences){

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String sorted=sharedPreferences.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_most_popular));
        if (sorted.equals(getString(R.string.pref_most_popular))){
            Toast.makeText(this, "Most Popular", Toast.LENGTH_SHORT).show();
            loadJson("popular");
        }else if (sorted.equals(this.getString(R.string.favorite_movie))){
            displayFavorites();

        }
        else {
            Toast.makeText(this, "Highest Rated", Toast.LENGTH_SHORT).show();
            loadJson("top_rated");
        }
    }
    private void checkSortOrder(String key){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (key.equals(getString(R.string.pref_most_popular))){
            Toast.makeText(this, "Most Popular", Toast.LENGTH_SHORT).show();

            loadJson("popular");
        }else if (key.equals(this.getString(R.string.favorite_movie))){
            displayFavorites();

        }
        else {
            Toast.makeText(this, "Highest Rated", Toast.LENGTH_SHORT).show();
            loadJson("top_rated");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE, savedRecyclerViewState);
    }




    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerViewState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    private void restorePosition() {
        if (savedRecyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            savedRecyclerViewState = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (savedRecyclerViewState !=null){
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }


    }


    private void setupView(){
        MainViewModel mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getLiveData().observeForever(new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                List<Movie> movies=new ArrayList<>();
                for (FavoriteEntry entry:favoriteEntries)
                {
                    Movie movie=new Movie();
                    movie.setId(entry.getMovie_id());
                    movie.setOriginalTitle(entry.getTitle());
                    movie.setPosterPath(entry.getPosterPath());
                    movie.setOverview(entry.getSynopsis());
                    movie.setVoteAverage(entry.getRate());
                    movies.add(movie);
                }
                adapter.setMovies(movies);
               // movieData.addAll(movies);



            }
        });
    }


   
    //to Grid layout for display different size devices
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
