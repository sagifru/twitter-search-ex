package com.example.sagi.twittersearchtestapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twitter_search.model.Tweet;

import java.util.List;

/**
 * Created by Sagi on 1/13/2017.
 * Adapter to connect the tweets list with its relevant UI
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private List<Tweet> mTweetList;
    private LayoutInflater mInflater;
    private Context mContext;

    public TweetAdapter(Context ctx) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tweet_item, parent, false);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        // Fill the data from the relevant tweet
        Tweet tweet = mTweetList.get(position);
        holder.tweetTxt.setText(tweet.getText());
        holder.userNameTxt.setText(tweet.getUserName());

        // Set date - show how long ago the tweet was
        long currTime = System.currentTimeMillis();
        CharSequence formatDate = DateUtils.getRelativeDateTimeString(mContext, tweet.getCreatedAt().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL);
        holder.dateTxt.setText(formatDate);
    }

    @Override
    public int getItemCount() {
        if (mTweetList != null) {
            return mTweetList.size();
        }

        return 0;
    }

    public void updateTweetData(List<Tweet> tweetList) {
        mTweetList = tweetList;
    }
}
