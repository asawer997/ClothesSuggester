package com.example.clothessuggester.data

import com.example.clothessuggester.model.WeatherResponse
import com.example.clothessuggester.ui.Contract
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class MainModel : Contract.Model {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val baseUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?q=baghdad&appid=ed60fcfbd110ee65c7150605ea8aceea&units=metric"

    override fun getWeatherData(callback: Contract.WeatherDataCallback) {
        val request = Request.Builder().url(baseUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseString = response.body?.string()
                    val weatherResponse = gson.fromJson(responseString, WeatherResponse::class.java)
                    callback.onSuccess(weatherResponse)
                } else {
                    callback.onError(response.message)
                }
            }
        })
    }
}