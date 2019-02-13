package com.example.andriodtweets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class TweetAdapter constructor(val tweets: List<Tweet>): RecyclerView.Adapter<TweetAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTweet =tweets[position]
        holder.handleTextView.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tweets.size


    fun onBindViewHolder(holder: TweetActivity, p1: Int) {

    }

    class ViewHolder constructor(view: View): RecyclerView.ViewHolder(view){
        private val usernameTestView: TextView = view.findViewById(R.id.username)
         val handleTextView: TextView = view.findViewById(R.id.handle)
        private val contentTextView: TextView = view.findViewById(R.id.content)
        private val iconImageView: ImageView = view.findViewById(R.id.icon)
    }
}