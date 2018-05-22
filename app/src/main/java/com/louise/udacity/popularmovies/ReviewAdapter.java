package com.louise.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviewList;
    private Context mContext;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    public void swapData(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewer.setText(review.getReviewerName());
        holder.reviewContent.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        if (reviewList == null)
            return 0;
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContent;
        TextView reviewer;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.textView_review_content);
            reviewer = itemView.findViewById(R.id.textView_reviewer_name);
        }
    }
}
