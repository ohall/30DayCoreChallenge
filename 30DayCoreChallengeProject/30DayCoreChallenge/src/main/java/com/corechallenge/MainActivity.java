package com.corechallenge;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     *Using this to track all our workouts
     */
    private ArrayList allWorkouts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize workouts array
        allWorkouts = new ArrayList();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /*
            getItem is called to instantiate the fragment for the given page.
            Return a DummySectionFragment (defined as a static inner class
            below) with the page number as its lone argument.
            */


            ChallengeSectionFragment fragment = new ChallengeSectionFragment();
            Bundle args = new Bundle();
            args.putInt(ChallengeSectionFragment.ARG_SECTION_NUMBER, position + 1);
            args.putString(ChallengeSectionFragment.ARG_SECTION_LABEL, getPageTitle(position).toString() );
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.situps).toUpperCase(l);
                case 1:
                    return getString(R.string.crunches).toUpperCase(l);
                case 2:
                    return getString(R.string.leg_lifts).toUpperCase(l);
                case 3:
                    return getString(R.string.plank).toUpperCase(l);

            }
            return null;
        }
    }



    public static class ChallengeSectionFragment extends Fragment{

        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_SECTION_LABEL = "section_label";

        public Workout todaysWorkout;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            todaysWorkout = getWorkoutForToday();
            View rootView;
            String movement = getArguments().getString(ARG_SECTION_LABEL);
            if(movement.equalsIgnoreCase(getString(R.string.plank))){
               rootView = inflater.inflate(R.layout.fragment_plank_workout, container, false);
            }else{
                rootView = inflater.inflate(R.layout.fragment_main_workout, container, false);
                Chronometer chron = (Chronometer) rootView.findViewById(R.id.chronometer);
                chron.(todaysWorkout.getRepsByMovement(movement)*1000);
            }
            TextView chTextView = (TextView) rootView.findViewById(R.id.section_label);
            chTextView.setText( todaysWorkout.getRepsByMovement(movement) + " " + movement );

            ToggleButton goonButton  = (ToggleButton) rootView.findViewById(R.id.doneButton);

            return rootView;
        }


        public Workout getWorkoutForToday() {
            //TODO: Schedule workouts by calendar and get from some services\
            return new Workout(10,7,5,20);
        }

        public class Workout {
            public int situps;
            public int crunches;
            public int legLifts;
            public int plank;

            public Workout(int pSitups, int pCrunches, int pLegLifts, int pPlank ){
                situps      = pSitups;
                crunches    = pCrunches;
                legLifts    = pLegLifts;
                plank       = pPlank;

            }

            public int getRepsByMovement(String pMovement){

                String wtvr = getString(R.string.situps).toUpperCase();
                if(pMovement.equalsIgnoreCase(getString(R.string.situps))){
                    return situps;
                }else if(pMovement.equalsIgnoreCase(getString(R.string.crunches))){
                    return crunches;
                }else if(pMovement.equalsIgnoreCase(getString(R.string.leg_lifts))){
                    return legLifts;
                }else if(pMovement.equalsIgnoreCase(getString(R.string.plank))){
                    return plank;
                }else{
                    return 0;
                }
            }
        }

    }







    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
//    public static class DummySectionFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        public static final String ARG_SECTION_NUMBER = "section_number";
//
//        public DummySectionFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
//            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
//            dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

}
