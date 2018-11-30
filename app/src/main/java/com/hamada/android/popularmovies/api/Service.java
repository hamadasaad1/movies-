package com.hamada.android.popularmovies.api;

import com.hamada.android.popularmovies.model.MoviesResponse;
import com.hamada.android.popularmovies.model.ReviewsResponse;
import com.hamada.android.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
//    @GET("movie/popular")
//    Call<MoviesResponse>getPopularMovie(@Query("api_key")String apiKey);
//    @GET("movie/top_rated")
//    Call<MoviesResponse>getTopRatedMovie(@Query("api_key")String apiKey);

    @GET("movie/{sort_by}")
    Call<MoviesResponse> getMovies(@Path("sort_by") String sortBy,
                                   @Query("api_key") String apiKey);

    //to get video for any movie
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse>getVidoes(@Path("movie_id") int id, @Query("api_key")String apiKey );

    //to get review
    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse>getReviews(@Path("movie_id")int id, @Query("api_key")String apiKey);




}
