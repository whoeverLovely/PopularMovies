package com.louise.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{
    Context mContext;
    List<Trailer> trailerList;
    private ItemClickListener mClickListener;


    public TrailerAdapter(Context context, ItemClickListener clickListener) {
        mContext = context;
        mClickListener = clickListener;
    }

    public void swapData(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trailerName.setText(trailerList.get(position).getTrailerName());
    }

    @Override
    public int getItemCount() {
        if (trailerList == null)
            return 0;
        return trailerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView trailerName;
        ImageButton play;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.textView_trailer_name);
            play = itemView.findViewById(R.id.imageButton_play_icon);
            play.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
