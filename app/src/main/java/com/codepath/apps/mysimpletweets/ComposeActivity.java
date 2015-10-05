package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


public class ComposeActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 200;

    EditText tweetText;
    Button submit;

    TwitterClient client = TwitterApplication.getRestClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tweetText = (EditText) findViewById(R.id.etNewTweetText);
        submit = (Button) findViewById(R.id.btnSubmitNewTweet);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.postUpdate(tweetText.getText().toString(), new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode,  JSONObject response) {
                        // Go back to the timeline
                        Tweet tweet = Tweet.fromJSON(response);
                        Intent i = new Intent();
                        i.putExtra("tweet", tweet);
                        setResult(REQUEST_CODE, i);
                        finish();
                    }

                    //Failure

                    public void onFailure(Throwable throwable, String s) {
                        Log.i("DEBUG", throwable.toString());
                        Log.i("DEBUG", s.toString());
                    }
                });

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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
}
