package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andres on 10/3/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets){
        super(context, 0, tweets);
    }

    //ViewHolder Pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Tweet tweeet = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileName= (ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName= (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvBody= (TextView)convertView.findViewById(R.id.tvBody);
        TextView tvTweetAge= (TextView)convertView.findViewById(R.id.tvTweetAge);

        tvUserName.setText(tweeet.getUser().getScreenName());
        tvBody.setText(tweeet.getBody());
        tvTweetAge.setText(tweeet.getCreatedAt());
        ivProfileName.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweeet.getUser().getProfileImageUrl()).into(ivProfileName);

        return convertView;
    }

}
