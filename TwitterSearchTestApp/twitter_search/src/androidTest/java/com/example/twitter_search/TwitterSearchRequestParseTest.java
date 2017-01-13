package com.example.twitter_search;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.twitter_search.model.Tweet;
import com.example.twitter_search.volley.RequestQueueMngr;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test TwitterSearchRequestParseTest class
 */
@RunWith(AndroidJUnit4.class)
public class TwitterSearchRequestParseTest {
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

    @Test
    public void testTwitterSearchParser() throws Exception {
        JSONObject twitterResult = new JSONObject("{  \n" +
                "   \"statuses\":[  \n" +
                "      {  \n" +
                "         \"created_at\":\"Thu Jan 12 08:10:59 +0000 2017\",\n" +
                "         \"id\":819456627824795649,\n" +
                "         \"id_str\":\"819456627824795649\",\n" +
                "         \"text\":\"Dumbass honestly thinks he gonna get away with something \uD83D\uDE02 #tryme #testme https:\\/\\/t.co\\/GVaaj43OF6\",\n" +
                "         \"truncated\":false,\n" +
                "         \"entities\":{  },\n" +
                "         \"extended_entities\":{  },\n" +
                "         \"metadata\":{  },\n" +
                "         \"source\":\"<a href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\">Twitter for iPhone<\\/a>\",\n" +
                "         \"in_reply_to_status_id\":null,\n" +
                "         \"in_reply_to_status_id_str\":null,\n" +
                "         \"in_reply_to_user_id\":null,\n" +
                "         \"in_reply_to_user_id_str\":null,\n" +
                "         \"in_reply_to_screen_name\":null,\n" +
                "         \"user\":{  \n" +
                "            \"id\":978599785,\n" +
                "            \"id_str\":\"978599785\",\n" +
                "            \"name\":\"Sarah Ashleigh\",\n" +
                "            \"screen_name\":\"Sarrrr__Bearrrr\",\n" +
                "            \"location\":\"\",\n" +
                "            \"description\":\"Just another white girl trying not to be basic\",\n" +
                "            \"url\":null,\n" +
                "            \"entities\":{  \n" +
                "               \"description\":{  \n" +
                "                  \"urls\":[  \n" +
                "\n" +
                "                  ]\n" +
                "               }\n" +
                "            },\n" +
                "            \"protected\":false,\n" +
                "            \"followers_count\":85,\n" +
                "            \"friends_count\":132,\n" +
                "            \"listed_count\":6,\n" +
                "            \"created_at\":\"Thu Nov 29 15:40:01 +0000 2012\",\n" +
                "            \"favourites_count\":19,\n" +
                "            \"utc_offset\":-14400,\n" +
                "            \"time_zone\":\"Atlantic Time (Canada)\",\n" +
                "            \"geo_enabled\":false,\n" +
                "            \"verified\":false,\n" +
                "            \"statuses_count\":89,\n" +
                "            \"lang\":\"en\",\n" +
                "            \"contributors_enabled\":false,\n" +
                "            \"is_translator\":false,\n" +
                "            \"is_translation_enabled\":false,\n" +
                "            \"profile_background_color\":\"C0DEED\",\n" +
                "            \"profile_background_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_background_images\\/724871000\\/bf619ba7c8324c5c6ccdf17b2d417096.jpeg\",\n" +
                "            \"profile_background_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_background_images\\/724871000\\/bf619ba7c8324c5c6ccdf17b2d417096.jpeg\",\n" +
                "            \"profile_background_tile\":true,\n" +
                "            \"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/811498358976155648\\/nNPilBB5_normal.jpg\",\n" +
                "            \"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/811498358976155648\\/nNPilBB5_normal.jpg\",\n" +
                "            \"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/978599785\\/1478312771\",\n" +
                "            \"profile_link_color\":\"0084B4\",\n" +
                "            \"profile_sidebar_border_color\":\"000000\",\n" +
                "            \"profile_sidebar_fill_color\":\"DDEEF6\",\n" +
                "            \"profile_text_color\":\"333333\",\n" +
                "            \"profile_use_background_image\":true,\n" +
                "            \"has_extended_profile\":true,\n" +
                "            \"default_profile\":false,\n" +
                "            \"default_profile_image\":false,\n" +
                "            \"following\":null,\n" +
                "            \"follow_request_sent\":null,\n" +
                "            \"notifications\":null,\n" +
                "            \"translator_type\":\"none\"\n" +
                "         },\n" +
                "         \"geo\":null,\n" +
                "         \"coordinates\":null,\n" +
                "         \"place\":null,\n" +
                "         \"contributors\":null,\n" +
                "         \"is_quote_status\":false,\n" +
                "         \"retweet_count\":0,\n" +
                "         \"favorite_count\":0,\n" +
                "         \"favorited\":false,\n" +
                "         \"retweeted\":false,\n" +
                "         \"possibly_sensitive\":false,\n" +
                "         \"lang\":\"en\"\n" +
                "      }" +
                "   ]\n" +
                "}");

        List<Tweet> tweets = TwitterSearchParser.parse(twitterResult);

        Tweet tweet = tweets.get(0);
        Assert.assertNotNull(tweet.getCreatedAt());
        Assert.assertEquals("Sarah Ashleigh", tweet.getUserName());
        Assert.assertEquals("Dumbass honestly thinks he gonna get away with something \uD83D\uDE02 #tryme #testme https://t.co/GVaaj43OF6", tweet.getText());
    }
}
