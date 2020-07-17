package com.example.geomob.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geomob.Activities.PaysActivity
import com.example.geomob.Adapters.TweetAdapter
import com.example.geomob.DataClasses.Tweet
import com.example.geomob.R
import com.example.geomob.Threads.AppExecutors
import kotlinx.android.synthetic.main.fragment_videos.*
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder


class TweetsFragment : Fragment() {

    private var countryCode = ""
    private var countryName = ""
    var tweetList = arrayListOf<Tweet>()

    lateinit var tweetAdapter: TweetAdapter
    lateinit var layoutManager : LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryCode = (activity as PaysActivity).getCountryCode()
        countryName = (activity as PaysActivity).getCountryName()

        recyclerView.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        tweetAdapter = TweetAdapter(activity!! as PaysActivity, tweetList)
        recyclerView.adapter = tweetAdapter


        initTwitter()


        /*val tweetUrl = "https://api.twitter.com/1.1/search/tweets.json?q=$countryName&lang=en&count=30&tweet_mode=extended"
        //val tweetUrl = "https://ptsv2.com/t/9ukhq-1592581274/post"
        val request = object : CustomRequest(
            Method.GET,
            tweetUrl,
            Response.Listener {
                Log.i("response", it.statusCode.toString() + "   "+it.data)


            },
            Response.ErrorListener {
                val err = String(it.networkResponse.data)
                Log.i("response","error:"+err)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                var headers: MutableMap<String, String> = mutableMapOf()
                val ts = System.currentTimeMillis()/1000
                val nonce = (1..Int.MAX_VALUE).random()

                headers["Accept"] = "*"
                headers["Authorization"] = "OAuth oauth_consumer_key=\"$consumerKey\"," +
                        "oauth_nonce= \"$nonce\"," +
                        "oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"$ts\"," +
                        "oauth_token=\"$accessToken\", oauth_version=\"1.0\"," +
                        "oauth_signature=\"%2F%2Fl0JcVySsYDZVZxDZUWgz8iRmA%3D\""

                return headers
            }





        }
        Volley.newRequestQueue(activity).add(request)*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tweets, container, false)
    }
    
    fun initTwitter(){
        AppExecutors.instance!!.diskIO().execute {
            val consumerKey = "Na9iLBHIupFXEoGwQL0zyaSEs"
            val consumerKeySecret = "CuK8ZUmFSY9VU4Xw0s5YSwgMvhyM4FjN2dywbqLNwaC05wyHkv"
            val accessToken = "1223554396883124224-eQ73Yq9Iw2573F46R7I4nSQMNRXScV"
            val accessTokenSecret = "DLCjwJkm9vxJjqqoYM92oEgDDOrV5J2YEkhvIHA6NAGMb"


            val cb = ConfigurationBuilder()
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerKeySecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)

            val tf = TwitterFactory(cb.build())
            val twitter = tf.instance

            searchQuery(twitter,countryName)
        }

    }
    
    fun searchQuery(twitter : Twitter, query : String){
        try {
            val query = Query(query)
            val result: QueryResult
            tweetList.clear()
            query.count = 30
            query.lang = "en"
            result = twitter.search(query)
            for (status in result.tweets) {
                var mediaUrl = ""
                if (status.mediaEntities.isNotEmpty()){
                    mediaUrl = status.mediaEntities[0].mediaURLHttps
                }
                val tweet = Tweet(status.id, status.user.originalProfileImageURLHttps, status.user.name, "@"+status.user.screenName,
                mediaUrl, status.text, countryCode)
                tweetList.add(tweet)
            }

            AppExecutors.instance!!.mainThread().execute {
                tweetAdapter.notifyDataSetChanged()
            }

        } catch (e: TwitterException) {
            e.printStackTrace()
        }
    }
}
