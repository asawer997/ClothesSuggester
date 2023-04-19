package com.example.clothessuggester.ui

import android.util.Log
import android.view.LayoutInflater
import com.example.clothessuggester.R
import com.example.clothessuggester.data.MainModel
import com.example.clothessuggester.databinding.ActivityMainBinding
import com.example.clothessuggester.model.WeatherInfo
import com.example.clothessuggester.model.WeatherResponse
import com.example.clothessuggester.ui.base.BaseActivity
import com.example.clothessuggester.utils.SharedPrefsUtils
import com.example.clothessuggester.utils.WeatherUtils
import com.example.clothessuggester.utils.WeatherUtils.getDayNameFromTimestamp
import com.example.clothessuggester.utils.WeatherUtils.updateClothImage
import com.example.clothessuggester.utils.WeatherUtils.updateWeatherInfo
import com.google.android.material.chip.Chip

class MainActivity : BaseActivity<ActivityMainBinding>(), Contract.View {
    override val LOG_TAG: String? = MainActivity::class.simpleName
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate
    private val context = this
    private val presenter: Contract.Presenter = MainPresenter(MainModel())
    override fun setup() {
        initPrefs()
        presenter.attachView(this)
        presenter.fetchWeatherData()
        setupNextSuggestClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun initPrefs() = SharedPrefsUtils.initPrefUtil(applicationContext)

    private fun setupNextSuggestClickListener() {
        binding?.nextSuggest?.setOnClickListener {
            val currentTemperature =
                binding?.weatherDegree?.text?.toString()?.replace("°C", "")?.toDoubleOrNull()
            if (currentTemperature != null) {
                presenter.onNextSuggestClicked(currentTemperature)
            }
        }
    }

    override fun updateChipGroup(days: List<WeatherInfo>) {
        runOnUiThread {
            val chipGroup = binding?.chipGroup
            chipGroup?.removeAllViews()
            chipGroup?.isSingleSelection = true

            days.forEachIndexed { index, day ->
                createChip(index, day).let { chip ->
                    chipGroup?.addView(chip)
                }
            }
        }
    }

    override fun updateWeatherDegreeAndIcon(weatherInfo: WeatherInfo) {
        WeatherUtils.updateWeatherInfo(context, weatherInfo, binding!!.weatherDegree, binding!!.WeatherIcon)
    }

    override fun updateClothImage(currentTemperature: Double) {
        WeatherUtils.updateClothImage(binding!!, currentTemperature, SharedPrefsUtils)
    }

    override fun showError(message: String) {
        Log.e(LOG_TAG, "Error: $message")
    }

    private fun createChip(index: Int, day: WeatherInfo): Chip {
        return Chip(context).apply {
            text = WeatherUtils.getDayNameFromTimestamp(day.timestamp)
            isCheckable = true
            setChipBackgroundColorResource(R.color.chip_background_color)
            isChecked = index == 0
            if (index == 0) { presenter.onDayChipClicked(day) }
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    presenter.onDayChipClicked(day)
                }
            }
        }
    }
}