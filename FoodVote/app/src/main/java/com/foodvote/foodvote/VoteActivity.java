package com.foodvote.foodvote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.foodvote.foodvote.R;
import com.foodvote.model.Place;

public class VoteActivity extends ActionBarActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        //ViewPager for left and right cards
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapterViewPager = new VotePagerAdapter(fragmentManager);
        vpPager.setAdapter(adapterViewPager);

        //Tabs
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(vpPager);

        getSupportActionBar().setElevation(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class VotePagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private VoteCardFragment cardA;
        private VoteCardFragment cardB;

        public VotePagerAdapter(FragmentManager fm) {
            super(fm);
            cardA = VoteCardFragment.newInstance();
            cardB = VoteCardFragment.newInstance();
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return cardA;
                case 1:
                    return cardB;
                default:
                    return new Fragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Place A";
                case 1:
                    return "Place B";
                default:
                    return "";
            }
        }

        public void setPlaceCardA(Place place) {
            cardA.setPlace(place);
        }

        public void setPlaceCardB(Place place) {
            cardB.setPlace(place);
        }
    }
}
