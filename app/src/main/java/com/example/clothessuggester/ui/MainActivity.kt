package com.example.clothessuggester.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.clothessuggester.R
import com.example.clothessuggester.databinding.ActivityMainBinding
import com.example.clothessuggester.ui.base.BaseActivity
import com.example.clothessuggester.utils.PrefsUtil

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val LOG_TAG: String? = MainActivity::class.simpleName
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setup() {
        PrefsUtil.initPrefUtil(applicationContext)
    }

    override fun addCallbacks() {

    }

}