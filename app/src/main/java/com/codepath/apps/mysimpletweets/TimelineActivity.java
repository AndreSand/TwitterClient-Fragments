package com.codepath.apps.mysimpletweets;
//https://github.com/codepath/android-rest-client-template/blob/master/README.md

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.adapters.EndlessScroll;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by andres on 10/3/15.
 */
public class TimelineActivity extends AppCompatActivity {



    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
            tweets= new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        //lvTweets.setAdapter(aTweets);
       // ActionBar bar = getActionBar();
       // bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));

        client = TwitterApplication.getRestClient();

        // populateTimeline();
        //pbTweetsLoading = (ProgressBar) view.findViewById(R.id.pbTweetsLoading);
        //lvTimeline = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScroll() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                System.out.println("tweet adapter: " + aTweets.getCount());
                if (aTweets.getCount() == 0) {
                    populateTimeline(null);
                } else if (aTweets.getCount() >= TwitterClient.TWEETS_PER_PAGE) {
                    Tweet oldest = aTweets.getItem(aTweets.getCount()-1);
                    populateTimeline(oldest.getUid());
                }
            }

        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override

            public void onRefresh() {
                aTweets.clear();
                aTweets.notifyDataSetChanged();
                populateTimeline(null);
            }
        });
    }


    public void populateTimeline(String maxId) {

            client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {

                //Success
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());
                    //JSON HERE
                    //DESERIALIZE JSON
                    //CREATE MODELS
                    //LOAD THE MODEL DATA INTO LISTVIEW
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                   addAll(tweets);

                }

                //Failure
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ComposeActivity.REQUEST_CODE && resultCode == ComposeActivity.REQUEST_CODE) {
            // Append this tweet to the top of the feed
            Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
            add(tweet);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void add(Tweet tweet) {
        aTweets.insert(tweet, 0);
        aTweets.notifyDataSetChanged();
    }

    protected void addAll(ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
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

        return super.onOptionsItemSelected(item);
    }


    }
