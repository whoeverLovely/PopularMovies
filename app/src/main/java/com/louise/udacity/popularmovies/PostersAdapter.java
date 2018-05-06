package com.louise.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.ViewHolder> {

    private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<Movie> mMovieList;

    public PostersAdapter(ItemClickListener itemClickListener, Context context) {
        mItemClickListener = itemClickListener;
        mContext = context;
    }

    public void swapData(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poster_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostersAdapter.ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Picasso.get().load(NetworkUtil.getFullImagePath(movie.getPosterPath())).into(holder.posterImageView);
        holder.movieNameTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mMovieList == null)
            return 0;
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView posterImageView;
        TextView movieNameTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster_grid_item_image);
            movieNameTextView = itemView.findViewById(R.id.poster_grid_item_movie_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
