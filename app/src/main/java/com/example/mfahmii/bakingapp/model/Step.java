package com.example.mfahmii.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfahmii on 8/9/2017.
 */

public class Step implements Parcelable{
    private int stepId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(int stepId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL=thumbnailURL;
    }

    public int getStepId() {
        return stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stepId);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
    }

    protected Step(Parcel in) {
        this.stepId = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
