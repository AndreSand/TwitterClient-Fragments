package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailActivity extends Activity {
    private ImageView ivTweetImage;
    private TextView tvTweetName;
    private TextView tvTweetUserName;
    private TextView tvTweetBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Tweet tweet = (Tweet) getIntent().getExtras().get("tweet");

        ivTweetImage = (ImageView) findViewById(R.id.ivTweetImage);
        tvTweetName = (TextView) findViewById(R.id.tvTweetName);
        tvTweetUserName = (TextView) findViewById(R.id.tvTweetUserName);
        tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);

        tvTweetName.setText(tweet.getUser().getName());
        tvTweetUserName.setText(tweet.getUser().getScreenName());
        tvTweetBody.setText(tweet.getBody());
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivTweetImage);







    }
}