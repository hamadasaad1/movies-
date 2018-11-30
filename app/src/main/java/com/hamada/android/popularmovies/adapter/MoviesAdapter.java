package com.hamada.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hamada.android.popularmovies.DetailActivity;
import com.hamada.android.popularmovies.R;
import com.hamada.android.popularmovies.livedata.FavoriteEntry;
import com.hamada.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.myViewHolder> {


    private Context mcontect;
    private List<Movie>movies;
    private List<FavoriteEntry>favoriteEntryList;
    public MoviesAdapter(Context mcontect, List<Movie> movies) {
        this.mcontect = mcontect;
        this.movies = movies;
    }



    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView mUserrate;
        public TextView mOrignalTitle;
        public ImageView mthumbnialImageView;

        public myViewHolder(View itemView) {
            super(itemView);
            mOrignalTitle=itemView.findViewById(R.id.mtitle);
            mUserrate=itemView.findViewById(R.id.userrating);
            mthumbnialImageView=itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion=getAdapterPosition();
                    if (postion !=RecyclerView.NO_POSITION) {
                        Movie clickItem=movies.get(postion);
                        Intent intent = new Intent(mcontect, DetailActivity.class);
                        intent.putExtra("original_title",movies.get(postion).getOriginalTitle());
                        intent.putExtra("poster_path",movies.get(postion).getPosterPath());
                        intent.putExtra("overview",movies.get(postion).getOverview());
                        intent.putExtra("vote_average",Double.toString(movies.get(postion).getVoteAverage()));
                        intent.putExtra("release_date",movies.get(postion).getReleaseDate());
                        intent.putExtra("id",movies.get(postion).getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mcontect.startActivity(intent);
                        Toast.makeText(v.getContext(), "Your Clicked"+clickItem.getOriginalTitle()
                                , Toast.LENGTH_SHORT).show();


                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public MoviesAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);

        return new myViewHolder(view);
    }

    public List<FavoriteEntry> getFavoriteEntryList() {
        return favoriteEntryList;
    }

    public void setFavoriteEntryList(List<FavoriteEntry> favoriteEntryList) {
        this.favoriteEntryList = favoriteEntryList;
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.myViewHolder holder, int position) {

        holder.mOrignalTitle.setText(movies.get(position).getOriginalTitle());
        //String vote=Double.toString(movies.get(position).getVoteAverage());
        holder.mUserrate.setText(""+movies.get(position).getVoteAverage());
        //holder.mUserrate.setText(vote);
        Picasso.with(mcontect)
                .load(movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.mthumbnialImageView);

    }

    @Override
    public int getItemCount() {

        return movies.size();
    }

}
