package com.example.andriodtweets

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.email


class TweetActivity: AppCompatActivity(){

    private val twitterManager: TwitterManager = TwitterManager()

    private lateinit var recyclerView : RecyclerView
    private val tweetList:MutableList<Tweet> = mutableListOf()


    //todo FB
    private lateinit var  tweetContent: EditText
    private lateinit var addTweet: FloatingActionButton

    private lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)

        recyclerView =findViewById(R.id.recyclerView)


        recyclerView.layoutManager= LinearLayoutManager( this)

        val intent: Intent = intent
        val location: Address = intent.getParcelableExtra("location")


        //todo FB
        val state: String = location.adminArea?:"unknown"

        title=getString(R.string.tweet_title, location)

        //todo FB
        val reference = firebaseDatabase.getReference("tweets/$state")

        addTweet.setOnClickListener{
            val content= tweetContent.text.toString().trim()
            val currenUser: String = FirebaseAuth.getInstance().currentUser!!.email!!
            if(content.isNotEmpty()){
                val tweet = Tweet(
                    username = "email",
                    handle = "email",
                    content = content,
                    iconUrl = ""
                )

                reference.child("Nick's Tweet").setValue(tweet)
            }
        }

        if(savedInstanceState!=null){

            val previousTweets =savedInstanceState.getSerializable("TWEETS") as List<Tweet>

            tweetList.addAll(previousTweets)
            recyclerView.adapter = TweetAdapter(tweetList)

        }

        else{

            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tweetList.clear()
                    dataSnapshot.children.forEach{ data ->
                        val tweet=data.getValue(Tweet::class.java)
                        if(tweet!=null){
                            tweetList.add(tweet)
                        }

                    }
                    recyclerView.adapter= TweetAdapter(tweetList)
                }
            })

            twitterManager.retrieveOAuthToken(
                successCallback = { token ->

                    twitterManager.retrieveTweets(
                        oAuthToken = token,
                        address = location,
                        successCallback = { tweets ->
                            runOnUiThread {

                                tweetList.clear()
                                tweetList.addAll(tweets)
                                // Create the adapter and assign it to the RecyclerView
                                recyclerView.adapter = TweetAdapter(tweets)
                            }
                        },
                        errorCallback = {
                            runOnUiThread {
                                // Runs if we have an error
                                Toast.makeText(this@TweetActivity, "Error retrieving Tweets", Toast.LENGTH_LONG).show()
                            }
                        }
                    )


                },
                errorCallback = { exception ->
                    runOnUiThread {
                        // Runs if we have an error
                        Toast.makeText(this@TweetActivity, "Error performing OAuth", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            )

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("TWEETS", ArrayList(tweetList))
    }



    private fun generateFakeTweet(): List<Tweet>{
return listOf(
    Tweet(
        username = "Nick Capurso",
        handle = "@nickcapurso",
        content = "We're learning lists!",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "Android Central",
        handle = "@androidcentral",
        content = "NVIDIA Shield TV vs. Shield TV Pro: Which should I buy?",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "DC Android",
        handle = "@DCAndroid",
        content = "FYI - another great integration for the @Firebase platform",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "KotlinConf",
        handle = "@kotlinconf",
        content = "Can't make it to KotlinConf this year? We have a surprise for you. We'll be live streaming the keynotes, closing panel and an entire track over the 2 main conference days. Sign-up to get notified once we go live!",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "Android Summit",
        handle = "@androidsummit",
        content = "What a #Keynote! @SlatteryClaire is the Director of Performance at Speechless, and that's exactly how she left us after her amazing (and interactive!) #keynote at #androidsummit. #DCTech #AndroidDev #Android",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "Fragmented Podcast",
        handle = "@FragmentedCast",
        content = ".... annnnnnnnnd we're back!\n\nThis week @donnfelker talks about how it's Ok to not know everything and how to set yourself up mentally for JIT (Just In Time [learning]). Listen in here: \nhttp://fragmentedpodcast.com/episodes/135/ ",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "Jake Wharton",
        handle = "@JakeWharton",
        content = "Free idea: location-aware physical password list inside a password manager. Mostly for garage door codes and the like. I want to open my password app, switch to the non-URL password section, and see a list of things sorted by physical distance to me.",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "Droidcon Boston",
        handle = "@droidconbos",
        content = "#DroidconBos will be back in Boston next year on April 8-9!",
        iconUrl = "https://...."
    ),
    Tweet(
        username = "AndroidWeekly",
        handle = "@androidweekly",
        content = "Latest Android Weekly Issue 327 is out!\nhttp://androidweekly.net/ #latest-issue  #AndroidDev",
        iconUrl = "https://...."
    ),
    Tweet(
        username = ".droidconSF",
        handle = "@droidconSF",
        content = "Drum roll please.. Announcing droidcon SF 2018! November 19-20 @ Mission Bay Conference Center. Content and programming by @tsmith & @joenrv.",
        iconUrl = "https://...."
    )

)
    }
}