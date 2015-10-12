package com.codepath.apps.mysimpletweets;
//https://github.com/codepath/android-rest-client-template/blob/master/README.md


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.helpers.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

/**
 * Created by andres on 10/3/15.
 */
public class TimelineActivity extends AppCompatActivity {
    TweetsPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //Find the pager sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        if (id == R.id.action_compose) {
            //client = TwitterApplication.getRestClient();
            //client.clearAccessToken();
            Intent i = new Intent(this, ComposeActivity.class);
            startActivityForResult(i, ComposeActivity.REQUEST_CODE);
        }

        if(id == R.id.action_profile) {
            Intent i = new Intent(this, ComposeActivity.class);  //ProfileActivity
            startActivity(i);
        }

        if(id == R.id.action_settings) {
            Intent i = new Intent(this, ComposeActivity.class);  //SettingsActivity
            startActivity(i);
        }
*/
        return super.onOptionsItemSelected(item);
    }

    //min 48 on video
    public void onProfileView(MenuItem mi) {
        //Launch the Profile View
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

    }


    public void onCompose(MenuItem mi) {
        //Launch the Profile View
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, ComposeActivity.REQUEST_CODE);

    }

    public void onLogOut(MenuItem mi) {
        // LogOut session
        TwitterClient client = TwitterApplication.getRestClient();
        client.clearAccessToken();
        //Send user to login Page
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ComposeActivity.REQUEST_CODE && resultCode == ComposeActivity.REQUEST_CODE) {
            // Append this tweet to the top of the feed
            Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
          //HomeTimelineFragment homeTimlineFragment = (HomeTimelineFragment) pagerAdapter.getRegisteredFragment(0);
           //homeTimlineFragment.add(tweet);
        }
    }

    //Return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        //Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();

            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        //Retunr the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //How many fragments there are to swipe between?
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


}
