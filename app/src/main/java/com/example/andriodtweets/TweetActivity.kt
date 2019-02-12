package com.example.andriodtweets

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

class TweetActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_tweet)

        val intent: Intent= intent
        val location: String = intent.getStringExtra("location")
      //  title= "Andriod Tweet near {location}"
       // setTitle("Andriod Tweet near" + location)

        title==getString(R.string.tweet_title, location)

    }
}