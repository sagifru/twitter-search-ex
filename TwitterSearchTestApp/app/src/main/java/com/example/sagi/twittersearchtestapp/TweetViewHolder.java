package com.example.sagi.twittersearchtestapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Sagi on 1/13/2017.
 * View Holder for a Tweet item
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    public TextView tweetTxt;
    public TextView dateTxt;
    public TextView userNameTxt;
    public NetworkImageView profileImage;

    public TweetViewHolder(View itemView) {
        super(itemView);

        tweetTxt = (TextView) itemView.findViewById(R.id.tweet_txt);
        dateTxt = (TextView) itemView.findViewById(R.id.date_txt);
        userNameTxt = (TextView) itemView.findViewById(R.id.user_txt);
        profileImage = (NetworkImageView) itemView.findViewById(R.id.profile_img);
    }
}
