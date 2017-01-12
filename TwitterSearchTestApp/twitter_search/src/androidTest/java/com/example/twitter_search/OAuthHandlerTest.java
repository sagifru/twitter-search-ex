package com.example.twitter_search;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.twitter_search.volley.RequestQueueMngr;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test OAuthHandler class
 */
@RunWith(AndroidJUnit4.class)
public class OAuthHandlerTest {
    @Test
    public void generateAccessToken() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        RequestQueueMngr.getInstance(appContext);

        OAuthHandler oAuthHandler = OAuthHandler.getInstance();

        // Initiate access token generation
        oAuthHandler.generateNewToken(appContext);

        assertNotNull(oAuthHandler.getAccessToken());
    }
}
