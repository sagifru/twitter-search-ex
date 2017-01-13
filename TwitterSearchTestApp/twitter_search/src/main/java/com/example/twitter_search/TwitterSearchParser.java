package com.example.twitter_search;

import android.util.Log;

import com.example.twitter_search.model.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sagi on 1/13/2017.
 * Twitter Search Parser
 * Should parse a result of twitter search - and give back a list of all the tweets found
 */
public class TwitterSearchParser {
    private static final String LOG_TAG = TwitterSearchParser.class.getName();

    private static final SimpleDateFormat TWEETER_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    public static List<Tweet> parse(JSONObject tweetsResult) throws JSONException {
        List<Tweet> retVal = new ArrayList<>();

        if (tweetsResult == null) {
            return retVal;
        }

        JSONArray tweets = tweetsResult.getJSONArray("statuses");

        if (tweets != null) {
            // Run over the tweets in the array, and build each one into a Tweet object
            for (int i = 0; i < tweets.length(); i++) {
                JSONObject currTweet = tweets.getJSONObject(i);

                if (currTweet != null) {
                    Tweet tweet = new Tweet();

                    // Set tweet's text
                    tweet.setText(currTweet.getString("text"));

                    try {
                        // Set tweet's date
                        String createdAt = currTweet.getString("created_at");
                        Date date = TWEETER_DATE_FORMAT.parse(createdAt);
                        tweet.setCreatedAt(date);
                    } catch (ParseException pe) {
                        Log.e(LOG_TAG, "Failed parsing tweet time", pe);
                    }

                    // Set tweet's user
                    JSONObject user = currTweet.getJSONObject("user");
                    if (user != null) {
                        tweet.setUserName(user.getString("name"));
                        tweet.setProfileImageUrl(user.getString("profile_image_url"));
                    }

                    retVal.add(tweet);
                }
            }
        }

        return retVal;
    }
}
