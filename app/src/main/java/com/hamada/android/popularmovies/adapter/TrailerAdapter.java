package com.hamada.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamada.android.popularmovies.R;
import com.hamada.android.popularmovies.model.Result;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.myViewHolder> {

    Context mContext;
    List<Result> mList;

    public TrailerAdapter(Context mContext, List<Result> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public TrailerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_trailer
                ,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.myViewHolder holder, int position) {

        holder.mTextViewTrailer.setText(mList.get(position).getName());
    }


    @Override
    public int getItemCount() {

        return mList.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView mTextViewTrailer;
        public myViewHolder(View itemView) {
            super(itemView);
            mTextViewTrailer=itemView.findViewById(R.id.textViewTrailerNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pastion=getAdapterPosition();
                    if (pastion !=RecyclerView.NO_POSITION){
                        Result result=mList.get(pastion);
                        String videoKey=mList.get(pastion).getKey();
                        playVideo(videoKey);
                    }
                }
            });
        }
    }
    //this method to play video
    public void playVideo(String key){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));

        // Check if the youtube app exists on the device
        if (intent.resolveActivity(mContext.getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }
        intent.putExtra("videoId",key);

        mContext.startActivity(intent);
    }
}
