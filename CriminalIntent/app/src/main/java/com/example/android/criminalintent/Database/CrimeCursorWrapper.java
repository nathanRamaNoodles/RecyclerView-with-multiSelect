package com.example.android.criminalintent.Database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.ParcelUuid;

import com.example.android.criminalintent.Objects.Crime;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(new ParcelUuid(UUID.fromString(uuidString)));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setTime(crime.getDate());
        crime.setSolved(isSolved != 0);

        return crime;

    }
}
