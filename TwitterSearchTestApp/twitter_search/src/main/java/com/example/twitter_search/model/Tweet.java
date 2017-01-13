package com.example.twitter_search.model;

import java.util.Date;

/**
 * Created by Sagi on 1/13/2017.
 * Represents a "tweet" - this is a very basic class - that should be later on filled with all the relevant
 * fields as required.
 * For tests, the basic data is good enough for us
 */

public class Tweet {
    private String mText;
    private Date mCreatedAt;
    private String mUserName;
    private String mProfileImageUrl;

    public void setText(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }


    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }


    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public void setProfileImageUrl(String mProfileImageUrl) {
        this.mProfileImageUrl = mProfileImageUrl;
    }
}
