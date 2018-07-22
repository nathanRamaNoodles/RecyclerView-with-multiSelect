/*
This is the main Activity and it is a recyclerview
 */
package com.example.android.criminalintent.Activites;

import android.support.v4.app.Fragment;

import com.example.android.criminalintent.fragments.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {//goes to onCreate in SingleFragmentActivity
        return new CrimeListFragment();
    }
}
