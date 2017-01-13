package com.example.sagi.twittersearchtestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private EditText mQueryEditTxt;
    private RecyclerView mTweetsLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryEditTxt = (EditText) findViewById(R.id.query_edt);
        mTweetsLv = (RecyclerView) findViewById(R.id.tweets_lv);
    }
}
