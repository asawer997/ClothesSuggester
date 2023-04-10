package com.example.clothessuggester.ui

import android.util.Log
import android.view.LayoutInflater
import com.example.clothessuggester.R
import com.example.clothessuggester.data.Network
import com.example.clothessuggester.data.WeatherCallback
import com.example.clothessuggester.databinding.ActivityMainBinding
import com.example.clothessuggester.model.WeatherInfo
import com.example.clothessuggester.model.WeatherResponse
import com.example.clothessuggester.ui.base.BaseActivity
import com.example.clothessuggester.utils.SharedPrefsUtils
import com.example.clothessuggester.utils.WeatherUtils.getDayNameFromTimestamp
import com.example.clothessuggester.utils.WeatherUtils.updateClothImage
import com.example.clothessuggester.utils.WeatherUtils.updateWeatherInfo
import com.google.android.material.chip.Chip

class MainActivity : BaseActivity<ActivityMainBinding>(), WeatherCallback {
    override val LOG_TAG: String? = MainActivity::class.simpleName
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate
    private val context = this
    override fun setup() {
        initPrefs()
        fetchWeatherData()
        setupNextSuggestClickListener()
    }

    override fun onSuccess(weatherResponse: WeatherResponse) {
        updateChipGroup(weatherResponse.list)
    }

    override fun onError(message: String) {
        Log.e(LOG_TAG, "Error: $message")
    }

    private fun initPrefs() = SharedPrefsUtils.initPrefUtil(applicationContext)

    private fun fetchWeatherData() = Network.makeRequestUsingOkhttp(this)

    private fun setupNextSuggestClickListener() {
        binding?.nextSuggest?.setOnClickListener {
            val currentTemperature =
                binding?.weatherDegree?.text?.toString()?.replace("°C", "")?.toDoubleOrNull()
            if (currentTemperature != null) {
                updateClothImage(binding!!, currentTemperature, SharedPrefsUtils)
            }
        }
    }

    private fun updateChipGroup(days: List<WeatherInfo>) {
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

    private fun createChip(index: Int, day: WeatherInfo): Chip {
        return Chip(context).apply {
            text = getDayNameFromTimestamp(day.timestamp)
            isCheckable = true
            setChipBackgroundColorResource(R.color.chip_background_color)
            isChecked = index == 0
            if (index == 0) { updateWeatherInfo(context, day,binding!!.weatherDegree, binding!!.WeatherIcon) }
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    updateWeatherInfo(context,day,binding!!.weatherDegree,binding!!.WeatherIcon)
                    updateClothImage(binding!!, day.temperature.day, SharedPrefsUtils)
                }
            }
        }
    }
}