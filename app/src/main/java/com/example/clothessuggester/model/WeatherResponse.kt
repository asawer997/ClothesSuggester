package com.example.clothessuggester.model

import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @SerializedName("list")
    val list: List<WeatherInfo>
)