package com.hamada.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamada.android.popularmovies.R;
import com.hamada.android.popularmovies.model.ResultReview;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.myHolder> {

    private List<ResultReview> reviewList;
    Context mcontext;

    public ReviewAdapter(Context mcontext, List<ResultReview> reviewList) {
        this.reviewList = reviewList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ReviewAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews,
                parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.myHolder holder, int position) {

        holder.mAuthor.setText(reviewList.get(position).getAuthor());
        holder.mContent.setText(reviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    public static class myHolder extends RecyclerView.ViewHolder{

        TextView mAuthor,mContent;
        public myHolder(View itemView) {
            super(itemView);
            mAuthor=itemView.findViewById(R.id.textViewAuthor);
            mContent=itemView.findViewById(R.id.textViewContent);
        }
    }
}
