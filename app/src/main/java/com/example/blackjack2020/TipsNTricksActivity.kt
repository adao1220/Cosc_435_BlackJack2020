package com.example.blackjack2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_tips_n_tricks.*
import okhttp3.*
import java.io.IOException


class TipsNTricksActivity : AppCompatActivity() {

    private lateinit var tipsAdapter: TipsAdapter
    private var url: String = "https://my-json-server.typicode.com/jcbreen/blackjackJsonRepo/db"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips_n_tricks)
        Recycler_View.layoutManager = LinearLayoutManager(this)
        fetchJson(url)

        supportActionBar?.hide()

    }
    fun fetchJson(url: String) {
        var URL = url
        val request = Request.Builder().url(URL).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val tipsFeed = gson.fromJson(body, TipsFeed::class.java)

                runOnUiThread {
                    Recycler_View.adapter = TipsAdapter(tipsFeed)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("test", "Failed to connect to Rest API")
            }

        })

    }
    public class TipsFeed(val newTips: List<TipsNew>)
    class TipsNew(val tipName: Int, val tipText: String)
    fun getUrl() : String{
        return url
    }
}