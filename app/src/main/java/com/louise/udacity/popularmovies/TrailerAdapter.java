package com.louise.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{
    Context mContext;
    int mCount;
    private ItemClickListener mClickListener;

    public TrailerAdapter(Context context, ItemClickListener clickListener) {
        mContext = context;
        mClickListener = clickListener;
    }

    public void swapData(int count) {
        mCount = count;
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
        holder.trailerNo.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView trailer;
        TextView trailerNo;
        ImageButton play;

        public ViewHolder(View itemView) {
            super(itemView);
            trailer = itemView.findViewById(R.id.textView_trailer);
            trailerNo = itemView.findViewById(R.id.textView_trailer_no);
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
