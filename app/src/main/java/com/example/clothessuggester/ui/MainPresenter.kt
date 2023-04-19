package com.example.clothessuggester.ui

import com.example.clothessuggester.model.WeatherInfo
import com.example.clothessuggester.model.WeatherResponse

class MainPresenter(private val model: Contract.Model) : Contract.Presenter {
    private var view: Contract.View? = null

    override fun attachView(view: Contract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun fetchWeatherData() {
        model.getWeatherData(object : Contract.WeatherDataCallback {
            override fun onSuccess(weatherResponse: WeatherResponse) {
                view?.updateChipGroup(weatherResponse.list)
            }

            override fun onError(message: String) {
                view?.showError(message)
            }
        })
    }

    override fun onDayChipClicked(day: WeatherInfo) {
        view?.updateWeatherDegreeAndIcon(day)
        view?.updateClothImage(day.temperature.day)
    }

    override fun onNextSuggestClicked(currentTemperature: Double) {
        view?.updateClothImage(currentTemperature)
    }
}