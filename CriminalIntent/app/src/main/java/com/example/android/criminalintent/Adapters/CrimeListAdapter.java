//package com.example.android.criminalintent.Adapters;
//
//import android.graphics.Color;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.view.ActionMode;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.android.criminalintent.Objects.Crime;
//import com.example.android.criminalintent.Objects.CrimeLab;
//import com.example.android.criminalintent.R;
//import com.example.android.criminalintent.fragments.CrimeListFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.MyViewHolder> {
//    private List<Crime> mCrimes;
//    private boolean multiSelect = false;
//    private ArrayList<Crime> selectedItems = new ArrayList<Crime>();
//    private myInterface mInterface;
//    private ActionMode mMode;
//    private Menu mMenu;
//
//    public CrimeListAdapter() {
//        CrimeLab crimeLab = CrimeLab.get();
//        mCrimes = crimeLab.getCrimes();
//    }
//
//
//    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            multiSelect = true;
//            mInterface.onChangeToolbar();
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.fragment_crime_list_delete, menu);
//            mMode = mode;
//            mMenu = menu;
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.menu_item_delete_crime:
//                    for (Crime intItem : selectedItems) {
//                        mCrimes.remove(intItem);
//                        mInterface.itemRemoved(true);
//                    }
//                    mode.finish();
//                    break;
//                case R.id.menu_item_add_crime:
//                    Crime crime = new Crime();
//                    CrimeLab.get().addCrime(crime);
//                    mInterface.nextActivity(true, crime);
//                    break;
//            }
//            return true;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            multiSelect = false;
//            selectedItems.clear();
//            notifyDataSetChanged();
//        }
//    };
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_crime, parent, false);
//
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.update(mCrimes.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mCrimes.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        private RelativeLayout mRelativeLayout;
//        private TextView mTitleTextView;
//        private TextView mDateTextView;
//        private CheckBox mSolvedCheckBox;
////        private Crime mCrime; //troublemaker >:(
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
//            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
//            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
//            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
//
//        }
//
//        void selectItem(Crime item) {
//            if (multiSelect) {
//                if (selectedItems.contains(item)) {
//                    selectedItems.remove(item);
//                    mRelativeLayout.setBackgroundColor(Color.WHITE);
//                } else {
//                    selectedItems.add(item);
//                    mRelativeLayout.setBackgroundColor(Color.LTGRAY);
//                }
//                int size = selectedItems.size();
//                if (size == 0) {
//                    MenuItem register = mMenu.findItem(R.id.menu_item_add_crime);
//                    register.setVisible(true);
//                    register = mMenu.findItem(R.id.menu_item_delete_crime);
//                    register.setVisible(false);
//                } else {
//                    MenuItem register = mMenu.findItem(R.id.menu_item_add_crime);
//                    register.setVisible(false);
//                    register = mMenu.findItem(R.id.menu_item_delete_crime);
//                    register.setVisible(true);
//                }
//                mMode.setTitle(size + " selected");
//            } else {
//                mInterface.nextActivity(true, item);//CrimePager
//            }
//        }
//
//        void update(final Crime value) {
//            mTitleTextView.setText(value.getTitle());
//            mDateTextView.setText(value.getDateFormat());
//
//            mSolvedCheckBox.setChecked(value.isSolved());
//            if (selectedItems.contains(value)) {
//                mRelativeLayout.setBackgroundColor(Color.LTGRAY);
//            } else {
//                mRelativeLayout.setBackgroundColor(Color.WHITE);
//            }
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
//                    selectItem(value);
//                    return true;
//                }
//            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    selectItem(value);
//                }
//            });
//        }
//    }
//
//    public void setInterface(myInterface myInt) {
//        this.mInterface = myInt;
//    }
//
//
//    public interface myInterface {
//        void onChangeToolbar();
//
//        void itemRemoved(boolean isRemoved);
//
//        void nextActivity(boolean next, Crime position);
//    }
//}
