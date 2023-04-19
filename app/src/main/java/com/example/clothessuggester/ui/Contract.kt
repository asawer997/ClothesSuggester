package com.example.clothessuggester.ui

import com.example.clothessuggester.model.WeatherInfo
import com.example.clothessuggester.model.WeatherResponse

interface Contract {
    interface View {
        fun updateChipGroup(days: List<WeatherInfo>)
        fun updateWeatherDegreeAndIcon(weatherInfo: WeatherInfo)
        fun updateClothImage(currentTemperature: Double)
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun fetchWeatherData()
        fun onDayChipClicked(day: WeatherInfo)
        fun onNextSuggestClicked(currentTemperature: Double)
    }

    interface Model {
        fun getWeatherData(callback: WeatherDataCallback)
    }

    interface WeatherDataCallback {
        fun onSuccess(weatherResponse: WeatherResponse)
        fun onError(message: String)
    }
}