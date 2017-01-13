package com.example.sagi.twittersearchtestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.twitter_search.OAuthHandler;
import com.example.twitter_search.TwitterSearch;
import com.example.twitter_search.model.Tweet;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String CONSUMER_KEY = "98ZDjZnG8nr9a95aRA2mducVG";
    private static final String CONSUMER_SECRET = "pUkGxH6RY3jYv910egOaoSvXRjKcBqfkveMueYRDsvN9fNWANQ";

    private EditText mQueryEditTxt;
    private Button mQueryBtn;
    private RecyclerView mTweetsLv;
    private TweetAdapter mTweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryEditTxt = (EditText) findViewById(R.id.query_edt);
        mQueryBtn = (Button) findViewById(R.id.query_btn);
        mTweetsLv = (RecyclerView) findViewById(R.id.tweets_lv);

        mTweetAdapter = new TweetAdapter(this);
        mTweetsLv.setAdapter(mTweetAdapter);

        // Start by fetching OAuth token which we'll need to use - for it to be available for us
        OAuthHandler.getInstance().generateNewToken(getApplicationContext());

        // Start searchin when pressing on the search button
        mQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = mQueryEditTxt.getText().toString();
                startSearch(searchString);
            }
        });
    }

    private void startSearch(final String searchQuery) {
        TwitterSearch search = new TwitterSearch(CONSUMER_KEY, CONSUMER_SECRET);
        search.executeSearch(this, searchQuery, new TwitterSearch.TwitterSearchListener() {
            @Override
            public void onSearchResult(final List<Tweet> resultTweets) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the adapter
                        mTweetAdapter.updateTweetData(resultTweets);
                        mTweetAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSearchError(Exception ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed using twitter API", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
