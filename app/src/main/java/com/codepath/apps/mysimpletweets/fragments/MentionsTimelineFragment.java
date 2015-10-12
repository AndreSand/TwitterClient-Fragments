package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by andres on 10/9/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    private TwitterClient client = TwitterApplication.getRestClient();

    public void populateTimeline(String maxId) {
        client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("mentions response: " + response);
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("mentions timeline failed");
                System.out.println(errorResponse);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Couldn't get Tweets :(", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}