package io.github.zachfalcone.bakingtime.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Step implements Parcelable {
    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;

        int lengthThumbnailURL = thumbnailURL.length();
        if (mVideoURL.isEmpty() &&
                lengthThumbnailURL >= 4 &&
                thumbnailURL.substring(lengthThumbnailURL - 4).equals(".mp4")) {
            mVideoURL = thumbnailURL;
        } else {
            mThumbnailURL = thumbnailURL;
        }
    }

    private Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoURL);
        dest.writeString(mThumbnailURL);
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }
}
