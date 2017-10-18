package com.brayadiaz.merkapues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab1Fragment extends Fragment {

    private String title;
    private String nameTab1 = "Mis Listas";
    private String nameTab2 = "Listas Favoritas";
    /*private String nameTab3 = "Proximas Promo";
    private String nameTab4 = "Cover ";
    private String nameTab5 = "Otra cos";*/

    private String option;
    private int numberTabs = 0;

    private Fragment[] zonesList;

    private ListFragment temp;
    private Bundle arg;

    public static TabLayout tabLayout;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager viewPager;
    public static int int_items = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.view_pager);



        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        numberTabs = 2;
        zonesList = new Fragment[numberTabs];

        zonesList[0] = new MiListasFragment();
        zonesList[1] = new FavoritasFragment();
        //zonesList[2] = new MesPromoFragment();
/*
        for(int i = 0; i < 3; i++){
            //Bundle arg= new Bundle();
           // arg.putInt("position", i);
            zonesList[i] = new OfertFragment();
            //zonesList[i].setArguments(arg);
        }*/

        return x;

    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            for(int i = 0; i < numberTabs; i++){
                if (i == position){
                    return zonesList[i];
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return nameTab1;
                case 1:
                    return nameTab2;
                /*case 2:
                    return nameTab3;
                case 3:
                    return nameTab4;
                case 4:
                    return nameTab5;*/
            }
            return null;
        }
    }

    /** Method to set names to all string variables **/
    private void setStringNames(String _title, String _nameTab1, String _nameTab2){//, String _nameTab3){//, String _nameTab4, String _nameTab5){
        title = _title;
        nameTab1 = _nameTab1;
        nameTab2 = _nameTab2;
        /*nameTab3 = _nameTab3;
        nameTab4 = _nameTab4;
        nameTab5 = _nameTab5;*/
    }
}