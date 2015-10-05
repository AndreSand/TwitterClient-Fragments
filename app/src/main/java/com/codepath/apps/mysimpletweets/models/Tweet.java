package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.apps.mysimpletweets.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andres on 10/3/15.
 */


/*
 "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
      "location": "San Francisco, CA",
      "follow_request_sent": false,
      "id_str": "119476949",
      "is_translator": false,
 */

public class Tweet implements Parcelable {
    private String body;
    private String uid;
    private String createdAt;
    private User user;
    private int retweetCount;

    public Tweet() {

    }
    public User getUser() {
        return user;
    }

    public String getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return TwitterClient.getRelativeTimeAgo(createdAt);
    }

    public String getBody() {
        return body;

    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    //Desirialize the JSON and build Tweet objects
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getString("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tweet;

    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){

        ArrayList<Tweet> tweets= new ArrayList<>();

        for (int i=0; i < jsonArray.length(); i++)
        {
            try{
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);

                if(tweet !=null){
                    tweets.add(tweet);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return tweets;
    }

    protected Tweet(Parcel in) {
        uid = in.readString();
        body = in.readString();
        createdAt = in.readString();
        retweetCount = in.readInt();
        user = (User) in.readValue(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeInt(retweetCount);
        dest.writeValue(user);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
