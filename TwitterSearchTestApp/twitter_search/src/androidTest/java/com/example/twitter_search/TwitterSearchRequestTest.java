package com.example.twitter_search;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.twitter_search.volley.RequestQueueMngr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test TwitterSearchRequestTest class
 */
@RunWith(AndroidJUnit4.class)
public class TwitterSearchRequestTest {
    @Test
    public void testTwitterSearchRequest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        final CountDownLatch signal = new CountDownLatch(1);
        final RequestQueueMngr requestMngr = RequestQueueMngr.getInstance(appContext);

        final OAuthHandler oAuthHandler = OAuthHandler.getInstance();
        oAuthHandler.setRequestListener(new OAuthHandler.RequestListener() {
            @Override
            public void onRequestSuccess() {
                String accessToken = oAuthHandler.getAccessToken();
                if (accessToken != null) {
                    TwitterSearchRequest request = TwitterSearchRequest.newTwitterSearch(
                            accessToken, "#TestMe", new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray tweets = response.getJSONArray("statuses");
                                        assertTrue(tweets.length() > 0);
                                    } catch (JSONException e) {
                                        fail();
                                    }
                                    signal.countDown();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    fail();
                                    signal.countDown();
                                }
                            });

                    requestMngr.addToRequestQueue(request);
                } else {
                    fail();
                    signal.countDown();
                }
            }

            @Override
            public void onRequestFailure(Exception ex) {
                fail();
                signal.countDown();
            }
        });

        // Initiate access token generation
        oAuthHandler.generateNewToken(appContext);

        signal.await();
    }
}
