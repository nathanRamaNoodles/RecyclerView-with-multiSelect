/*This is where we use recyclerview with adapters and viewholders in order to use fragment_list_crime.xml and create an efficient list for our lists from CrimeLab
        Similar to CrimeFragment, we will setup up the TextViews and checkbox for one item in the list.  Then using recyclerview, we can make more of the same item.
        */
package com.example.android.criminalintent.fragments;


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.criminalintent.Objects.Crime;
import com.example.android.criminalintent.Objects.CrimeLab;
import com.example.android.criminalintent.Activites.CrimePagerActivity;
import com.example.android.criminalintent.R;

import java.util.ArrayList;
import java.util.List;


public class CrimeListFragment extends Fragment {
    //nothing yet
    private RecyclerView mCrimeRecyclerView; //These should be the only private variable when working with RecyclerView, otherwise, your app will mix data
    private ImageView mEmptyImage;
    private int clickedCrimePosition;  //notifyItemChanged variable
    private Toast mToast;
    private static final String CLICKED_CRIME_POSITION_ID = "clicked_crime_position_id";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle_Visible";
    private static final String ADVANCED_TOOLBAR = "contextual_bar_visible";
    private CrimeListAdapter myAdapter;
    public static boolean mSubtitleVisible;
    private boolean multiSelect = false;
    private ArrayList<ParcelUuid> selectedItems = new ArrayList<ParcelUuid>();
    private List<Crime> mCrimes;
    private Menu mMenu;
    private ActionMode mMode;


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
                CrimeLab.get(getActivity()).addCrime(crime);
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
        CrimeLab crimeLab = CrimeLab.get(getActivity());
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

    public void nextActivity(Crime c) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), c.getTitle() + " clicked!", Toast.LENGTH_SHORT);
        mToast.show();

        Intent i = CrimePagerActivity.newIntent(getActivity(), c.getId());
        CrimePagerActivity.isSubtitleShown(CrimeListFragment.mSubtitleVisible);
        startActivity(i);
    }

    public void changeIcon() {
        int size = selectedItems.size();
        if (size == 0) {
            MenuItem register = mMenu.findItem(R.id.menu_item_add_crime);
            register.setVisible(true);
            register = mMenu.findItem(R.id.menu_item_delete_crime);
            register.setVisible(false);
        } else {
            MenuItem register = mMenu.findItem(R.id.menu_item_add_crime);
            register.setVisible(false);
            register = mMenu.findItem(R.id.menu_item_delete_crime);
            register.setVisible(true);
        }
        mMode.setTitle(size + " selected");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        myAdapter = new CrimeListAdapter();
        mCrimeRecyclerView.setAdapter(myAdapter);

        if (savedInstanceState != null) {
            clickedCrimePosition = savedInstanceState.getInt(CLICKED_CRIME_POSITION_ID, 0);
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            multiSelect = savedInstanceState.getBoolean(ADVANCED_TOOLBAR, false);
            if (multiSelect) {
                ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                selectedItems = savedInstanceState.getParcelableArrayList("key");
                changeIcon();
            }
        }
        updateSubtitle();
        return view;
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.fragment_crime_list_delete, menu);
            mMode = mode;
            mMenu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_delete_crime:
                    new AlertDialog.Builder(getActivity())
                            .setTitle(null)
                            .setMessage("Do you want to delete these crimes?")
                            .setNegativeButton("cancel", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("ok", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (ParcelUuid intItem : selectedItems) {
//                                        mCrimes.remove(intItem);
                                        CrimeLab.get(getActivity()).deleteCrime(intItem);
                                    }
                                    mode.finish();
                                    updateSubtitle();
                                }
                            }).show();
                    break;
                case R.id.menu_item_add_crime:
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    nextActivity(crime);
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            CrimeLab crimeLab = CrimeLab.get(getActivity());
            mCrimes = crimeLab.getCrimes();
            myAdapter.notifyDataSetChanged();
        }
    };


    //memory for vertical to horizontal modes
    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        onSavedInstanceState.putInt(CLICKED_CRIME_POSITION_ID, clickedCrimePosition);
        onSavedInstanceState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
        onSavedInstanceState.putBoolean(ADVANCED_TOOLBAR, multiSelect);
        onSavedInstanceState.putParcelableArrayList("key", selectedItems);
    }


    class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.MyViewHolder> {

        public CrimeListAdapter() {
            CrimeLab crimeLab = CrimeLab.get(getActivity());
            mCrimes = crimeLab.getCrimes();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_crime, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.update(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout mRelativeLayout;
            private TextView mTitleTextView;
            private TextView mDateTextView;
            private CheckBox mSolvedCheckBox;
//        private Crime mCrime; //troublemaker >:(

            MyViewHolder(View itemView) {
                super(itemView);
                mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
                mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
                mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
                mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

            }

            void selectItem(ParcelUuid item) {
                if (multiSelect) {
                    if (selectedItems.contains(item)) {
                        selectedItems.remove(item);
                        mRelativeLayout.setBackgroundColor(Color.WHITE);
                    } else {
                        selectedItems.add(item);
                        mRelativeLayout.setBackgroundColor(Color.LTGRAY);
                    }
                    changeIcon();
                } else {
                    CrimeLab crimeLab = CrimeLab.get(getActivity());
                    Crime crime = crimeLab.getCrime(item);
                    nextActivity(crime);
                }
            }

            void update(final Crime value) {
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                mCrimes = crimeLab.getCrimes();
                final ParcelUuid mId = value.getId();
                mTitleTextView.setText(value.getTitle());
                mDateTextView.setText(value.getDateFormat());

                mSolvedCheckBox.setChecked(value.isSolved());

                if (selectedItems.contains(mId)) {
                    mRelativeLayout.setBackgroundColor(Color.LTGRAY);
                } else {
                    mRelativeLayout.setBackgroundColor(Color.WHITE);
                }
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                        selectItem(mId);
                        return true;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectItem(mId);
                    }
                });
            }

        }

    }
}