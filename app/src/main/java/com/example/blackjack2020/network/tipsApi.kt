package com.example.blackjack2020.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

interface ITipsApi {
    suspend fun fetchTipName(): String
    suspend fun fetchTipText(): String
}

class tipsApi : ITipsApi {

    private val URL = "https://my-json-server.typicode.com/jcbreen/blackjackJsonRepo/db"

    override suspend fun fetchTipName(): String {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(URL)
                .get()
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {

            }
        }
        return URL
    }

    override suspend fun fetchTipText(): String {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(URL)
                .get()
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {

            }
        }
        return URL
    }
}

