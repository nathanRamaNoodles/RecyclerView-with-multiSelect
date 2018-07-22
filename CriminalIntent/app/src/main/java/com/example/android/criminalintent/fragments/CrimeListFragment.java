/*
This is where we use recyclerview with adapters and viewholders in order to use fragment_list_crime.xml and create an efficient list for our lists from CrimeLab
Similar to CrimeFragment, we will setup up the TextViews and checkbox for one item in the list.  Then using recyclerview, we can make more of the same item.
 */
package com.example.android.criminalintent.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.criminalintent.Adapters.CrimeListAdapter;
import com.example.android.criminalintent.Objects.Crime;
import com.example.android.criminalintent.Objects.CrimeLab;
import com.example.android.criminalintent.Activites.CrimePagerActivity;
import com.example.android.criminalintent.R;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class CrimeListFragment extends Fragment implements CrimeListAdapter.myInterface {
    //nothing yet
    private RecyclerView mCrimeRecyclerView; //These should be the only private variable when working with RecyclerView, otherwise, your app will mix data
    private ImageView mEmptyImage;
    private int clickedCrimePosition;  //notifyItemChanged variable
    private Toast mToast;
    private static final String CLICKED_CRIME_POSITION_ID = "clicked_crime_position_id";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle_Visible";
    public static boolean mSubtitleVisible;

    @Override
    public void onResume() {
        super.onResume();
//        updateUI(); //update data from From CrimeActivity
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
            updateSubtitle();
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get().addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                CrimePagerActivity.isSubtitleShown(mSubtitleVisible);
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get();
        int crimeCount = crimeLab.getCrimes().size();
        String plural = (crimeCount == 1) ? " crime" : " crimes";
        String subtitle = crimeCount + plural;
        if (crimeCount == 0) {
            mEmptyImage.setVisibility(View.VISIBLE);
        } else {
            mEmptyImage.setVisibility(View.INVISIBLE);
        }
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        mSubtitleVisible = getActivity().getIntent().getBooleanExtra(CrimePagerActivity.EXTRA_SUB_SHOWN, false);

    }

    @Nullable
    @Override
    //Main stuff
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mEmptyImage = (ImageView) view.findViewById(R.id.emptyImage);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(mCrimeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        CrimeListAdapter myAdapter = new CrimeListAdapter();
        myAdapter.setInterface(this);
        mCrimeRecyclerView.setAdapter(myAdapter);

        if (savedInstanceState != null) {
            clickedCrimePosition = savedInstanceState.getInt(CLICKED_CRIME_POSITION_ID, 0);
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            if (savedInstanceState.containsKey("key")) {
                savedInstanceState.getParcelableArrayList("key");
            }
        }
        updateSubtitle();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public void onChangeToolbar() {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), "Advanced Mode", Toast.LENGTH_SHORT);
        mToast.show();
    }


    @Override
    public void itemRemoved(boolean isRemoved) {
        if (isRemoved) {
            updateSubtitle();
        }
    }

    @Override
    public void nextActivity(boolean next, Crime position) {
        if (next) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), position.getTitle() + " clicked!", Toast.LENGTH_SHORT);
            mToast.show();

            Intent i = CrimePagerActivity.newIntent(getActivity(), position.getId());
            CrimePagerActivity.isSubtitleShown(CrimeListFragment.mSubtitleVisible);
            startActivity(i);
        }
    }


    //memory for vertical to horizontal modes
    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        onSavedInstanceState.putInt(CLICKED_CRIME_POSITION_ID, clickedCrimePosition);
        onSavedInstanceState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

}
