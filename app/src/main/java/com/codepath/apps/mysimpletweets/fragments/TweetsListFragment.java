package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import org.apache.http.Header;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.ComposeActivity;
import com.codepath.apps.mysimpletweets.DetailActivity;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import android.widget.AdapterView.OnItemLongClickListener;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.EndlessScroll;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by andres on 10/9/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private final int REQUEST_CODE = 200;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    public SwipeRefreshLayout swipeContainer;

    public TweetsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets= new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isNetworkAvailable()) {
            //populateTimeline(null);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tweets_list, parent , false);
        lvTweets = (ListView)v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        client = TwitterApplication.getRestClient();

        //lvTimeline = (ListView) v.findViewById(R.id.lvTimeline);
        aTweets = new TweetsArrayAdapter(getActivity(), new ArrayList<Tweet>());

        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScroll() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                System.out.println("tweet adapter: " + aTweets.getCount());
                if (aTweets.getCount() == 0) {
                    populateTimeline(null);
                } else if (aTweets.getCount() >= TwitterClient.TWEETS_PER_PAGE) {
                    Tweet oldest = aTweets.getItem(aTweets.getCount() - 1);
                    populateTimeline(oldest.getUid());
                }
            }
        });
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                aTweets.notifyDataSetChanged();
                populateTimeline(null);
            }
        });

        lvTweets.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", tweet);
                startActivityForResult(i, REQUEST_CODE);
                return false;
            }
        });


        return v;
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



    public void addAll(ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
        //swipeContainer.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ComposeActivity.REQUEST_CODE && resultCode == ComposeActivity.REQUEST_CODE) {
            // Append this tweet to the top of the feed
            Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
            add(tweet);
        }
    }



    public void add(Tweet tweet) {
        aTweets.insert(tweet, 0);
        aTweets.notifyDataSetChanged();
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }





}
