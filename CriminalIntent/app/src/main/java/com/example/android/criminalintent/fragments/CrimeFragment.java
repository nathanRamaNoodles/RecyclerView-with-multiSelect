/*
This is for the second Activity.
This is where we create the fragment_crime.xml and setup up the Textviews and checkbox.
 */
package com.example.android.criminalintent.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.CompoundButton;

import com.example.android.criminalintent.Objects.Crime;
import com.example.android.criminalintent.Objects.CrimeLab;
import com.example.android.criminalintent.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.UUID;

public class CrimeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    public static final String ARG_CRIME_ID = "crime_id"; //Fragment arguments, instead of EXTRAs
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private TimePickerDialog tpd;
    private DatePickerDialog dpd;

    //Fragment Arguments instead of Intent Extras(This makes code more neat and organized)
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId); //Similar to EXTRA format

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get().getCrime(crimeId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);


        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing
            }
        });

        mDateButton.setText(mCrime.getDateFormat());
        if (mCrime.getTime() != null) {
            mTimeButton.setText(mCrime.getTime().toString());
        }
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                now.setTime(mCrime.getDate());
                dpd = DatePickerDialog.newInstance(
                        CrimeFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.vibrate(true);
                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
                dpd.setTitle("Set a Date");
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(getFragmentManager(), "someTag");
            }
        });
        mTimeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                now.setTime(mCrime.getDate());
                tpd = TimePickerDialog.newInstance(
                        CrimeFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setTitle("Set a Time");
                tpd.setVersion(TimePickerDialog.Version.VERSION_1);
                tpd.vibrate(true);
                tpd.show(getFragmentManager(), "someTag");
            }
        });

        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set Crime's solved property
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCrime.getDate());
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        mCrime.setDate(cal.getTime());
        mCrime.setTime(mCrime.getDate());
        mTimeButton.setText(mCrime.getTime());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCrime.getDate());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mCrime.setDate(cal.getTime());
        mDateButton.setText(mCrime.getDateFormat());
    }
}
