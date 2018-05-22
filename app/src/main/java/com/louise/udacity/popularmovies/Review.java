package com.louise.udacity.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String reviewContent;
    private String reviewerName;

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerName() {

        return reviewerName;
    }

    public String getReviewContent() {

        return reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewContent);
        dest.writeString(this.reviewerName);
    }

    public Review() {
    }

    protected Review(Parcel in) {
        this.reviewContent = in.readString();
        this.reviewerName = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
