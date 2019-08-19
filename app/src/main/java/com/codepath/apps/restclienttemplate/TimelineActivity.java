package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;
    private final int REQUEST_CODE = 20;

    private SwipeRefreshLayout swipeContaner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);
        swipeContaner = findViewById(R.id.swipeContaner);

        //Configure the Refreshing color
        swipeContaner.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Find the Recycler View
        rvTweets = findViewById(R.id.rvTweets);
        //Initialize list of tweets and Adapter from the date source
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);
        //recycler view setup: layout manager and setting the adapter
         rvTweets.setLayoutManager(new LinearLayoutManager(this));
         rvTweets.setAdapter(adapter);
        populateHomeTimeline();

        swipeContaner.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient","content is being refreshed");
                populateHomeTimeline();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.compose){
            //Tapped on compose icon
            //Toast.makeText(this, "Compose !", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ComposeActivity.class);
            startActivityForResult(i,REQUEST_CODE);
            //Navigate to a new Activity.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // REQUEST_CODE is defined above
        if(requestCode == REQUEST_CODE && requestCode ==RESULT_OK ){

            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0,tweet);
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
            rvTweets.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               // Log.d("TwitterClient ",response.toString());
                //iterate through the list of tweets
                List<Tweet> tweetsToAdd = new ArrayList<>();
                for(int i = 0; i < response.length(); i++) {
                    try {
                        //Convert each JSON object into a tweet object
                        JSONObject jsonTweetObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonTweetObject);
                        //Add the tweet into our data source
                        tweetsToAdd.add(tweet);
                        //tweets.add(tweet);
                        //Notify adapter
                        adapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //clear the exiting data
                adapter.clear();
                //show the data just received
                adapter.addTweets(tweetsToAdd);
                //adapter.addTweets(tweets);

                swipeContaner.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TwitterClient ",responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient ",errorResponse.toString());
            }
        });
    }
}
