package com.example.clothessuggester.data

import com.example.clothessuggester.model.WeatherResponse

interface WeatherCallback {
    fun onSuccess(weatherResponse: WeatherResponse)
    fun onError(message: String)
}