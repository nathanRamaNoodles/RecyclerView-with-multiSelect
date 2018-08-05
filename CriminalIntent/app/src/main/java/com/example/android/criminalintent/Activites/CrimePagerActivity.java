/*
Activity for CrimeFragment.java
 */
package com.example.android.criminalintent.Activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.criminalintent.Objects.Crime;
import com.example.android.criminalintent.Objects.CrimeLab;
import com.example.android.criminalintent.R;
import com.example.android.criminalintent.fragments.CrimeFragment;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.example.android.criminalIntent.crime_id";
    public static final String EXTRA_SUB_SHOWN = "com.example.android.criminalIntent.sub_shown";
    private static boolean mSubtitle;
    private ViewPager mViewPager;
    private List<Crime> mCrimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        ParcelUuid crimeId = (ParcelUuid) getIntent().getParcelableExtra(EXTRA_CRIME_ID);

        mCrimes = CrimeLab.get(this).getCrimes();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //Set View pager to item that was selected(otherwise, we will always start at location 0)
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


    public static Intent newIntent(Context packageContext, ParcelUuid crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    public static void isSubtitleShown(boolean sub) {
        mSubtitle = sub;
    }


    //This code makes sure that the subtitle in his parent's activity is the same as it was before(It doesn't act like the back button).
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.putExtra(EXTRA_SUB_SHOWN, mSubtitle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.putExtra(EXTRA_SUB_SHOWN, mSubtitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        NavUtils.navigateUpTo(this, intent);
    }
}
