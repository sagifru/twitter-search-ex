package com.example.twitter_search;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.twitter_search.volley.JsonObjectHeadersRequest;
import com.example.twitter_search.volley.RequestQueueMngr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sagi on 1/12/2017.
 * This is a singleton class to handle OAuth token requests for our library.
 * Since this library allows only searching in twitter, we've created a specific app for it (MyTwitterSearchTest1123)
 * This will allow us to use the application's key & secret for connecting via OAuth - without requesting
 * the user to login himself.
 *
 * This class will be responsible for generating and returning usable access token
 */
public class OAuthHandler {
    private static final String LOG_TAG = OAuthHandler.class.getName();

    private static final String CONSUMER_KEY = "98ZDjZnG8nr9a95aRA2mducVG";
    private static final String CONSUMER_SECRET = "pUkGxH6RY3jYv910egOaoSvXRjKcBqfkveMueYRDsvN9fNWANQ";
    private static final String OAUTH_REQUEST_URL = "https://api.twitter.com/oauth2/token";

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private static final String CONTENT_TYPE_HEADER_VALUE = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final String REQUEST_STRING_BODY = "grant_type=client_credentials";

    private static final String TOKEN_TYPE = "token_type";
    private static final String BEARER = "bearer";
    private static final String ACCESS_TOKEN = "access_token";

    private String mAccessToken;
    private RequestListener mListener;

    private OAuthHandler() {
    }

    public static OAuthHandler getInstance() {
        return OAuthHandlerHolder.INSTANCE;
    }

    public void setRequestListener(RequestListener l) {
        mListener = l;
    }

    /**
     * Generate new token using our application's key & secret
     */
    public void generateNewToken(Context ctx) {
        // Execute a post request to the twitter's api
        JsonObjectHeadersRequest request = new JsonObjectHeadersRequest(
                Request.Method.POST,
                OAUTH_REQUEST_URL,
                REQUEST_STRING_BODY,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Exception ex = null;

                        try {
                            // Parse the response
                            if (response.has(TOKEN_TYPE) && response.has(ACCESS_TOKEN)) {
                                String tokenType = response.getString(TOKEN_TYPE);
                                if (BEARER.equals(tokenType)) {
                                    // the token type is correct, let's take it
                                    mAccessToken = response.getString(ACCESS_TOKEN);
                                }
                            }
                        } catch (JSONException je) {
                            Log.e(LOG_TAG, "Failed processing the token response correctly", je);
                            ex = je;
                        }

                        if (mListener != null) {
                            if (ex != null) {
                                mListener.onRequestFailure(ex);
                            } else {
                                mListener.onRequestSuccess();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Failed requesting access token", error);
                        mListener.onRequestFailure(error);
                    }
                });

        initRequestHeaders(request);

        // Execute the request
        RequestQueueMngr requestMngr = RequestQueueMngr.getInstance(ctx);
        requestMngr.addToRequestQueue(request);
    }

    private void initRequestHeaders(JsonObjectHeadersRequest request) {
        // Notice: twitter api requests us to URL encode the consumer key & secret.
        // the result of the URL encoding is the same as key & secret are now.
        // Instead of URL encoding these fields every time - I'm just gonna assume I've already
        // encoded them beforehand, and I'm holding them URL encoded.

        String concatenatedStr = CONSUMER_KEY + ":" + CONSUMER_SECRET;

        // Encode the data using Base64
        String base64Str = Base64.encodeToString(concatenatedStr.getBytes(), 0);

        // Make sure to remove \n characters
        base64Str = base64Str.replace("\n", "");

        // Add headers to the request
        request.addHeader(AUTHORIZATION_HEADER_KEY, base64Str);
        request.addHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_HEADER_VALUE);
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public boolean hasAccessToken() {
        return (mAccessToken == null);
    }

    /**
     * Use Initialization-on-demand holder idiom for singleton implementation
     * More details in wikipedia: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class OAuthHandlerHolder {
        static OAuthHandler INSTANCE = new OAuthHandler();
    }

    /**
     * Listener for when generating access token is finished
     */
    public interface RequestListener {
        void onRequestSuccess();
        void onRequestFailure(Exception ex);
    }
}
