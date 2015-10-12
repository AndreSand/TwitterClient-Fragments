package com.codepath.apps.mysimpletweets;

import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class ProfileActivity extends ActionBarActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
        //Success
        public void onSuccess(int statusCode, Header[] headers, JSONObject response)
        {
            user= User.fromJSON(response);
            //My current user account's info
            getSupportActionBar().setTitle("@"+user.getScreenName());
            populateProfileHeaders(user);
           Log.i("DEBUG --- userName: ", user.getScreenName().toString());
              }
              //Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
        //Get the screen name
        String screenName = getIntent().getStringExtra("scree_name");
        if (savedInstanceState == null) {
            //Create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            //Display user fragment within this activity (dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();//changes the fragments
        }

    }

    private void populateProfileHeaders(User user) {

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
       // TextView tvProfileScreenName = (TextView) findViewById(R.id.tvProfileScreenName);
        tvProfileName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

    }


}
