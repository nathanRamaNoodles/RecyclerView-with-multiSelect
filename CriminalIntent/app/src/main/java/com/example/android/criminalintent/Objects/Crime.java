package com.example.android.criminalintent.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private String DateFormat;
    private Date mDate;
    private String mTime;
    private boolean mSolved;

    public Crime() {
        //generate unique identifier
        mId = UUID.randomUUID();
        mDate = Calendar.getInstance().getTime();
    }

    public UUID getId() {
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

}
