package com.example.twitter_search;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.twitter_search.model.Tweet;
import com.example.twitter_search.volley.RequestQueueMngr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sagi on 1/13/2017.
 * A class to wrap up the usage of flow for twitter search
 * This class can either get a consumer key & secret - and handle OAuth himself,
 * or receive a made OAuth token to work with
 * This class wraps the flow required for executing a search in twitter API, and will give back
 * A list of tweets
 */
public class TwitterSearch {
    private String mConsumerKey;
    private String mConsumerSecret;
    private String mOAuthToken;

    public TwitterSearch(String consumerKey, String consumerSecret) {
        mConsumerKey = consumerKey;
        mConsumerSecret = consumerSecret;
    }

    public TwitterSearch(String oauthToken) {
        mOAuthToken = oauthToken;
    }

    public void executeSearch(final Context context, final String searchQuery, final TwitterSearchListener listener) {
        if (mOAuthToken == null) {
            // We need to retrieve OAuth token
            final OAuthHandler oAuthHandler = OAuthHandler.getInstance();
            if (oAuthHandler.hasAccessToken()) {
                mOAuthToken = oAuthHandler.getAccessToken();
            } else {
                // We need to generate a new access token
                oAuthHandler.initConsumerData(mConsumerKey, mConsumerSecret);
                oAuthHandler.setRequestListener(new OAuthHandler.RequestListener() {
                    @Override
                    public void onRequestSuccess() {
                        // Take the access token and move on
                        mOAuthToken = oAuthHandler.getAccessToken();

                        innerExecuteSearch(context, searchQuery, listener);
                    }

                    @Override
                    public void onRequestFailure(Exception ex) {
                        // We've failed executing search
                        listener.onSearchError(ex);
                    }
                });
                oAuthHandler.generateNewToken(context);
            }
        }

        if (mOAuthToken != null) {
            innerExecuteSearch(context, searchQuery, listener);
        }
    }

    private void innerExecuteSearch(final Context ctx, final String searchQuery, final TwitterSearchListener listener) {
        // Create twitter search request and run it on the request manager
        TwitterSearchRequest searchRequest = TwitterSearchRequest.newTwitterSearch(mOAuthToken, searchQuery,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the result
                            List<Tweet> searchResult = TwitterSearchParser.parse(response);
                            listener.onSearchResult(searchResult);
                        } catch (JSONException e) {
                            // We've failed executing search
                            listener.onSearchError(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // We've failed executing search
                        listener.onSearchError(error);
                    }
                });

        RequestQueueMngr.getInstance(ctx).addToRequestQueue(searchRequest);
    }

    public interface TwitterSearchListener {
        void onSearchResult(List<Tweet> resultTweets);
        void onSearchError(Exception ex);
    }
}
