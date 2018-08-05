package com.example.android.criminalintent.Objects;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Crime implements Parcelable {

    private ParcelUuid mId;
    private String mTitle;
    private String DateFormat;
    private Date mDate;
    private String mTime;
    private boolean mSolved;

    public Crime() {
        //generate unique identifier
        this(new ParcelUuid(UUID.randomUUID()));
//        mId = UUID.randomUUID();
    }

    public Crime(ParcelUuid id) {
        mId = id;
        mDate = Calendar.getInstance().getTime();
    }

    protected Crime(Parcel in) {
        mTitle = in.readString();
        DateFormat = in.readString();
        mTime = in.readString();
        mSolved = in.readByte() != 0;
    }

    public static final Creator<Crime> CREATOR = new Creator<Crime>() {
        @Override
        public Crime createFromParcel(Parcel in) {
            return new Crime(in);
        }

        @Override
        public Crime[] newArray(int size) {
            return new Crime[size];
        }
    };

    public ParcelUuid getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,  MMM d, yyyy");
        DateFormat = sdf.format(mDate);
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        mTime = sdf.format(date);
    }

    public String getDateFormat() {
        return DateFormat;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(DateFormat);
        dest.writeString(mTime);
        dest.writeByte((byte) (mSolved ? 1 : 0));
    }
}
