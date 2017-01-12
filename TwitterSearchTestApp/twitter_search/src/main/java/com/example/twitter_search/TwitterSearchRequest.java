package com.example.twitter_search;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sagi on 1/13/2017.
 * Search request for twitter
 * Extends StringRequest and implements all the requirements to send a search request to twitter
 */
public class TwitterSearchRequest extends JsonObjectRequest {
    private static final String TWITTER_API_URL = "https://api.twitter.com/1.1/search/tweets.json?q=";

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer ";

    private Map<String, String> mHeaders;

    /**
     * Initializes a new Twitter Search request
     * @param accessToken The access token to use with the request. can retrieve a general token usin OAuthHandler,
     *                    or supply your own access token to use a specific user's context
     * @param listener Listener to the response of the search request
     * @param errorListener Listener to the error response of the search request
     */
    private TwitterSearchRequest(String accessToken, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);

        // Add the relevant header to use the access token
        mHeaders = new HashMap<>();
        mHeaders.put(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + accessToken);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Create a new twitter search request with the relevant search query
     * @param accessToken The access token to use with the request. can retrieve a general token usin OAuthHandler,
     *                    or supply your own access token to use a specific user's context
     * @param searchQuery The search query to use
     * @param listener Listener to the response of the search request
     * @param errorListener Listener to the error response of the search request
     * @return
     */
    public static TwitterSearchRequest newTwitterSearch(String accessToken, String searchQuery,
                                                        Response.Listener<JSONObject> listener,
                                                        Response.ErrorListener errorListener) {
        // Build the url from the search query
        String urlEncodedQuery = URLEncoder.encode(searchQuery);
        String url = TWITTER_API_URL + urlEncodedQuery;

        return new TwitterSearchRequest(accessToken, url, listener, errorListener);
    }
}
